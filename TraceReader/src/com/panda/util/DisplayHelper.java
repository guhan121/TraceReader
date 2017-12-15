package com.panda.util;

import com.panda.trace.MethodLog;
import com.panda.trace.ThreadList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayHelper {
    public static String SelectedThreadId = "";
    static List<String> Filter_List = new ArrayList(Arrays.asList("com.duowan", "com.yy"));
    static String SelectedThreadId_last = "";
    static List<MethodLog> last = null;

    /**
     * 如果未设置则返回空
     *
     * @param traceThreads
     * @return
     */
    public static List<MethodLog> getFilterMethods(ThreadList traceThreads) {
        if (SelectedThreadId == null || SelectedThreadId.isEmpty()) {
            return new ArrayList<MethodLog>();
        }
        if (SelectedThreadId == SelectedThreadId_last) {
            return last;
        }
//        List<String> list1 = new ArrayList<String>();
        List<MethodLog> list1 = new ArrayList<>();
        list1 = traceThreads.getThreadId_thread_map()
                .get(SelectedThreadId)
                .getMethodLogs().stream()
                .filter((MethodLog b) -> Filter_List.stream().anyMatch(f -> b.getFullName().startsWith(f)))
                .collect(Collectors.toList());

        System.out.println("list1.size()：" + list1.size());
        SelectedThreadId_last = SelectedThreadId;
        last = list1;
        return list1;
//        List<String> list = new ArrayList<String>();
//        list.addAll(traceThreads.getRecFullName_TraceRec_Map().keySet());
//        return list.stream()
//                .filter((String b) -> Filter_List.stream().anyMatch(f -> b.startsWith(f)))
//                .collect(Collectors.toList());
    }

    public static void checkSelctedThreadId() {
        if(SelectedThreadId!= SelectedThreadId_last){
            //TODO 重新准备数据
        }
    }
}
