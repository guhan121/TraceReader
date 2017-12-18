package com.panda.ui.menu;

import com.panda.ui.TraceFrame;
import com.panda.ui.tree.AbstractNode;
import com.panda.ui.tree.CallStackTree;
import com.panda.ui.tree.MethodsExtendTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class TreePopupMenu extends JPopupMenu {
    public static final String 显示该节点及子节点时间 = "显示该节点及子节点时间";
    public static final String 隐藏该节点及子节点时间 = "隐藏该节点及子节点时间";
    public static final String 隐藏所有时间 = "隐藏所有时间";
    public static final String 显示所有时间 = "显示所有时间";
    private static final String 展开所有节点 = "展开所有节点";
    private static final String 展开该节点 = "展开该节点";
    JTree tree;
    TreePath focus;
    private JMenuItem copy = null;
//    private JMenuItem hook = null;
//    private JMenuItem rename = null;
//    private JMenuItem fresh = null;
    private JMenuItem showTime = null;
    private JMenuItem hideTime = null;
    private JMenuItem unfold = null;

    public TreePopupMenu(JTree thread) {
        this.tree = thread;
        this.add(unfold = new JMenuItem("展开"));
        this.add(copy = new JMenuItem("复制"));
//        hook = new JMenuItem("添加hook");
        // this.add(hook = new JMenuItem("添加hook"));
//        this.add(rename = new JMenuItem("编辑"));
        this.add(showTime = new JMenuItem("显示该节点及子节点时间"));
        this.add(hideTime = new JMenuItem("隐藏该节点及子节点时间"));
//	    this.add(fresh = new JMenuItem("刷新"));
        copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String name = "";
                if (getSelectPathLength() >= 1) {
                    AbstractNode node = (AbstractNode) focus.getLastPathComponent();
                    if (node != null) {
                        name = node.getM().getFullName();
                    }
                }
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(name);
//		        System.out.println(name);
                clip.setContents(tText, null);
            }
        });

        unfold.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                // 展开所有结点
                if (unfold.getText().equals(展开所有节点)) {
                    TreeNode root = (TreeNode) tree.getModel().getRoot();
                    expandTreeNode(tree, (DefaultMutableTreeNode) root);
//                    expandAll(tree, new TreePath(root), true);
                } else {
                    if (getSelectPathLength() >= 1) {
                        expandTreeNode(tree, (DefaultMutableTreeNode) tree.getLastSelectedPathComponent());
//                        TreeNode node = (TreeNode) focus.getLastPathComponent();
//                        expandAll(tree, new TreePath(node), true);
                    }
                }
            }
        });
//        hook.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // TODO Auto-generated method stub
//                if (getSelectPathLength() >= 1) {
//                    AbstractNode node = (AbstractNode) focus.getLastPathComponent();
//                    if (node != null) {
//                        node.getM().getFullName();
//                    }
//                }
//
//            }
//        });
//        rename.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // TODO Auto-generated method stub
//                if (getSelectPathLength() >= 1) {
//                    AbstractNode node = (AbstractNode) focus.getLastPathComponent();
//                    if (node != null) {
//                        String rename = JOptionPane.showInputDialog("输入新名称：");
//                        if (rename == null) {
//                            return;
//                        }
//                        TraceFrame f;
//                        if (tree instanceof CallStackTree) {
//                            f = ((CallStackTree) tree).getFrame();
//                        } else {
//                            f = ((MethodsExtendTree) tree).getFrame();
//                        }
//                        new Thread(new Change(f, node.getM().getFullName(), rename, node.getM().getOriginFullName())).start();
////						node.getM().setFullName(rename);
//
//                    }
//                }
//
//            }
//        });
        showTime.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (showTime.getText().startsWith(显示所有时间)) {
                    AbstractNode node = (AbstractNode) tree.getModel().getRoot();
                    SetOneNodeAndChildToShowTime(node, true);
                } else {
                    if (getSelectPathLength() >= 1) {
                        AbstractNode node = (AbstractNode) focus.getLastPathComponent();
                        SetOneNodeAndChildToShowTime(node, true);
                    }
                }
                //更新UI，立即显示更新结果
                thread.updateUI();
            }
        });
        hideTime.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (hideTime.getText().startsWith(隐藏所有时间)) {
                    AbstractNode node = (AbstractNode) tree.getModel().getRoot();
                    SetOneNodeAndChildToShowTime(node, false);
                } else {
                    if (getSelectPathLength() >= 1) {
                        AbstractNode node = (AbstractNode) focus.getLastPathComponent();
                        SetOneNodeAndChildToShowTime(node, false);
                    }
                }
                thread.updateUI();
            }
        });
    }


    /**
     * 展开指定节点所有后代节点
     * @param aTree<span style="color: #ff0000;"><strong>展开所有节点，后代节点层层展开</strong></span>
     * @param aNode
     */
    public static void expandTreeNode(JTree aTree, DefaultMutableTreeNode aNode) {
        if (aNode.isLeaf()) {
            return;
        }
        aTree.expandPath(new TreePath( ( (DefaultMutableTreeNode) aNode).getPath()));
        int n = aNode.getChildCount();
        for (int i = 0; i <n; i++) {
            expandTreeNode(aTree, (DefaultMutableTreeNode) aNode.getChildAt(i));
        }
    }

    /**
     * 关闭指定节点所有后代节点
     * @param aTree，<span style="color: #ff0000;"><strong>直接关闭指定节点</strong></span>
     * @param aNode
     */
    public static void collapseTreeNode(JTree aTree, DefaultMutableTreeNode aNode) {
        if (aNode.isLeaf()) {
            return;
        }

        TreePath path=new TreePath(aNode.getPath());
        aTree.collapsePath(path);
    }

    // 展开树的所有节点的方法
    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    private int getSelectPathLength() {
        if (focus == null) {
            return 0;
        }
        return focus.getPath().length;
    }

    private void SetOneNodeAndChildToShowTime(AbstractNode node, boolean isShow) {
        if (node != null) {
            node.setShowTime(isShow);
            Enumeration<AbstractNode> enums = node.preorderEnumeration();
            while (enums.hasMoreElements()) {
                AbstractNode an = (AbstractNode) enums.nextElement();
                if (an.getM().getFullName().startsWith("===")) {
                    continue;
                }
                an.setShowTime(isShow);
            }
        }
    }

    public void isSetAll(Boolean isAll) {
        if (isAll) {
            showTime.setText(显示所有时间);
            hideTime.setText(隐藏所有时间);
            unfold.setText(展开所有节点);
            copy.setEnabled(false);
//            rename.setEnabled(false);
        } else {
            copy.setEnabled(true);
//            rename.setEnabled(true);
            showTime.setText(显示该节点及子节点时间);
            hideTime.setText(隐藏该节点及子节点时间);
            unfold.setText(展开该节点);
        }
    }

    public TreePath getFocus() {
        return focus;
    }

    public void setFocus(TreePath focus) {
        this.focus = focus;
    }

//    class Change implements Runnable {
//        TraceFrame f;
//        String rename;
//        String name;
//        String origin;
//
//        public Change(TraceFrame f, String name, String rename, String origin) {
//            this.f = f;
//            this.rename = rename;
//            this.name = name;
//            this.origin = origin;
//        }
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            f.rename(origin, name, rename);
//        }
//
//    }
}
