package com.ishland.fixes.identityworldgen.mixin.fixes;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;
import java.util.UUID;

@Mixin(Entity.class)
public class MixinEntity {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;randomUuid(Ljava/util/Random;)Ljava/util/UUID;"))
    private UUID onRandomUUID(Random random) {
        return UUID.randomUUID();
    }

}
