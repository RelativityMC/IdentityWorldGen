package com.ishland.fixes.identityworldgen;

import com.ishland.fixes.identityworldgen.transformers.C2METhreadLocalChunkRandomTransformer;
import com.ishland.fixes.identityworldgen.transformers.ModCandidateTransformer;
import com.ishland.fixes.identityworldgen.transformers.RandomTransformer;
import org.spongepowered.asm.mixin.Mixins;

import java.lang.instrument.Instrumentation;
import java.util.Random;

public class EntryPoint {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new C2METhreadLocalChunkRandomTransformer(), true);
        instrumentation.addTransformer(new RandomTransformer(), true);
//        instrumentation.addTransformer(new ModCandidateTransformer(), true);
        Random.class.toString();
        System.setProperty("com.ishland.fixes.identityworldgen.JavaAgentLoaded", "true");
        System.out.println("IdentityWorldGen initialized");
    }
}
