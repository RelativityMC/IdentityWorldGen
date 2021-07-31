package com.ishland.fixes.identityworldgen.mixin.structurestart;

import com.ishland.fixes.identityworldgen.common.IStructureStart;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(StructureStart.class)
public class MixinStructureStart<C extends FeatureConfig> implements IStructureStart {

    @Mutable
    @Shadow @Final protected ChunkRandom random;

    @Unique
    private Supplier<ChunkRandom> iwg$randomSupplier = null;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(StructureFeature<C> feature, ChunkPos pos, int references, long seed, CallbackInfo ci) {
        this.iwg$randomSupplier = () -> {
            final ChunkRandom chunkRandom = new ChunkRandom();
            chunkRandom.setCarverSeed(seed, pos.x, pos.z);
            return chunkRandom;
        };
    }

    @Override
    public void iwg$resetRandom() {
        this.random = this.iwg$randomSupplier.get();
    }
}
