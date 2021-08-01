package com.ishland.fixes.identityworldgen.mixin.fixed_random;

import com.ishland.fixes.identityworldgen.common.FixedChunkRandom;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator {

    @Redirect(method = "generateFeatures", at = @At(value = "NEW", target = "net/minecraft/world/gen/ChunkRandom"))
    private ChunkRandom redirectChunkRandom() {
        return new FixedChunkRandom();
    }

}
