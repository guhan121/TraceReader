package com.panda.ui.tree;

import com.panda.trace.MethodLog;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * 节点渲染器
 *
 * @author qiantao
 */
public class CallStackRender extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        System.out.println("value:" + value + "\tsel:" + sel + "\texpanded:" + expanded + "\tleaf:" + leaf + "\trow:" + row + "\thasFocus:" + hasFocus);
        MethodLog m = ((CallStackNode) value).getM();
        setOpaque(true);
        setForeground(Color.BLUE);
//        setOpaque(false);
        setBackgroundSelectionColor(Color.LIGHT_GRAY);
        String text = m.getFullName();
        text = text + "(" + ((CallStackNode) value).getChildNum() + ")";
        text = ((CallStackNode) value).showTime ? (text + "    <threadTime:" + (m.getThreadCostTime() * 1.0) / 1000 + "ms/wallTime:" + (m.getWallCostTime() * 1.0) / 1000 + "ms>") :
                text;
        if (((CallStackNode) value).isNeedTab()) {
            setOpaque(true);
            setForeground(Color.RED);
            setBackground(Color.LIGHT_GRAY);
            return this;
        }
        setIcon(null);
        setText(text);
//       if(((CallStackNode) value).showTime){
//    	   String time=(" <threadTime:"+(m.getThreadCostTime()*1.0)/1000+"s/wallTime:"+(m.getWallCostTime()*1.0)/1000+"s>");
//    	   JLabel l=new JLabel(time);
//    	   l.setForeground(Color.BLACK);
//       }
        return this;
    }
}