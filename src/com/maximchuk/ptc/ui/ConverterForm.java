package com.maximchuk.ptc.ui;

import com.maximchuk.ptc.ConverterHandler;
import com.maximchuk.ptc.entity.CssPropertyEntity;
import com.maximchuk.ptc.entity.CssPropertyEnum;
import com.maximchuk.ptc.helper.UIHelper;
import com.maximchuk.ptc.parser.ThemerollerZipParser;
import com.maximchuk.ptc.parser.impl.ThemerollerZipParser192;
import com.maximchuk.ptc.ui.filesystem.FileTypeEnum;
import com.maximchuk.ptc.ui.table.CssPropertyTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Maxim L. Maximchuk
 */
public class ConverterForm {
    private static JFrame frame;
    private static ConsoleOutput consoleOutput;

    private CssPropertyTableModel tableModel;
    private String profileFileName;

    private JPanel mainPanel;
    private JTextField srcInput;
    private JButton browseButton;
    private JTextField themenameInput;
    private JLabel srcLabel;
    private JLabel themenameLabel;
    private JTable cssPropertyTable;
    private JButton createButton;
    private JButton addCssPropertyButton;
    private JScrollPane scrollPane;
    private JButton removeCssPropertyButton;

    public ConverterForm() {
        tableModel = new CssPropertyTableModel();
        cssPropertyTable.setModel(tableModel);

        consoleOutput = new ConsoleOutput();
        consoleOutput.setTitle("Console output");

        // Action listeners
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowseDialog browseDialog = new BrowseDialog(FileTypeEnum.THEME_SRC, srcThemeFileChoosedListener);
                browseDialog.showOpenDialog(mainPanel);
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                consoleOutput.clear();
                UIHelper.centerWindow(consoleOutput);
                consoleOutput.setVisible(true);

                ConverterHandler.process(bindProfile());
            }
        });

        addCssPropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCssPropertyDialog addCssPropertyDialog = new AddCssPropertyDialog(tableModel);
                UIHelper.centerWindow(addCssPropertyDialog);
                addCssPropertyDialog.setVisible(true);
                cssPropertyTable.revalidate();
                checkAddButton();
            }
        });

        removeCssPropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selInd = cssPropertyTable.getSelectedRow();
                tableModel.removeRowByInd(selInd);
                cssPropertyTable.revalidate();
                checkAddButton();
                checkRemoveButton();
            }
        });

        cssPropertyTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                checkRemoveButton();
            }
        });
    }

    public static void show() throws Exception{
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        ConverterForm converterForm = new ConverterForm();

        frame = new JFrame(UIHelper.getAppTitle());
        frame.setJMenuBar(initMenu(converterForm));
        frame.setContentPane(converterForm.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.addWindowListener(windowShowListener);
        frame.setVisible(true);
    }

    public static boolean isFrameVisible() {
        return frame.isVisible();
    }

    public static void writeConsoleLine(String line) {
        consoleOutput.writeLine(line);
    }

    private static JMenuBar initMenu(final ConverterForm creator) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newProfMenuItem = new JMenuItem();
        JMenuItem openProfMenuItem = new JMenuItem();
        JMenuItem saveMenuItem = new JMenuItem();
        JMenuItem saveAsMenuItem = new JMenuItem();

        fileMenu.add(newProfMenuItem);
        fileMenu.add(openProfMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);

        menuBar.add(fileMenu);

        newProfMenuItem.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creator.acceptProfile(new Properties());
                creator.profileFileName = null;
                creator.refreshTitle();
            }

        });
        newProfMenuItem.setText("New");

        openProfMenuItem.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowseDialog browseDialog = new BrowseDialog(FileTypeEnum.PROFILE, creator.profileFileChoosedListener);
                browseDialog.showOpenDialog(creator.mainPanel);
                try {
                    creator.loadProfile();
                    creator.refreshTitle();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        openProfMenuItem.setText("Open");

        saveMenuItem.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int browseDialogRes = 0;
                if (creator.profileFileName == null) {
                    BrowseDialog browseDialog = new BrowseDialog(FileTypeEnum.PROFILE, creator.profileFileChoosedListener);
                    browseDialogRes = browseDialog.showSaveDialog(creator.mainPanel);
                }
                if (browseDialogRes == BrowseDialog.APPROVE_OPTION) {
                    try {
                        creator.saveProfile();
                        creator.refreshTitle();
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
            }
        });
        saveMenuItem.setText("Save");

        saveAsMenuItem.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowseDialog browseDialog = new BrowseDialog(FileTypeEnum.PROFILE, creator.profileFileChoosedListener);
                int browseDialogRes = browseDialog.showSaveDialog(creator.mainPanel);
                if (browseDialogRes == BrowseDialog.APPROVE_OPTION) {
                    try {
                        creator.saveProfile();
                        creator.refreshTitle();
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
            }
        });
        saveAsMenuItem.setText("Save as");
        return menuBar;
    }

    private ActionListener srcThemeFileChoosedListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File selectedFile = ((JFileChooser)e.getSource()).getSelectedFile();
            try {
                ThemerollerZipParser parser = new ThemerollerZipParser192(selectedFile);
                if (parser.parse()) {
                    themenameInput.setText(parser.getThemeName());
                }
            } catch (IOException ex) {
                System.err.print(ex.getStackTrace());
            }

            srcInput.setText(selectedFile.getPath());
        }
    };

    private ActionListener profileFileChoosedListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File selectedFile = ((JFileChooser)e.getSource()).getSelectedFile();

            profileFileName = selectedFile.getPath();
        }
    };

    private static WindowListener windowShowListener = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {
            UIHelper.centerWindow(e.getWindow());
        }

        @Override
        public void windowClosing(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    };

    private void checkAddButton() {
        addCssPropertyButton.setEnabled(tableModel.canAddedNewCssProperty());
    }

    private void checkRemoveButton() {
        removeCssPropertyButton.setEnabled(cssPropertyTable.getSelectedRow() > -1 && tableModel.getRowCount() > 0);
    }

    private void loadProfile() throws IOException {
        if (profileFileName != null) {
            Properties profile = new Properties();
            profile.load(new FileInputStream(profileFileName));
            acceptProfile(profile);
        }
    }

    private void saveProfile() throws IOException {
        Properties profile = bindProfile();
        profile.store(new FileOutputStream(profileFileName), "Created from Primefaces theme converter");
    }

    private void acceptProfile(Properties profile) {
        srcInput.setText((String)profile.get(ConverterHandler.SRC_FILENAME_KEY));
        themenameInput.setText((String) profile.get(ConverterHandler.THEME_NAME_KEY));

        tableModel.clear();
        for (CssPropertyEnum cssPropertyType: CssPropertyEnum.values()) {
            String cssPropertyValue = (String)profile.get(cssPropertyType.getPropertyKey());
            if (cssPropertyValue != null) {
                tableModel.addRow(new CssPropertyEntity(cssPropertyType, cssPropertyValue));
            }
        }
        checkAddButton();
        checkRemoveButton();
        cssPropertyTable.revalidate();
    }

    private Properties bindProfile() {
        Properties profile = new Properties();
        profile.setProperty(ConverterHandler.SRC_FILENAME_KEY, srcInput.getText());
        profile.setProperty(ConverterHandler.THEME_NAME_KEY, themenameInput.getText());

        for (CssPropertyEntity cssProperty: tableModel.getDataList()) {
            profile.setProperty(cssProperty.getType().getPropertyKey(), cssProperty.getValue());
        }
        return profile;
    }

    private void refreshTitle() {
        StringBuilder titleBuilder = new StringBuilder(UIHelper.getAppTitle());
        if (profileFileName != null) {
            titleBuilder.append(" - ").append(getProfilename());
        }
        frame.setTitle(titleBuilder.toString());
    }

    private String getProfilename() {
        return new File(profileFileName).getName().replace(".prof", "");
    }

}
