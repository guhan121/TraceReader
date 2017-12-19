package com.panda.ui.methodtable;

import com.panda.trace.MethodLog;
import com.panda.ui.TraceFrame;
import com.panda.util.DisplayHelper;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiantao
 */
public class MethodTableModel extends AbstractTableModel {
    TraceFrame frame;

    List<String> header = new ArrayList<String>() {{
        add("序号");
        add("类");
        add("方法");
        add("总执行时间us");
        add("exclusive Time(us)");
    }};

    public MethodTableModel(TraceFrame traceFrame) {
        this.frame = traceFrame;
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
//        System.out.println(getRowCount() );
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
        // TODO Auto-generated method stub
        if (frame.traceThreads != null) {
            //显示当前选择的方法
            int b = DisplayHelper.getFilterMethods(frame.traceThreads).size();
            return b;
        } else {
            return 0;
        }
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
            return rowIndex;
        } else if (columnIndex == 1) {
            //返回方法类名
            if (frame.traceThreads != null) {
                List<MethodLog> filterMethods = DisplayHelper.getFilterMethods(frame.traceThreads);
                if (filterMethods != null && filterMethods.size() != 0 && filterMethods.size() > rowIndex) {
                    String m = filterMethods.get(rowIndex).getMethodDes().getMethodClazz();
                    return m;
                }
                return "";
            }
        } else if (columnIndex == 2) {
            //返回方法名称
            if (frame.traceThreads != null) {
                List<MethodLog> filterMethods = DisplayHelper.getFilterMethods(frame.traceThreads);
                if (filterMethods != null && filterMethods.size() != 0 && filterMethods.size() > rowIndex) {
                    String m = filterMethods.get(rowIndex).getMethodDes().getMethodName();
                    return m;
                }
                return "";
            }
        } else if (columnIndex == 3) {
            List<MethodLog> filterMethods = DisplayHelper.getFilterMethods(frame.traceThreads);
            if (filterMethods != null && filterMethods.size() != 0 && filterMethods.size() > rowIndex) {
                long m = filterMethods.get(rowIndex).getThreadCostTime();
                return m;
            }
            return "";
        } else if (columnIndex == 4) {
            //返回方法执行时间
            List<MethodLog> filterMethods = DisplayHelper.getFilterMethods(frame.traceThreads);
            if (filterMethods != null && filterMethods.size() != 0 && filterMethods.size() > rowIndex) {
                long m = filterMethods.get(rowIndex).getExclusiveTime();
                return m;
            }
            return "";
        }
        return null;
    }


}
