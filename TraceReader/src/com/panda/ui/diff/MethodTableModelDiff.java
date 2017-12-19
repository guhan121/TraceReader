package com.panda.ui.diff;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MethodTableModelDiff extends AbstractTableModel {

    List<DiffInfo> diffInfoList = new ArrayList<>();
    List<String> header = new ArrayList<String>() {{
        add("类");
        add("方法");
        add("总执行时间一us");
        add("总执行时间二us");
        add("差值us");
    }};

    public MethodTableModelDiff(List<DiffInfo> diffInfoList) {
        this.diffInfoList = diffInfoList;
    }

    @Override
    public String getColumnName(int column) {
        if (column < header.size()) {
            return header.get(column);
        }
        return null;
    }

    /**
     * 获取列对应的数据类型
     *
     * @param column
     * @return
     */
    @Override
    public Class getColumnClass(int column) {
        Class returnValue;
        if ((column >= 0) && (column < getColumnCount()) && getRowCount() > 0) {
            returnValue = getValueAt(0, column).getClass();
        } else {
            returnValue = Object.class;
        }
        return returnValue;
    }

    /**
     * 获取行数
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return diffInfoList.size();
    }

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return header.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        if (columnIndex == 0) {
            return diffInfoList.get(rowIndex).clazz;
        } else if (columnIndex == 1) {
            return diffInfoList.get(rowIndex).m;
        } else if (columnIndex == 2) {
            return diffInfoList.get(rowIndex).t1;
        } else if (columnIndex == 3) {
            return diffInfoList.get(rowIndex).t2;
        } else if (columnIndex == 4) {
            return diffInfoList.get(rowIndex).diff;
        }
        return null;
    }


}