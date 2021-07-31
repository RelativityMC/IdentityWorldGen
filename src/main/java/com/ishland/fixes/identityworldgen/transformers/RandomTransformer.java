package com.ishland.fixes.identityworldgen.transformers;

import com.ishland.fixes.identityworldgen.Constants;
import com.ishland.fixes.identityworldgen.common.AbstractClassFileTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class RandomTransformer extends AbstractClassFileTransformer {

    @Override
    protected String[] targetClassName() {
        return new String[]{"java.util.Random"};
    }

    protected void transform(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("<init>") && method.desc.equals("()V")) {
                System.err.println("Transforming method " + method.name + method.desc);
                for (AbstractInsnNode insnNode = method.instructions.getFirst(); insnNode.getNext() != null; insnNode = insnNode.getNext()) {
                    if (insnNode.getOpcode() == Opcodes.LXOR) {
                        final AbstractInsnNode previous = insnNode.getPrevious();
                        final AbstractInsnNode previous2 = previous.getPrevious();
                        removeMethodCall(method.instructions, previous2, "java/util/Random", "seedUniquifier", "()J");
                        removeMethodCall(method.instructions, previous, "java/lang/System", "nanoTime", "()J");
                        method.instructions.set(insnNode, new LdcInsnNode((long) Constants.identityWorldGenSeed));
                        break;
                    }
                }
            }
        }
    }

    private void removeMethodCall(InsnList insnNodes, AbstractInsnNode insnNode, String owner, String name, String desc) {
        if (insnNode instanceof MethodInsnNode methodInsnNode) {
            if (!methodInsnNode.owner.equals(owner) || !methodInsnNode.name.equals(name) || !methodInsnNode.desc.equals(desc)) {
                throw new IllegalArgumentException(String.format("Invalid method signature: expected %s.%s%s but got %s.%s%s", owner, name, desc, methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc));
            }
            insnNodes.remove(insnNode);
        } else {
            throw new IllegalArgumentException(String.format("Invalid insn: Expected %s but got %s", MethodInsnNode.class.getName(), insnNode.getClass().getName()));
        }
    }

}
