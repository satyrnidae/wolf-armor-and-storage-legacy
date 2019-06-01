package com.attributestudios.wolfarmor.mixin.entity;

import net.minecraft.entity.passive.EntityTameable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityTameable.class)
public abstract class MixinEntityTameable extends MixinEntityAnimal {
}
