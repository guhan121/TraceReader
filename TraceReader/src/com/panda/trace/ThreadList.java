package com.panda.trace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class ThreadList {

    public List<String> threadId_List;
    /**
     * 记录是所有的
     */
    protected Map<String, TraceRecord> recFullName_TraceRec_Map;
    Map<String, TraceThread> threadId_thread_map;
    boolean sort;

    @SuppressWarnings("unchecked")
    protected ThreadList() throws Throwable {
        threadId_List = new ArrayList<>();
        threadId_thread_map = new HashMap<>();
        recFullName_TraceRec_Map = new HashMap<>();
        sort = true;
    }

    public Map<String, TraceRecord> getRecFullName_TraceRec_Map() {
        return recFullName_TraceRec_Map;
    }

    public Map<String, TraceThread> getThreadId_thread_map() {
        return threadId_thread_map;
    }

    public void setThreadId_thread_map(Map<String, TraceThread> threadId_thread_map) {
        this.threadId_thread_map = threadId_thread_map;
    }

    protected void sort() {
        if (sort) {
            sort = false;
            for (String key : threadId_thread_map.keySet()) {
                threadId_thread_map.get(key).sortMethods();
            }
        }
    }

    public List<String> getAll_mlFullName() {
        List<String> all_mlFullName_List = new ArrayList<String>();
        all_mlFullName_List.addAll(recFullName_TraceRec_Map.keySet());
        return all_mlFullName_List;
    }

    public void reset() {
        threadId_List.clear();
        threadId_thread_map.clear();
        recFullName_TraceRec_Map.clear();
    }
}
