package com.me.amator.service.transform;

import com.me.amator.service.Constants;
import com.ss.android.ugc.bytex.common.log.LevelLog;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

/**
 * Created by liguangquan on 2020-02-02
 * email: liguangquan@bytedance.com
 * desc:
 **/
public class GlueInjectFieldMethodVisitor extends MethodVisitor {

    private List<FieldNode> fieldNodes;
    private String className;

    public GlueInjectFieldMethodVisitor(MethodVisitor nethodVisitor, String className, List<FieldNode> fieldNodes) {
        super(Opcodes.ASM5, nethodVisitor);
        this.fieldNodes = fieldNodes;
        this.className = className;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        Type type;
        for (int i = 0; i < fieldNodes.size(); i++) {
            FieldNode fieldNode = fieldNodes.get(i);
            Label label1 = new Label();
            mv.visitLabel(label1);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitLdcInsn(Type.getType(fieldNode.desc));
            Label label2 = new Label();
            mv.visitLabel(label2);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Constants.SERVICEGLUE_CLASS,
                    Constants.SERVICEGLUE_METHOD,
                    "(Ljava/lang/Class;)Ljava/lang/Object;", false);
            type = Type.getType(fieldNode.desc);
            mv.visitTypeInsn(Opcodes.CHECKCAST, type.getInternalName());
            mv.visitFieldInsn(Opcodes.PUTFIELD, className, fieldNode.name, fieldNode.desc);
            LevelLog.DEFAULT.e(String.format("service inject instance %s %s = ServiceGlue.getService(%s.class) for %s", type.getClassName(), fieldNode.name, type.getClassName(), className));
        }
    }
}
