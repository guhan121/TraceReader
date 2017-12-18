package com.panda.ui.tree;

import com.panda.trace.MethodLog;
import com.panda.trace.TraceThread;
import com.panda.ui.TraceFrame;
import com.panda.ui.menu.TreePopupMenu;
import com.panda.util.MethodUtil;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * 树形结构
 */
public class CallStackTree extends JTree {
    static final CallStackNode ROOT = new CallStackNode(new MethodLog("Method CallStack:"));
    static String mtReg = "";
    static int times;
    TraceFrame frame;
    TreePopupMenu pop;
    private CallStackModel treeModel;
    private String name;

    public CallStackTree(TraceFrame frame) {
        this.frame = frame;
        treeModel = new CallStackModel(ROOT);
        this.setModel(treeModel);
        pop = new TreePopupMenu(this);
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

                if (e.isMetaDown()) {
                    TreePath pathForLocation = CallStackTree.this.getPathForLocation(e.getX(), e.getY());
                    pop.isSetAll(pathForLocation == null);
                    pop.show(e.getComponent(), e.getX(), e.getY());
                    CallStackTree.this.setSelectionPath(pathForLocation);
                    pop.setFocus(pathForLocation);
                }
            }
        });
    }

    public TraceFrame getFrame() {
        return frame;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void fillTree(String reg) {
        if (name == null || name == "") {
            return;
        }
        ROOT.removeAllChildren();
        MethodLog top = TraceThread.topMethod;
        CallStackNode node = new CallStackNode(top);
        treeModel.insertNodeInto(node, ROOT, 0);
        int i = 0;
        for (MethodLog m : top.getChild()) {
            if (m.getRecord().getThreadId() == Integer.parseInt(name)) {
                if (reg != null && !reg.equals("")) {
                    m = MethodUtil.getRealMethod(m, reg);
                }
                if (m.getChild().size() == 0) {
                    continue;
                }
//                System.out.println(m.getFullName() + " " + top.getChild().size() + " " + i + " " + m.getChild().size());
                CallStackNode md = new CallStackNode(m, MethodUtil.getChildNum(m));
//                System.out.println(m.getFullName() + " " + top.getChild().size() + " " + i + " " + m.getChild().size());
                treeModel.insertNodeInto(md, node, i++);
                treeModel.addNode(md, m);
            }
        }
        treeModel.reload();
    }

    /**
     * 搜索后,展开特定的对象
     *
     * @param keyWord
     */
    public void extendTreeMode(String keyWord) {
        /**
         * 关键字未改变的时候每调用一次，表明要搜索下一个
         */
        if (mtReg.equals(keyWord)) {
            times++;
        } else {
            mtReg = keyWord;
            times = 1;
        }
        int n = times;
        Enumeration<CallStackNode> enums = ROOT.preorderEnumeration();
        while (enums.hasMoreElements() && n > 0) {
            CallStackNode node = (CallStackNode) enums.nextElement();
            TreePath path = new TreePath(node.getPath());
            if (node.getText().contains(keyWord)) {
                this.addSelectionPath(path);
                n--;
            } else {
                /**
                 * 从选择列表中移除
                 */
                this.removeSelectionPath(path);
            }
        }
    }

    public void extendMethod(String reg) {
        fillTree(reg);
    }
}
