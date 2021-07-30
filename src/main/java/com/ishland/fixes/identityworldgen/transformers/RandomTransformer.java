package com.ishland.fixes.identityworldgen.transformers;

import com.ishland.fixes.identityworldgen.Constants;
import com.ishland.fixes.identityworldgen.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.ProtectionDomain;

public class RandomTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        try {
            if (!className.equals("java.util.Random".replace('.', '/'))) return null;
            System.err.println("[IdentityWorldGen] Transforming class " + className);
            final ClassReader classReader = new ClassReader(classfileBuffer);
            final ClassWriter classWriter = new ClassWriter(classReader, 0);
            final ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            transform(classNode);
            classNode.accept(classWriter);
            final byte[] bytes = classWriter.toByteArray();
            FileUtils.writeClassFile(className, bytes);
            return bytes;
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    private void transform(ClassNode classNode) {
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
