package com.panda.trace;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

//File format:
//header
//record 0
//record 1
//...
//
//Header format:
//u4  magic ('SLOW')
//u2  version
//u2  offset to data
//u8  start date/time in usec
//u2  record size in bytes (version >= 2 only)
//... padding to 32 bytes
//
//Record format v1:
//u1  threadList ID
//u4  method ID | method action
//u4  time delta since start, in usec
//
//Record format v2:
//u2  threadList ID
//u4  method ID | method action
//u4  time delta since start, in usec
//
//Record format v3:
//u2  threadList ID
//u4  method ID | method action
//u4  time delta since start, in usec
//u4  wall time since start, in usec (when clock == "dual" only)
//
//32 bits of microseconds is 70 minutes.
//
//All values are stored in little-endian order.
//copy from android 4.4
public class Trace {

    private static Trace instance;
    final Pattern patternMothod = Pattern.compile("^\\s+(.*\\))\\s->\\s(.*)$");
    final Pattern patternClass = Pattern.compile("^(.*)\\s->\\s(.*):$");
    FormatFile fmFile;
    ThreadList threadList;
    TraceSection traceSection;

    private Trace() {
    }

    public static synchronized Trace getInstance() {
        if (instance == null) {
            instance = new Trace();
        }
        return instance;
    }

    public ThreadList getThreadList() {
        return threadList;
    }

    private void readTrace(byte[] data) throws Throwable {
        ThreadList localThreadList = new ThreadList();
        traceSection = new TraceSection();
        int offset = 0;
        traceSection.header.kTraceMagicValue = (char) (data[0]) + "" + (char) (data[1]) + "" + (char) (data[2]) + "" + (char) (data[3]);
        offset = offset + 4;
        traceSection.header.trace_version = BytesHelper.read2LE(data, offset);
        offset = offset + 2;
        traceSection.header.kTraceHeaderLength = BytesHelper.read2LE(data, offset);
        offset = offset + 2;
        traceSection.header.start_time_ = BytesHelper.read8LE(data, offset);
        offset = offset + 8;
        if (traceSection.header.trace_version >= 3) {
            traceSection.header.record_size = 14;
        } else {
            traceSection.header.record_size = 10;
        }
        offset = traceSection.header.kTraceHeaderLength;
        long value;
        long current = System.currentTimeMillis();
        while (offset < data.length) {
            TraceRecord r = new TraceRecord();
            r.threadId = BytesHelper.read2LE(data, offset);
            value = BytesHelper.read4LE(data, offset + 2);
            r.methodId = TraceAction.decodeMethodValue(value);
            r.threadClockDiff = BytesHelper.read4LE(data, offset + 6);
            if (traceSection.header.record_size == 14) {
                r.wallClockDiff = BytesHelper.read4LE(data, offset + 10);
            }
            MethodDes m1 = fmFile.methods.get(r.methodId);
            r.action = TraceAction.decodeAction(value);
            traceSection.records.add(r);
            MethodLog ml;
            if (m1 == null) {
                ml = new MethodLog("0x" + Long.toHexString(r.methodId), r.action);
            } else {
                ml = new MethodLog(r, m1);
            }

            /**
             * 记录所有的线程记录
             */
            if (!localThreadList.recFullName_TraceRec_Map.containsKey(ml.getMethodDes().getFullName())) {
                TraceRecord rd = ml.record;
                localThreadList.recFullName_TraceRec_Map.put(ml.getMethodDes().getFullName(), rd);
            }


            /**
             * 对线程列表追加记录  threadId_List 里面的记录是已经分线程的了
             */
            if (!localThreadList.threadId_thread_map.containsKey(ml.getRecord().threadId + "")) {
                TraceThread thread = new TraceThread();
                thread.threadId = ml.getRecord().threadId;
                thread.methodLogs.add(ml);
                thread.currentName = getThreadName(fmFile, ml);
                localThreadList.threadId_thread_map.put(ml.getRecord().threadId + "", thread);


                localThreadList.threadId_List.add(ml.getRecord().threadId + "");
            } else {
                localThreadList.threadId_thread_map.get(ml.getRecord().threadId + "").methodLogs.add(ml);
            }
            offset = offset + traceSection.header.record_size;
        }
        long current1 = System.currentTimeMillis();
//        System.out.println(current1 - current);
        localThreadList.sort();
        this.threadList = localThreadList;
    }

    /**
     * 从trace文件中已经得出的线程map中得到对应的线程名称
     *
     * @param fmFile
     * @param ml     一条记录
     * @return
     */
    private String getThreadName(FormatFile fmFile, MethodLog ml) {
        return fmFile.threads.get(ml.getRecord().threadId + "");
    }

    /**
     * 读取前三段内容
     *
     * @param format
     */
    private void readFileFormat(String format) {
        String[] lists = format.split("\n");
        fmFile = new FormatFile();
        int offset = 0;
        fmFile.setVersion("version");
        fmFile.setVersioncode(Integer.parseInt(lists[1]));
        while (!lists[offset].equals("*threads")) {
            if (lists[offset].contains("data-file-overflow")) {
                String res = lists[offset].replace("data-file-overflow=", "");
                if (res.equals("true")) {
                    fmFile.setOverflow(true);
                } else {
                    fmFile.setOverflow(false);
                }
            } else if (lists[offset].contains("clock")) {
                fmFile.setClock(lists[offset].replace("clock=", ""));
            } else if (lists[offset].contains("elapsed-time-usec")) {
                fmFile.setElapsedTime(Long.parseLong(lists[offset].replace("elapsed-time-usec=", "")));
            } else if (lists[offset].contains("num-method-calls")) {
                fmFile.setMethodNum(Integer.parseInt(lists[offset].replace("num-method-calls=", "")));
            } else if (lists[offset].contains("clock-call-overhead-nsec")) {
                fmFile.setClockCallOverhead(Integer.parseInt(lists[offset].replace("clock-call-overhead-nsec=", "")));
            } else if (lists[offset].contains("vm")) {
                fmFile.setVm(lists[offset].replace("vm=", ""));
            }
            offset++;
        }
        offset++;
        while (!lists[offset].equals("*methods")) {
            String params[] = lists[offset].split("\t");
            if (params.length >= 2) {
                fmFile.threads.put(params[0], params[1]);
//                System.out.println(params[0] + " " + params[1]);
            } else {
                fmFile.threads.put(params[0], "unknown");
            }
            offset++;
        }
        offset++;
        while (!lists[offset].equals("*end")) {
            MethodDes m = new MethodDes();
            String params[] = lists[offset].split("\t");
            m.setMethodId(Long.parseLong(params[0].replace("0x", ""), 16));
            m.setMethodClazz(params[1]);
            m.setMethodName(params[2]);
            m.setMethodSig(params[3]);
            if (params.length == 6) {
                m.setSource(params[4] + " " + params[5]);
            } else {
                m.setSource(params[4]);
            }
            offset++;
            fmFile.methods.put(m.getMethodId(), m);
        }
    }


    public void divideBytes(byte[] bytes) {
        int padding = 0;
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] == 'S' && bytes[i + 1] == 'L' && bytes[i + 2] == 'O' && bytes[i + 3] == 'W') {
                padding = i;
                break;
            }
        }
        StringBuffer buffer = new StringBuffer(new String(bytes, 0, padding - 1));
        byte[] data = new byte[bytes.length - padding];
        System.arraycopy(bytes, padding, data, 0, data.length - 1);
        readFileFormat(buffer.toString());
        try {
            readTrace(data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Map<Long, MethodDes> getMethods() {
        return fmFile.methods;
    }

    public void updateMethodInfo(File fl) {
        int[] step = new int[]{0};
        // Java8用流的方式读文件，更加高效
        List<clazz_info> all = new ArrayList<>();
        try {
            Files.lines(Paths.get(fl.getPath())).forEach((String line) -> {
                Matcher m = patternClass.matcher(line);
                if (m.matches()) {
                    String old_class = m.group(1);
                    String new_class = m.group(2);
                    all.add(new clazz_info(old_class, new_class));
                }
                Matcher m1 = patternMothod.matcher(line);
                if (m1.matches()) {
                    String old_class = m1.group(1).replaceFirst("\\d+:\\d+:", "");
                    String new_class = m1.group(2);
                    all.get(all.size() - 1).method_infos.add(new Method_info(old_class, new_class));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "mapping.txt按行读取失败", "文件读取失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //修改方法中的定义
        for (MethodDes value : fmFile.methods.values()) {
            String clazz = value.getOldMethodClazz();
            for (clazz_info info : all) {
                if (info.new_name.equals(clazz)) {
                    value.setMethodClazz(info.originalName);
                    String methodName = value.getMethodName();
                    for (Method_info info2 : info.method_infos) {
                        if (info2.new_name.equals(methodName)) {
                            value.setMethodName(info2.originalName);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private class clazz_info {
        String originalName;
        String new_name;
        List<Method_info> method_infos = new ArrayList<>();

        public clazz_info(String old_class, String new_class) {
            originalName = old_class;
            new_name = new_class;
        }
    }

    private class Method_info {
        String originalName;
        String new_name;

        public Method_info(String old_class, String new_class) {
            originalName = old_class;
            new_name = new_class;
        }
    }
}
