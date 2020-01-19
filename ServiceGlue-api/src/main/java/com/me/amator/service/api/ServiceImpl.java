package com.me.amator.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liguangquan on 2019-1-18
 * email: liguangquan@bytedance.com
 * desc：服务实现注解
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ServiceImpl {
}
