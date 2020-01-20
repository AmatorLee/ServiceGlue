package com.me.amator.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liguangquan on 2020-01-02
 * email: liguangquan@bytedance.com
 * desc:
 **/
public class ServiceGlue {

    private static final ConcurrentHashMap<Class, Object> SERVICES = new ConcurrentHashMap<>();


    public static <T> T getService(Class<T> tClass) {
        T imp = (T) SERVICES.get(tClass);
        if (imp == null) {
            synchronized (tClass) {
                imp = (T) SERVICES.get(tClass);
                if (imp != null) {
                    return imp;
                }
                imp = ServiceFinder.findService(tClass);
                if (imp != null) {
                    SERVICES.put(tClass, imp);
                    return imp;
                }
                imp = tryGetByReflect(tClass);
                if (imp != null) {
                    SERVICES.put(tClass, imp);
                    return imp;
                }
            }
        }
        return imp;
    }

    private static <T> T tryGetByReflect(Class<T> tClass) {
        String name = tClass.getName();
        String gluerClassName = String.format("%s$$ServiceGluer", name);
        try {
            Class<?> aClass = Class.forName(gluerClassName);
            Object newInstance = aClass.newInstance();
            Method glueService = aClass.getMethod("glueService");
            if (glueService != null) {
                return (T) glueService.invoke(newInstance);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
