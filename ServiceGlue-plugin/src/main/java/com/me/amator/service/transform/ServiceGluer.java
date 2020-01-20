package com.me.amator.service.transform;

import com.android.build.api.transform.Status;
import com.me.amator.service.Utils;
import com.me.amator.service.traverse.GlueHolder;
import com.me.amator.service.traverse.ServiceModel;
import com.ss.android.ugc.bytex.common.log.LevelLog;
import com.ss.android.ugc.bytex.transformer.TransformEngine;
import com.ss.android.ugc.bytex.transformer.cache.FileData;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.List;

import static org.objectweb.asm.Opcodes.ALOAD;

/**
 * Created by liguangquan on 2020-01-20
 * email: liguangquan@bytedance.com
 * desc:
 **/
public class ServiceGluer {


    public static byte[] getServiceGluer(ServiceModel serviceModel) {

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;

        classWriter.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, serviceModel.getCurrentInterface() + "$$ServiceGluer",
                null, "java/lang/Object", null);

        classWriter.visitSource(Utils.path2Current(false, Utils.path2Current(false, serviceModel.getCurrentInterface()) + "$$ServiceGluer"), null);

        //init
        {
            methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(Opcodes.RETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLocalVariable("this", String.format("L%s$$ServiceGluer;", serviceModel.getCurrentInterface()), null, label0, label1, 0);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }

        // glueService
        {
            methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "glueService", String.format("()L%s;", serviceModel.getCurrentInterface()), null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            if (serviceModel.getMethodNode() != null) {
                MethodNode methodNode = serviceModel.getMethodNode();
                methodVisitor.visitMethodInsn(methodNode.access, serviceModel.getClassName(), methodNode.name, methodNode.desc, false);
                LevelLog.DEFAULT.e(String.format("glue Service %s service = %s.%s()", serviceModel.getCurrentInterface(), serviceModel.getClassName(), methodNode.name));
            } else {
                methodVisitor.visitTypeInsn(Opcodes.NEW, serviceModel.getClassName());
                methodVisitor.visitInsn(Opcodes.DUP);//出栈
                methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, serviceModel.getClassName(), "<init>", "()V", false);
                LevelLog.DEFAULT.e(String.format("glue Service %s service = new %s()", serviceModel.getCurrentInterface(), serviceModel.getClassName()));
            }
            methodVisitor.visitInsn(Opcodes.ARETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLocalVariable("this", String.format("L%s$$ServiceGluer;", serviceModel.getCurrentInterface()), null, label0, label1, 0);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }

        classWriter.visitEnd();

        return classWriter.toByteArray();

    }


    public static void injectPluginService(final TransformEngine engine) {
        List<ServiceModel> pluginServiceModels = GlueHolder.getInstance().getPluginServiceModel();
        if (Utils.isEmpty(pluginServiceModels)) {
            return;
        }
        for (int i = 0; i < pluginServiceModels.size(); i++) {
            final ServiceModel serviceModel = pluginServiceModels.get(i);
            createPluginServiceGluer(engine, serviceModel);
        }
    }

    private static void createPluginServiceGluer(TransformEngine engine, ServiceModel serviceModel) {
        if (serviceModel == null || engine == null) {
            return;
        }
        byte[] bytes = ServiceGluer.getServiceGluer(serviceModel);
        String currentInterface = serviceModel.getCurrentInterface();
        String current = Utils.path2Current(true, currentInterface);
        String simpleName = Utils.path2Current(false, currentInterface);
        String relativePath = current + File.separator + simpleName + "$$ServiceGluer.class";
        engine.addFile("serviceGluer", new FileData(bytes, relativePath, Status.ADDED));
    }


}
