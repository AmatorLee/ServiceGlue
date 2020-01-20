package com.me.amator.service.base;

import com.android.build.gradle.AppExtension;
import com.me.amator.service.transform.GlueInjectClassVisitor;
import com.me.amator.service.transform.ServiceGluer;
import com.me.amator.service.traverse.GlueHolder;
import com.me.amator.service.traverse.GlueTraverseClassVisitor;
import com.ss.android.ugc.bytex.common.CommonPlugin;
import com.ss.android.ugc.bytex.common.TransformConfiguration;
import com.ss.android.ugc.bytex.common.flow.TransformFlow;
import com.ss.android.ugc.bytex.common.flow.main.MainTransformFlow;
import com.ss.android.ugc.bytex.common.log.LevelLog;
import com.ss.android.ugc.bytex.common.visitor.ClassVisitorChain;
import com.ss.android.ugc.bytex.pluginconfig.anno.PluginConfig;
import com.ss.android.ugc.bytex.transformer.TransformContext;
import com.ss.android.ugc.bytex.transformer.TransformEngine;

import org.gradle.api.Project;

import javax.annotation.Nonnull;

/**
 * Created by liguangquan on 2019-12-31
 * email: liguangquan@bytedance.com
 * desc:
 **/
@PluginConfig("service-glue")
public class GluePlugin extends CommonPlugin<GlueExtension, GlueContext> {

    private boolean hasRunTransform = false;

    @Override
    protected GlueContext getContext(Project project, AppExtension appExtension, GlueExtension serviceExtension) {
        LevelLog.DEFAULT.setTag(serviceExtension.getName());
        return new GlueContext(project, appExtension, serviceExtension);
    }

    @Override
    protected TransformFlow provideTransformFlow(@Nonnull MainTransformFlow mainFlow, @Nonnull TransformContext transformContext) {
        TransformTwiceTransformFlow flow = new TransformTwiceTransformFlow(new TransformEngine(transformContext), context);
        flow.appendHandler(this);
        return flow;
    }

    @Override
    public void beforeTransform(@Nonnull TransformEngine engine) {
        super.beforeTransform(engine);
        if (!GlueHolder.getInstance().hasRunTransform()) {
            GlueHolder.getInstance().check();
        }
    }

    @Override
    public void afterTransform(@Nonnull TransformEngine engine) {
        super.afterTransform(engine);
        if (!GlueHolder.getInstance().hasRunTransform()) {
            GlueHolder.getInstance().setRunTransform(true);
            ServiceGluer.injectPluginService(engine);
        }
    }

    @Override
    public boolean transform(@Nonnull String relativePath, @Nonnull ClassVisitorChain chain) {
        chain.connect(new GlueInjectClassVisitor());
        return true;
    }

    @Override
    public void traverse(@Nonnull String relativePath, @Nonnull ClassVisitorChain chain) {
        super.traverse(relativePath, chain);
        chain.connect(new GlueTraverseClassVisitor());
    }

    @Override
    public void init() {
        super.init();
        GlueHolder.getInstance().setRunTransform(false);
        GlueHolder.getInstance().clear();
    }

    @Nonnull
    @Override
    public TransformConfiguration transformConfiguration() {
        return new TransformConfiguration() {
            @Override
            public boolean isIncremental() {
                return false;
            }
        };
    }
}
