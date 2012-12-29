package com.maximchuk.ptc.parser.impl;

import com.maximchuk.ptc.parser.AbstractThemerollerZipParser;
import com.maximchuk.ptc.parser.ThemerollerZipParser;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

/**
 * Themeroller zip parser implementation for version 1.9.2
 *
 * @author Maxim L. Maximchuk
 */
public class ThemerollerZipParser192 extends AbstractThemerollerZipParser implements ThemerollerZipParser {

    public ThemerollerZipParser192(File file) throws IOException {
        super(file);
    }

    @Override
    protected String getVersion() {
        return "jquery-ui-1.9.2";
    }

    @Override
    public boolean parse() {
        if (validate()) {
            Enumeration<? extends ZipEntry> entryEnumeration = zipFile.entries();
            while (entryEnumeration.hasMoreElements()) {
                ZipEntry entry = entryEnumeration.nextElement();
                String entryName = entry.getName();
                if (entryName.contains(".css") && !entryName.contains(".min")
                        && !entryName.contains("development-bundle")) {
                    cssEntry = entry;
                }
                if (entryName.contains("images") && entryName.contains("css")) {
                    imagesEntryList.add(entry);
                }
            }
        }
        return cssEntry != null && !imagesEntryList.isEmpty();
    }

    @Override
    public String getThemeName() {
        checkCssEntry();
        String[] nameParts = cssEntry.getName().split("/");
        return nameParts[nameParts.length - 2].toLowerCase();
    }
}
