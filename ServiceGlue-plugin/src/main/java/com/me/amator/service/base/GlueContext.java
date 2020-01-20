package com.me.amator.service.base;

import com.android.build.gradle.AppExtension;
import com.ss.android.ugc.bytex.common.BaseContext;

import org.gradle.api.Project;

/**
 * Created by liguangquan on 2019-1-19
 * email: liguangquan@bytedance.com
 * desc: 胶水上下文
 **/
public class GlueContext extends BaseContext<GlueExtension> {
    private int time;

    public GlueContext(Project project, AppExtension android, GlueExtension extension) {
        super(project, android, extension);
    }

    public void setTraverseTime(int time) {
        this.time = time;
    }

    public int getTraverseTime() {
        return this.time;
    }
}
