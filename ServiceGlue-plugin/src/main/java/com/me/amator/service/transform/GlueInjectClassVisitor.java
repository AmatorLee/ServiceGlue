package com.me.amator.service.transform;

import com.me.amator.service.ServiceFinder;
import com.ss.android.ugc.bytex.common.utils.TypeUtil;
import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;

import org.objectweb.asm.MethodVisitor;

import java.io.File;


/**
 * Created by liguangquan on 2020-01-18
 * email: liguangquan@bytedance.com
 * desc:注入classVisitor
 **/
public class GlueInjectClassVisitor extends BaseClassVisitor {

    private boolean mIsFinder;
    private String serviceFinderName;

    public GlueInjectClassVisitor(){
        serviceFinderName = ServiceFinder.class.getName().replace(".", File.separator);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mIsFinder = serviceFinderName.equals(name);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mIsFinder && name.equals("findService") && TypeUtil.isStatic(access)) {
            return new GlueMethodVisitor(mv);
        }
        return mv;
    }
}
