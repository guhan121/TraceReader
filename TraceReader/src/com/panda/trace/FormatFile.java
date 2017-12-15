package com.panda.trace;

import java.util.HashMap;
import java.util.Map;

public class FormatFile {

    private final VersionSection versionSection = new VersionSection();
    Map<String, String> threads = new HashMap<String, String>();
    Map<Long, MethodDes> methods = new HashMap<Long, MethodDes>();

    public Map getMethods() {
        return methods;
    }

    public void setMethods(Map methods) {
        this.methods = methods;
    }

    public void setVersion(String version) {
        versionSection.setVersion(version);
    }

    public int getVersioncode() {
        return versionSection.getVersioncode();
    }

    public void setVersioncode(int versioncode) {
        versionSection.setVersioncode(versioncode);
    }

    public boolean isOverflow() {
        return versionSection.isOverflow();
    }

    public void setOverflow(boolean overflow) {
        versionSection.setOverflow(overflow);
    }

    public String getClock() {
        return versionSection.getClock();
    }

    public void setClock(String clock) {
        versionSection.setClock(clock);
    }

    public long getElapsedTime() {
        return versionSection.getElapsedTime();
    }

    public void setElapsedTime(long elapsedTime) {
        versionSection.setElapsedTime(elapsedTime);
    }

    public int getMethodNum() {
        return versionSection.getMethodNum();
    }

    public void setMethodNum(int methodNum) {
        versionSection.setMethodNum(methodNum);
    }

    public int getClockCallOverhead() {
        return versionSection.getClockCallOverhead();
    }

    public void setClockCallOverhead(int clockCallOverhead) {
        versionSection.setClockCallOverhead(clockCallOverhead);
    }

    public String getVm() {
        return versionSection.getVm();
    }

    public void setVm(String vm) {
        versionSection.setVm(vm);
    }

}
