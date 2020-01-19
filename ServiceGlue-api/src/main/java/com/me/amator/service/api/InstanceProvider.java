package com.me.amator.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liguangquan on 2019-1-18
 * email: liguangquan@bytedance.com
 * desc:注解单例方法
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface InstanceProvider {
}
