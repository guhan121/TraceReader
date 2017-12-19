package com.panda.ui.methodtable;

import com.panda.trace.TraceFileHelper;
import com.panda.ui.ToolTip;
import com.panda.ui.TraceFrame;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MethodTableJPanel extends JPanel {
    TraceFrame frame;
    JTable mtable;
    JButton 导出数据;

    public MethodTableJPanel(TraceFrame frame) {
        super();
        this.frame = frame;
        MethodTableModel model = new MethodTableModel(frame);
        mtable = new JTable(model);
        int minWidth = 40;
        int maxWidth = 80;
        mtable.getColumnModel().getColumn(0).setPreferredWidth(maxWidth);
        mtable.getColumnModel().getColumn(0).setMaxWidth(maxWidth);
        mtable.getColumnModel().getColumn(0).setMinWidth(minWidth);
        mtable.getColumnModel().getColumn(1).setPreferredWidth(80);
        mtable.getColumnModel().getColumn(2).setPreferredWidth(80);
        mtable.getColumnModel().getColumn(3).setPreferredWidth(maxWidth + minWidth);
        mtable.getColumnModel().getColumn(3).setMinWidth(minWidth + minWidth);
        mtable.getColumnModel().getColumn(3).setMaxWidth(maxWidth * 3);

        mtable.getColumnModel().getColumn(4).setPreferredWidth(maxWidth + minWidth);
        mtable.getColumnModel().getColumn(4).setMaxWidth(maxWidth * 2);
        mtable.getColumnModel().getColumn(4).setMinWidth(minWidth);
        RowSorter sorter = new TableRowSorter(model);
        mtable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(mtable);

        JPanel jp4_1 = new JPanel();
        BorderLayout border6_1 = new BorderLayout();
        jp4_1.setLayout(border6_1);
        jp4_1.add(new JPanel(), BorderLayout.CENTER);
        导出数据 = new JButton("按执行总时间导出数据");
        导出数据.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (TraceFileHelper.isTraceFileLoadOk) {
                    try {
                        if (mtable.getModel().getRowCount() <= 0) {
                            ToolTip tip = new ToolTip();
                            tip.setToolTip(null, "无数据");
                            return;
                        }
                        String first = System.getProperty("user.dir") + File.separator + TraceFileHelper.fileName.replace(".trace", "_sort.txt");
                        exportTable(mtable, new File(first));
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    ToolTip tip = new ToolTip();
                    tip.setToolTip(null, "Trace文件没有导入");
                }
            }
        });

        jp4_1.add(导出数据, BorderLayout.WEST);

        BorderLayout border6 = new BorderLayout();
        this.setLayout(border6);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(jp4_1, BorderLayout.SOUTH);
    }

    private void exportTable(JTable table, File file) throws IOException {
        TableModel model = table.getModel();
        FileWriter out = new FileWriter(file);


        Map<String, Integer> map = new HashMap<>();

        ArrayList<String> arrayList = new ArrayList<String>() {{
            add("总执行时间us");
            add("类");
            add("方法");
        }};
        for (int i = 0; i < model.getColumnCount(); i++) {
            int finalI = i;
            arrayList.forEach(key -> {
                if (model.getColumnName(finalI).equals(key)) {
                    map.put(key, finalI);
                }
            });
        }

        if (map.size() != arrayList.size()) {
            ToolTip tip = new ToolTip();
            tip.setToolTip(null, "数据异常，无法导出，联系作者！");
            return;
        }


        Map<String, Long> map2 = new HashMap<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String s = model.getValueAt(i, map.get(arrayList.get(1))) + "\t"
                    + model.getValueAt(i, map.get(arrayList.get(2)));
            Long time = (Long) model.getValueAt(i, map.get(arrayList.get(0)));
            if (map2.containsKey(s)) {
                int i1 = map2.get(s).intValue();
                map2.put(s, i1 + time);
            } else {
                map2.put(s, time);
            }
        }
        // 升序比较器
        Comparator<Map.Entry<String, Long>> valueComparator = new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                long l = o2.getValue() - o1.getValue();
                if (l < 0) {
                    return -1;
                } else if (l == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        // map转换成list进行排序
        List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String, Long>>(map2.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, valueComparator);


        String s = list.get(0).getValue().toString();
        System.out.println(s);
        int length = s.length();
        String format = "%-" + length + "d";

        for (Map.Entry<String, Long> entry : list) {
            out.write(String.format(format, entry.getValue()) + "\t" + entry.getKey() + "\n");
        }
        out.close();
        ToolTip tip = new ToolTip();
        String msg = "导出完成:" + file.getPath();
        tip.setToolTip(null, msg);
        frame.setStatusBarTips(msg);
    }

    public JTable getMethodTable() {
        return this.mtable;
    }
}
