package com.panda.util;

import com.panda.trace.MethodLog;
import com.panda.trace.ThreadList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayHelper {
    public static String SelectedThreadId = "";

    public static List<String> FILTER_INCLUDE_LIST = new ArrayList(Arrays.asList("com.duowan", "com.yy"));
    public static List<String> FILTER_EXCLUDE_LIST = new ArrayList();

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
        List<MethodLog> list1;
        list1 = traceThreads.getThreadId_thread_map()
                .get(SelectedThreadId)
                .getMethodLogs().stream()
                .filter((MethodLog b) -> FILTER_INCLUDE_LIST.stream().anyMatch(f -> b.getFullName().startsWith(f)))
                .collect(Collectors.toList());
        SelectedThreadId_last = SelectedThreadId;
        last = list1;

        return list1;
    }

    public static void checkSelctedThreadId() {
        if (SelectedThreadId != SelectedThreadId_last) {
            //TODO 重新准备数据
        }
    }

    public static void initFilter() {
        DisplayHelper.FILTER_EXCLUDE_LIST.clear();
        DisplayHelper.FILTER_INCLUDE_LIST.clear();
        try {
            Files.lines(Paths.get(System.getProperty("user.dir") + File.separator + "filter.txt")).forEach(s -> {
                        if (s.startsWith("-")) {
                            DisplayHelper.FILTER_EXCLUDE_LIST.add(s.substring(1));
                        } else if (s.startsWith("+")) {
                            DisplayHelper.FILTER_INCLUDE_LIST.add(s.substring(1));
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
