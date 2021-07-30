package com.ishland.fixes.identityworldgen.transformers;

import com.ishland.fixes.identityworldgen.Constants;
import com.ishland.fixes.identityworldgen.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.stream.StreamSupport;

public class C2METhreadLocalChunkRandomTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        try {
            if (!className.equals("com.ishland.c2me.common.config.IdentityWorldGen".replace('.', '/'))) return null;
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
