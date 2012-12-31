package com.maximchuk.ptc.ui;

import com.maximchuk.ptc.ConverterHandler;
import com.maximchuk.ptc.helper.UIHelper;
import com.maximchuk.ptc.parser.ThemerollerZipParser;
import com.maximchuk.ptc.parser.impl.ThemerollerZipParser192;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Maxim L. Maximchuk
 */
public class ConverterForm {
    private static JFrame frame;
    private static ConsoleOutput consoleOutput;

    private JPanel mainPanel;
    private JTextField srcInput;
    private JButton browseButton;
    private JTextField themenameInput;
    private JLabel srcLabel;
    private JLabel themenameLabel;
    private JTable table1;
    private JButton createButton;

    private Properties config;

    public ConverterForm() {
        config = new Properties();
        consoleOutput = new ConsoleOutput();
        consoleOutput.setTitle("Console output");

        // Action listeners
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowseDialog browseDialog = new BrowseDialog(srcThemeFileChoosedListener);
                UIHelper.centerWindow(browseDialog);
                browseDialog.setVisible(true);
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.setProperty(ConverterHandler.SRC_FILENAME_KEY, srcInput.getText());
                config.setProperty(ConverterHandler.THEME_NAME_KEY, themenameInput.getText());

                consoleOutput.clear();
                UIHelper.centerWindow(consoleOutput);
                consoleOutput.setVisible(true);
                ConverterHandler.process(config);
            }
        });
    }

    public static void show() {
        frame = new JFrame("ConverterForm");
        frame.setContentPane(new ConverterForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.addWindowListener(windowShowListener);
        frame.setVisible(true);
    }

    public static boolean isFrameVisible() {
        return frame.isVisible();
    }

    public static void writeConsoleLine(String line) {
        consoleOutput.writeLine(line);
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

}
