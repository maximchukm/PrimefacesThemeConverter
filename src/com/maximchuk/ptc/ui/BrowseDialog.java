package com.maximchuk.ptc.ui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BrowseDialog extends JDialog {
    private JPanel contentPane;
    private JFileChooser fileChooser;
    private ActionListener fileChoosedListener;

    public BrowseDialog(final ActionListener fileChoosedListener) {
        this.fileChoosedListener = fileChoosedListener;
        setContentPane(contentPane);
        setModal(true);
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
        fileChooser.addChoosableFileFilter(zipFilter);
        pack();

        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JFileChooser.CANCEL_SELECTION.equals(e.getActionCommand())) {
                    dispose();
                }
                if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                    fileChoosedListener.actionPerformed(e);
                    dispose();
                }
            }
        });
    }

    private FileFilter zipFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            String[] filenameParts = f.getName().replace(".", "&").split("&");
            return f.isDirectory() || filenameParts.length > 0 && filenameParts[filenameParts.length - 1].equals(getDescription());
        }

        @Override
        public String getDescription() {
            return "zip";
        }
    };


}
