package com.me.amator.service.traverse;

import com.me.amator.service.Utils;

import org.objectweb.asm.tree.FieldNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by liguangquan on 2019-1-18
 * email: liguangquan@bytedance.com
 * desc:
 **/
public class GlueHolder {

    //接口缓存表
    private static List<String> sInterfaces = new ArrayList<>();
    private static List<ServiceModel> sServiceModels = new ArrayList<>();
    private boolean hasRunTransform;
    private static Map<String,List<FieldNode>> sFields = new HashMap<>();

    private GlueHolder(){

    }

    public static GlueHolder getInstance(){
        return ServiceRegisterHolder.sInstance;
    }

    public void check() {
        if (Utils.isEmpty(sInterfaces) || Utils.isEmpty(sServiceModels)){
            return;
        }
        //回填interface
        for (ServiceModel model : sServiceModels) {
            String[] interfaces = model.getInterfaces();
            if (interfaces == null){
                throw new IllegalArgumentException("@serviceimpl class has not interface?");
            }
            String curInterface = null;
            for (String anInterface : interfaces) {
                if (sInterfaces.contains(anInterface)) {
                    curInterface = anInterface;
                    break;
                }
            }
            if (Utils.isEmpty(curInterface)){
                throw new IllegalArgumentException("@serviceimpl class has not interface?");
            }
            model.setCurrentInterface(curInterface);
        }
        checkInner();
    }

    private void checkInner() {
        for (ServiceModel sServiceModel : sServiceModels) {
            if (Utils.isEmpty(sServiceModel.getClassName()) || Utils.isEmpty(sServiceModel.getCurrentInterface())) {
                throw new IllegalArgumentException("@serviceImpl has not current interface");
            }
        }
    }

    public void setRunTransform(boolean flag) {
        this.hasRunTransform = flag;
    }

    public boolean hasRunTransform(){
        return this.hasRunTransform;
    }

    public List<ServiceModel> getPluginServiceModel() {
        List<ServiceModel> serviceModels = new ArrayList<>();
        for (int i = 0; i < sServiceModels.size(); i++) {
            ServiceModel serviceModel = sServiceModels.get(i);
            if (serviceModel.isPlugin()) {
                serviceModels.add(serviceModel);
            }
        }
        return serviceModels;
    }

    public void addField(String className, FieldNode fieldNode) {
        List<FieldNode> fieldNodes;
        if (sFields.containsKey(className)) {
            fieldNodes = sFields.get(className);
        } else {
            fieldNodes = new LinkedList<>();
        }
        fieldNodes.add(fieldNode);
        sFields.put(className, fieldNodes);
    }

    public List<FieldNode> containFieldKey(String className) {
        return sFields.get(className);
    }

    private static class ServiceRegisterHolder{
        static GlueHolder sInstance = new GlueHolder();
    }

    public void addInterface(String itf) {
        sInterfaces.add(itf);
    }

    public void addServiceModel(ServiceModel serviceModel) {
        if (sServiceModels.contains(serviceModel)) {
            throw new IllegalArgumentException("has two same service?");
        }
        sServiceModels.add(serviceModel);
    }

    public List<ServiceModel> getServiceNotInjectPlugin() {
        List<ServiceModel> notPlugin = new ArrayList<>();
        for (int i = 0; i < sServiceModels.size(); i++) {
            ServiceModel serviceModel = sServiceModels.get(i);
            if (!serviceModel.isPlugin()) {
                notPlugin.add(serviceModel);
            }
        }
        return notPlugin;
    }


    public void clear(){
        sServiceModels.clear();;
        sInterfaces.clear();
        sFields.clear();
    }


}
