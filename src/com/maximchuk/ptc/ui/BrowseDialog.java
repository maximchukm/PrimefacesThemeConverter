package com.maximchuk.ptc.ui;

import com.maximchuk.ptc.ui.filesystem.FileTypeEnum;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrowseDialog extends JDialog {

    private JPanel contentPane;
    private JFileChooser fileChooser;
    private ActionListener fileChoosedListener;

    public BrowseDialog(FileTypeEnum fileType, final ActionListener fileChoosedListener) {
        this.fileChoosedListener = fileChoosedListener;
        setContentPane(contentPane);
        setModal(true);
        fileChooser.setCurrentDirectory(fileType.getRootDir());
        fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
        fileChooser.addChoosableFileFilter(fileType.getFileFilter());
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

}
