package com.maximchuk.ptc;


import com.maximchuk.ptc.parser.ThemerollerZipParser;
import com.maximchuk.ptc.parser.impl.ThemerollerZipParser192;

import java.io.File;

/**
 * @author Maxim L. Maximchuk
 *         Date: 25.12.12
 */
public class Launcher {

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                ThemerollerZipParser parser = new ThemerollerZipParser192(new File(args[0]));
                if (parser.validate()) {
                    parser.parse();
                    System.out.println(parser.getDefaultThemeName());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
