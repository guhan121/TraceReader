package com.panda.trace;

/**
 * 方法的描述信息
 * @author qiantao
 */
public class MethodDes {
    private long methodId;
    private String methodClazz;
    private String methodName;
    private String methodSig;
    private String oldMethodClazz;
    private String oldMethodName;
    private String oldMethodSig;
    private String source;

    public String getOldMethodClazz() {
        return oldMethodClazz;
    }

    public String getOldMethodName() {
        return oldMethodName;
    }

    public String getOldMethodSig() {
        return oldMethodSig;
    }

    public long getMethodId() {
        return methodId;
    }

    public void setMethodId(long id) {
        this.methodId = id;
    }

    public String getMethodClazz() {
        return methodClazz;
    }

    public void setMethodClazz(String methodDescriptor) {
        this.methodClazz = methodDescriptor;
        this.oldMethodClazz = methodDescriptor;
    }

    public String getMethodSig() {
        return methodSig;
    }

    public void setMethodSig(String methodSig) {
        this.methodSig = methodSig;
        this.oldMethodSig = methodSig;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
        this.oldMethodName = methodName;
    }

    public void renameClass(String oldCls, String cls) {
        this.methodClazz = methodClazz.replace(oldCls, cls);
    }

    public void renameSig(String oldCls, String cls) {
        this.methodSig = methodSig.replace(oldCls, cls);
    }

    public void renameMethod(String method) {
        this.methodName = method;
    }

    public String getFullName() {
        return getMethodClazz() + "." + getMethodName() + getMethodSig();
    }

    public String getOriginFullName() {
        return getOldMethodClazz() + "." + getOldMethodName() + getOldMethodSig();
    }
}
