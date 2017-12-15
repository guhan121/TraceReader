package com.panda.trace;

import java.util.ArrayList;
import java.util.List;

public class TraceSection {
    TraceHeader header = new TraceHeader();
    /**
     * 记录所有轨迹，没有排序，按记录先后顺序排序
     */
    List<TraceRecord> records = new ArrayList<>();

    /**
     * Trace的头部 32个字节
     */
    public class TraceHeader {
        String kTraceMagicValue;
        int trace_version;
        int kTraceHeaderLength;
        long start_time_;
        int record_size;
    }
}

