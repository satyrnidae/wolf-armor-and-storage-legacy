package dev.satyrn.wolfarmor.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTameable.class)
public abstract class MixinEntityTameable extends MixinEntityAnimal {

    @Shadow public abstract boolean isSitting();

    @Shadow public abstract void setSitting(boolean sitting);

    @Shadow public void onDeath(DamageSource p_onDeath_1_) {}

    @Shadow public abstract boolean isOwner(EntityLivingBase p_isOwner_1_);

    @Shadow public abstract boolean isTamed();

    @Shadow
    public abstract EntityAISit getAISit();
}
