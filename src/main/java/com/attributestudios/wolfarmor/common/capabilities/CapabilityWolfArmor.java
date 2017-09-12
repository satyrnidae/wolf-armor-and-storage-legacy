package com.attributestudios.wolfarmor.common.capabilities;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.IWolfArmorCapability;

import com.attributestudios.wolfarmor.api.definitions.Resources;
import com.attributestudios.wolfarmor.api.util.Future;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

public class CapabilityWolfArmor {
    @CapabilityInject(IWolfArmorCapability.class)
    public static Capability<IWolfArmorCapability> WOLF_ARMOR_CAPABILITY = null;

    @Future
    public static final int MAX_SIZE_INVENTORY = 7;
    @Future
    public static final int INVENTORY_SLOT_ARMOR = 0;
    public static final int INVENTORY_SLOT_CHEST_START = 1;
    public static final int INVENTORY_SLOT_CHEST_LENGTH = 6;

    public static void register() {
        CapabilityManager.INSTANCE.register(IWolfArmorCapability.class, new Storage(), CapabilityWolfArmor.Instance.class);

        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    public static boolean isValidWolfArmor(@Nonnull ItemStack armorItemStack) {
        return armorItemStack.isEmpty() || isValidWolfArmor(armorItemStack.getItem());
    }

    public static boolean isValidWolfArmor(@Nullable Item armorItem) {
        return armorItem != null && armorItem instanceof ItemWolfArmor;
    }

    public static class Storage implements Capability.IStorage<IWolfArmorCapability> {
        static final String NBT_TAG_HAS_CHEST = "hasChest";
        static final String NBT_TAG_SLOT = "slot";
        static final String NBT_TAG_INVENTORY = "inventory";
        static final String NBT_TAG_ARMOR_ITEM = "armorItem";

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IWolfArmorCapability> capability, IWolfArmorCapability instance, EnumFacing side) { return null; }

        @Override
        public void readNBT(Capability<IWolfArmorCapability> capability, IWolfArmorCapability instance, EnumFacing side, NBTBase nbt) { }
    }

    public static class Instance implements ICapabilitySerializable<NBTTagCompound>, IWolfArmorCapability, IInventoryChangedListener {
        private static final DataParameter<Boolean> HAS_CHEST =
                EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
        private static final DataParameter<ItemStack> ARMOR_ITEM_STACK =
                EntityDataManager.createKey(EntityWolf.class, DataSerializers.ITEM_STACK);

        private ContainerHorseChest inventory;
        private final EntityWolf wolf;
        private final EntityDataManager dataManager;

        public Instance(@Nonnull EntityWolf wolf) {
            this.wolf = wolf;
            this.dataManager = wolf.getDataManager();
            this.dataManagerInit();
            this.inventoryInit();
        }

        private void dataManagerInit() {
            this.dataManager.register(HAS_CHEST, false);
            this.dataManager.register(ARMOR_ITEM_STACK, ItemStack.EMPTY);
        }

        private void inventoryInit() {
            ContainerHorseChest inventoryExisting = this.inventory;
            this.inventory = new ContainerHorseChest("inventory.wolfarmor.wolf", this.getMaxSizeInventory());

            String customWolfName = this.wolf.getCustomNameTag();
            if(!customWolfName.isEmpty()) { this.inventory.setCustomName(customWolfName); }

            if(inventoryExisting != null) {
                inventoryExisting.removeInventoryChangeListener(this);
                int slotCount = Math.min(this.inventory.getSizeInventory(), inventoryExisting.getSizeInventory());
                for(int slotIndex = 0; slotIndex < slotCount; ++slotIndex) {
                    @Nonnull ItemStack stackInSlot = inventoryExisting.getStackInSlot(slotIndex);
                    if(!stackInSlot.isEmpty()) {
                        this.inventory.setInventorySlotContents(slotIndex, stackInSlot.copy());
                    }
                }
            }

            this.inventory.addInventoryChangeListener(this);
            this.inventory.markDirty();
        }

        private void playEquipSound(@Nonnull ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.getItem() instanceof ItemWolfArmor) {
                ItemWolfArmor armorItem = (ItemWolfArmor) itemStack.getItem();
                SoundEvent sound = armorItem.getMaterial().getEquipSound();
                this.wolf.playSound(sound, 1, 1);
            }
        }

        private void openWolfInventory(@Nonnull EntityPlayer player) {
            if(!this.wolf.getEntityWorld().isRemote) {
                this.wolf.getAISit().setSitting(true);
                player.openGui(WolfArmorMod.instance,
                        this.wolf.getEntityId(),
                        this.wolf.getEntityWorld(),
                        MathHelper.floor(this.wolf.posX),
                        MathHelper.floor(this.wolf.posY),
                        MathHelper.floor(this.wolf.posZ));
            }
        }

        @Override
        public void onInventoryChanged(@Nonnull IInventory inventory) {
            @Nonnull ItemStack armorItemStack = inventory.getStackInSlot(INVENTORY_SLOT_ARMOR);
            @Nonnull ItemStack previousArmorItem = this.getArmorItemStack();
            if(armorItemStack != previousArmorItem) {
                this.setArmorItemStack(armorItemStack);
                this.playEquipSound(armorItemStack);
            }

            // So contractually this should never be null, but when attaching capabilities, it can be.
            @Nullable IAttributeInstance armorAttribute = this.wolf.getEntityAttribute(SharedMonsterAttributes.ARMOR);
            //noinspection ConstantConditions
            if(armorAttribute != null) {
                armorAttribute.removeAllModifiers();
                if (!armorItemStack.isEmpty()) {
                    ItemWolfArmor wolfArmor = (ItemWolfArmor) armorItemStack.getItem();
                    armorAttribute.applyModifier(wolfArmor.getMaterial().getArmorAttribute());
                }
            }
        }

        @Override
        @Nonnull
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tags = new NBTTagCompound();

            Boolean hasChest = this.getHasChest();
            tags.setBoolean(Storage.NBT_TAG_HAS_CHEST, hasChest);

            if(hasChest) {
                NBTTagList inventoryItems = new NBTTagList();
                IInventory inventory = this.getInventory();

                for(byte slotIndex = 0; slotIndex < inventory.getSizeInventory(); ++slotIndex) {
                    @Nonnull ItemStack stackInSlot = inventory.getStackInSlot(slotIndex);
                    if(stackInSlot.isEmpty()) { continue; }
                    NBTTagCompound stackInSlotTags = new NBTTagCompound();

                    stackInSlotTags.setByte(Storage.NBT_TAG_SLOT, slotIndex);
                    stackInSlot.writeToNBT(stackInSlotTags);

                    inventoryItems.appendTag(stackInSlotTags);
                }

                tags.setTag(Storage.NBT_TAG_INVENTORY, inventoryItems);
            }

            if(this.getHasArmor()) {
                ItemStack armorItemStack = this.getArmorItemStack();
                if(!armorItemStack.isEmpty()) {
                    NBTTagCompound armorItemTag = armorItemStack.writeToNBT(new NBTTagCompound());
                    tags.setTag(Storage.NBT_TAG_ARMOR_ITEM, armorItemTag);
                }
                else {
                    tags.removeTag(Storage.NBT_TAG_ARMOR_ITEM);
                }
            }
            return tags;
        }

        @Override
        public void deserializeNBT(@Nonnull NBTTagCompound tags) {
            boolean hasChest = tags.getBoolean(Storage.NBT_TAG_HAS_CHEST);
            this.setHasChest(hasChest);

            if(hasChest) {
                this.inventoryInit();
                NBTTagList inventoryTagList = tags.getTagList(Storage.NBT_TAG_INVENTORY, 10);

                for(int index = 0; index < inventoryTagList.tagCount(); ++index) {
                    NBTTagCompound compoundTagAt = inventoryTagList.getCompoundTagAt(index);
                    byte slotIndex = compoundTagAt.getByte(Storage.NBT_TAG_SLOT);
                    if(slotIndex >= 0 && slotIndex < this.inventory.getSizeInventory()) {
                        this.inventory.setInventorySlotContents(slotIndex, new ItemStack(compoundTagAt));
                    }
                    else {
                        WolfArmorMod.getLogger().log(Level.WARN, String.format("[NBT LOAD] Discarded invalid slot information at index %d", slotIndex));
                    }
                }
            }

            NBTTagCompound armorTags = tags.getCompoundTag(Storage.NBT_TAG_ARMOR_ITEM);
            if(!armorTags.hasNoTags()) {
                @Nonnull ItemStack armorItemStack = new ItemStack(armorTags);
                this.equipArmor(armorItemStack);
            }
        }

        @Override
        public boolean getHasChest() {
            return this.dataManager.get(HAS_CHEST);
        }

        @Override
        public void setHasChest(boolean value) {
            this.dataManager.set(HAS_CHEST, value);
        }

        @Override
        @Nonnull
        public ItemStack getArmorItemStack() {
            return this.dataManager.get(ARMOR_ITEM_STACK);
        }

        @Override
        public void setArmorItemStack(@Nonnull ItemStack value) {
            this.dataManager.set(ARMOR_ITEM_STACK, value);
        }

        @Override
        public boolean getHasArmor() {
            return !this.getArmorItemStack().isEmpty();
        }

        @Override
        @Nonnegative
        public int getMaxSizeInventory() {
            return MAX_SIZE_INVENTORY;
        }

        @Override
        @Nonnull
        public InventoryBasic getInventory() {
            return this.inventory;
        }

        @Override
        public boolean canEquipItem(@Nonnull ItemStack armorItemStack) {
            return isValidWolfArmor(armorItemStack) && (!this.getHasArmor() || armorItemStack.isEmpty());
        }

        @Override
        public void equipArmor(@Nonnull ItemStack armorItemStack) {
            if(canEquipItem(armorItemStack)) {
                this.inventory.setInventorySlotContents(INVENTORY_SLOT_ARMOR, armorItemStack);
            }
        }

        @Override
        public boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
            if(this.wolf.isTamed() && this.wolf.isOwner(player) && !this.wolf.isChild()) {
                if(player.isSneaking()) {
                    openWolfInventory(player);
                    return true;
                }
                else {
                    @Nonnull ItemStack heldItem = player.getHeldItem(hand);
                    if(!heldItem.isEmpty()) {
                        if(WolfArmorMod.getConfiguration().getIsWolfChestEnabled() &&
                                Block.getBlockFromItem(heldItem.getItem()) == Blocks.CHEST &&
                                !this.getHasChest()) {
                            this.wolf.playSound(SoundEvents.ENTITY_CHICKEN_EGG,
                                    1,
                                    (this.wolf.getRNG().nextFloat() - this.wolf.getRNG().nextFloat()) * 0.2F + 1);
                            this.setHasChest(true);
                            if(!player.capabilities.isCreativeMode) {
                                heldItem.shrink(1);
                            }
                            return true;
                        }
                        if(isValidWolfArmor(heldItem)) {
                            this.openWolfInventory(player);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public void dropEquipment(boolean killedByPlayer, int lootingModifier) {
            if(this.getHasArmor()) {
                @Nonnull ItemStack armorItemStack = this.getArmorItemStack();
                if(!armorItemStack.isEmpty()) {
                    this.wolf.entityDropItem(armorItemStack, 0);
                    this.equipArmor(ItemStack.EMPTY);
                }
            }
            if(this.getHasChest()) {
                wolf.entityDropItem(new ItemStack(Blocks.CHEST, 1), 0);
                for(int slotIndex = INVENTORY_SLOT_CHEST_START; slotIndex < INVENTORY_SLOT_CHEST_START + INVENTORY_SLOT_CHEST_LENGTH; ++slotIndex) {
                    @Nonnull ItemStack stackInSlot = this.inventory.getStackInSlot(slotIndex);
                    if(!stackInSlot.isEmpty()) {
                        this.wolf.entityDropItem(stackInSlot, 0);
                        this.inventory.setInventorySlotContents(slotIndex, ItemStack.EMPTY);
                    }
                }
            }
        }

        @Override
        public void damageArmor(float damage) {
            if(this.getHasArmor()) {
                ItemStack stackInSlot = this.inventory.getStackInSlot(INVENTORY_SLOT_ARMOR);
                if(!stackInSlot.isEmpty()) {
                    stackInSlot.damageItem((int)Math.ceil(damage), this.wolf);
                    if(stackInSlot.getCount() == 0) {
                        @Nonnull ItemStack particleStack = stackInSlot.copy();
                        particleStack.setCount(1);
                        this.equipArmor(ItemStack.EMPTY);
                        this.wolf.renderBrokenItemStack(particleStack);
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        @Nullable
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY ? (T)this : null;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY;
        }
    }

    public static final class EventHandlers {
        @SubscribeEvent
        public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
            Entity entity = event.getObject();
            if (entity instanceof EntityWolf && !(entity instanceof IWolfArmorCapability)) {
                event.addCapability(Resources.CAPABILITY_WOLF_ARMOR, new CapabilityWolfArmor.Instance((EntityWolf)entity));
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
            if(event.getTarget() instanceof EntityWolf) {
                @Nullable IWolfArmorCapability wolfArmorCapability = event.getTarget().getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
                if(wolfArmorCapability != null) {
                    EntityPlayer player = event.getEntityPlayer();
                    EnumHand hand = event.getHand();
                    if(wolfArmorCapability.processInteract(player, hand)) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onDropItems(LivingDropsEvent event) {
            if(event.getEntity() instanceof EntityWolf) {
                @Nullable IWolfArmorCapability wolfArmorCapability = event.getEntity().getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
                if(wolfArmorCapability != null) {
                    DamageSource source = event.getSource();
                    wolfArmorCapability.dropEquipment(source != null && source.getTrueSource() instanceof EntityPlayer, event.getLootingLevel());
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onDamageArmor(LivingHurtEvent event) {
            if(event.getEntity() instanceof EntityWolf) {
                @Nullable IWolfArmorCapability wolfArmorCapability = event.getEntity().getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
                if(wolfArmorCapability != null) {
                    wolfArmorCapability.damageArmor(event.getAmount());
                }
            }
        }
    }
}
