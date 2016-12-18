package com.attributestudios.wolfarmor.entity.passive;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.ReflectionCache;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.attributestudios.wolfarmor.item.WolfArmorItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Replacement entity for EntityWolf that supports armor
 */
public class EntityWolfArmored extends EntityWolf implements IInvBasic {
    
    //region Fields

    private AnimalChest inventory;

    private static final String NBT_TAG_HAS_CHEST  = "hasChest";
    private static final String NBT_TAG_SLOT       = "slot";
    private static final String NBT_TAG_INVENTORY  = "inventory";
    private static final String NBT_TAG_HAS_ARMOR  = "hasArmor";
    private static final String NBT_TAG_ARMOR_ITEM = "armorItem";

    private static final int INVENTORY_WOLF_MAX_SIZE = 7;
    private static final int DATA_WATCHER_HAS_CHEST  = 21;
    private static final int DATA_WATCHER_HAS_ARMOR  = 22;
    private static final int DATA_WATCHER_ARMOR_ITEM = 23;

    private static final String STRING_TO_ID_MAPPING_SRG = "field_180126_g";

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
        this.inventory = new AnimalChest("container.wolfarmor.wolf", this.getMaxSizeInventory());

        String customName = this.getCustomNameTag();
        if (customName != null
             && !customName.isEmpty()) {
            this.inventory.func_110133_a(customName);
        }

        if (inventoryExisting != null) {
            inventoryExisting.func_110132_b(this);

            int numberOfItemsExisting = Math.min(this.inventory.getSizeInventory(), inventoryExisting.getSizeInventory());

            for (int slotIndex = 0; slotIndex < numberOfItemsExisting; slotIndex++) {
                ItemStack stackInSlot = inventoryExisting.getStackInSlot(slotIndex);

                if (stackInSlot != null) {
                    this.inventory.setInventorySlotContents(slotIndex, stackInSlot.copy());
                }
            }
        }

        this.inventory.func_110134_a(this);
        this.inventory.markDirty();
    }

    /**
     * Initializes the entity's data watcher values
     */
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(DATA_WATCHER_HAS_CHEST, (byte) 0);
        this.dataWatcher.addObject(DATA_WATCHER_HAS_ARMOR, (byte) 0);
        this.dataWatcher.addObject(DATA_WATCHER_ARMOR_ITEM, getEmptyArmorStack());
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

        tags.setBoolean(NBT_TAG_HAS_ARMOR, entityHasArmor);

        if(entityHasArmor)
        {
            ItemStack armorItem = getArmorItemStack();

            if(armorItem == null) {
                armorItem = getEmptyArmorStack();
            }

            tags.setTag(NBT_TAG_ARMOR_ITEM, armorItem.writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * Reads the entity from the provided NBT Tag Compound
     * @param tags The NBT data containing the entity's values
     */
    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound tags) {
        super.readEntityFromNBT(tags);

        // Only load EntityWolfArmored-specific data if the NBT we are reading is actually an EntityWolfArmored NBT.
        boolean entityHasChest = tags.hasKey(NBT_TAG_HAS_CHEST, 1) && tags.getBoolean(NBT_TAG_HAS_CHEST);
        boolean entityHasArmor = tags.hasKey(NBT_TAG_HAS_ARMOR, 1) && tags.getBoolean(NBT_TAG_HAS_ARMOR);

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

        this.setHasArmor(entityHasArmor);

        if(entityHasArmor)
        {
            ItemStack armorItem = ItemStack.loadItemStackFromNBT(tags.getCompoundTag(NBT_TAG_ARMOR_ITEM));

            this.setArmorItemStack(armorItem);

            this.inventory.setInventorySlotContents(0, armorItem);
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
     * Gets the total armor value of the armor present on this entity
     *
     * @return The armor value of this wolf's armor
     */
    @Override
    public int getTotalArmorValue()
    {
        int totalArmor = super.getTotalArmorValue();

        if(this.getHasArmor())
        {
            ItemStack armorItemStack = this.getArmorItemStack();

            if(armorItemStack != null && getIsValidWolfArmorItem(armorItemStack))
            {
                totalArmor += ((ItemWolfArmor)armorItemStack.getItem()).getDamageReductionAmount();
            }
        }

        return totalArmor;
    }

    /**
     * Drops the entity's equipment on death.
     * @param killedByPlayer Whtehr or not the entity was killed by a player.
     * @param lootingModifier The looting modifier of the killing entity.
     */
    @Override
    public void dropEquipment(boolean killedByPlayer, int lootingModifier) {
        if(this.getHasArmor()) {
            ItemStack armorItem = this.getArmorItemStack();

            if(armorItem != null) {
                this.entityDropItem(armorItem, 0);
                this.inventory.setInventorySlotContents(0, null);
            }
        }

        if(this.getHasChest()) {
            for(int slotIndex = 1; slotIndex < getMaxSizeInventory(); slotIndex++) {
                ItemStack stack = this.inventory.getStackInSlot(slotIndex);

                if(stack != null) {
                    this.entityDropItem(stack, 0);
                    this.inventory.setInventorySlotContents(0, null);
                }
            }
        }
    }

    /**
     * Gets the item held in the entity's hand
     * @return The armored wolf's armor if the hand is the main hand.  (custom enchants support)
     */
    @Override
    @Nullable
    public ItemStack getHeldItem() {
        return this.getArmorItemStack();
    }

    /**
     * Processes a player's attempt to interact with this entity
     * @param player The player attempting to interact with this entity
     * @return <tt>true</tt> if the player successfully interacted with this entity, <tt>false</tt> if not
     */
    @Override
    public boolean interact(@Nonnull EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();

        if(stack != null && stack.getItem() == Items.spawn_egg) {
            if(!worldObj.isRemote) {
                return spawnEggInteract(player, stack) || super.interact(player);
            }
            return super.interact(player);
        }

        if(this.isTamed() && this.func_152114_e(player) && !this.isChild()) {
            if(player.isSneaking()) {
                this.openWolfGui(player);
                return true;
            }
            else {
                if(stack != null) {
                    if(WolfArmorMod.getConfiguration().getIsWolfChestEnabled()
                       && Block.getBlockFromItem(stack.getItem()) == Blocks.chest
                       && !this.getHasChest()) {
                        this.setHasChest(true);
                        this.playSound("mob.chickenplop", 1, (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F + 1);
                        this.inventoryInit();
                        if(!player.capabilities.isCreativeMode && --stack.stackSize == 0) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }

                        return true;
                    }

                    if(getIsValidWolfArmorItem(stack)) {
                        this.openWolfGui(player);
                        return true;
                    }
                }
            }
        }

        return super.interact(player);
    }

    /**
     * Gets the wolf spawn egg, since this entity overrides the wolf.
     * @param target The ray trace result
     * @return The wolf spawn egg
     */
    @Override
    @Nullable
    public ItemStack getPickedResult(@Nonnull MovingObjectPosition target) {
        String name = (String)EntityList.classToStringMapping.get(EntityWolf.class);
        int id = -1;
        try {
            Field stringToIdMappingField = ReflectionCache.getField(EntityList.class, STRING_TO_ID_MAPPING_SRG, "stringToIDMapping");

            if(stringToIdMappingField != null) {
                Map stringToIdMapping = (Map)stringToIdMappingField.get(null);
                id = (Integer)stringToIdMapping.get(name);
            }
        } catch(IllegalAccessException ex) {
            WolfArmorMod.getLogger().fatal(ex);
            throw new RuntimeException(ex);
        }

        if(EntityList.entityEggs.containsKey(id)) {
            return new ItemStack(Items.spawn_egg, 1, id);
        }

        return super.getPickedResult(target);
    }

    /**
     * Damages the entity's armor
     * @param damage The damage to apply
     */
    @Override
    protected void damageArmor(float damage) {
        if (this.getHasArmor()) {
            ItemStack armorStack = this.inventory.getStackInSlot(0);

            if (armorStack != null && getIsValidWolfArmorItem(armorStack)) {
                armorStack.damageItem((int) Math.ceil(damage), this);

                if (armorStack.stackSize == 0)
                {
                    this.equipArmor(null);
                }
            }
        }
    }

    /**
     * Equips a wolf armor item
     * @param armorItemStack The armor to equip
     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
     */
    public boolean equipArmor(@Nullable ItemStack armorItemStack) {
        if(!getIsValidWolfArmorItem(armorItemStack) || (this.getHasArmor() && armorItemStack != null)) {
            return false;
        }

        this.inventory.setInventorySlotContents(0, armorItemStack);

        return true;
    }

    /**
     * Used to determine whether or not the item is a valid wolf armor item.
     * @param item The item
     * @return <tt>true</tt> if the item is an instance of <tt>ItemWolfArmor</tt>, false if not.
     */
    public static boolean getIsValidWolfArmorItem(@Nullable Item item) {
        return item == null || item instanceof ItemWolfArmor;
    }

    /**
     * Used to determine whether or not the item stack is a valid wolf armor item.
     * @param item The item
     * @return <tt>true</tt> if the item is an instance of <tt>ItemWolfArmor</tt>, false if not.
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean getIsValidWolfArmorItem(@Nullable ItemStack item) {
        return item == null || item.getItem() instanceof ItemWolfArmor;
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Gets an empty item stack to use as a placeholder in the wolf's data watcher.
     *
     * @return An empty item stack
     */
    @Nonnull
    private static ItemStack getEmptyArmorStack() {
        return new ItemStack(WolfArmorItems.LEATHER_WOLF_ARMOR, 0);
    }

    /**
     * Opens the wolf inventory
     * @param player The player
     */
    private void openWolfGui(@Nonnull EntityPlayer player) {
        if(!this.worldObj.isRemote) {
            this.aiSit.setSitting(true);
            player.openGui(WolfArmorMod.instance,
                           this.getEntityId(),
                           this.worldObj,
                           MathHelper.floor_double(this.posX),
                           MathHelper.floor_double(this.posY),
                           MathHelper.floor_double(this.posZ));
        }
    }

    /**
     * Processes a player's attempt to interact with this entity with a spawn egg
     * @param player The player attempting to interact with this entity
     * @param stack The stack with which the player is interacting with this entity
     * @return <tt>true</tt> if the player successfully interacted with this entity, <tt>false</tt> if not
     */
    private boolean spawnEggInteract(@Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
        Class clazz = EntityList.getClassFromID(stack.getItemDamage());

        if(clazz != null && (clazz == EntityWolf.class || clazz == EntityWolfArmored.class)) {
            EntityAgeable child = this.createChild(this);
            child.setGrowingAge(-24000);
            child.setLocationAndAngles(posX, posY, posZ, 0, 0);
            worldObj.spawnEntityInWorld(child);

            if(stack.hasDisplayName()) {
                child.setCustomNameTag(stack.getDisplayName());
            }
            if(!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            }

            return true;
        }

        return false;
    }


    //endregion Private Methods

    //region Accessors / Mutators

    /**
     * Gets a boolean value from the data watcher indicating whether or not the entity currently has a chest
     *
     * @return A boolean value indicating whether or not the entity currently has a chest
     */
    public boolean getHasChest() {
        return WolfArmorMod.getConfiguration().getIsWolfChestEnabled() && (this.dataWatcher.getWatchableObjectByte(DATA_WATCHER_HAS_CHEST) & 0x2) != 0;
    }

    /**
     * Sets a boolean value on the data watcher representing whether or not the entity currently has an inventory.
     *
     * @param value The new value of the field.
     */
    @SuppressWarnings("WeakerAccess")
    public void setHasChest(boolean value) {
        byte hasChest = this.dataWatcher.getWatchableObjectByte(DATA_WATCHER_HAS_CHEST);

        if(value)
        {
            this.dataWatcher.updateObject(DATA_WATCHER_HAS_CHEST, (byte)(hasChest | 2));
        }
        else
        {
            this.dataWatcher.updateObject(DATA_WATCHER_HAS_CHEST, (byte)(hasChest & -3));
        }
    }

    /**
     * Gets a boolean value from the data watcher indicating whether or not the entity is currently armored.
     * @return A boolean value indicating whether or not the entity is currently armored.
     */
    public boolean getHasArmor() {
        return (this.dataWatcher.getWatchableObjectByte(DATA_WATCHER_HAS_ARMOR) & 2) != 0;
    }

    /**
     * Sets a boolean value on the data watcher representing whether or not the entity is currently armored.
     * @param value Whether the wolf has armor
     */
    @SuppressWarnings("WeakerAccess")
    public void setHasArmor(boolean value) {
        byte hasArmor = this.dataWatcher.getWatchableObjectByte(DATA_WATCHER_HAS_ARMOR);

        if(value)
        {
            this.dataWatcher.updateObject(DATA_WATCHER_HAS_ARMOR, (byte)(hasArmor | 2));
        }
        else
        {
            this.dataWatcher.updateObject(DATA_WATCHER_HAS_ARMOR, (byte)(hasArmor & -3));
        }
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
        ItemStack stack = this.dataWatcher.getWatchableObjectItemStack(DATA_WATCHER_ARMOR_ITEM);

        if (!this.getHasArmor() || stack != null && stack.stackSize == 0) {
            return null;
        }

        return stack;
    }

    /**
     * Updates the entity data watcher with the value of the armor item stack.  If the item stack is null, replaces the value with a zero-sized item stack.
     *
     * @param armorItemStack The item stack to use, or null
     */
    @SuppressWarnings("WeakerAccess")
    public void setArmorItemStack(@Nullable ItemStack armorItemStack) {
        boolean isValidArmor = armorItemStack != null && getIsValidWolfArmorItem(armorItemStack.getItem());

        this.setHasArmor(isValidArmor);

        if (!isValidArmor) {
            armorItemStack = getEmptyArmorStack();
        }
        this.dataWatcher.updateObject(DATA_WATCHER_ARMOR_ITEM, armorItemStack);
    }

    /**
     * The maximum size for the entity's inventory, including armor slots.
     *
     * @return The maximum size for the entity's inventory
     */
    @SuppressWarnings("WeakerAccess")
    @Nonnegative
    protected int getMaxSizeInventory() {
        return INVENTORY_WOLF_MAX_SIZE;
    }

    //endregion Accessors / Mutators
}
