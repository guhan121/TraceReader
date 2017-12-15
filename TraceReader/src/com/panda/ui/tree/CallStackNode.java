package com.panda.ui.tree;

import com.panda.trace.MethodLog;

import javax.swing.*;

public class CallStackNode extends AbstractNode {
    int childNum;
    private boolean needTab = false;

    public CallStackNode(MethodLog m) {
        super(m);
        this.setAllowsChildren(true);
    }

    public CallStackNode(MethodLog m, int childNum) {
        super(m);
        this.childNum = childNum;
        this.setAllowsChildren(true);
    }

    //包含文本和图片的节点构造
    public CallStackNode(Icon icon, MethodLog m) {
        super(icon, m);
        this.setAllowsChildren(true);
    }

    public int getChildNum() {
        return childNum;
    }

    public void setChildNum(int childNum) {
        this.childNum = childNum;
    }

    public boolean isNeedTab() {
        return needTab;
    }

    public void setNeedTab(boolean needTab) {
        this.needTab = needTab;
    }
}
