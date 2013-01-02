package com.maximchuk.ptc.ui;

import com.maximchuk.ptc.ui.filesystem.FileTypeEnum;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BrowseDialog extends JFileChooser {

    public BrowseDialog(final FileTypeEnum fileType, final ActionListener fileChoosedListener) {
        setCurrentDirectory(fileType.getRootDir());
        removeChoosableFileFilter(getChoosableFileFilters()[0]);
        addChoosableFileFilter(fileType.getFileFilter());

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                    if (!getSelectedFile().getName().endsWith(fileType.getFileFilter().getDescription())) {
                        setSelectedFile(new File(getSelectedFile().getPath() + "." + fileType.getFileFilter().getDescription()));
                    }
                    fileChoosedListener.actionPerformed(e);
                }
            }
        });
    }

}
