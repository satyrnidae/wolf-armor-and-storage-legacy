package dev.satyrn.wolfarmor.mixin.accessors;

import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityLiving.class)
public interface EntityLivingAccessor {
    @Accessor
    float[] getInventoryHandsDropChances();
    @Accessor
    float[] getInventoryArmorDropChances();
}
