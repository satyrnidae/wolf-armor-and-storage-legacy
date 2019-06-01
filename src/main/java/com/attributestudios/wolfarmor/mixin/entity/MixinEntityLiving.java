package com.attributestudios.wolfarmor.mixin.entity;

import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends MixinEntityLivingBase {
}
