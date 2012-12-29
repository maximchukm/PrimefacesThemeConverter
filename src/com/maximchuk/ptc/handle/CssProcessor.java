package com.maximchuk.ptc.handle;

import com.maximchuk.ptc.entity.CssPropertyEntity;
import com.maximchuk.ptc.entity.FileEntity;

import java.io.*;

/**
 * @author Maxim L. Maximchuk
 */
public class CssProcessor {

    private String themeName;
    private FileEntity cssEntity;
    private CssPropertyEntity[] cssProps;

    public CssProcessor(String themeName, FileEntity cssEntity, CssPropertyEntity... cssProps) {
        this.themeName = themeName;
        this.cssEntity = cssEntity;
        this.cssProps = cssProps;
    }

    public FileEntity process() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(cssEntity.getData());
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder cssBuilder = new StringBuilder();
        System.out.println("Converting css...");
        while (reader.ready()) {
            String line = reader.readLine();
            if (line.contains("url(")) {
                int startInd = line.indexOf("url(") + 4;
                int endInd = line.indexOf(")");
                String imageName = line.substring(startInd, endInd);
                StringBuilder imageNameBuilder = new StringBuilder("\"#{resource['primefaces-");
                imageNameBuilder.append(themeName).append(":");
                imageNameBuilder.append(imageName).append("']}\"");
                line = line.replace(imageName, imageNameBuilder.toString());
            }
            cssBuilder.append(line);
        }
        reader.close();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(cssBuilder.toString());

        if (cssProps.length > 0) {
            writer.write("/*primefaces customization added from primefaces theme converter*/");
        }

        for (CssPropertyEntity prop: cssProps) {
            System.out.println("adding custom css class for '" + prop.getType().getPropertyKey() + "'");
            StringBuilder cssClassBuilder = new StringBuilder(prop.getType().getCssClass());
            cssClassBuilder.append("{").append(prop.getValue()).append("}");
            writer.write(cssClassBuilder.toString());
        }

        writer.close();
        cssEntity.setData(os.toByteArray());

        return cssEntity;
    }
}
