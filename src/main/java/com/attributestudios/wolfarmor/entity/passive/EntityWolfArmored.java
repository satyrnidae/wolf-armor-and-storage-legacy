package com.attributestudios.wolfarmor.entity.passive;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Replacement entity for EntityWolf that supports armor
 */
public class EntityWolfArmored extends EntityWolf implements IInventoryChangedListener {
    //region Fields

    private ContainerHorseChest inventory;

    private static final String NBT_TAG_HAS_CHEST  = "hasChest";
    private static final String NBT_TAG_SLOT       = "slot";
    private static final String NBT_TAG_INVENTORY  = "inventory";
    private static final String NBT_TAG_ARMOR_ITEM = "armorItem";

    private static final int INVENTORY_WOLF_MAX_SIZE = 7; // One armor + 6 potential storage

    private static final DataParameter<Byte>      HAS_CHEST = EntityDataManager.createKey(EntityWolfArmored.class, DataSerializers.BYTE);
    private static final DataParameter<ItemStack> ARMOR_ITEM = EntityDataManager.createKey(EntityWolfArmored.class, DataSerializers.OPTIONAL_ITEM_STACK);

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

    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed<EntityAnimal>(this, EntityAnimal.class, false, new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity entity)
            {
                return entity instanceof EntitySheep || entity instanceof EntityRabbit;
            }
        }));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<EntitySkeleton>(this, EntitySkeleton.class, false));
    }

    /**
     * Sets up the entity's inventory.
     */
    @SuppressWarnings("WeakerAccess")
    protected void inventoryInit() {
        ContainerHorseChest inventoryExisting = this.inventory;
        this.inventory = new ContainerHorseChest("container.wolfarmor.wolf", this.getMaxSizeInventory());

        String customName = this.getCustomNameTag();

        if (!customName.isEmpty()) {
            // set inventory custom name
            this.inventory.setCustomName(customName);
        }

        if (inventoryExisting != null) {
            // disassociate the existing inventory from this entity
            inventoryExisting.removeInventoryChangeListener(this);

            int numberOfItemsExisting = Math.min(this.inventory.getSizeInventory(), inventoryExisting.getSizeInventory());

            for (int slotIndex = 0; slotIndex < numberOfItemsExisting; slotIndex++) {
                ItemStack stackInSlot = inventoryExisting.getStackInSlot(slotIndex);

                if (!stackInSlot.func_190926_b()) {
                    this.inventory.setInventorySlotContents(slotIndex, stackInSlot.copy());
                }
            }
        }

        // associate the new inventory with this entity
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
        this.dataManager.register(ARMOR_ITEM, ItemStack.field_190927_a);
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

                if (!stackInSlot.func_190926_b()) {
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

            if(!armorItem.func_190926_b()) {
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

        if(!armorTags.hasNoTags())
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

            if(!armorItemStack.func_190926_b() && getIsValidWolfArmorItem(armorItemStack.getItem()))
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

            if(!armorItem.func_190926_b()) {
                this.entityDropItem(armorItem, 0);
                this.inventory.setInventorySlotContents(0, ItemStack.field_190927_a);
            }
        }

        if(this.getHasChest()) {
            this.entityDropItem(new ItemStack(Blocks.CHEST, 1), 0);
            for(int slotIndex = 1; slotIndex < getMaxSizeInventory(); slotIndex++) {
                ItemStack stack = this.inventory.getStackInSlot(slotIndex);

                if(!stack.func_190926_b()) {
                    this.entityDropItem(stack, 0);
                    this.inventory.setInventorySlotContents(slotIndex, ItemStack.field_190927_a);
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
                return ItemStack.field_190927_a;
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

        if(!stack.func_190926_b() && stack.getItem() == Items.SPAWN_EGG) {
            if (!worldObj.isRemote) {
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
                if(!stack.func_190926_b()) {
                    if(WolfArmorMod.getConfiguration().getIsWolfChestEnabled()
                       && Block.getBlockFromItem(stack.getItem()) == Blocks.CHEST
                       && !this.getHasChest()) {
                        this.setHasChest(true);
                        this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1, (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F + 1);
                        this.inventoryInit();
                        if (!player.capabilities.isCreativeMode) {
                            stack.func_190918_g(1);
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
        if(!stack.func_190926_b() && stack.getItem() instanceof ItemWolfArmor) {
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

        ResourceLocation name = EntityList.func_191306_a(EntityWolf.class);
        if(name != null && EntityList.ENTITY_EGGS.containsKey(name)) {
            ItemStack stack = new ItemStack(Items.SPAWN_EGG);
            ItemMonsterPlacer.applyEntityIdToItemStack(stack, name);

            return stack;
        }

        return ItemStack.field_190927_a;
    }

    /**
     * Damages the entity's armor
     * @param damage The damage to apply
     */
    @Override
    protected void damageArmor(float damage) {
        if (this.getHasArmor()) {
            ItemStack armorStack = this.inventory.getStackInSlot(0);

            if (!armorStack.func_190926_b() && getIsValidWolfArmorItem(armorStack)) {
                armorStack.damageItem((int) Math.ceil(damage), this);

                if (armorStack.func_190926_b())
                {
                    this.equipArmor(ItemStack.field_190927_a);
                }
            }
        }
    }

    /**
     * Equips a wolf armor item
     * @param armorItemStack The armor to equip
     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
     */
    public boolean equipArmor(@Nonnull ItemStack armorItemStack) {
        if(!getIsValidWolfArmorItem(armorItemStack) || (this.getHasArmor() && !armorItemStack.func_190926_b())) {
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
    public static boolean getIsValidWolfArmorItem(@Nonnull ItemStack item) {
        return item.func_190926_b() || getIsValidWolfArmorItem(item.getItem());
    }


    //endregion Public / Protected Methods

    //region Private Methods

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
        ResourceLocation entityName = ItemMonsterPlacer.func_190908_h(stack);

        if(entityName != null) {
            Class<? extends Entity> clazz = EntityList.getClass(entityName);

            if (clazz == EntityWolf.class || clazz == EntityWolfArmored.class) {
                EntityAgeable child = this.createChild(this);
                if (child != null) {
                    child.setGrowingAge(-24000);
                    child.setLocationAndAngles(posX, posY, posZ, 0, 0);
                    worldObj.spawnEntityInWorld(child);

                    if (stack.hasDisplayName()) {
                        child.setCustomNameTag(stack.getDisplayName());
                    }
                    if (!player.capabilities.isCreativeMode) {
                        stack.func_190918_g(1);
                    }

                    return true;
                }
            }
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
        return !this.getArmorItemStack().func_190926_b();
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
    @Nonnull
    public ItemStack getArmorItemStack() {
        ItemStack itemStack = this.dataManager.get(ARMOR_ITEM);

        if(!itemStack.func_190926_b()) {

            if(!getIsValidWolfArmorItem(itemStack)) {
                this.dataManager.set(ARMOR_ITEM, ItemStack.field_190927_a);
                return ItemStack.field_190927_a;
            }

            return itemStack;
        }

        return ItemStack.field_190927_a;
    }

    /**
     * Updates the entity data watcher with the value of the armor item stack.  If the item stack is null, replaces the value with a zero-sized item stack.
     *
     * @param armorItemStack The item stack to use, or null
     */
    @SuppressWarnings("WeakerAccess")
    public void setArmorItemStack(@Nonnull ItemStack armorItemStack) {
        if(getIsValidWolfArmorItem(armorItemStack)) {
            this.dataManager.set(ARMOR_ITEM, armorItemStack);
        }
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
