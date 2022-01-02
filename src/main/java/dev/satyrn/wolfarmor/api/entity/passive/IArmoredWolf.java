package dev.satyrn.wolfarmor.api.entity.passive;

import dev.satyrn.wolfarmor.api.entity.IFoodStatsCreature;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The main interface for all of the functionality added to the wolf entity by Wolf Armor and Storage
 * @author Isabel Maskrey (satyrnidae)
 * @version 2.3.0
 */
public interface IArmoredWolf extends IFoodStatsCreature, IInventoryChangedListener {
    /**
     * Gets the type of the wolf backpack as an item
     * @return The chest type of the wolf backpack
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Nullable Item getChestType();

    /**
     * Sets the chest type of the wolf backpack to the specified item stack
     * @param chestType An item stack which describes the backpack chest type
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void setChestType(@Nonnull ItemStack chestType);

    /**
     * Checks if the wolf has a backpack
     * @return {@code true} if the wolf has a backpack; otherwise, {@code false}
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    boolean getHasChest();

    /**
     * Sets the wolf to either have or not have an backpack
     * @param hasChest {@code true} to add a wolf backpack, {@code false} to remove it
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void setHasChest(boolean hasChest);

    /**
     * Gets the maximum size of a wolf's inventory
     * @return The maximum configured size of a wolf's inventory
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    int getMaxSizeInventory();

    /**
     * Gets the inventory instance
     * @return The wolf's inventory instance
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Nonnull InventoryBasic getInventory();

    /**
     * Gets the armor item stack from the data manager
     * @return The current armor item stack
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Nonnull ItemStack getArmorItemStack();

    /**
     * Sets the wolf's armor item stack in its data manager
     * @param armorItemStack The armor item stack
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void setArmorItemStack(@Nonnull ItemStack armorItemStack);

    /**
     * Checks if the wolf currently has armor equipped
     * @return {@code true} if the armor item is set; otherwise, {@code false}
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    boolean getHasArmor();

    /**
     * Checks if a specific item stack can be equipped by the wolf as armor
     * @param armorItemStack The armor item stack
     * @return {@code true} if the armor item can be equipped; otherwise, {@code false}.
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    boolean canEquipItem(@Nonnull ItemStack armorItemStack);

    /**
     * Equips an item stack as armor, if possible
     * @param armorItemStack The armor item stack to equip, provided canEquipItem is {@code true}
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void equipArmor(@Nonnull ItemStack armorItemStack);

    /**
     * Sets the inventory item in the specified slot
     * @param index The index of the slot to alter in the inventory
     * @param itemStack The item stack to insert into the specified slot
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void setInventoryItem(int index, @Nonnull ItemStack itemStack);

    /**
     * Drops any equipment that the wolf is carrying or wearing
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void dropEquipment();

    /**
     * Drops the wolf's backpack as a chest
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void dropChest();

    /**
     * Empties all items in the wolf's inventory into the world.
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void dropInventoryContents();
}
