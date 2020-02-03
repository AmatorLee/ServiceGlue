package com.me.amator.service.transform;

import com.me.amator.service.ServiceFinder;
import com.me.amator.service.Utils;
import com.me.amator.service.api.ServiceInject;
import com.me.amator.service.traverse.GlueHolder;
import com.ss.android.ugc.bytex.common.log.LevelLog;
import com.ss.android.ugc.bytex.common.utils.TypeUtil;
import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

import java.io.File;
import java.util.List;


/**
 * Created by liguangquan on 2020-01-18
 * email: liguangquan@bytedance.com
 * desc:注入classVisitor
 **/
public class GlueInjectClassVisitor extends BaseClassVisitor {

    private boolean mIsFinder;
    private String serviceFinderName;
    private List<FieldNode> fieldNodes;
    private String mClassName;

    public GlueInjectClassVisitor(){
        serviceFinderName = ServiceFinder.class.getName().replace(".", File.separator);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClassName = name;
        mIsFinder = serviceFinderName.equals(name);
        fieldNodes = GlueHolder.getInstance().containFieldKey(name);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (!GlueHolder.getInstance().hasRunTransform()) {
            if (mIsFinder && name.equals("findService") &&
                    TypeUtil.isStatic(access)) {
                return new GlueMethodVisitor(mv);
            } else if (!Utils.isEmpty(fieldNodes)
                    && name.equals("<init>")) {
                return new GlueInjectFieldMethodVisitor(mv, mClassName, fieldNodes);
            }
        }
        return mv;
    }
}
