package com.ishland.fixes.identityworldgen;

import net.fabricmc.api.ModInitializer;

import java.util.UUID;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        if (!Boolean.parseBoolean(System.getProperty("com.ishland.fixes.identityworldgen.JavaAgentLoaded"))) {
            throw new RuntimeException("Java Agent not loaded");
        }
    }
}
