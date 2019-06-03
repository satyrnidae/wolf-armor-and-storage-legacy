package dev.satyrn.wolfarmor.entity.passive;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.IArmoredWolf;
import dev.satyrn.wolfarmor.api.util.Items;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Replacement entity for EntityWolf that supports armor
 * @deprecated Since 2.1.0
 */
@Deprecated
public class EntityWolfArmored extends EntityWolf implements IInventoryChangedListener, IArmoredWolf {
    //region Fields

    private ContainerHorseChest inventory;

    private static final String NBT_TAG_HAS_CHEST  = "hasChest";
    private static final String NBT_TAG_SLOT       = "slot";
    private static final String NBT_TAG_INVENTORY  = "inventory";
    private static final String NBT_TAG_ARMOR_ITEM = "armorItem";

    private static final int MAX_SIZE_INVENTORY = 7;

    private static final DataParameter<Boolean>   HAS_CHEST  = EntityDataManager.createKey(EntityWolfArmored.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ARMOR_ITEM = EntityDataManager.createKey(EntityWolfArmored.class, DataSerializers.ITEM_STACK);

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
    private void inventoryInit() {
        ContainerHorseChest inventoryExisting = this.inventory;
        this.inventory = new ContainerHorseChest("container.wolfarmor.wolf", 7);

        String customName = this.getCustomNameTag();

        if (!customName.isEmpty()) {
            this.inventory.setCustomName(customName);
        }

        if (inventoryExisting != null) {
            inventoryExisting.removeInventoryChangeListener(this);

            int numberOfItemsExisting = Math.min(this.inventory.getSizeInventory(), inventoryExisting.getSizeInventory());

            for (int slotIndex = 0; slotIndex < numberOfItemsExisting; slotIndex++) {
                ItemStack stackInSlot = inventoryExisting.getStackInSlot(slotIndex);

                if (!stackInSlot.isEmpty()) {
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
        this.dataManager.register(HAS_CHEST, false);
        this.dataManager.register(ARMOR_ITEM, ItemStack.EMPTY);
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

                if (!stackInSlot.isEmpty()) {
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

            if(!armorItem.isEmpty()) {
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
                    this.inventory.setInventorySlotContents(slotIndex, new ItemStack(itemTag));
                }
            }
        }

        NBTTagCompound armorTags = tags.getCompoundTag(NBT_TAG_ARMOR_ITEM);

        if(!armorTags.isEmpty())
        {
            ItemStack armorItemStack = new ItemStack(armorTags);

            this.equipArmor(armorItemStack);
        }
    }

    /**
     * Event called when the entity's inventory is changed.
     *
     * @param inventory The inventory
     */
    @Override
    public void onInventoryChanged(@Nonnull IInventory inventory) {
        ItemStack armor = inventory.getStackInSlot(0);

        this.setArmorItemStack(armor);
    }

    /**
     * Equips a wolf armor item
     * @param armorItemStack The armor to equip
     */
    @Override
    public void equipArmor(@Nonnull ItemStack armorItemStack) {
        if(this.canEquipItem(armorItemStack)) {
            this.inventory.setInventorySlotContents(0, armorItemStack);
        }
    }

    @Override
    public boolean canEquipItem(@Nonnull ItemStack armorItemStack) {
        return Items.isValidWolfArmor(armorItemStack) && (!this.getHasArmor() || armorItemStack.isEmpty());
    }

    @Override
    public boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        return super.processInteract(player, hand);
    }

    @Override
    public void dropEquipment(boolean killedByPlayer, int lootingModifier) {
        super.dropEquipment(killedByPlayer, lootingModifier);
    }

    @Override
    public void damageArmor(float damage) {
        super.damageArmor(damage);
    }

    @Override
    public void dropInventoryContents() { }

    @Override
    public void dropEquipment() { }

    //endregion Public / Protected Methods

    //region Accessors / Mutators

    /**
     * Gets a boolean value from the data watcher indicating whether or not the entity currently has a chest
     *
     * @return A boolean value indicating whether or not the entity currently has a chest
     */
    @Override
    public boolean getHasChest() {
        return WolfArmorMod.getConfiguration().getIsWolfChestEnabled() && this.dataManager.get(HAS_CHEST);
    }

    /**
     * Sets a boolean value on the data watcher representing whether or not the entity currently has an inventory.
     *
     * @param value The new value of the field.
     */
    @Override
    public void setHasChest(boolean value) {
       this.dataManager.set(HAS_CHEST, value);
    }

    /**
     * Gets a boolean value from the data watcher indicating whether or not the entity is currently armored.
     * @return A boolean value indicating whether or not the entity is currently armored.
     */
    @Override
    public boolean getHasArmor() {
        return !getArmorItemStack().isEmpty();
    }

    /**
     * Gets the entity's inventory
     *
     * @return The entity's inventory
     */
    @Override
    @Nonnull
    public InventoryBasic getInventory() {
        return this.inventory;
    }

    @Override
    public void setInventoryItem(int index, @Nonnull ItemStack itemStack) {
    }

    /**
     * Gets the entity's armor item from the data watcher.
     *
     * @return The entity's armor item.  If the item's stack size is zero, returns null.
     */
    @Override
    @Nonnull
    public ItemStack getArmorItemStack() {
        ItemStack itemStack = this.dataManager.get(ARMOR_ITEM);

        if(!Items.isValidWolfArmor(itemStack)) {
            this.dataManager.set(ARMOR_ITEM, ItemStack.EMPTY);
            return ItemStack.EMPTY;
        }

        return itemStack;
    }

    /**
     * Updates the entity data watcher with the value of the armor item stack.  If the item stack is null, replaces the value with a zero-sized item stack.
     *
     * @param itemStack The item stack to use, or null
     */
    @Override
    public void setArmorItemStack(@Nonnull ItemStack itemStack) {
        if(itemStack.isEmpty() || !Items.isValidWolfArmor(itemStack)) {
            return;
        }
        ItemStack currentArmor = getArmorItemStack();
        if(!currentArmor.isEmpty()) {
            return;
        }

        this.dataManager.set(ARMOR_ITEM, itemStack);
    }

    @Override
    public int getMaxSizeInventory() {
        return MAX_SIZE_INVENTORY;
    }


    //endregion Accessors / Mutators
}