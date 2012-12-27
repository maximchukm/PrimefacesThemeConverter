package com.maximchuk.ptc;


import com.maximchuk.ptc.handle.ThemeJarBuilder;
import com.maximchuk.ptc.parser.ThemerollerZipParser;

import java.io.IOException;

/**
 * @author Maxim L. Maximchuk
 *         Date: 25.12.12
 */
public class Launcher {

    public static void main(String[] args) {
        if (args.length > 0) {
            String themeName = null;
            if (args.length > 1) {
                themeName = args[1];
            }
            ConverterHandler.process(args[0], themeName, "out_theme");
        }
    }

}
