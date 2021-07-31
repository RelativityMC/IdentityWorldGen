package com.ishland.fixes.identityworldgen.transformers;

import com.ishland.fixes.identityworldgen.Constants;
import com.ishland.fixes.identityworldgen.common.AbstractClassFileTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.stream.StreamSupport;

public class C2METhreadLocalChunkRandomTransformer extends AbstractClassFileTransformer {

    @Override
    protected String[] targetClassName() {
        return new String[]{"com.ishland.c2me.common.config.IdentityWorldGen"};
    }

    protected void transform(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("getIdentityWorldGenSeed") && Type.getReturnType(method.desc).getSort() == Type.INT) {
                System.err.println("Transforming method " + method.name + method.desc);
                StreamSupport.stream(method.instructions.spliterator(), false)
                        .filter(abstractInsnNode -> abstractInsnNode.getOpcode() == Opcodes.ICONST_0)
                        .findFirst()
                        .ifPresentOrElse(abstractInsnNode -> {
                            method.instructions.set(abstractInsnNode, new LdcInsnNode(Constants.identityWorldGenSeed));
                        }, () -> {
                            throw new IllegalArgumentException("No matching insn found");
                        });
            }
        }

    }
}
