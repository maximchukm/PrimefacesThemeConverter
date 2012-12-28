package com.maximchuk.ptc;

import com.maximchuk.ptc.handle.ThemeJarBuilder;
import com.maximchuk.ptc.parser.ThemerollerZipParser;
import com.maximchuk.ptc.parser.impl.ThemerollerZipParser192;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Main converter handler
 *
 * @author Maxim L. Maximchuk
 */
public class ConverterHandler {
    public static final String SRC_FILENAME_KEY = "source.filename";
    public static final String THEME_NAME_KEY = "theme.name";
    public static final String OUTPUT_DIR_KEY = "output.dir";

    private static final String DEFAULT_OUTPUT_DIR = "out_theme";

    /**
     * Hided class constructor
     */
    private ConverterHandler() {
        super();
    }

    /**
     * Main processing method
     * generates primefaces theme jar from themeroller jar
     *
     * @param themeProps theme properties
     */
    public static void process(Properties themeProps) {
        String filename = themeProps.getProperty(SRC_FILENAME_KEY);
        String themeName = themeProps.getProperty(THEME_NAME_KEY);
        String outDir = themeProps.getProperty(OUTPUT_DIR_KEY);
        if (filename != null) {
            try {
                ThemerollerZipParser parser = new ThemerollerZipParser192(new File(filename));
                if (parser.validate()) {
                    parser.setThemeName(themeName);
                    if (parser.parse()) {
                        buildTheme(parser, outDir != null? outDir: DEFAULT_OUTPUT_DIR);
                    } else {
                        System.err.println("Invalid themeroller zip");
                    }
                } else {
                    System.err.println("Unsupported themeroller version");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.err.println("Unspecified source file name");
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
