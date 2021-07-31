package com.ishland.fixes.identityworldgen.transformers;

import com.ishland.fixes.identityworldgen.common.AbstractClassFileTransformer;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ModCandidateTransformer extends AbstractClassFileTransformer {

    @Override
    protected String[] targetClassName() {
        return new String[]{"net.fabricmc.loader.discovery.ModCandidate", "net.fabricmc.loader.impl.discovery.ModCandidate"};
    }

    @Override
    protected void transform(ClassNode classNode) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("requiresRemap") && method.desc.equals("()Z")) {
                System.err.println("Transforming method " + method.name + method.desc);
                for (AbstractInsnNode insnNode = method.instructions.getFirst(); insnNode.getNext() != null; insnNode = insnNode.getNext()) {
                    if (insnNode instanceof FieldInsnNode fieldInsnNode) {
                        if (fieldInsnNode.name.equals("requiresRemap")) {
                            method.instructions.remove(fieldInsnNode.getPrevious());
                            method.instructions.set(fieldInsnNode, new LdcInsnNode(1));
                            break;
                        }
                    }
                }
            }
        }

    }
}
