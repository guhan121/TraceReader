package com.panda.ui;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JToolBar{
    JLabel statusJLabel;
    JPanel jPanelFill;
    JProgressBar progressBar;
    public StatusBar() {
        super();
        GridBagConstraints gbdc = new GridBagConstraints();
        gbdc.fill = GridBagConstraints.BOTH;
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。
        gbdc.gridwidth = 1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        gbdc.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        gbdc.weighty = 0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        statusJLabel = new JLabel("拖入Trace文件到工具上面以便进行解析");
        this.add(statusJLabel); //把标签加到工具栏上
        JPanel comp = new JPanel();
        this.add(comp);
        progressBar = new JProgressBar();
        this.add(progressBar);
        layout.setConstraints(statusJLabel, gbdc);
        gbdc.gridwidth = 1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        gbdc.weightx = 1;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        layout.setConstraints(comp, gbdc);
        gbdc.gridwidth = 0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        gbdc.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        layout.setConstraints(progressBar, gbdc);
        this.setFloatable(false);
    }
}
