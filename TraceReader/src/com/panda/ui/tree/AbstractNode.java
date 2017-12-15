package com.panda.ui.tree;

import com.panda.trace.MethodLog;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 一个抽象的节点，包含一个方法的记录
 */
public class AbstractNode extends DefaultMutableTreeNode {
    protected MethodLog m;
    boolean showTime = false;

    public AbstractNode(MethodLog m) {
        super();
        this.m = m;
    }

    public AbstractNode(Icon icon, MethodLog m) {
        super();
        this.m = m;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public MethodLog getM() {
        return m;
    }

    public void setM(MethodLog m) {
        this.m = m;
    }

    public String getText() {
        return this.m.getFullName();
    }
}
