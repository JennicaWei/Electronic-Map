/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwq.map.calendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;


public class MButton extends JButton {

    public MButton(int day) {
        super(day + "");
        setBorderPainted(false);// 不绘制边框

        setBackground(Color.WHITE);// 背景色为白色

        setMargin(new Insets(0, 0, 0, 0));// 文本和边框间的距离为0

        setFont(new Font("宋体", Font.BOLD, 14));// 设置字体及样式

    }
}
