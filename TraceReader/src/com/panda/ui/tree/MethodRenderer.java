package com.panda.ui.tree;

import com.panda.trace.MethodLog;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

@SuppressWarnings("serial")
public class MethodRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); //调用父类的该方法
        MethodLog m = ((MethodNode) value).getM(); //从节点读取文本
        setForeground(Color.BLUE);
        setBackgroundSelectionColor(Color.LIGHT_GRAY);
        setOpaque(false);
        String text = ((MethodNode) value).isRootNode() ? (((MethodNode) value).getPos() + "\t" + m.getFullName()) : m.getFullName();
        text = ((MethodNode) value).showTime ? (text + "    <threadTime:" + (m.getThreadCostTime() * 1.0) / 1000 + "s/wallTime:" + (m.getWallCostTime() * 1.0) / 1000 + "s>") :
                text;

        if (((MethodNode) value).isNeedTab()) {
            setOpaque(true);
            setForeground(Color.RED);
            setBackground(Color.LIGHT_GRAY);
        }
        setIcon(null);
        setText(text);//设置文本
        return this;
    }
}
