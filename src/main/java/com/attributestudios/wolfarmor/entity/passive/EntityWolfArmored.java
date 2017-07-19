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
        this.dataManager.register(HAS_CHEST, (byte) 0);
<<<<<<< HEAD
        this.dataManager.register(ARMOR_ITEM, ItemStack.EMPTY);
=======
        this.dataManager.register(ARMOR_ITEM, Optional.<ItemStack>absent());
>>>>>>> origin/1.10.2-testing
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

<<<<<<< HEAD
                if (!stackInSlot.isEmpty()) {
=======
                if (stackInSlot != null) {
>>>>>>> origin/1.10.2-testing
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

<<<<<<< HEAD
            if(!armorItem.isEmpty()) {
=======
            if(armorItem != null) {
>>>>>>> origin/1.10.2-testing
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
<<<<<<< HEAD
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

            if(!armorItemStack.isEmpty() && getIsValidWolfArmorItem(armorItemStack.getItem()))
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

            if(!armorItem.isEmpty()) {
                this.entityDropItem(armorItem, 0);
                this.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
            }
        }

        if(this.getHasChest()) {
            this.entityDropItem(new ItemStack(Blocks.CHEST, 1), 0);
            for(int slotIndex = 1; slotIndex < getMaxSizeInventory(); slotIndex++) {
                ItemStack stack = this.inventory.getStackInSlot(slotIndex);

                if(!stack.isEmpty()) {
                    this.entityDropItem(stack, 0);
                    this.inventory.setInventorySlotContents(slotIndex, ItemStack.EMPTY);
                }
            }
        }
    }

    /**
     * Gets the item held in the entity's hand
     * @param hand Main or offhand
     * @return The armored wolf's armor if the hand is the main hand.  (custom enchants support)
     */
    @Override
    @Nonnull
    public ItemStack getHeldItem(@Nonnull EnumHand hand)
    {
        switch(hand) {
            case MAIN_HAND:
                return this.getArmorItemStack();
            default:
                return ItemStack.EMPTY;
        }
    }

    /**
     * Processes a player's attempt to interact with this entity
     * @param player The player attempting to interact with this entity
     * @param hand Main or offhand
     * @return <tt>true</tt> if the player successfully interacted with this entity, <tt>false</tt> if not
     */
    @Override
    public boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(!stack.isEmpty() && stack.getItem() == Items.SPAWN_EGG) {
            if (!world.isRemote) {
                return spawnEggInteract(player, stack) || super.processInteract(player, hand);
            }
            return super.processInteract(player, hand);

        }

        if(this.isTamed() && this.isOwner(player) && !this.isChild()) {
            if(player.isSneaking()) {
                this.openWolfGui(player);
                return true;
            }
            else {
                if(!stack.isEmpty()) {
                    if(WolfArmorMod.getConfiguration().getIsWolfChestEnabled()
                       && Block.getBlockFromItem(stack.getItem()) == Blocks.CHEST
                       && !this.getHasChest()) {
                        this.setHasChest(true);
                        this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1, (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F + 1);
                        this.inventoryInit();
                        if (!player.capabilities.isCreativeMode) {
                            stack.shrink(1);
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

        return super.processInteract(player, hand);
    }

    /**
     * Plays the entity item equip sound
     * @param stack The equipped stack
     */
    @Override
    public void playEquipSound(@Nonnull ItemStack stack) {
        if(!stack.isEmpty() && stack.getItem() instanceof ItemWolfArmor) {
            ItemWolfArmor armorItem = (ItemWolfArmor)stack.getItem();
            SoundEvent sound = armorItem.getMaterial().getEquipSound();

            this.playSound(sound, 1, 1);
        }

        super.playEquipSound(stack);
    }

    /**
     * Gets the wolf spawn egg, since this entity overrides the wolf.
     * @param rayTraceResult The ray trace result
     * @return The wolf spawn egg
     */
    @Override
    @Nonnull
    public ItemStack getPickedResult(@Nonnull RayTraceResult rayTraceResult) {

        ResourceLocation name = EntityList.getKey(EntityWolf.class);
        if(name != null && EntityList.ENTITY_EGGS.containsKey(name)) {
            ItemStack stack = new ItemStack(Items.SPAWN_EGG);
            ItemMonsterPlacer.applyEntityIdToItemStack(stack, name);

            return stack;
        }

        return ItemStack.EMPTY;
    }

    /**
     * Damages the entity's armor
     * @param damage The damage to apply
     */
    @Override
    protected void damageArmor(float damage) {
        if (this.getHasArmor()) {
            ItemStack armorStack = this.inventory.getStackInSlot(0);

            if (!armorStack.isEmpty() && getIsValidWolfArmorItem(armorStack)) {
                armorStack.damageItem((int) Math.ceil(damage), this);

                if (armorStack.isEmpty())
                {
                    this.equipArmor(ItemStack.EMPTY);
                }
            }
        }
    }

    /**
=======
>>>>>>> origin/1.10.2-testing
     * Equips a wolf armor item
     * @param armorItemStack The armor to equip
     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
     */
<<<<<<< HEAD
    public boolean equipArmor(@Nonnull ItemStack armorItemStack) {
        if(!getIsValidWolfArmorItem(armorItemStack) || (this.getHasArmor() && !armorItemStack.isEmpty())) {
=======
    private boolean equipArmor(@Nullable ItemStack armorItemStack) {
        if(!CapabilityWolfArmor.getIsValidWolfArmorItem(armorItemStack) || (this.getHasArmor() && armorItemStack != null)) {
>>>>>>> origin/1.10.2-testing
            return false;
        }

        this.inventory.setInventorySlotContents(0, armorItemStack);

        return true;
    }

<<<<<<< HEAD
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
    public static boolean getIsValidWolfArmorItem(@Nonnull ItemStack item) {
        return item.isEmpty() || getIsValidWolfArmorItem(item.getItem());
    }


    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Opens the wolf inventory
     * @param player The player
     */
    private void openWolfGui(@Nonnull EntityPlayer player) {
        if(!this.world.isRemote) {
            this.aiSit.setSitting(true);
            player.openGui(WolfArmorMod.instance,
                           this.getEntityId(),
                           this.world,
                           MathHelper.floor(this.posX),
                           MathHelper.floor(this.posY),
                           MathHelper.floor(this.posZ));
        }
    }

    /**
     * Processes a player's attempt to interact with this entity with a spawn egg
     * @param player The player attempting to interact with this entity
     * @param stack The stack with which the player is interacting with this entity
     * @return <tt>true</tt> if the player successfully interacted with this entity, <tt>false</tt> if not
     */
    private boolean spawnEggInteract(@Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
        ResourceLocation entityName = ItemMonsterPlacer.getNamedIdFrom(stack);

        if(entityName != null) {
            Class<? extends Entity> clazz = EntityList.getClass(entityName);

            if (clazz == EntityWolf.class || clazz == EntityWolfArmored.class) {
                EntityAgeable child = this.createChild(this);
                if (child != null) {
                    child.setGrowingAge(-24000);
                    child.setLocationAndAngles(posX, posY, posZ, 0, 0);
                    world.spawnEntity(child);

                    if (stack.hasDisplayName()) {
                        child.setCustomNameTag(stack.getDisplayName());
                    }
                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    return true;
                }
            }
        }

        return false;

    }

    //endregion Private Methods

=======
    //endregion Public / Protected Methods

>>>>>>> origin/1.10.2-testing
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
<<<<<<< HEAD
        return !this.getArmorItemStack().isEmpty();
=======
        return this.getArmorItemStack() != null;
>>>>>>> origin/1.10.2-testing
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

<<<<<<< HEAD
        if(!itemStack.isEmpty()) {

            if(!getIsValidWolfArmorItem(itemStack)) {
                this.dataManager.set(ARMOR_ITEM, ItemStack.EMPTY);
                return ItemStack.EMPTY;
=======
        if(itemStackOptional.isPresent()) {
            ItemStack armorItemStack = itemStackOptional.get();

            if(!CapabilityWolfArmor.getIsValidWolfArmorItem(armorItemStack.getItem()) || armorItemStack.stackSize == 0) {
                this.dataManager.set(ARMOR_ITEM, Optional.<ItemStack>absent());
                return null;
>>>>>>> origin/1.10.2-testing
            }

            return armorItemStack;
        }

<<<<<<< HEAD
        return ItemStack.EMPTY;
=======
        return null;
>>>>>>> origin/1.10.2-testing
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
