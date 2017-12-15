package com.panda.trace;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个方法的记录，包含一个record，并且是已经计算了 父子关系，耗时等数据
 *
 * @author qiantao
 */
public class MethodLog implements Comparable {
    TraceRecord record;
    long threadCostTime;
    long wallCostTime;
    /**
     * 该记录的方法描述
     */
    MethodDes m;
    /**
     * 父节点
     */
    MethodLog parent;
    /**
     * 兄弟节点
     */
    MethodLog partner;
    /**
     * 子节点们
     */
    List<MethodLog> child = new ArrayList<>();

    /**
     * 退出的记录
     */
    TraceRecord xit;

    public MethodLog() {
    }

    public MethodLog(String name) {
        this.parent = null;
        this.record = new TraceRecord();
        this.record.m = new MethodDes();
        this.record.m.setMethodDescriptor("");
        this.record.m.setMethodName(name);
        this.record.m.setMethodSig("");
        this.record.m.setSource("unknown");
        ;
        this.record.action = 3;
    }

    public MethodLog(TraceRecord r) {
        this.record = r;

    }

    public MethodLog(String methodName, int action) {
        this.parent = null;
        this.record = new TraceRecord();
        this.record.m = new MethodDes();
        this.record.m.setMethodDescriptor("");
        this.record.m.setMethodName(methodName);
        this.record.m.setMethodSig("");
        this.record.m.setSource("unknown");
        ;
        this.record.action = action;

    }

    public long getThreadCostTime() {
        return threadCostTime;
    }

    public void setThreadCostTime(long threadCostTime) {
        this.threadCostTime = threadCostTime;
    }

    public long getWallCostTime() {
        return wallCostTime;
    }

    public void setWallCostTime(long wallCostTime) {
        this.wallCostTime = wallCostTime;
    }

    public TraceRecord getRecord() {
        return record;
    }

    public String getFullName() {
        return record.m.getFullName();
    }

    public String getOriginFullName() {
        return record.m.getOriginFullName();
    }

    public String getMethodName() {
        return record.m.getMethodName();
    }

    public String getSource() {
        return record.m.getSource().split("\t")[0];
    }

    public int getAction() {
        return record.action;
    }

    public MethodLog getParent() {
        return parent;
    }

    public void setParent(MethodLog parent) {
        this.parent = parent;
    }

    public MethodLog getPartner() {
        return partner;
    }

    public void setPartner(MethodLog partner) {
        this.partner = partner;
    }

    public List<MethodLog> getChild() {
        return child;
    }

    public void setChild(List<MethodLog> child) {
        this.child = child;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        if (!(o instanceof MethodLog)) {
            throw new ClassCastException("Not MethodLog Class!");
        }
        MethodLog o1 = (MethodLog) o;
        return (int) (o1.child.size() - this.child.size());
    }

    public boolean isNeedPass() {
        if (record.m.getMethodDescriptor().startsWith("com.duowan") || record.m.getMethodDescriptor().startsWith("com.yy")) {
//            System.out.println(this.getFullName());
            return false;
        }

        for (MethodLog x : child) {
            if (!x.isNeedPass()) {
//                System.out.println("xx:" + x.getFullName());
                return false;
            }
        }
        return true;
    }
    public boolean isSelfNeedPass() {
        if (record.m.getMethodDescriptor().startsWith("com.duowan") || record.m.getMethodDescriptor().startsWith("com.yy")) {
//            System.out.println(this.getFullName());
            return false;
        }
        return true;
    }

    public void reNameMethod(String name) {
        m.renameMethod(name);
    }

    public void reNameClass(String old, String name) {
        m.renameClass(old, name);
        m.renameSig("L" + old + ";", "L" + name + ";");
    }
}
