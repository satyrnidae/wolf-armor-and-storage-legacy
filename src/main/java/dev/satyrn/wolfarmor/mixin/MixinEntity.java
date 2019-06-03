package dev.satyrn.wolfarmor.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow protected abstract void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn);

    @Shadow public double motionZ;
    @Shadow public double motionX;
    @Shadow public float rotationYaw;

    @Shadow @Nullable public abstract EntityItem entityDropItem(ItemStack p_entityDropItem_1_, float p_entityDropItem_2_);

    @Shadow public double posX;
    @Shadow public double posY;
    @Shadow public double posZ;

    @Shadow protected EntityDataManager dataManager;

    @Shadow public abstract String getCustomNameTag();

    @Shadow public abstract World getEntityWorld();

    @Shadow public abstract void playSound(SoundEvent soundIn, float volume, float pitch);

    @Shadow public abstract int getEntityId();
}
