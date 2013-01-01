package com.maximchuk.ptc;


import com.maximchuk.ptc.ui.ConverterForm;

import java.io.*;
import java.util.Properties;

/**
 * Application launcher with main method
 *
 * @author Maxim L. Maximchuk
 */
public class Launcher {
    private static final String OUTPUT_DIR = "out_theme";

    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                if (args[0].equals("-w")) {
                    // execute GUI mode
                    ConverterForm.show();

                    // redirecting system output to piped stream for reading
                    PipedOutputStream pOut = new PipedOutputStream();
                    System.setOut(new PrintStream(pOut));
                    System.setErr(new PrintStream(pOut));
                    PipedInputStream pIn = new PipedInputStream(pOut);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(pIn));

                    // reading system output
                    while(ConverterForm.isFrameVisible()) {
                        String line = reader.readLine();
                        if (line != null) {
                            ConverterForm.writeConsoleLine(line);
                        }
                    }
                } else {
                    // execute console mode
                    try {
                        Properties props = new Properties();
                        props.load(new FileInputStream(args[0]));
                        ConverterHandler.process(props);
                    } catch (IOException e) {
                        System.err.printf("Properties with name '%s' is not found\n", args[0]);
                    }
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

}
