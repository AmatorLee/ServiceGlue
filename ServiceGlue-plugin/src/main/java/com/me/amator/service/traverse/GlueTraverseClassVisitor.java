package com.me.amator.service.traverse;

import com.me.amator.service.api.InstanceProvider;
import com.me.amator.service.api.PluginServiceImpl;
import com.me.amator.service.api.ServiceImpl;
import com.me.amator.service.api.ServiceInterface;
import com.ss.android.ugc.bytex.common.utils.TypeUtil;
import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by liguangquan on 2019-1-19
 * email: liguangquan@bytedance.com
 * desc:
 **/
public class GlueTraverseClassVisitor extends BaseClassVisitor {

    private String className;
    private String[] interfaces;
    private boolean findServiceImpl;
    private boolean findServiceInterface;
    private ServiceModel serviceModel;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.interfaces = interfaces;
    }


    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(Type.getDescriptor(ServiceImpl.class))) {
            findServiceImpl = true;
            serviceModel = new ServiceModel();
            serviceModel.setPlugin(false);
            serviceModel.setClassName(className);
            serviceModel.setInterfaces(interfaces);
        } else if (descriptor.equals(Type.getDescriptor(ServiceInterface.class))) {
            findServiceInterface = true;
        } else if (descriptor.equals(Type.getDescriptor(PluginServiceImpl.class))) {
            findServiceImpl = true;
            serviceModel = new ServiceModel();
            serviceModel.setPlugin(true);
            serviceModel.setClassName(className);
            serviceModel.setInterfaces(interfaces);
        }
        return super.visitAnnotation(descriptor, visible);
    }


    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        if (findServiceImpl) {
            return new MethodVisitor(Opcodes.ASM5, mv) {

                @Override
                public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {

                    if (descriptor.equals(Type.getDescriptor(InstanceProvider.class))) {
                        if (!TypeUtil.isStatic(access) || !TypeUtil.isPublic(access)) {
                            throw new IllegalArgumentException("InstanceProvider must be public and static method");
                        }
                        serviceModel.setMethodNode(new MethodNode(Opcodes.INVOKESTATIC, name, desc, signature, exceptions));
                    }

                    return super.visitAnnotation(descriptor, visible);
                }
            };
        }
        return mv;
    }


    @Override
    public void visitEnd() {
        super.visitEnd();
        if (findServiceImpl && findServiceInterface) {
            throw new IllegalArgumentException("@ServiceInterface and @ServiceImpl add on a same class?");
        }
        if (findServiceInterface) {
            GlueHolder.getInstance().addInterface(className);
        }

        if (findServiceImpl) {
            GlueHolder.getInstance().addServiceModel(serviceModel);
        }
    }
}
