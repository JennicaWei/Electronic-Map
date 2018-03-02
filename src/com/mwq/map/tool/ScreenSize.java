package com.mwq.map.tool;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenSize {

    private static int width;
    private static int height;
    

    static {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public static void centered(Container container) {
        int w = container.getWidth();
        int h = container.getHeight();
        container.setBounds((width - w) / 2, (height - h) / 2, w, h);
    }
}
