package com.maximchuk.ptc.ui.filesystem;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Maxim L. Maximchuk
 */
public enum FileTypeEnum {
    THEME_SRC, PROFILE;

    private final static String DEFAULT_THEME_SRC_ROOT_DIR_NAME = "theme_src";
    private final static String DEFAULT_PROFILE_ROOT_DIR_NAME = "config";

    public FileFilter getFileFilter() {
        FileFilter fileFilter = null;
        switch (this) {
            case THEME_SRC: fileFilter = new ZipFileFilter(); break;
            case PROFILE: fileFilter = new ProfileFileFilter(); break;
        }
        return fileFilter;
    }

    public File getRootDir() {
        File f = null;
        switch (this) {
            case THEME_SRC: f = new File(DEFAULT_THEME_SRC_ROOT_DIR_NAME); break;
            case PROFILE: f = new File(DEFAULT_PROFILE_ROOT_DIR_NAME); break;
        }
        if (f == null || !f.exists()) {
            f = new File(".");
        }
        return f;
    }
}
