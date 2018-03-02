/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwq.map.calendar;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


public class MTableCell extends JPanel implements TableCellRenderer {

    private static String selectedDay;// 被选中的日期

    public MTableCell() {
        setLayout(new BorderLayout());// 采用边框式布局

    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JButton button = (JButton) value;// 获得单元格中的按钮对象

        if (hasFocus && button.isEnabled()) {// 如果选中了该按钮

            selectedDay = button.getText();// 获得日期

        }
        removeAll();// 移除其他按钮组件

        add(button, BorderLayout.CENTER);// 添加该按钮组件到边框式布局的中心

        return this;// 返回该单元格对象

    }

    public static String getSelectedDay() {// 返回选中的日期

        return selectedDay;
    }

    public static void setSelectedDay(String selectedDay) {// 设置选中的日期

        MTableCell.selectedDay = selectedDay;
    }
}
