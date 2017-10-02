package com.attributestudios.wolfarmor.api;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Interface describing wolf entity extensions
 */
public interface IWolfArmorExtendedProperties extends IExtendedEntityProperties {

    /**
     * The default max size of a wolf inventory.
     */
    static final int MAX_WOLF_INVENTORY = 7;

    /**
     * Gets whether or not the wolf has a backpack.
     */
    boolean getHasChest();

    /**
     * Sets whether or not the wolf has a backpack.
     * @param value flag for whether or not the wolf has a backpack
     */
    void setHasChest(boolean value);

    /**
     * Gets the wolf's armor item stack.
     */
    @Nullable
    ItemStack getArmorItemStack();

    /**
     * Sets the wolf's armor item stack.
     * @param armorItemStack The armor item stack. Set to null for no armor.
     */
    void setArmorItemStack(@Nullable ItemStack armorItemStack);

    /**
     * Gets the wolf's inventory
     */
    @Nonnull
    InventoryBasic getWolfInventory();

    /**
     * Gets the maximum size of the wolf's inventory.
     *   Defaults to MAX_WOLF_INVENTORY
     */
    @Nonnegative
    default int getMaxSizeInventory() {
        return MAX_WOLF_INVENTORY;
    }

    /**
     * Returns true if the given item may be equipped.
     * @param armorItemStack the item stack to test.
     *                       Returns true if this item is null.
     */
    boolean canEquipItem(@Nullable ItemStack armorItemStack);

    /**
     * Equips the given armor item stack as armor, 
     *   if possible
     * @param armorItemStack the armor to equip
     */
    void equipArmor(@Nullable ItemStack armorItemStack);

    /**
     * Processes player interact.  Returns true if successful,
     *   cancelling further interaction.
     * @param player The player entity who interacted
     *               with the wolf.
     */
    boolean interact(@Nonnull EntityPlayer player);

    /**
     * Drops the entity's equipped items.
     * @param killedByPlayer  True if the entity that killed
     *                        this wolf was a player
     * @param lootingModifier The looting modifier from the
     *                        weapon or entity that killed
     *                        the wolf
     */
    void dropEquipment(boolean killedByPlayer, int lootingModifier);

    /**
     * Drops the contents of the wolf's inventory.
     */
    void dropInventoryContents();

    /**
     * Damages the wolf's armor.
     * @param damage the damage amount.
     */
    void damageArmor(float damage);
}