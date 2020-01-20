package com.me.amator.service.traverse;

import org.objectweb.asm.tree.MethodNode;

/**
 * Created by liguangquan on 2019-1-19
 * email: liguangquan@bytedance.com
 * desc: service数据类
 **/
public class ServiceModel {

    private String mClassName;
    private MethodNode mMethodNode;
    private String[] mInterfaces;
    private String mCurInterface;
    private boolean isPlugin;

    public boolean isPlugin() {
        return isPlugin;
    }

    public void setPlugin(boolean plugin) {
        isPlugin = plugin;
    }

    public void setInterfaces(String[] interfaces) {
        mInterfaces = interfaces;
    }

    public String[] getInterfaces() {
        return mInterfaces;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public MethodNode getMethodNode() {
        return mMethodNode;
    }

    public void setMethodNode(MethodNode mMethodNode) {
        this.mMethodNode = mMethodNode;
    }

    public void setCurrentInterface(String curInterface) {
        mCurInterface = curInterface;
    }

    public String getCurrentInterface() {
        return mCurInterface;
    }
}
