package com.attributestudios.wolfarmor.api;

import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;

/**
 * Represents the capability instance for an armored wolf.
 *
 * @author Isabel Maskrey
 * @deprecated Since 3.0.
 */
@Deprecated
public interface IWolfArmorCapability extends IArmoredWolf, ICapabilitySerializable<NBTTagCompound>, IInventoryChangedListener {
    /**
     * Handles a player interacting with the capable entity.
     *
     * @param player The player.
     * @param hand   The hand that the player is using to interact with the entity.
     * @return {@code true} if the interaction was handled; otherwise, {@code false}
     * @deprecated Since 3.0
     */
    @Deprecated
    boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand);

    /**
     * Damages the entity's armor.
     *
     * @param damage The damage to apply.
     * @return {@code true} if the armor was damaged; otherwise, {@code false}
     */
    @Deprecated
    boolean damageArmor(float damage);
}
