package com.maximchuk.ptc.parser.impl;

import com.maximchuk.ptc.entity.FileEntity;
import com.maximchuk.ptc.parser.AbstractThemerollerZipParser;
import com.maximchuk.ptc.parser.ThemerollerZipParser;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

/**
 * @author Maxim L. Maximchuk
 *         Date: 25.12.12
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
    protected FileEntity prepareCss(byte[] cssData) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(cssData);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder cssBuilder = new StringBuilder();
        while (reader.ready()) {
            String line = reader.readLine();
            if (line.contains("url(")) {
                int startInd = line.indexOf("url(") + 4;
                int endInd = line.indexOf(")");
                String imageName = line.substring(startInd, endInd);
                StringBuilder imageNameBuilder = new StringBuilder("\"#{resource['primefaces-");
                imageNameBuilder.append(getDefaultThemeName()).append(":");
                imageNameBuilder.append(imageName).append("']}\"");
                line = line.replace(imageName, imageNameBuilder.toString());
            }
            cssBuilder.append(line);
        }
        reader.close();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(cssBuilder.toString());
        writer.close();
        return new FileEntity("theme.css", os.toByteArray());
    }

    @Override
    public String getDefaultThemeName() {
        String[] nameParts = cssEntry.getName().split("/");
        return nameParts[nameParts.length - 2].toLowerCase();
    }
}
