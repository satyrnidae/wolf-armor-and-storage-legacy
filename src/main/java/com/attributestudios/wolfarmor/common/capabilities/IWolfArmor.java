package com.attributestudios.wolfarmor.common.capabilities;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.attributestudios.wolfarmor.WolfArmorMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;


public interface IWolfArmor {
    /**
     * Gets a boolean value from the data watcher indicating whether or not the entity currently has a chest
     *
     * @return A boolean value indicating whether or not the entity currently has a chest
     */
    public boolean getHasChest();
    /**
     * Sets a boolean value on the data watcher representing whether or not the entity currently has an inventory.
     *
     * @param value The new value of the field.
     */
    public void setHasChest(boolean value);
    /**
     * Gets a boolean value from the data watcher indicating whether or not the entity is currently armored.
     * @return A boolean value indicating whether or not the entity is currently armored.
     */
    public boolean getHasArmor();
    /**
     * Gets the entity's inventory
     *
     * @return The entity's inventory
     */
    @Nonnull
    public IInventory getInventory();
    /**
     * Gets the entity's armor item from the data watcher.
     *
     * @return The entity's armor item.  If the item's stack size is zero, returns null.
     */
    @Nullable
    public ItemStack getArmorItemStack();
    /**
     * Updates the entity data watcher with the value of the armor item stack.  If the item stack is null, replaces the value with a zero-sized item stack.
     *
     * @param armorItemStack The item stack to use, or null
     */
    public void setArmorItemStack(@Nullable ItemStack armorItemStack);
    /**
     * The maximum size for the entity's inventory, including armor slots.
     *
     * @return The maximum size for the entity's inventory
     */
    @Nonnegative
    public int getMaxSizeInventory();
    /**
     * Equips a wolf armor item
     * @param armorItemStack The armor to equip
     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
     */
    public boolean equipArmor(@Nullable ItemStack armorItemStack);
    /**
     * Processes a player's attempt to interact with this entity
     * @param player The player attempting to interact with this entity
     * @param hand Main or offhand
     * @param stack The stack with which the player is interacting with this entity
     * @return <tt>true</tt> if the player successfully interacted with this entity, <tt>false</tt> if not
     */
    public boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nullable ItemStack stack);
    /**
     * Drops the entity's equipment on death.
     * @param killedByPlayer Whtehr or not the entity was killed by a player.
     * @param lootingModifier The looting modifier of the killing entity.
     */
    public void dropEquipment(boolean killedByPlayer, int lootingModifier);
    /**
     * Damages the entity's armor
     * @param damage The damage to apply
     */
    public void damageArmor(float damage);
}
