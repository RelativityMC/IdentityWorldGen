package com.ishland.fixes.identityworldgen.mixin.structurestart;

import com.ishland.fixes.identityworldgen.common.IStructureStart;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(Biome.class)
public class MixinBiome {

    @Dynamic
    @Redirect(method = "method_28401", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/StructureStart;generateStructure(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;)V"))
    private static void redirectGenerate(StructureStart<?> structureStart, StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox box, ChunkPos chunkPos) {
        ((IStructureStart) structureStart).iwg$resetRandom();
        structureStart.generateStructure(world, structureAccessor, chunkGenerator, random, box, chunkPos);
    }

}
