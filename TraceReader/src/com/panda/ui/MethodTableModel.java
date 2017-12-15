package com.panda.ui;

import com.panda.trace.MethodLog;
import com.panda.util.DisplayHelper;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author qiantao
 */
public class MethodTableModel extends AbstractTableModel {
    TraceFrame log;

    public MethodTableModel(TraceFrame log) {
        this.log = log;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "序号";
        } else if (column == 1) {
            return "方法";
        } else if (column == 2) {
            return "运行时间(包含调用者的时间,us)";
        }
        return null;
    }

    @Override
    public Class getColumnClass(int column) {
        Class returnValue;
        if ((column >= 0) && (column < getColumnCount())) {
            returnValue = getValueAt(0, column).getClass();
        } else {
            returnValue = Object.class;
        }
        return returnValue;
    }

    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
//        System.out.println(TraceFrame.getChildNum(log));
        if (log.traceThreads != null) {
            //显示当前选择的方法
            int a = log.traceThreads.getAll_mlFullName().size();
            int b = DisplayHelper.getFilterMethods(log.traceThreads).size();
            System.out.println("a：" + a);
            System.out.println("b：" + b);
            return b;
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        if (columnIndex == 0) {
            return rowIndex;
        } else if (columnIndex == 1) {
            //返回方法名称
            if (log.traceThreads != null) {
                List<MethodLog> filterMethods = DisplayHelper.getFilterMethods(log.traceThreads);
                if (filterMethods != null && filterMethods.size() != 0 && filterMethods.size() > rowIndex) {
                    String m = filterMethods.get(rowIndex).getFullName();
                    return m;
                }
                return "";
            }
        } else if (columnIndex == 2) {
            //返回方法执行时间
            List<MethodLog> filterMethods = DisplayHelper.getFilterMethods(log.traceThreads);
            if (filterMethods != null && filterMethods.size() != 0 && filterMethods.size() > rowIndex) {
                long m = filterMethods.get(rowIndex).getThreadCostTime();
                return m;
            }
            return "";
        }
        return null;
    }

}
