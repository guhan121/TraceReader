package com.panda.ui.menu;

import com.panda.ui.list.ThreadListExt;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 线程列表上的 PopupMenu
 *
 * @author qiantao
 */
public class ThreadListPopupMenu extends JPopupMenu {
    ThreadListExt thread;
    Component focus;
    private JMenuItem copy = null, filter = null, delete = null;

    public ThreadListPopupMenu(ThreadListExt thread) {
        this.thread = thread;
        this.add(copy = new JMenuItem("复制"));
        this.add(filter = new JMenuItem("过滤"));
//	    this.add(delete = new JMenuItem("删除"));
        copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String name = ThreadListPopupMenu.this.thread.getSelectedId();
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(name);
//		        System.out.println(name);
                clip.setContents(tText, null);
            }
        });
        filter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String filterStr = JOptionPane.showInputDialog(ThreadListPopupMenu.this.thread, "输入过滤字符串：");
                if (filterStr == null) {
                    return;
                }
                ThreadListPopupMenu.this.thread.filterToThread(filterStr);
            }
        });
//	    delete.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				String name=ThreadListPopupMenu.this.focus.getCurrentName();
//				 System.out.println(name);
//				 ThreadListPopupMenu.this.thread.deleteSelectedName(name);
//			}
//		});
//	    this.setVisible(true);
    }

    public Component getFocus() {
        return focus;
    }

    public void setFocus(Component focus) {
        this.focus = focus;
    }
}
