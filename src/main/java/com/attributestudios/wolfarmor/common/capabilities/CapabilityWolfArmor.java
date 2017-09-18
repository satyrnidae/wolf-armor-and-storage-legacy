package com.attributestudios.wolfarmor.common.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityWolfArmor implements ICapabilitySerializable<NBTTagCompound>, IWolfArmor, IInventoryChangedListener {

    @CapabilityInject(IWolfArmor.class)
    public static Capability<IWolfArmor> WOLF_ARMOR = null;
    
    public static void RegisterCapability() {
    	CapabilityManager.INSTANCE.register(IWolfArmor.class, new Capability.IStorage<IWolfArmor>() {

			@Override
			public NBTBase writeNBT(Capability<IWolfArmor> capability, IWolfArmor instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IWolfArmor> capability, IWolfArmor instance, EnumFacing side, NBTBase nbt) {
				
			}
		}, CapabilityWolfArmor.class);
    }

    /**
     * Used to determine whether or not the item is a valid wolf armor item.W
     *
     * @param item The item
     * @return <tt>true</tt> if the item is an instance of <tt>ItemWolfArmor</tt>, false if not.
     */
    public static boolean getIsValidWolfArmorItem(@Nullable Item item) {
        return item == null || item instanceof ItemWolfArmor;
    }

    /**
     * Used to determine whether or not the item stack is a valid wolf armor item.
     *
     * @param item The item
     * @return <tt>true</tt> if the item is an instance of <tt>ItemWolfArmor</tt>, false if not.
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean getIsValidWolfArmorItem(@Nullable ItemStack item) {
        return item == null || getIsValidWolfArmorItem(item.getItem());
    }

    private AnimalChest inventory;
    private static final String NBT_TAG_HAS_CHEST = "hasChest";
    private static final String NBT_TAG_SLOT = "slot";
    private static final String NBT_TAG_INVENTORY = "inventory";
    private static final String NBT_TAG_ARMOR_ITEM = "armorItem";

    private static final int INVENTORY_WOLF_MAX_SIZE = 7; // One armor + 6 potential storage

    private final EntityWolf wolf;
    private final EntityDataManager dataManager;

    private static final DataParameter<Byte> HAS_CHEST = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BYTE);
    private static final DataParameter<Optional<ItemStack>> ARMOR_ITEM = EntityDataManager.createKey(EntityWolf.class, DataSerializers.OPTIONAL_ITEM_STACK);

    //private boolean

    public CapabilityWolfArmor(@Nonnull EntityWolf wolf) {
        this.wolf = wolf;
        dataManager = wolf.getDataManager();
        dataManager.register(HAS_CHEST, (byte) 0);
        dataManager.register(ARMOR_ITEM, Optional.<ItemStack>absent());
        this.inventoryInit();
    }

    @Override
    @Nonnull
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tags = new NBTTagCompound();

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

        if (entityHasArmor) {
            ItemStack armorItem = getArmorItemStack();

            if (armorItem != null) {
                tags.setTag(NBT_TAG_ARMOR_ITEM, armorItem.writeToNBT(new NBTTagCompound()));
            } else {
                tags.removeTag(NBT_TAG_ARMOR_ITEM);
            }
        }
        return tags;

    }

    @Override
    public void deserializeNBT(@Nonnull NBTTagCompound tags) {
        boolean entityHasChest = tags.hasKey(NBT_TAG_HAS_CHEST, 1) && tags.getBoolean(NBT_TAG_HAS_CHEST);

        this.setHasChest(entityHasChest);

        if (entityHasChest) {
            this.inventoryInit();
            // Tags of type NBTTagCompound
            NBTTagList inventoryItemsTagList = tags.getTagList(NBT_TAG_INVENTORY, 10);

            for (int tagIndex = 0; tagIndex < inventoryItemsTagList.tagCount(); tagIndex++) {
                NBTTagCompound itemTag = inventoryItemsTagList.getCompoundTagAt(tagIndex);
                byte slotIndex = itemTag.getByte(NBT_TAG_SLOT);

                if (slotIndex < this.inventory.getSizeInventory()) {
                    this.inventory.setInventorySlotContents(slotIndex, ItemStack.loadItemStackFromNBT(itemTag));
                }
            }
        }

        NBTTagCompound armorTags = tags.getCompoundTag(NBT_TAG_ARMOR_ITEM);

        if (!armorTags.hasNoTags()) {
            ItemStack armorItemStack = ItemStack.loadItemStackFromNBT(armorTags);

            this.equipArmor(armorItemStack);
        }
    }

    @Override
    public boolean getHasChest() {
        return WolfArmorMod.getConfiguration().getIsWolfChestEnabled() && (this.dataManager.get(HAS_CHEST) & 0x2) != 0;
    }

    @Override
    public void setHasChest(boolean value) {
        byte hasChest = this.dataManager.get(HAS_CHEST);

        if (value) {
            this.dataManager.set(HAS_CHEST, (byte) (hasChest | 2));
        } else {
            this.dataManager.set(HAS_CHEST, (byte) (hasChest & -3));
        }
    }

    @Override
    public boolean getHasArmor() {
        return this.getArmorItemStack() != null;
    }

    @Override
    @Nonnull
    public IInventory getInventory() {
        return this.inventory;
    }

    @Override
    @Nullable
    public ItemStack getArmorItemStack() {
        Optional<ItemStack> itemStackOptional = this.dataManager.get(ARMOR_ITEM);

        if (itemStackOptional.isPresent()) {
            ItemStack armorItemStack = itemStackOptional.get();

            if (!getIsValidWolfArmorItem(armorItemStack.getItem()) || armorItemStack.stackSize == 0) {
                this.dataManager.set(ARMOR_ITEM, Optional.<ItemStack>absent());
                return null;
            }

            return armorItemStack;
        }

        return null;
    }

    private void playEquipSound(@Nullable ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemWolfArmor) {
            ItemWolfArmor armorItem = (ItemWolfArmor) stack.getItem();
            SoundEvent sound = armorItem.getMaterial().getEquipSound();

            wolf.playSound(sound, 1, 1);
        }
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void setArmorItemStack(@Nullable ItemStack armorItemStack) {
        ItemStack previousItemStack = getArmorItemStack();

        if (armorItemStack != null && (previousItemStack == null || previousItemStack.getItem() != armorItemStack.getItem())) {
            this.playEquipSound(armorItemStack);
        }

        Optional<ItemStack> itemStackOptional = Optional.fromNullable(armorItemStack);

        if (getIsValidWolfArmorItem(armorItemStack)) {
            this.dataManager.set(ARMOR_ITEM, itemStackOptional);
        }

        //update armor attribute
        @Nullable IAttributeInstance armorAttribute = wolf.getEntityAttribute(SharedMonsterAttributes.ARMOR);
        if (armorAttribute != null) {
            armorAttribute.removeAllModifiers();
            if (armorItemStack != null)
                armorAttribute.applyModifier(((ItemWolfArmor) armorItemStack.getItem()).getMaterial().armorAttr);
        }
    }

    @Override
    public int getMaxSizeInventory() {
        return INVENTORY_WOLF_MAX_SIZE;
    }

    @Override
    public boolean equipArmor(@Nullable ItemStack armorItemStack) {
        if (!getIsValidWolfArmorItem(armorItemStack) || (this.getHasArmor() && armorItemStack != null)) {
            return false;
        }

        this.inventory.setInventorySlotContents(0, armorItemStack);

        return true;
    }

    private void openWolfGui(@Nonnull EntityPlayer player) {
        if (!wolf.getEntityWorld().isRemote) {
            wolf.getAISit().setSitting(true);
            player.openGui(WolfArmorMod.instance,
                    wolf.getEntityId(),
                    wolf.getEntityWorld(),
                    MathHelper.floor_double(wolf.posX),
                    MathHelper.floor_double(wolf.posY),
                    MathHelper.floor_double(wolf.posZ));
        }
    }

    @Override
    public boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nullable ItemStack stack) {
        if (wolf.isTamed() && wolf.isOwner(player) && !wolf.isChild()) {
            if (player.isSneaking()) {
                openWolfGui(player);
                return true;
            } else {
                if (stack != null) {
                    if (WolfArmorMod.getConfiguration().getIsWolfChestEnabled()
                            && Block.getBlockFromItem(stack.getItem()) == Blocks.CHEST
                            && !this.getHasChest()) {
                        this.setHasChest(true);
                        wolf.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1, (wolf.getRNG().nextFloat() - wolf.getRNG().nextFloat()) * 0.2F + 1);
                        this.inventoryInit();
                        if (!player.capabilities.isCreativeMode) {
                            stack.stackSize--;
                        }

                        return true;
                    }

                    if (getIsValidWolfArmorItem(stack)) {
                        this.openWolfGui(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void inventoryInit() {

        AnimalChest inventoryExisting = this.inventory;
        this.inventory = new AnimalChest("container.wolfarmor.wolf", this.getMaxSizeInventory());

        String customName = wolf.getCustomNameTag();

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

    @Override
    public void onInventoryChanged(@Nonnull InventoryBasic invBasic) {
        ItemStack armor = inventory.getStackInSlot(0);

        this.setArmorItemStack(armor);
    }

    @Override
    public void dropEquipment(boolean killedByPlayer, int lootingModifier) {
        if (this.getHasArmor()) {
            ItemStack armorItem = this.getArmorItemStack();

            if (armorItem != null) {
                wolf.entityDropItem(armorItem, 0);
                this.inventory.setInventorySlotContents(0, null);
            }
        }

        if (this.getHasChest()) {
            wolf.entityDropItem(new ItemStack(Blocks.CHEST, 1), 0);
            for (int slotIndex = 1; slotIndex < getMaxSizeInventory(); slotIndex++) {
                ItemStack stack = this.inventory.getStackInSlot(slotIndex);

                if (stack != null) {
                    wolf.entityDropItem(stack, 0);
                    this.inventory.setInventorySlotContents(slotIndex, null);
                }
            }
        }
    }

    @Override
    public void damageArmor(float damage) {
        if (this.getHasArmor()) {
            ItemStack armorStack = this.inventory.getStackInSlot(0);

            if (armorStack != null && getIsValidWolfArmorItem(armorStack)) {
                armorStack.damageItem((int) Math.ceil(damage), wolf);

                if (armorStack.stackSize == 0) {
                    this.equipArmor(null);
                }
            }
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == WOLF_ARMOR;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@Nullable Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityWolfArmor.WOLF_ARMOR) {
            return (T) this;
        }

        return null;
    }
}
