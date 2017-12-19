package com.panda.ui.diff;

import com.panda.ui.ToolTip;
import com.panda.ui.TraceFrame;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DiffTraceTextPage extends JPanel {

    public List<DiffInfo> diffInfoList = new ArrayList<>();
    JCheckBox jCheckBox1;
    JCheckBox jCheckBox2;
    TraceFrame frame;
    JLabel jLabel1;
    JLabel jLabel2;
    JTable mtable;
    JTable mtable2;
    JTable mtable3;
    JButton jButtonDiff;
    String filePath1 = "";
    String filePath2 = "";
    int existedFileNum = 0;

    public DiffTraceTextPage(TraceFrame traceFrame) {
        super();
        BorderLayout border6 = new BorderLayout();

        this.setLayout(border6);
        this.frame = traceFrame;
        JPanel jTop = initDefaultRuleArea();
        this.add(jTop, BorderLayout.NORTH);

        JPanel jCenter = initCenter();
        this.add(jCenter, BorderLayout.CENTER);

        JPanel jButtom = initButtom();
        this.add(jButtom, BorderLayout.SOUTH);

    }


    public static void procSortFile(TraceFrame traceFrame, File fl) {
        DiffTraceTextPage diffPage = traceFrame.getDiffPage();
        Boolean onlyAdd = false;
        if (diffPage.existedFileNum == 0) {
            diffPage.diffInfoList.clear();
            diffPage.existedFileNum = 1;
            diffPage.filePath1 = fl.getPath();
            diffPage.jLabel1.setText("文件一路径:" + diffPage.filePath1);
            onlyAdd = true;
        } else if (diffPage.existedFileNum == 1) {
            diffPage.existedFileNum = 2;
            diffPage.filePath2 = fl.getPath();
            diffPage.jLabel2.setText("文件二路径:" + diffPage.filePath2);
        } else {
            diffPage.existedFileNum = 0;
            procSortFile(traceFrame, new File(diffPage.filePath2));
            procSortFile(traceFrame, fl);
        }
        List<DiffInfo> diffInfoList = diffPage.diffInfoList;
        try {
            FileReader reader = new FileReader(fl.getPath());
            BufferedReader br = new BufferedReader(reader);
            String line = null;
            int cnt = 0;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split("\\t");
                cnt++;
                if (strs.length == 3) {
                    long i = Long.parseLong(strs[0].trim());
                    if (diffPage.jCheckBox1.isSelected() && i < 5000) {
                        continue;
                    }
                    if (onlyAdd) {
                        diffInfoList.add(new DiffInfo(strs[1], strs[2], i, -1L));
                        continue;
                    }
                    boolean isFind = false;
                    for (DiffInfo info : diffInfoList) {
                        if (info.clazz.equals(strs[1]) && info.m.equals(strs[2])) {
                            isFind = true;
                            info.t2 = i;

                            info.diff = info.t1 - info.t2;
                            break;
                        }
                    }
                    if (!isFind) {
                        diffInfoList.add(new DiffInfo(strs[1], strs[2], -1L, i));
                    }
                } else {
                    diffPage.reset();
                    traceFrame.setStatusBarTips("文件中有错误:" + fl.getPath());
                    ToolTip tip = new ToolTip();
                    tip.setToolTip(null, "文件中有错误:\n" + "按\\t分割[" + fl.getPath() + "]时在行 " + cnt + "分割后的数量不为3," + strs.length + "!!!");
                }
            }
            br.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        ((AbstractTableModel) traceFrame.getDiffPage().mtable.getModel()).fireTableDataChanged();
    }

    private void reset() {
        diffInfoList.clear();
        jLabel1.setText("第一个文件路径:");
        jLabel2.setText("第二个文件路径:");
        filePath1 = "";
        filePath2 = "";
        existedFileNum = 0;
    }

    public int getfileNum() {
        return existedFileNum;
    }


    private JPanel initCenter() {
        BorderLayout border7 = new BorderLayout();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(border7);

        JPanel jPanel1 = initTableTop();
        jPanel.add(jPanel1, BorderLayout.NORTH);

        MethodTableModelDiff model = new MethodTableModelDiff(diffInfoList);
        mtable = new JTable(model);
        int minWidth = 40;
        int maxWidth = 80;
        mtable.getColumnModel().getColumn(0).setPreferredWidth(80);
        mtable.getColumnModel().getColumn(1).setPreferredWidth(80);

        mtable.getColumnModel().getColumn(2).setPreferredWidth(maxWidth + minWidth);
        mtable.getColumnModel().getColumn(2).setMinWidth(minWidth + minWidth);
        mtable.getColumnModel().getColumn(2).setMaxWidth(maxWidth * 3);

        mtable.getColumnModel().getColumn(3).setPreferredWidth(maxWidth + minWidth);
        mtable.getColumnModel().getColumn(3).setMinWidth(minWidth + minWidth);
        mtable.getColumnModel().getColumn(3).setMaxWidth(maxWidth * 3);

        mtable.getColumnModel().getColumn(4).setPreferredWidth(maxWidth + minWidth);
        mtable.getColumnModel().getColumn(4).setMaxWidth(maxWidth * 2);
        mtable.getColumnModel().getColumn(4).setMinWidth(minWidth);
        RowSorter sorter = new TableRowSorter(model);
        mtable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(mtable);
        jPanel.add(scrollPane, BorderLayout.CENTER);
        return jPanel;
    }

    private JPanel initTableTop() {
        BorderLayout border7 = new BorderLayout();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(border7);

        jLabel1 = new JLabel("第一个文件路径:");
        jLabel2 = new JLabel("第二个文件路径:");

        jPanel.add(jLabel1, BorderLayout.NORTH);
        jPanel.add(jLabel2, BorderLayout.SOUTH);
        return jPanel;
    }

    private JPanel initButtom() {
        BorderLayout border7 = new BorderLayout();
        JPanel jTop = new JPanel();
        jTop.setLayout(border7);
        jTop.add(new JLabel("-1表示该值不存在,没有任何意义"), BorderLayout.SOUTH);
//        jButtonDiff = new JButton("开始比较");
//        jButtonDiff.setEnabled(false);
//        jTop.add(jButtonDiff, BorderLayout.WEST);

        return jTop;
    }

    public JPanel initDefaultRuleArea() {
        BorderLayout border7 = new BorderLayout();
        JPanel jTop = new JPanel();
        jTop.setLayout(border7);
        JPanel jp = intRule2();
        jTop.add(jp, BorderLayout.WEST);
        return jTop;
    }

    private JPanel intRule2() {
        BorderLayout border7 = new BorderLayout();
        JPanel jTop = new JPanel();
        jTop.setLayout(border7);
        jCheckBox1 = new JCheckBox("忽略5000us以下的函数调用");
        jCheckBox1.setSelected(true);
        jTop.add(jCheckBox1, BorderLayout.NORTH);

//        jCheckBox2 = new JCheckBox("忽略5000us以下的时间差异");
//        jCheckBox2.setSelected(true);
//        jTop.add(jCheckBox2, BorderLayout.SOUTH);
        return jTop;
    }
}
