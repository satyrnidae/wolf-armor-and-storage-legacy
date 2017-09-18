package com.attributestudios.wolfarmor.entity.passive;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.google.common.base.Optional;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Replacement entity for EntityWolf that supports armor
 */
@SuppressWarnings({"DeprecatedIsStillUsed", "deprecation"})
@Deprecated
public class EntityWolfArmored extends EntityWolf implements IInventoryChangedListener {
    //region Fields

    private AnimalChest inventory;

    private static final String NBT_TAG_HAS_CHEST  = "hasChest";
    private static final String NBT_TAG_SLOT       = "slot";
    private static final String NBT_TAG_INVENTORY  = "inventory";
    private static final String NBT_TAG_ARMOR_ITEM = "armorItem";

    private static final DataParameter<Byte>                HAS_CHEST  = EntityDataManager.createKey(EntityWolfArmored.class, DataSerializers.BYTE);
    private static final DataParameter<Optional<ItemStack>> ARMOR_ITEM = EntityDataManager.createKey(EntityWolfArmored.class, DataSerializers.OPTIONAL_ITEM_STACK);

    //endregion Fields

    //region Constructors

    /**
     * Creates a new entity in the specified world.
     *
     * @param world The world in which to create the entity.
     */
    public EntityWolfArmored(@Nonnull World world) {
        super(world);
        this.inventoryInit();
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Sets up the entity's inventory.
     */
    @SuppressWarnings("WeakerAccess")
    protected void inventoryInit() {
        AnimalChest inventoryExisting = this.inventory;
        this.inventory = new AnimalChest("container.wolfarmor.wolf", 7);

        String customName = this.getCustomNameTag();

        if (!customName.isEmpty()) {
            this.inventory.setCustomName(customName);
        }

        if (inventoryExisting != null) {
            inventoryExisting.removeInventoryChangeListener(this);

            int numberOfItemsExisting = Math.min(this.inventory.getSizeInventory(), inventoryExisting.getSizeInventory());

            for (int slotIndex = 0; slotIndex < numberOfItemsExisting; slotIndex++) {
                ItemStack stackInSlot = inventoryExisting.getStackInSlot(slotIndex);

                if (stackInSlot != null) {
                    this.inventory.setInventorySlotContents(slotIndex, stackInSlot.copy());
                }
            }
        }

        this.inventory.addInventoryChangeListener(this);
        this.inventory.markDirty();
    }

    /**
     * Initializes the entity's data watcher values
     */
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HAS_CHEST, (byte) 0);
        this.dataManager.register(ARMOR_ITEM, Optional.<ItemStack>absent());
    }

    /**
     * Writes the entity to the provided NBT Tag Compound
     *
     * @param tags The NBT data to write the entity to
     */
    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound tags) {
        super.writeEntityToNBT(tags);
        boolean entityHasChest = this.getHasChest();
        boolean entityHasArmor = this.getHasArmor();

        tags.setBoolean(NBT_TAG_HAS_CHEST, entityHasChest);

        if (entityHasChest) {
            NBTTagList inventoryItemsTagList = new NBTTagList();

            for (byte slotIndex = 0;
                 slotIndex < this.getInventory().getSizeInventory();
                 slotIndex++) {
                ItemStack stackInSlot = this.getInventory().getStackInSlot(slotIndex);

                if (stackInSlot != null) {
                    NBTTagCompound slotTag = new NBTTagCompound();

                    slotTag.setByte(NBT_TAG_SLOT, slotIndex);
                    stackInSlot.writeToNBT(slotTag);

                    inventoryItemsTagList.appendTag(slotTag);
                }
            }

            tags.setTag(NBT_TAG_INVENTORY, inventoryItemsTagList);
        }

        if(entityHasArmor)
        {
            ItemStack armorItem = getArmorItemStack();

            if(armorItem != null) {
                tags.setTag(NBT_TAG_ARMOR_ITEM, armorItem.writeToNBT(new NBTTagCompound()));
            }
            else {
                tags.removeTag(NBT_TAG_ARMOR_ITEM);
            }
        }
    }

    /**
     * Reads the entity from the provided NBT Tag Compound
     *
     * @param tags The NBT data containing the entity's values
     */
    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound tags) {
        super.readEntityFromNBT(tags);

        // Only load EntityWolfArmored-specific data if the NBT we are reading is actually an EntityWolfArmored NBT.
        boolean entityHasChest = tags.hasKey(NBT_TAG_HAS_CHEST, 1) && tags.getBoolean(NBT_TAG_HAS_CHEST);

        this.setHasChest(entityHasChest);

        if(entityHasChest)
        {
            this.inventoryInit();
            // Tags of type NBTTagCompound
            NBTTagList inventoryItemsTagList = tags.getTagList(NBT_TAG_INVENTORY, 10);

            for(int tagIndex = 0; tagIndex < inventoryItemsTagList.tagCount(); tagIndex++)
            {
                NBTTagCompound itemTag = inventoryItemsTagList.getCompoundTagAt(tagIndex);
                byte slotIndex = itemTag.getByte(NBT_TAG_SLOT);

                if(slotIndex < this.inventory.getSizeInventory())
                {
                    this.inventory.setInventorySlotContents(slotIndex, ItemStack.loadItemStackFromNBT(itemTag));
                }
            }
        }

        NBTTagCompound armorTags = tags.getCompoundTag(NBT_TAG_ARMOR_ITEM);

        if(!armorTags.hasNoTags())
        {
            ItemStack armorItemStack = ItemStack.loadItemStackFromNBT(armorTags);

            this.equipArmor(armorItemStack);
        }
    }

    /**
     * Event called when the entity's inventory is changed.
     *
     * @param inventory The inventory
     */
    @Override
    public void onInventoryChanged(@Nonnull InventoryBasic inventory) {
        ItemStack armor = inventory.getStackInSlot(0);

        this.setArmorItemStack(armor);
    }

    /**
     * Equips a wolf armor item
     * @param armorItemStack The armor to equip
     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
     */
    private boolean equipArmor(@Nullable ItemStack armorItemStack) {
        if(!CapabilityWolfArmor.getIsValidWolfArmorItem(armorItemStack) || (this.getHasArmor() && armorItemStack != null)) {
            return false;
        }

        this.inventory.setInventorySlotContents(0, armorItemStack);

        return true;
    }

    //endregion Public / Protected Methods

    //region Accessors / Mutators

    /**
     * Gets a boolean value from the data watcher indicating whether or not the entity currently has a chest
     *
     * @return A boolean value indicating whether or not the entity currently has a chest
     */
    public boolean getHasChest() {
        return WolfArmorMod.getConfiguration().getIsWolfChestEnabled() && (this.dataManager.get(HAS_CHEST) & 0x2) != 0;
    }

    /**
     * Sets a boolean value on the data watcher representing whether or not the entity currently has an inventory.
     *
     * @param value The new value of the field.
     */
    @SuppressWarnings("WeakerAccess")
    public void setHasChest(boolean value) {
        byte hasChest = this.dataManager.get(HAS_CHEST);

        if(value)
        {
            this.dataManager.set(HAS_CHEST, (byte)(hasChest | 2));
        }
        else
        {
            this.dataManager.set(HAS_CHEST, (byte)(hasChest & -3));
        }
    }

    /**
     * Gets a boolean value from the data watcher indicating whether or not the entity is currently armored.
     * @return A boolean value indicating whether or not the entity is currently armored.
     */
    public boolean getHasArmor() {
        return this.getArmorItemStack() != null;
    }

    /**
     * Gets the entity's inventory
     *
     * @return The entity's inventory
     */
    @Nonnull
    public IInventory getInventory() {
        return this.inventory;
    }

    /**
     * Gets the entity's armor item from the data watcher.
     *
     * @return The entity's armor item.  If the item's stack size is zero, returns null.
     */
    @Nullable
    public ItemStack getArmorItemStack() {
        Optional<ItemStack> itemStackOptional = this.dataManager.get(ARMOR_ITEM);

        if(itemStackOptional.isPresent()) {
            ItemStack armorItemStack = itemStackOptional.get();

            if(!CapabilityWolfArmor.getIsValidWolfArmorItem(armorItemStack.getItem()) || armorItemStack.stackSize == 0) {
                this.dataManager.set(ARMOR_ITEM, Optional.<ItemStack>absent());
                return null;
            }

            return armorItemStack;
        }

        return null;
    }

    /**
     * Updates the entity data watcher with the value of the armor item stack.  If the item stack is null, replaces the value with a zero-sized item stack.
     *
     * @param armorItemStack The item stack to use, or null
     */
    @SuppressWarnings("WeakerAccess")
    public void setArmorItemStack(@Nullable ItemStack armorItemStack) {
        ItemStack previousItemStack = getArmorItemStack();

        if(armorItemStack != null && (previousItemStack == null || previousItemStack.getItem() != armorItemStack.getItem())) {
            this.playEquipSound(armorItemStack);
        }

        Optional<ItemStack> itemStackOptional = Optional.fromNullable(armorItemStack);

        if(CapabilityWolfArmor.getIsValidWolfArmorItem(armorItemStack)) {
            this.dataManager.set(ARMOR_ITEM, itemStackOptional);
        }
    }

    //endregion Accessors / Mutators
}
