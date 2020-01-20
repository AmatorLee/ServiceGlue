package com.me.amator.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liguangquan on 2020-01-20
 * email: liguangquan@bytedance.com
 * desc:标注插件服务实现类
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PluginServiceImpl {
}
