package com.ishland.fixes.identityworldgen.common;

import net.minecraft.world.gen.ChunkRandom;

public class FixedChunkRandom extends ChunkRandom {

    @SuppressWarnings("FieldCanBeLocal")
    private final boolean initialized = true;
    private boolean needInit = true;
    private volatile long lastSeed = 0L;
    private final int[] value = new int[33];

    @Override
    public synchronized void setSeed(long seed) {
        if (seed != lastSeed) {
            if (!initialized) resetCache();
            lastSeed = seed;
        }
        super.setSeed(seed);
    }

    @Override
    public int next(int count) {
        if (needInit) {
            resetCache();
            needInit = false;
        }
        if (count > value.length - 1) {
            super.setSeed(lastSeed);
            return super.next(count);
        }
        return value[count];
    }

    private void resetCache() {
        for (int i = 0; i < value.length; i ++) {
            super.setSeed(lastSeed);
            value[i] = super.next(i);
        }
        needInit = false;
    }

}
