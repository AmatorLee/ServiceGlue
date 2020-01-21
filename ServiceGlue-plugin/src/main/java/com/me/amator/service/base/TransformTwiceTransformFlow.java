package com.me.amator.service.base;

import com.ss.android.ugc.bytex.common.flow.AbsTransformFlow;
import com.ss.android.ugc.bytex.common.flow.TransformFlow;
import com.ss.android.ugc.bytex.common.flow.main.MainProcessHandler;
import com.ss.android.ugc.bytex.common.flow.main.Process;
import com.ss.android.ugc.bytex.common.graph.Graph;
import com.ss.android.ugc.bytex.common.graph.GraphBuilder;
import com.ss.android.ugc.bytex.common.log.Timer;
import com.ss.android.ugc.bytex.common.processor.ClassFileAnalyzer;
import com.ss.android.ugc.bytex.common.processor.ClassFileTransformer;
import com.ss.android.ugc.bytex.transformer.TransformEngine;
import com.ss.android.ugc.bytex.transformer.processor.ClassFileProcessor;
import com.ss.android.ugc.bytex.transformer.processor.FileHandler;
import com.ss.android.ugc.bytex.transformer.processor.FileProcessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by liguangquan on 2020-01-20
 * thanks {@link "https://github.com/Zane96"}
 */
public class TransformTwiceTransformFlow extends AbsTransformFlow {
    private final List<MainProcessHandler> handlers = new ArrayList<>();
    private Graph mClassGraph;
    private GlueContext glueContext;

    public TransformTwiceTransformFlow(TransformEngine transformEngine, GlueContext context) {
        super(transformEngine);
        this.glueContext = context;
    }

    public void run() throws IOException, InterruptedException {
        if (!this.handlers.isEmpty()) {
            this.handlers.forEach(new Consumer<MainProcessHandler>() {
                @Override
                public void accept(MainProcessHandler mainProcessHandler) {
                    mainProcessHandler.init();
                }
            });
            Timer timer = new Timer();
            timer.startRecord("PRE_PROCESS");
            GraphBuilder graphBuilder = null;

            if (!this.handlers.isEmpty()) {
                timer.startRecord("PROJECT_CLASS");
                traverseArtifactOnly(getProcessors(Process.TRAVERSE, new ClassFileAnalyzer(context, false, graphBuilder, handlers)));
                timer.stopRecord("PROJECT_CLASS", "Process project all .class files cost time = [%s ms]");
            }

            if (!this.handlers.isEmpty()) {
                timer.startRecord("ANDROID");
                this.traverseAndroidJarOnly(this.getProcessors(Process.TRAVERSE_ANDROID, new ClassFileAnalyzer(this.context, true, graphBuilder, this.handlers)));
                timer.stopRecord("ANDROID", "Process android jar cost time = [%s ms]");
            }

//            this.mClassGraph = graphBuilder.build();

            timer.stopRecord("PRE_PROCESS", "Collect info cost time = [%s ms]");
            if (!this.handlers.isEmpty()) {
                timer.startRecord("PROCESS");
                glueContext.setTraverseTime(0);
                this.transform(this.getProcessors(Process.TRANSFORM, new ClassFileTransformer(this.handlers, this.needPreVerify(), this.needVerify())));
                timer.stopRecord("PROCESS", "Transform cost time = [%s ms]");
            }

            if (!this.handlers.isEmpty()) {
                timer.startRecord("PROCESS");
                glueContext.setTraverseTime(1);
                this.transform(this.getProcessors(Process.TRANSFORM, new ClassFileTransformer(this.handlers, this.needPreVerify(), this.needVerify())));
                timer.stopRecord("PROCESS", "Transform cost time = [%s ms]");
            }

        }
    }

    private FileProcessor[] getProcessors(final Process process, FileHandler fileHandler) {
        List<Object> processors = handlers.stream()
                .flatMap(new Function<MainProcessHandler, Stream<FileProcessor>>() {
                    @Override
                    public Stream<FileProcessor> apply(MainProcessHandler handler) {
                        return handler.process(process).stream();
                    }
                })
                .collect(Collectors.toList());
        processors.add(ClassFileProcessor.newInstance(fileHandler));
        return processors.toArray(new FileProcessor[0]);
    }

    private boolean needPreVerify() {
        Iterator var1 = this.handlers.iterator();

        MainProcessHandler handler;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            handler = (MainProcessHandler)var1.next();
        } while(!handler.needPreVerify());

        return true;
    }

    private boolean needVerify() {
        Iterator var1 = this.handlers.iterator();

        MainProcessHandler handler;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            handler = (MainProcessHandler)var1.next();
        } while(!handler.needVerify());

        return true;
    }

    public final TransformFlow appendHandler(MainProcessHandler handler) {
        this.handlers.add(handler);
        return this;
    }

    protected AbsTransformFlow beforeTransform(final TransformEngine transformEngine) {
        this.handlers.forEach(new Consumer<MainProcessHandler>() {
            @Override
            public void accept(MainProcessHandler plugin) {
                plugin.beforeTransform(transformEngine);
            }
        });
        return this;
    }

    protected AbsTransformFlow afterTransform(final TransformEngine transformEngine) {
        this.handlers.forEach(new Consumer<MainProcessHandler>() {
            @Override
            public void accept(MainProcessHandler plugin) {
                plugin.afterTransform(transformEngine);
            }
        });
        return this;
    }

//    @Nullable
//    public Graph getClassGraph() {
//        return this.mClassGraph;
//    }
}