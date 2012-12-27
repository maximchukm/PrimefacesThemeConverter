package com.maximchuk.ptc.parser;

import com.maximchuk.ptc.entity.FileEntity;

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

    private String themeName;
    protected ZipFile zipFile;
    protected ZipEntry cssEntry = null;
    protected List<ZipEntry> imagesEntryList = new ArrayList<ZipEntry>();

    public AbstractThemerollerZipParser(File file) throws IOException {
        zipFile = new ZipFile(file);
    }

    @Override
    public boolean validate() {
        return zipFile.entries().nextElement().getName().contains(getVersion());
    }

    @Override
    public FileEntity getCss() throws IOException {
        if (cssEntry == null) {
            throw new IllegalStateException("Execute parse first!");
        }
        InputStream is = zipFile.getInputStream(cssEntry);
        byte[] data = new byte[is.available()];
        is.read(data);
        return prepareCss(data);
    }

    @Override
    public List<FileEntity> getImages() throws IOException {
        List<FileEntity> images = new ArrayList<FileEntity>();
        for (ZipEntry imageEntry: imagesEntryList) {
            InputStream is = zipFile.getInputStream(imageEntry);
            byte[] data = new byte[is.available()];
            is.read(data);
            String[] filenameParts = imageEntry.getName().split("/");
            images.add(new FileEntity(filenameParts[filenameParts.length - 1], data));
            is.close();
        }
        return images;
    }

    protected abstract FileEntity prepareCss(byte[] data) throws IOException;

    protected abstract String getVersion();

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}
