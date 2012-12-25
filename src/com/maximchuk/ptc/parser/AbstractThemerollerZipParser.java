package com.maximchuk.ptc.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Maxim L. Maximchuk
 *         Date: 25.12.12
 */
public abstract class AbstractThemerollerZipParser implements ThemerollerZipParser {

    protected ZipFile zipFile;
    protected ZipEntry cssEntry = null;
    protected List<ZipEntry> imagesEntryList = new ArrayList<ZipEntry>();

    public AbstractThemerollerZipParser(File file) throws IOException {
        zipFile = new ZipFile(file);
    }

    protected abstract String getVersion();

    @Override
    public boolean validate() {
        return zipFile.entries().nextElement().getName().contains(getVersion());
    }

    @Override
    public InputStream getCssInputStream() throws IOException {
        return zipFile.getInputStream(cssEntry);
    }

    @Override
    public List<InputStream> getImagesInputStreamList() throws IOException {
        List<InputStream> inputStreamList = new ArrayList<InputStream>();
        for (ZipEntry imageEntry: imagesEntryList) {
            inputStreamList.add(zipFile.getInputStream(imageEntry));
        }
        return inputStreamList;
    }
}
