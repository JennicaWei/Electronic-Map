/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwq.map.tool;

import javax.swing.JSlider;


public class InstancePool {

    private static MapProcessor mapProcessor;
    private static JSlider scaleSlider;

    public static MapProcessor getMapProcessor() {
        return mapProcessor;
    }

    public static void setMapProcessor(MapProcessor aMapProcessor) {
        mapProcessor = aMapProcessor;
    }

    public static JSlider getScaleSlider() {
        return scaleSlider;
    }

    public static void setScaleSlider(JSlider aScaleSlider) {
        scaleSlider = aScaleSlider;
    }
}
