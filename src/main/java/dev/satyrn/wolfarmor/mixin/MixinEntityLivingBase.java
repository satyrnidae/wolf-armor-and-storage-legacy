package dev.satyrn.wolfarmor.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity {
    @Shadow protected boolean isJumping;

    @Shadow public abstract boolean isChild();

    @Shadow public void travel(float p_travel_1_, float p_travel_2_, float p_travel_3_) {}

    @Shadow public abstract void renderBrokenItemStack(ItemStack stack);

    @Shadow public abstract IAttributeInstance getEntityAttribute(IAttribute attribute);

    @Shadow public abstract Random getRNG();

    @Shadow protected void onNewPotionEffect(PotionEffect id) {}

    @Shadow protected void onChangedPotionEffect(PotionEffect potionEffect, boolean sendUpdate) {}

    @Shadow protected void onFinishedPotionEffect(PotionEffect effect) {}

    @Shadow protected  void damageArmor(float damage) {};
}
