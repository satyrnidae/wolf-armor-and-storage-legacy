package dev.satyrn.wolfarmor.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow public boolean isDead;
    @Shadow public boolean onGround;
    @Shadow public double posX;
    @Shadow public double posY;
    @Shadow public double posZ;
    @Shadow public double motionZ;
    @Shadow public double motionX;
    @Shadow public float rotationYaw;
    @Shadow public World world;
    @Shadow protected EntityDataManager dataManager;

    @Shadow public abstract AxisAlignedBB getEntityBoundingBox();

    @Shadow @Nullable public abstract Entity getRidingEntity();

    @Shadow public abstract boolean isSprinting();

    @Shadow public abstract boolean isInWater();

    @Shadow public abstract boolean isInsideOfMaterial(Material p_isInsideOfMaterial_1_);

    @Shadow public abstract boolean isRiding();

    @Shadow protected abstract void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn);

    @Shadow @Nullable public abstract EntityItem entityDropItem(ItemStack p_entityDropItem_1_, float p_entityDropItem_2_);

    @Shadow public abstract String getCustomNameTag();

    @Shadow public abstract World getEntityWorld();

    @Shadow public abstract void playSound(SoundEvent soundIn, float volume, float pitch);

    @Shadow public abstract int getEntityId();
}
