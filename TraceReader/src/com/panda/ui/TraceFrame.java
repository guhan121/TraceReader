package com.panda.ui;

import com.panda.trace.MethodLog;
import com.panda.trace.ThreadList;
import com.panda.trace.TraceThread;
import com.panda.ui.combo.OrderComboBox;
import com.panda.ui.diff.DiffTraceTextPage;
import com.panda.ui.drop.DropTargetAdapterExt;
import com.panda.ui.list.ThreadListExt;
import com.panda.ui.menu.TraceMenuBar;
import com.panda.ui.methodtable.MethodTableJPanel;
import com.panda.ui.textfield.FilterTextField;
import com.panda.ui.tree.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.List;
import java.util.TooManyListenersException;

public class TraceFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    MethodTableJPanel methodTableJPanel;
    StatusBar statusBar;
    JTextField searchField;
    public ThreadList traceThreads;
    String filter = "";
    int hooks = 0;
    int renames = 0;
    JList threadList;
    JTree callstackTree, mTree;
    public JTabbedPane tabbedPane;
    String keyLast = "";
    int max_times;
    private DropTarget dropTarget;
    private JPanel jp0, jp1;
    private JSplitPane jspMain;
    DiffTraceTextPage diffTraceTextPage;
    public TraceFrame() throws TooManyListenersException {
        this.setTitle("Trace文件分析工具 V1.2");
        Toolkit tk = this.getToolkit();
        Dimension dm = tk.getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation((int) (dm.getWidth() - 1600) / 2,
                (int) (dm.getHeight() - 800) / 2);
        this.setSize(1600, 800);
        setMinimumSize(new Dimension(600, 400));
        initJMenu();
        initArea();

        //实现拖拽打开
        dropTarget = new DropTarget();
        dropTarget.setComponent(this);
        dropTarget.addDropTargetListener(new DropTargetAdapterExt(this));
        this.setVisible(true);
    }

    public static int getChildNum(MethodLog log) {
        int sum = log.getChild().size();
        if (sum == 0) {
            return sum;
        }
        for (MethodLog m1 : log.getChild()) {
            sum = sum + getChildNum(m1);
        }
        return sum;
    }

    public ThreadList getTraceThreads() {
        return traceThreads;
    }

    public void setTraceThreads(ThreadList traceThreads) {
        this.traceThreads = traceThreads;
    }

    public void initJMenu() {
        this.setJMenuBar(new TraceMenuBar(this));
    }

    public void initArea() {
        //combox
        jp0 = new JPanel();
        jp1 = new JPanel();
        BorderLayout border = new BorderLayout();
        jp1.setLayout(border);
        jp1.add(new OrderComboBox(this), BorderLayout.NORTH);
        BorderLayout border0 = new BorderLayout();
        jp0.setLayout(border0);

        //splitpane
        jspMain = new JSplitPane();
        jspMain.setDividerLocation(200);

        //threadlist界面
        threadList = new ThreadListExt(this);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(threadList);
        jp1.add(jScrollPane, BorderLayout.CENTER);
        jspMain.setLeftComponent(jp1);

        //callstack
        callstackTree = new CallStackTree(this);
        callstackTree.setCellRenderer(new CallStackRender());
        callstackTree.setBorder(BorderFactory.createTitledBorder(getBoderName(0)));
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(callstackTree);

        //Method list Tree
        mTree = new MethodsExtendTree(this);
        mTree.setCellRenderer(new MethodRenderer());
        mTree.setBorder(BorderFactory.createTitledBorder(getBoderName(1)));


        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Trace树形结构", jScrollPane1);
        JScrollPane jspMtree = new JScrollPane(mTree);
        final JScrollBar jsb = jspMtree.getVerticalScrollBar();
        jspMtree.setVerticalScrollBar(jsb);
//		jspMtree.setVerticalScrollBarPolicy(
//	                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsb.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                // TODO Auto-generated method stub
                if (((MethodsExtendModel) mTree.getModel()).getWhere() != -1 && jsb.getValue() > jsb.getMaximum() * 0.9) {
//					System.out.println(mModel.getWhere());
                    jsb.setValue((int) (jsb.getMaximum() * 0.5));
                    ((MethodsExtendTree) mTree).reloadMMode(((MethodsExtendModel) mTree.getModel()).getMethods(), ((MethodsExtendModel) mTree.getModel()).getWhere(), searchField.getText());
                }

            }

        });

        tabbedPane.addTab("Trace方法列表", jspMtree);
        methodTableJPanel = new MethodTableJPanel(this);
        tabbedPane.addTab("Trace方法集合", methodTableJPanel);
        diffTraceTextPage = new DiffTraceTextPage(this);
        tabbedPane.addTab("比较不同版本数据", diffTraceTextPage);
        jspMain.setRightComponent(tabbedPane);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                if (tabbedPane.getSelectedIndex() == 1) {
                    ((MethodsExtendTree) mTree).reloadMMode(0, "");
                    return;
                }

                if(tabbedPane.getSelectedIndex() == 3) {
                    JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                    int index = sourceTabbedPane.getSelectedIndex();
                    System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
                    setStatusBarTips("连续拖入两个文件进行比较");
                    return;
                }
                else{

                }
            }

        });

        JPanel jp3 = new JPanel();
        jp3.setBorder(BorderFactory.createTitledBorder("查  找"));
        BorderLayout border3 = new BorderLayout();
        jp3.setLayout(border3);
        searchField = new FilterTextField(this);
        jp3.add(searchField, BorderLayout.CENTER);

        jp0.add(jspMain, BorderLayout.CENTER);
        jp0.add(jp3, BorderLayout.SOUTH);
        this.getContentPane().add(jp0);
        initStatusBar();

    }

    public void initStatusBar() {
        statusBar = new StatusBar();
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    public File chooseFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showDialog(new JLabel(), "选择");
        File file = jfc.getSelectedFile();
        return file;
    }

    public void updateUI() {
        threadList.updateUI();
    }

    public void extendMethod(String threadId, String reg) {
        callstackTree.setName(threadId);
        ((CallStackTree) callstackTree).extendMethod(reg);
        TraceThread thread = traceThreads.getThreadId_thread_map().get(threadId);
        List<MethodLog> methods = thread.getMethodLogs();
        ((MethodsExtendModel) mTree.getModel()).setMethods(methods);
        if (tabbedPane.getSelectedIndex() == 1) {
            ((MethodsExtendTree) mTree).reloadMMode(0, reg);
        }
    }

    public void extendMethod(String reg) {
        ((CallStackTree) callstackTree).extendMethod(reg);
        TraceThread thread = traceThreads.getThreadId_thread_map().get(callstackTree.getName());
        List<MethodLog> methods = thread.getMethodLogs();
        ((MethodsExtendModel) mTree.getModel()).setMethods(methods);
        if (tabbedPane.getSelectedIndex() == 1) {
            ((MethodsExtendTree) mTree).reloadMMode(0, reg);
        }
    }

    public void evalSearch(String keyWord) {
        if (keyWord == null || keyWord.isEmpty()) {
            return;
        }
        if (tabbedPane.getSelectedIndex() == 0) {
            ((CallStackTree) callstackTree).extendTreeMode(keyWord);
        } else if (tabbedPane.getSelectedIndex() == 1) {
            ((MethodsExtendTree) mTree).reloadMMode(0, keyWord);
        } else if (tabbedPane.getSelectedIndex() == 2) {
            findInTable(getMethodTable(), keyWord);
        }
    }

    private void findInTable(JTable table, String str) {
        if (str.equals(keyLast)) {
            max_times++;
        } else {
            keyLast = str;
            max_times = 1;
        }
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        int cnt = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 1; j < columnCount; j++) {
                Object value = table.getValueAt(i, j);
                if (value.toString().contains(str)) {
                    cnt++;
                    if (cnt == max_times) {
                        table.getSelectionModel().setSelectionInterval(i, i);
                        Rectangle cellRect = table.getCellRect(i, 1, true);
                        table.scrollRectToVisible(cellRect);
                        return;
                    }
                }
            }
        }
    }

    public void addFilterName(String reg) {
        filter = reg;
        reloadLabelName();
        if (tabbedPane.getSelectedIndex() == 1) {
            ((MethodsExtendTree) mTree).reloadMMode(0, "");
        } else if (tabbedPane.getSelectedIndex() == 0) {
            ((CallStackTree) callstackTree).fillTree(reg);
        }
    }

    private void reloadLabelName() {
        mTree.setBorder(BorderFactory.createTitledBorder(getBoderName(1)));
        callstackTree.setBorder(BorderFactory.createTitledBorder(getBoderName(0)));
    }

    public String getBoderName(int i) {
        if (i == 0) {
            return "调用关系<显示字符:" + (filter.equals("") ? "all" : filter) + ",添加hook函数:" + hooks + ",重命名:" + renames + ">";
        } else if (i == 1) {
            return "方法列表<显示字符:" + (filter.equals("") ? "all" : filter) + ",添加hook函数:" + hooks + ",重命名:" + renames + ">";
        } else {
            return "uknown";
        }
    }

    public JTable getMethodTable() {
        return methodTableJPanel.getMethodTable();
    }

    public void setStatusBarTips(String s) {
        getStatusBarJLabel().setText(s);
    }

    private JLabel getStatusBarJLabel() {
        return this.statusBar.statusJLabel;
    }

    public JProgressBar getJProgressBar() {
        return this.statusBar.progressBar;
    }

    public DiffTraceTextPage getDiffPage() {
        return this.diffTraceTextPage;
    }
}
