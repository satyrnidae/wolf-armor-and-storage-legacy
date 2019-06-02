package dev.satyrn.wolfarmor.mixin;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends MixinEntityLivingBase {

    @Shadow
    public abstract void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack);

    @Shadow
    public abstract void setDropChance(EntityEquipmentSlot slotIn, float chance);
}
