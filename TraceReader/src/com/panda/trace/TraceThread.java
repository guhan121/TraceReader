package com.panda.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 每个线程对应的数据，写的真他妈的复杂，都不想改了
 */
public class TraceThread {

    public final static MethodLog topMethod = new MethodLog("TopMethod");
    public final static MethodLog endMethod = new MethodLog("endMethod");
    public final static MethodLog noPart = new MethodLog("noPart");

    int threadId;
    String currentName;

    List<MethodLog> methodLogs = new ArrayList<>();

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public List<MethodLog> getMethodLogs() {
        return methodLogs;
    }

    public void setMethodLogs(List<MethodLog> methodLogs) {
        this.methodLogs = methodLogs;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    /**
     * 对线程id对应的记录进行排序
     */
    protected void sortMethods() {
        Stack<MethodLog> stack = new Stack();
        int n = 0;
        for (int i = 0; i < methodLogs.size(); ++i) {
            //进入的时候压栈
            if (methodLogs.get(i).getAction() == 0) {
                stack.push(methodLogs.get(i));
            } else {
                //退出的时候出栈
                //如果是空的时候就有推出记录则表明 - 记录发生在函数进入之后
                if (stack.isEmpty()) {

                    //无进入记录
                    methodLogs.get(i).parent = noPart;
                    n++;
                    continue;
                }
                stack.pop();
            }
        }

        //无退出记录
        long t0 = methodLogs.get(methodLogs.size() - 1).getRecord().threadClockDiff;
        long t1 = methodLogs.get(methodLogs.size() - 1).getRecord().wallClockDiff;
        for (int i = 0; i < stack.size(); ++i) {
            MethodLog np = new MethodLog("noPart", 1);
            np.getRecord().threadClockDiff = t0;
            np.getRecord().wallClockDiff = t1;
            methodLogs.add(np);
        }


        //头部空数据
        stack.clear();
        stack.push(topMethod);
        for (int i = 0; i < n; ++i) {
            MethodLog np = new MethodLog("noPart", 0);
            //wrong diff
            np.getRecord().threadClockDiff = methodLogs.get(0).getRecord().threadClockDiff;
            np.getRecord().wallClockDiff = methodLogs.get(0).getRecord().wallClockDiff;
            stack.push(np);
        }


        for (int i = 0; i < methodLogs.size(); ++i) {
            if (methodLogs.get(i).getAction() == 0) {
                //如果是进入，则其父为上一个
                methodLogs.get(i).parent = stack.get(stack.size() - 1);
                stack.get(stack.size() - 1).child.add(methodLogs.get(i));
                stack.push(methodLogs.get(i));
            } else {
                MethodLog m = stack.pop();
                m.partner = methodLogs.get(i);
                methodLogs.get(i).partner = m;
            }
        }


        List<MethodLog> m = new ArrayList();
        for (int i = 0; i < methodLogs.size(); ++i) {
            if (methodLogs.get(i).record.getAction() == 0) {
                if (methodLogs.get(i).getFullName().equals("noPart")) {
                    methodLogs.get(i).record = methodLogs.get(i).partner.record;
                }
                if (methodLogs.get(i).parent == null) {
                    methodLogs.get(i).parent = methodLogs.get(i).partner.parent;
                    methodLogs.get(i).parent.child.remove(methodLogs.get(i).partner);
                    methodLogs.get(i).parent.child.add(methodLogs.get(i));
                    methodLogs.get(i).child = methodLogs.get(i).partner.child;
                    for (int j = 0; j < methodLogs.get(i).child.size(); ++j) {
                        methodLogs.get(i).child.get(j).parent = methodLogs.get(i);
                    }
                }
                long beginT = methodLogs.get(i).getRecord().threadClockDiff;
                long endT = methodLogs.get(i).partner.getRecord().threadClockDiff;
                long beginW = methodLogs.get(i).getRecord().wallClockDiff;
                long endW = methodLogs.get(i).partner.getRecord().wallClockDiff;
                methodLogs.get(i).setThreadCostTime(endT - beginT);
                methodLogs.get(i).setWallCostTime(endW - beginW);
                m.add(methodLogs.get(i));
            }
        }
        methodLogs.clear();
        methodLogs = m;
    }
}
