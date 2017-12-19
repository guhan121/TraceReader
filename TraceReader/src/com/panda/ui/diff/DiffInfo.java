package com.panda.ui.diff;

public class DiffInfo {
    public String clazz;
    public String m;
    public Long t1;
    public Long t2;
    public Long diff;


    public DiffInfo(String str, String str1, long i, long l) {
        clazz = str;
        m = str1;
        t1 = i;
        t2 = l;
        diff = -1l;
    }
}
