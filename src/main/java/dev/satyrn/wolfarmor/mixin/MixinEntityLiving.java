package dev.satyrn.wolfarmor.mixin;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends MixinEntityLivingBase {
    @Shadow public float[] inventoryHandsDropChances;
    @Shadow public float[] inventoryArmorDropChances;

    @Shadow public abstract PathNavigate getNavigator();

    @Shadow public abstract void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack);

    @Shadow public abstract void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn);
}
