package com.panda.ui.list;

import com.panda.ui.TraceFrame;
import com.panda.ui.menu.ThreadListPopupMenu;
import com.panda.util.DisplayHelper;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThreadMouseAdapter extends MouseAdapter {
    TraceFrame frame;
    ThreadListExt thread;
    ThreadListPopupMenu pop;

    public ThreadMouseAdapter(TraceFrame frame, ThreadListExt thread) {
        this.frame = frame;
        this.thread = thread;
        pop = new ThreadListPopupMenu(thread);
    }

    public void mouseClicked(MouseEvent e) {

        // TODO Auto-generated method stub
        if (e.getClickCount() == 2) {
            if (frame.getTraceThreads() == null || frame.getTraceThreads().threadId_List == null) {
                return;
            }
            String selectedId = thread.getSelectedId();
            System.out.println("请求线程显示:" + selectedId);
            frame.extendMethod(selectedId, thread.getModel().getFilter());
            //全局记录该id
            DisplayHelper.SelectedThreadId = selectedId;
            DisplayHelper.checkSelctedThreadId();

            ((AbstractTableModel) frame.getMethodTable().getModel()).fireTableDataChanged();
        } else if (e.isMetaDown()) {
            Component c = thread.findComponentAt(e.getX(), e.getY());
            pop.setFocus(c);
            pop.show(e.getComponent(), e.getX(), e.getY());
        }

    }
}
