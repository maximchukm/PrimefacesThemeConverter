package com.maximchuk.ptc.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Maxim L. Maximchuk
 *         Date: 25.12.12
 */
public interface ThemerollerZipParser {

    public boolean validate();

    public boolean parse();

    public InputStream getCssInputStream() throws IOException;

    public List<InputStream> getImagesInputStreamList() throws IOException;

    public String getDefaultThemeName();
}
