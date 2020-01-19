package com.me.amator.service.transform;


import com.me.amator.service.traverse.GlueHolder;
import com.me.amator.service.traverse.ServiceModel;
import com.ss.android.ugc.bytex.common.log.LevelLog;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * Created by liguangquan on 2020-01-19
 * email: liguangquan@bytedance.com
 * desc:注入
 **/
public class GlueMethodVisitor extends MethodVisitor {

    private Label gotoLabel;

    public GlueMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
    }


    @Override
    public void visitCode() {
        super.visitCode();
        List<ServiceModel> serviceModels = GlueHolder.getInstance().getsServiceModels();
        beforeInject();
        for (int i = 0; i < serviceModels.size(); i++) {
            ServiceModel model = serviceModels.get(i);
            inject(i == 0, (i == (serviceModels.size() - 1)), model);
        }
        afterInject();
    }

    private void afterInject() {
        mv.visitVarInsn(Opcodes.ALOAD, 1);//将变量推至栈顶
        mv.visitInsn(Opcodes.ARETURN);//return  object
    }

    private void inject(boolean isStart, boolean isLast,ServiceModel model) {
        mv.visitLdcInsn(Type.getType(String.format("L%s;", model.getCurrentInterface())));
        mv.visitVarInsn(Opcodes.ALOAD, 0);//压栈
        Label ifLabel;
        if (isLast && gotoLabel != null) {
            ifLabel = gotoLabel;
        } else {
            ifLabel = new Label();
        }
        mv.visitJumpInsn(Opcodes.IF_ACMPNE, ifLabel);//不想等跳转
        Label instanceLabel = new Label();
        mv.visitLabel(instanceLabel);
        if (model.getMethodNode() != null) {
            MethodNode methodNode = model.getMethodNode();
            mv.visitMethodInsn(methodNode.access, model.getClassName(), methodNode.name, methodNode.desc, false);
            LevelLog.DEFAULT.e(String.format("glue Service %s service = %s.%s()", model.getCurrentInterface(), model.getClassName(), methodNode.name));
        } else {
            mv.visitTypeInsn(Opcodes.NEW, model.getClassName());
            mv.visitInsn(Opcodes.DUP);//出栈
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, model.getClassName(), "<init>", "()V", false);
            LevelLog.DEFAULT.e(String.format("glue Service %s service = new %s()", model.getCurrentInterface(), model.getClassName()));
        }
        mv.visitVarInsn(Opcodes.ASTORE, 1);//赋值給栈第一个位置的变量，即为object
        if (isStart != isLast) {
            if (gotoLabel == null) {
                gotoLabel = new Label();
            }
            mv.visitJumpInsn(Opcodes.GOTO, gotoLabel);
        }
        mv.visitLabel(ifLabel);
        if (isStart) {//记录首帧变化情况
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"java/lang/Object"}, 0, null);
        } else {
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
    }

    private void beforeInject() {
        Label startLabel = new Label();
        mv.visitLabel(startLabel);
        mv.visitInsn(Opcodes.ACONST_NULL);
        mv.visitVarInsn(Opcodes.ASTORE, 1);//指定本地变量
        Label ifStartLabel = new Label();
        mv.visitLabel(ifStartLabel);
    }
}
