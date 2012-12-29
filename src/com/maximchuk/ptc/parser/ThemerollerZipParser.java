package com.maximchuk.ptc.parser;

import com.maximchuk.ptc.entity.FileEntity;

import java.io.IOException;
import java.util.List;

/**
 * Themeroller zip parser interface
 *
 * @author Maxim L. Maximchuk
 */
public interface ThemerollerZipParser {

    /**
     * Validating themeroller version
     *
     * @return true if version supported
     */
    public boolean validate();

    /**
     * Parsing themeroller zip
     *
     * @return true if parse successful
     */
    public boolean parse();

    /**
     * Getting css file entity from parsed zip
     * Execute after parse
     *
     * @return css file entity
     * @throws IOException
     */
    public FileEntity getCss() throws IOException;

    /**
     * Getting images file entity list
     * Execute after parse
     *
     * @return file entity list
     * @throws IOException
     */
    public List<FileEntity> getImages() throws IOException;

    /**
     * Getting theme name
     *
     * @return theme name
     */
    public String getThemeName();

}
