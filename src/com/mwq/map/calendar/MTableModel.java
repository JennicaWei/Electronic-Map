/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwq.map.calendar;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;


public class MTableModel extends AbstractTableModel {

    private final String[] columnNames;// 表格列名数组
    private final Object[][] tableDatas;// 表格数据数组

    public MTableModel(String[] columnNames, Object[][] tableDatas) {
        super();
        this.columnNames = columnNames;
        this.tableDatas = tableDatas;
    }

    public int getRowCount() {// 返回表格行数

        return tableDatas.length;
    }

    public int getColumnCount() {// 返回表格列数

        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {// 返回指定单元格的值

        return tableDatas[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {// 设置指定单元格的值

        tableDatas[rowIndex][columnIndex] = aValue;
    }

    @Override
    public String getColumnName(int column) {// 返回指定列的名称

        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {// 返回指定列值的类型

        return JButton.class;// 为按钮组件类型

    }
}
