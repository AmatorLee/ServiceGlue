package com.me.amator.service.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解服务实现接口
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ServiceInterface {
}
