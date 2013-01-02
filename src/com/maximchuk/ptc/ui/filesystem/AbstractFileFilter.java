package com.maximchuk.ptc.ui.filesystem;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Maxim L. Maximchuk
 */
public abstract class AbstractFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        String[] filenameParts = f.getName().replace(".", "&").split("&");
        return f.isDirectory() || filenameParts.length > 0 && filenameParts[filenameParts.length - 1]
                .equals(getDescription());
    }

}
