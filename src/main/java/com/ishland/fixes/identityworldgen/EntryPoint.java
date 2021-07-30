package com.ishland.fixes.identityworldgen;

import com.ishland.fixes.identityworldgen.transformers.C2METhreadLocalChunkRandomTransformer;
import com.ishland.fixes.identityworldgen.transformers.RandomTransformer;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Random;

public class EntryPoint {

    public static void premain(String agentArgs, Instrumentation instrumentation) throws UnmodifiableClassException {
        instrumentation.addTransformer(new C2METhreadLocalChunkRandomTransformer(), true);
        instrumentation.addTransformer(new RandomTransformer(), true);
        Random.class.toString();
        System.out.println("IdentityWorldGen initialized");
    }

}
