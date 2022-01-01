package com.attributestudios.wolfarmor.common.capabilities;

import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.api.util.Capabilities;
import dev.satyrn.wolfarmor.api.util.CreatureFoodStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Placeholder class for compatibility.
 * @author Isabel Maskrey
 * @deprecated Since 3.3.0.279.
 */
@Deprecated
public class CapabilityWolfArmor implements IWolfArmorCapability {
    /**
     * The capability storage instance.
     */
    public static final Storage STORAGE = new Storage();
    private final InventoryBasic inventory = new InventoryBasic("", false, this.getMaxSizeInventory());
    private final CreatureFoodStats foodStats = new CreatureFoodStats();

    /**
     * Handles a player interacting with the capable entity.
     *
     * @param player The player.
     * @param hand   The hand that the player is using to interact with the entity.
     * @return {@code true} if the interaction was handled; otherwise, {@code false}
     * @deprecated Since 3.0
     */
    @Override
    public boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        return false;
    }

    /**
     * Damages the entity's armor.
     *
     * @param damage The damage to apply.
     * @return {@code true} if the armor was damaged; otherwise, {@code false}
     */
    @Override
    public boolean damageArmor(float damage) {
        return false;
    }

    /**
     * Gets the food stats instance for this creature.
     *
     * @return The creature's food stats instance
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Nonnull
    @Override
    public CreatureFoodStats getFoodStats() {
        return foodStats;
    }

    /**
     * Adds levels of exhaustion to the creature's food stats instance
     *
     * @param exhaustion The levels of exhaustion to add
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void addExhaustion(float exhaustion) { }

    /**
     * Gets the type of the wolf backpack as an item
     *
     * @return The chest type of the wolf backpack
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Nullable
    @Override
    public Item getChestType() {
        return null;
    }

    /**
     * Sets the chest type of the wolf backpack to the specified item stack
     *
     * @param chestType An item stack which describes the backpack chest type
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void setChestType(@Nonnull ItemStack chestType) { }

    /**
     * Checks if the wolf has a backpack
     *
     * @return {@code true} if the wolf has a backpack; otherwise, {@code false}
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public boolean getHasChest() {
        return false;
    }

    /**
     * Sets the wolf to either have or not have an backpack
     *
     * @param hasChest {@code true} to add a wolf backpack, {@code false} to remove it
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void setHasChest(boolean hasChest) { }

    /**
     * Gets the maximum size of a wolf's inventory
     *
     * @return The maximum configured size of a wolf's inventory
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public int getMaxSizeInventory() { return 1; }

    /**
     * Gets the inventory instance
     *
     * @return The wolf's inventory instance
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Nonnull
    @Override
    public InventoryBasic getInventory() {
        return this.inventory;
    }

    /**
     * Gets the armor item stack from the data manager
     *
     * @return The current armor item stack
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Nonnull
    @Override
    public ItemStack getArmorItemStack() {
        return ItemStack.EMPTY;
    }

    /**
     * Sets the wolf's armor item stack in its data manager
     *
     * @param armorItemStack The armor item stack
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void setArmorItemStack(@Nonnull ItemStack armorItemStack) { }

    /**
     * Checks if the wolf currently has armor equipped
     *
     * @return {@code true} if the armor item is set; otherwise, {@code false}
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public boolean getHasArmor() {
        return false;
    }

    /**
     * Checks if a specific item stack can be equipped by the wolf as armor
     *
     * @param armorItemStack The armor item stack
     * @return {@code true} if the armor item can be equipped; otherwise, {@code false}.
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public boolean canEquipItem(@Nonnull ItemStack armorItemStack) {
        return false;
    }

    /**
     * Equips an item stack as armor, if possible
     *
     * @param armorItemStack The armor item stack to equip, provided canEquipItem is {@code true}
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void equipArmor(@Nonnull ItemStack armorItemStack) { }

    /**
     * Sets the inventory item in the specified slot
     *
     * @param index     The index of the slot to alter in the inventory
     * @param itemStack The item stack to insert into the specified slot
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void setInventoryItem(int index, @Nonnull ItemStack itemStack) { }

    /**
     * Drops any equipment that the wolf is carrying or wearing
     *
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void dropEquipment() { }

    /**
     * Drops the wolf's backpack as a chest
     *
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void dropChest() { }

    /**
     * Empties all items in the wolf's inventory into the world.
     *
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Override
    public void dropInventoryContents() { }

    /**
     * Handles the inventory changed event.
     *
     * @param invBasic The inventory.
     */
    @Override
    public void onInventoryChanged(@Nonnull IInventory invBasic) { }

    /**
     * Determines if this object has support for the capability in question on the specific side.
     * The return value of this MIGHT change during runtime if this object gains or loses support
     * for a capability. It is not required to call this function before calling
     * {@link #getCapability(Capability, EnumFacing)};
     * <p>
     * <em>Example:</em>
     * A Pipe getting a cover placed on one side causing it lose the Inventory attachment function for that side.
     * </p><p>
     * This is a light weight version of getCapability, intended for metadata uses.
     * </p>
     *
     * @param capability The capability to check
     * @param facing     The Side to check from:
     *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
     * @return True if this object supports the capability. If true, then {@link #getCapability(Capability, EnumFacing)}
     * must not return null.
     */
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    /**
     * Retrieves the handler for the capability requested on the specific side.
     * <ul>
     * <li>The return value <strong>CAN</strong> be null if the object does not support the capability.</il>
     * <li>The return value <strong>CAN</strong> be the same for multiple faces.</li>
     * </ul>
     *
     * @param capability The capability to check
     * @param facing     The Side to check from,
     *                   <strong>CAN BE NULL</strong>. Null is defined to represent 'internal' or 'self'
     * @return The requested capability. Must <strong>NOT</strong> be null when {@link #hasCapability(Capability, EnumFacing)}
     * would return true.
     */
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }

    /**
     * Writes the capability to an NBT instance.
     * @return The NBT instance.
     */
    @Nonnull
    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    /**
     * Reads the capability data from an NBT instance.
     * @param nbt The NBT instance.
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt) { }

    /**
     * Handles wolf armor capability storage.
     */
    public static class Storage implements Capability.IStorage<IWolfArmorCapability> {

        /**
         * Serialize the capability instance to a NBTTag.
         * This allows for a central implementation of saving the data.
         * <p>
         * It is important to note that it is up to the API defining
         * the capability what requirements the 'instance' value must have.
         * <p>
         * Due to the possibility of manipulating internal data, some
         * implementations MAY require that the 'instance' be an instance
         * of the 'default' implementation.
         * <p>
         * Review the API docs for more info.
         *
         * @param capability The Capability being stored.
         * @param instance   An instance of that capabilities interface.
         * @param side       The side of the object the instance is associated with.
         * @return a NBT holding the data. Null if no data needs to be stored.
         */
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IWolfArmorCapability> capability, IWolfArmorCapability instance, EnumFacing side) {
            if (capability != Capabilities.CAPABILITY_WOLF_ARMOR) return null;
            return instance.serializeNBT();
        }

        /**
         * Read the capability instance from a NBT tag.
         * <p>
         * This allows for a central implementation of saving the data.
         * <p>
         * It is important to note that it is up to the API defining
         * the capability what requirements the 'instance' value must have.
         * <p>
         * Due to the possibility of manipulating internal data, some
         * implementations MAY require that the 'instance' be an instance
         * of the 'default' implementation.
         * <p>
         * Review the API docs for more info.         *
         *
         * @param capability The Capability being stored.
         * @param instance   An instance of that capabilities interface.
         * @param side       The side of the object the instance is associated with.
         * @param nbt        A NBT holding the data. Must not be null, as doesn't make sense to call this function with nothing to read...
         */
        @Override
        public void readNBT(Capability<IWolfArmorCapability> capability, IWolfArmorCapability instance, EnumFacing side, NBTBase nbt) {
            if (capability != Capabilities.CAPABILITY_WOLF_ARMOR) return;
            if (nbt instanceof NBTTagCompound) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }
    }
}

