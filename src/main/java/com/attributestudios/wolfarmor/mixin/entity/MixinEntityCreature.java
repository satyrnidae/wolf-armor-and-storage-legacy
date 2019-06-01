package com.attributestudios.wolfarmor.mixin.entity;

import net.minecraft.entity.EntityCreature;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityCreature.class)
public abstract class MixinEntityCreature extends MixinEntityLiving {
}
