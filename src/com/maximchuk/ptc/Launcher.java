package com.maximchuk.ptc;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Application launcher with main method
 *
 * @author Maxim L. Maximchuk
 */
public class Launcher {
    private static final String OUTPUT_DIR = "out_theme";

    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equals("-w")) {

            } else {
                try {
                    Properties props = new Properties();
                    props.load(new FileInputStream(args[0]));
                    ConverterHandler.process(props);
                } catch (IOException e) {
                    System.err.printf("Properties with name '%s' is not found\n", args[0]);
                } catch (Exception e) {
                    System.err.println("Incorrect properties format");
                }
            }
        }
    }

}
