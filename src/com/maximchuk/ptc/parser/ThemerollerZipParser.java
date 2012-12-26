package com.maximchuk.ptc.parser;

import com.maximchuk.ptc.entity.FileEntity;

import java.io.IOException;
import java.util.List;

/**
 * @author Maxim L. Maximchuk
 *         Date: 25.12.12
 */
public interface ThemerollerZipParser {

    public boolean validate();

    public boolean parse();

    public FileEntity getCss() throws IOException;

    public List<FileEntity> getImages() throws IOException;

    public String getDefaultThemeName();
}
