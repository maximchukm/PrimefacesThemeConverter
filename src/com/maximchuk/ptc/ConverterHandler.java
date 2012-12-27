package com.maximchuk.ptc;

import com.maximchuk.ptc.handle.ThemeJarBuilder;
import com.maximchuk.ptc.parser.ThemerollerZipParser;
import com.maximchuk.ptc.parser.impl.ThemerollerZipParser192;

import java.io.File;
import java.io.IOException;

/**
 * Main static handler
 *
 * @author Maxim L. Maximchuk
 *         Date: 27.12.12
 */
public class ConverterHandler {

    /**
     * Hided class constructor
     */
    private ConverterHandler() {
        super();
    }

    /**
     * Main processing method
     *
     * @param sourceFilename source themeroller zip file name
     * @param themeName your theme preferred name, if null then making from source default name
     * @param outDir result output directory
     */
    public static void process(String sourceFilename, String themeName, String outDir) {
        try {
            ThemerollerZipParser parser = new ThemerollerZipParser192(new File(sourceFilename));
            if (parser.validate()) {
                parser.setThemeName(themeName);
                if (parser.parse()) {
                    buildTheme(parser, outDir);
                } else {
                    System.err.println("Invalid themeroller zip");
                }
            } else {
                System.err.println("Unsupported themeroller version");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Building theme
     *
     * @param parser themeroller zip parser
     * @param outDir result output directory
     * @throws IOException
     */
    private static void buildTheme(ThemerollerZipParser parser, String outDir) throws IOException {
        System.out.println("Theme: " + parser.getThemeName());
        ThemeJarBuilder jarBuilder = new ThemeJarBuilder(parser.getCss(), parser.getImages());
        jarBuilder.build(outDir, parser.getThemeName());
    }

}
