package com.panda.ui.list;

import com.panda.ui.TraceFrame;

import javax.swing.*;

public class ThreadListExt extends JList {
    TraceFrame frame;
    ThreadJListModel model;

    public ThreadListExt(TraceFrame frame) {
        this.frame = frame;
        model = new ThreadJListModel(frame);
        this.setModel(model);
        this.addMouseListener(new ThreadMouseAdapter(frame, this));
    }

    public String getSelectedId() {
        if (frame.getTraceThreads() == null || frame.getTraceThreads().threadId_List == null) {
            return "";
        }
        return frame.getTraceThreads().threadId_List.get(this.getSelectedIndex());
    }

    public void deleteSelectedName(String name) {
        if (frame.getTraceThreads() == null || frame.getTraceThreads().threadId_List == null) {
            return;
        }
        frame.getTraceThreads().threadId_List.remove(name);
        this.updateUI();
        return;
    }

    public void filterToThread(String reg) {
        model.setFilter(reg);
        frame.addFilterName(reg);
        this.updateUI();
    }

    public ThreadJListModel getModel() {
        return model;
    }
}
