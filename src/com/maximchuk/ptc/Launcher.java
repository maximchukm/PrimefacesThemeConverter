package com.maximchuk.ptc;


/**
 * Application launcher with main method
 *
 * @author Maxim L. Maximchuk
 */
public class Launcher {
    private static final String OUTPUT_DIR = "out_theme";

    public static void main(String[] args) {
        if (args.length > 0) {
            String themeName = null;
            if (args.length > 1) {
                themeName = args[1];
            }
            ConverterHandler.process(args[0], themeName, OUTPUT_DIR);
        }
    }

}
