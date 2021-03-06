package com.maximchuk.ptc;

import com.maximchuk.ptc.entity.CssPropertyEntity;
import com.maximchuk.ptc.entity.CssPropertyEnum;
import com.maximchuk.ptc.handle.CssProcessor;
import com.maximchuk.ptc.handle.ThemeJarBuilder;
import com.maximchuk.ptc.parser.ThemerollerZipParser;
import com.maximchuk.ptc.parser.impl.ThemerollerZipParser192;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private static final String DEFAULT_OUTPUT_DIR = "theme_out";

    private static String themeName;
    private static List<CssPropertyEntity> additionalCssProperties;

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
        // reading properties
        String filename = themeProps.getProperty(SRC_FILENAME_KEY);
        String outDir = themeProps.getProperty(OUTPUT_DIR_KEY);
        themeName = themeProps.getProperty(THEME_NAME_KEY);

        additionalCssProperties = new ArrayList<CssPropertyEntity>();
        for (CssPropertyEnum cssProps: CssPropertyEnum.values()) {
            if (themeProps.getProperty(cssProps.getPropertyKey()) != null) {
                additionalCssProperties.add(new CssPropertyEntity(cssProps,
                        themeProps.getProperty(cssProps.getPropertyKey())));
            }
        }

        // converting theme
        if (filename != null && !filename.isEmpty()) {
            try {
                ThemerollerZipParser parser = new ThemerollerZipParser192(new File(filename));
                if (parser.validate()) {
                    if (parser.parse()) {
                        buildTheme(parser, outDir != null? outDir: DEFAULT_OUTPUT_DIR);
                    } else {
                        System.err.println("Invalid themeroller zip");
                    }
                } else {
                    System.err.println("Unsupported themeroller version");
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Source file '" + filename + "' not found");
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
        if (themeName == null) {
            themeName = parser.getThemeName();
        }
        System.out.println("Theme: " + themeName);
        CssProcessor cssProcessor = new CssProcessor(themeName, parser.getCss(),
                additionalCssProperties.toArray(new CssPropertyEntity[additionalCssProperties.size()]));
        ThemeJarBuilder jarBuilder = new ThemeJarBuilder(cssProcessor.process(), parser.getImages());
        jarBuilder.build(outDir, themeName);
    }

}
