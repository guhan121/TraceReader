package com.panda.trace;

import com.panda.ui.diff.DiffTraceTextPage;
import com.panda.ui.ToolTip;
import com.panda.ui.TraceFrame;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.*;

public class TraceFileHelper {

    public static String fileName = "";
    public static boolean isTraceFileLoadOk = false;

    public static void procFile(TraceFrame traceFrame, File fl) {
        if (fl.getName().endsWith(".trace")) {
            try {
                if (traceFrame.getTraceThreads() != null) {
                    traceFrame.getTraceThreads().reset();
                }
                TraceThread.topMethod.getChild().clear();
                byte[] bytes = BytesHelper.toByteArray(fl.getPath());
                //long current=System.currentTimeMillis();
                Trace.getInstance().divideBytes(bytes);
                //long current1=System.currentTimeMillis();

                traceFrame.setTraceThreads(Trace.getInstance().getThreadList());
                long current2 = System.currentTimeMillis();
                //System.out.println(current1-current);
                //System.out.println(current2-current1);
                //threadList.
                traceFrame.updateUI();
                isTraceFileLoadOk = true;
                fileName = fl.getName();
                traceFrame.setStatusBarTips("如果是混淆后的Trace文件，可直接拖入对应的mapping.txt进行函数替换");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "解析文件出错！", "提示", JOptionPane.OK_OPTION);
                isTraceFileLoadOk = false;
            }
        } else if (fl.getName().equals("mapping.txt")) {
            if (isTraceFileLoadOk) {
                traceFrame.setStatusBarTips("开始处理mapping.txt,耐心等待几秒");
                Trace.getInstance().updateMethodInfo(fl);
                ((AbstractTableModel) traceFrame.getMethodTable().getModel()).fireTableDataChanged();
                traceFrame.setStatusBarTips("mapping.txt文件处理已经完成!!!");
                ToolTip tip = new ToolTip();
                tip.setToolTip(null, "mapping.txt文件处理已经完成!!!");
            } else {
                JOptionPane.showMessageDialog(null, "trace文件未加载！", "提示", JOptionPane.OK_OPTION);
            }
        } else {
            if (traceFrame.tabbedPane.getSelectedIndex() == 3) {
                if (fl.getName().endsWith("_sort.txt")) {
                    //todo  处理文件拖入
                    DiffTraceTextPage.procSortFile(traceFrame, fl);
                } else {
                    JOptionPane.showMessageDialog(null, "比较时文件必须以_sort.txt结尾！", "提示", JOptionPane.OK_OPTION);
                }
            } else {
                JOptionPane.showMessageDialog(null, "先拖进trace文件，再拖进去对应的mapping.txt！", "提示", JOptionPane.OK_OPTION);
            }
        }
    }


}
