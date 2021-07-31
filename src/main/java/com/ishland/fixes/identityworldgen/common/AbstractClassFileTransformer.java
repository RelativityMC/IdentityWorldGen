package com.ishland.fixes.identityworldgen.common;

import com.ishland.fixes.identityworldgen.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Arrays;

public abstract class AbstractClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        try {
            if (Arrays.stream(targetClassName()).noneMatch(s -> className.equals(s.replace('.', '/')))) return null;
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

    protected abstract String[] targetClassName();

    protected abstract void transform(ClassNode classNode);

}
