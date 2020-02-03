package com.me.amator.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liguangquan on 2020-02-02
 * email: liguangquan@bytedance.com
 * desc:
 **/
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface ServiceInject {
}
