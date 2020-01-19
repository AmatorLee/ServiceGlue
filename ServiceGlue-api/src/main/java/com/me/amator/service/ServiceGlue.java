package com.me.amator.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liguangquan on 2020-01-02
 * email: liguangquan@bytedance.com
 * desc:
 **/
public class ServiceGlue {

    private static final ConcurrentHashMap<Class, Object> SERVICES = new ConcurrentHashMap<>();


    public static  <T>T getService(Class<T> tClass){
        T imp = (T) SERVICES.get(tClass);
        if (imp == null) {
            synchronized (tClass) {
                imp = (T) SERVICES.get(tClass);
                if (imp != null) {
                    return imp;
                }
                T service = ServiceFinder.findService(tClass);
                if (service != null) {
                    SERVICES.put(tClass, service);
                    return service;
                }
            }
        }
        return imp;
    }

}
