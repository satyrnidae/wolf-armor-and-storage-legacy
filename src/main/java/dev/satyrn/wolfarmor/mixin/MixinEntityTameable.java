package dev.satyrn.wolfarmor.mixin;

import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityTameable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTameable.class)
public abstract class MixinEntityTameable extends MixinEntityAnimal {

    @Shadow
    public abstract EntityAISit getAISit();
}
