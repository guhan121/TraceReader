package com.panda.trace;

public class VersionSection {//
    String version;
    int versioncode;
    boolean overflow;
    String clock;
    long elapsedTime;
    int methodNum;
    int clockCallOverhead;
    String vm;

    public VersionSection() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public boolean isOverflow() {
        return overflow;
    }

    public void setOverflow(boolean overflow) {
        this.overflow = overflow;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getMethodNum() {
        return methodNum;
    }

    public void setMethodNum(int methodNum) {
        this.methodNum = methodNum;
    }

    public int getClockCallOverhead() {
        return clockCallOverhead;
    }

    public void setClockCallOverhead(int clockCallOverhead) {
        this.clockCallOverhead = clockCallOverhead;
    }

    public String getVm() {
        return vm;
    }

    public void setVm(String vm) {
        this.vm = vm;
    }
}