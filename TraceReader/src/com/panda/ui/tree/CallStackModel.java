package com.panda.ui.tree;

import com.panda.trace.MethodLog;
import com.panda.util.MethodUtil;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class CallStackModel extends DefaultTreeModel {
    public CallStackModel(TreeNode root) {
        super(root);
        // TODO Auto-generated constructor stub
    }

    /**
     * 添加节点的实际操作
     *
     * @param node
     * @param log
     */
    public void addNode(CallStackNode node, MethodLog log) {
        int i = 0;
        for (MethodLog m1 : log.getChild()) {
            CallStackNode node1 = null;
            if (m1.isNeedPass()) {
                continue;
            }

            if (!m1.isSelfNeedPass()) {
                node1 = new CallStackNode(m1, MethodUtil.getChildNum(m1));
                this.insertNodeInto(node1, node, i++);
            }
            if (m1.getChild().size() == 0) {
//                System.out.println(m1.getFullName() + ">>>>no child!");
                continue;
            } else {
                if (node1 == null) {
                    node1 = node;
                }
                addNode(node1, m1);
            }
        }
    }

}
