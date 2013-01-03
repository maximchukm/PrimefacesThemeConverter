package com.maximchuk.ptc.helper;

import javax.swing.*;
import java.awt.*;

/**
 * @author Maxim L. Maximchuk
 */
public class UIHelper {

    private UIHelper() {
        super();
    }

    public static void centerWindow(Window window) {
        int x = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getCenterPoint().x
                - window.getWidth() / 2;
        int y = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getCenterPoint().y
                - window.getHeight() / 2;
        window.setLocation(x, y);
    }

    public static String getAppTitle() {
        return "Primefaces theme converter (ver." + AppHelper.getVersion() +")";
    }

    public static void setIcon(Window window) {
        window.setIconImage(new ImageIcon(window.getClass().getResource("/com/maximchuk/ptc/ui/icon/icon.png")).getImage());
    }
}
