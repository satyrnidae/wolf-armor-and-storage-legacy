package dev.satyrn.wolfarmor.common.capabilities;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.advancements.WolfArmorTrigger;
import dev.satyrn.wolfarmor.api.IWolfArmorCapability;
import dev.satyrn.wolfarmor.api.ItemWolfArmor;
import dev.satyrn.wolfarmor.api.util.Capabilities;
import dev.satyrn.wolfarmor.api.util.Criteria;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.common.network.PacketHandler;
import dev.satyrn.wolfarmor.common.network.packets.WolfArmorCapabilityDataMessage;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
public class CapabilityWolfArmor implements IWolfArmorCapability, ICapabilitySerializable<NBTTagCompound>, IInventoryChangedListener {

    private static final Storage STORAGE = new Storage();
    private static final int MAX_SIZE_INVENTORY = 7;
    private static final int INVENTORY_SLOT_ARMOR = 0;
    public static final int INVENTORY_SLOT_CHEST_START = 1;
    public static final int INVENTORY_SLOT_CHEST_LENGTH = 6;

    /**
     * Registers the wolf armor capability with the capability manager, and sets up the event handlers for the capability.
     */
    public static void register() {
        CapabilityManager.INSTANCE.register(IWolfArmorCapability.class, STORAGE, () -> {
            try {
                return CapabilityWolfArmor.class.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
         });

        //MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    /**
     * Checks if <tt>armorItemStack</tt> is a valid wolf armor item stack.
     * @param armorItemStack The armor item to check
     * @return <tt>true</tt> if it is a valid wolf armor item.
     * @todo API Implementation
     */
    public static boolean isValidWolfArmor(@Nonnull ItemStack armorItemStack) {
        return armorItemStack.isEmpty() || isValidWolfArmor(armorItemStack.getItem());
    }

    /**
     * Checks if <tt>armorItem</tt> is a valid wolf armor item.
     * @param armorItem The armor item to check.
     * @return <tt>true</tt> if it is a valid wolf armor item.
     */
    public static boolean isValidWolfArmor(@Nullable Item armorItem) {
        return armorItem instanceof ItemWolfArmor;
    }

    /**
     * Handles Wolf Armor capability storage
     */
    public static class Storage implements Capability.IStorage<IWolfArmorCapability> {
        static final String NBT_TAG_HAS_CHEST = "hasChest";
        static final String NBT_TAG_SLOT = "slot";
        static final String NBT_TAG_INVENTORY = "inventory";
        static final String NBT_TAG_ARMOR_ITEM = "armorItem";

        /**
         * Writes a capability instance to NBT.
         * @param capability The capability to handle.
         * @param instance The instance to write.
         * @param side The facing side.
         * @return An NBT tag containing the capability data, or null.
         */
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IWolfArmorCapability> capability, IWolfArmorCapability instance, EnumFacing side) {
            if(capability != Capabilities.CAPABILITY_WOLF_ARMOR) return null;

            return ((CapabilityWolfArmor)instance).serializeNBT();
        }

        /**
         * Reads a capability instance from NBT.
         * @param capability The capability to read.
         * @param instance The instance to read.
         * @param side The facing side.
         * @param nbt The NBT to read from.
         */
        @Override
        @SuppressWarnings("rawtypes")
        public void readNBT(Capability<IWolfArmorCapability> capability, IWolfArmorCapability instance, EnumFacing side, NBTBase nbt) {
            if(capability != Capabilities.CAPABILITY_WOLF_ARMOR) return;
            if(nbt instanceof NBTTagCompound)
                ((CapabilityWolfArmor)instance).deserializeNBT((NBTTagCompound)nbt);
        }
    }

    /**
     * Handles events related to the capability and its interaction with the world via the wolf instance.
     */
    public static final class EventHandlers {

        @SubscribeEvent
        public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
            Entity entity = event.getObject();
            if (entity instanceof EntityWolf && !(entity instanceof IWolfArmorCapability)) {
                CapabilityWolfArmor capability = new CapabilityWolfArmor((EntityWolf) entity);
                event.addCapability(Resources.CAPABILITY_WOLF_ARMOR, capability);
            }
        }

        @SubscribeEvent
        public void onEntityKnockBack(LivingKnockBackEvent event) {
            if(!event.isCanceled() && event.getOriginalAttacker() instanceof EntityWolf) {
                @Nullable IWolfArmorCapability capability = event.getOriginalAttacker().getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
                if(capability == null || capability.getArmorItemStack().isEmpty()) return;

                int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, capability.getArmorItemStack());
                if(lvl > 0) {
                    if(lvl * 0.5F > event.getStrength()) {
                        event.setStrength(lvl * 0.5F);
                    }
                }
            }
        }

        @SubscribeEvent
        public void onEntityAttack(LivingHurtEvent event) {
            Entity source = event.getSource().getTrueSource();
            Entity target = event.getSource().getTrueSource();

            boolean isAttackerWolf = source instanceof EntityWolf;
            boolean isDefenderWolf = target instanceof EntityWolf;

            if(isAttackerWolf)
            {
                handleWolfAttackedOther((EntityWolf)source, event);
            }
            if(isDefenderWolf) {
                handleWolfAttackedBy((EntityWolf)target, event);
            }
        }

        @SubscribeEvent
        public void onSetAttackTarget(LivingSetAttackTargetEvent event) {
            if(event.getEntity() instanceof EntityWolf) {
                EntityWolf wolf = (EntityWolf)event.getEntity();
                @Nullable IWolfArmorCapability capability = wolf.getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);

                //TODO: Probably something
            }
        }

        private void handleWolfAttackedBy(EntityWolf wolf, LivingHurtEvent event) {
            @Nullable IWolfArmorCapability wolfArmorCapability = wolf.getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
            if(wolfArmorCapability == null || wolfArmorCapability.getArmorItemStack().isEmpty()) return;

            int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.RESPIRATION, wolfArmorCapability.getArmorItemStack());
            if("drown".equals(event.getSource().getDamageType()) && lvl > 0) {
                event.setCanceled(true);
                return;
            }

            //TODO: Other damage types handling
        }

        private void handleWolfAttackedOther(EntityWolf wolf, LivingHurtEvent event) {
            @Nullable IWolfArmorCapability wolfArmorCapability = wolf.getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
            if(wolfArmorCapability == null || wolfArmorCapability.getArmorItemStack().isEmpty()) return;

            int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, wolfArmorCapability.getArmorItemStack());
            if(lvl > 0 && event.getEntityLiving() != null && !event.getEntityLiving().isBurning()) {
                event.getEntityLiving().setFire(lvl * 4);
            }

            lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, wolfArmorCapability.getArmorItemStack());
            if(lvl > 0 && event.getEntityLiving() instanceof EntitySpider) {
                event.setAmount(event.getAmount() + lvl * 2.5F);
            }

            //TODO: Other damage types handling
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget() instanceof EntityWolf) {
                @Nullable IWolfArmorCapability wolfArmorCapability = event.getTarget().getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
                if (wolfArmorCapability != null) {
                    EntityPlayer player = event.getEntityPlayer();
                    EnumHand hand = event.getHand();
                    if (wolfArmorCapability.processInteract(player, hand)) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onDropItems(LivingDropsEvent event) {
            if (event.getEntity() instanceof EntityWolf) {
                @Nullable IWolfArmorCapability wolfArmorCapability = event.getEntity().getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
                if (wolfArmorCapability != null) {
                    DamageSource source = event.getSource();
                    wolfArmorCapability.dropEquipment(source != null && source.getTrueSource() instanceof EntityPlayer, event.getLootingLevel());
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onDamageArmor(LivingHurtEvent event) {
            if (event.getEntity() instanceof EntityWolf) {
                @Nullable IWolfArmorCapability wolfArmorCapability = event.getEntity().getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
                if (wolfArmorCapability != null) {
                    wolfArmorCapability.damageArmor(event.getAmount());
                }
            }
        }

        @SubscribeEvent
        public void onUpdate(LivingEvent.LivingUpdateEvent event) {
            if (event.getEntity() instanceof EntityWolf) {
                @Nullable IWolfArmorCapability wolfArmorCapability = event.getEntity().getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
                if (wolfArmorCapability != null && !event.getEntity().getEntityWorld().isRemote) {
                    PacketHandler.getChannel().sendToAll(new WolfArmorCapabilityDataMessage(event.getEntity().getEntityId(), wolfArmorCapability.getHasChest(), wolfArmorCapability.getArmorItemStack()));
                }
            }
        }
    }

    private final EntityWolf wolf;
    private ContainerHorseChest inventory;
    private ItemStack wolfArmorItem = ItemStack.EMPTY;
    private boolean hasChest;

    private CapabilityWolfArmor(@Nonnull EntityWolf wolf) {
        this.wolf = wolf;
        this.inventoryInit();
    }

    private void inventoryInit() {
        ContainerHorseChest inventoryExisting = this.inventory;
        this.inventory = new ContainerHorseChest("inventory.wolfarmor.wolf", this.getMaxSizeInventory());

        String customWolfName = this.wolf.getCustomNameTag();
        if (!customWolfName.isEmpty()) {
            this.inventory.setCustomName(customWolfName);
        }

        if (inventoryExisting != null) {
            inventoryExisting.removeInventoryChangeListener(this);
            int slotCount = Math.min(this.inventory.getSizeInventory(), inventoryExisting.getSizeInventory());
            for (int slotIndex = 0; slotIndex < slotCount; ++slotIndex) {
                @Nonnull ItemStack stackInSlot = inventoryExisting.getStackInSlot(slotIndex);
                if (!stackInSlot.isEmpty()) {
                    this.inventory.setInventorySlotContents(slotIndex, stackInSlot.copy());
                }
            }
        }

        this.inventory.addInventoryChangeListener(this);
        this.inventory.markDirty();
    }

    private void playEquipSound(@Nonnull ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return;
        }
        SoundEvent sound = null;
        if (Block.getBlockFromItem(itemStack.getItem()) == Blocks.CHEST) {
            sound = SoundEvents.ENTITY_CHICKEN_EGG;
        }

        if (sound != null) {
            this.wolf.playSound(sound, 1, (this.wolf.getRNG().nextFloat() - this.wolf.getRNG().nextFloat()) * 0.2F + 1);
        }
    }

    private void openWolfInventory(@Nonnull EntityPlayer player) {
        if (!this.wolf.getEntityWorld().isRemote) {
            this.wolf.getAISit().setSitting(true);
            player.openGui(WolfArmorMod.getInstance(),
                    this.wolf.getEntityId(),
                    this.wolf.getEntityWorld(),
                    MathHelper.floor(this.wolf.posX),
                    MathHelper.floor(this.wolf.posY),
                    MathHelper.floor(this.wolf.posZ));
        }
    }

    @Override
    public void onInventoryChanged(@Nonnull IInventory inventory) {
        if(!this.wolf.getEntityWorld().isRemote) {
            @Nonnull ItemStack armorItemStack = inventory.getStackInSlot(INVENTORY_SLOT_ARMOR);
            this.setArmorItemStack(armorItemStack);

            applyArmorModifiers(this.wolf.getEntityAttribute(SharedMonsterAttributes.ARMOR), armorItemStack);
            applyArmorModifiers(this.wolf.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS), armorItemStack);
        }
    }

    private void applyArmorModifiers(@Nullable IAttributeInstance instance, @Nonnull ItemStack stack) {
        if(instance == null) {
            return;
        }
        instance.removeModifier(ItemWolfArmor.ARMOR_UUID);
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemWolfArmor)) {
            return;
        }
        ItemWolfArmor armorItem = (ItemWolfArmor) stack.getItem();
        Multimap<String, AttributeModifier> map = armorItem.getAttributeModifiers(EntityEquipmentSlot.CHEST, stack);
        if (map.containsKey(instance.getAttribute().getName())) {
            map.get(instance.getAttribute().getName()).forEach(instance::applyModifier);
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tags = new NBTTagCompound();

        Boolean hasChest = this.getHasChest();
        tags.setBoolean(Storage.NBT_TAG_HAS_CHEST, hasChest);

        if (hasChest) {
            NBTTagList inventoryItems = new NBTTagList();
            IInventory inventory = this.getInventory();

            for (byte slotIndex = 0; slotIndex < inventory.getSizeInventory(); ++slotIndex) {
                @Nonnull ItemStack stackInSlot = inventory.getStackInSlot(slotIndex);
                if (stackInSlot.isEmpty()) {
                    continue;
                }
                NBTTagCompound stackInSlotTags = new NBTTagCompound();

                stackInSlotTags.setByte(Storage.NBT_TAG_SLOT, slotIndex);
                stackInSlot.writeToNBT(stackInSlotTags);

                inventoryItems.appendTag(stackInSlotTags);
            }

            tags.setTag(Storage.NBT_TAG_INVENTORY, inventoryItems);
        }

        if (this.getHasArmor()) {
            ItemStack armorItemStack = this.getArmorItemStack();
            if (!armorItemStack.isEmpty()) {
                NBTTagCompound armorItemTag = armorItemStack.writeToNBT(new NBTTagCompound());
                tags.setTag(Storage.NBT_TAG_ARMOR_ITEM, armorItemTag);
            } else {
                tags.removeTag(Storage.NBT_TAG_ARMOR_ITEM);
            }
        }
        return tags;
    }

    @Override
    public void deserializeNBT(@Nonnull NBTTagCompound tags) {
        boolean hasChest = tags.getBoolean(Storage.NBT_TAG_HAS_CHEST);
        this.setHasChest(hasChest);

        if (hasChest) {
            this.inventoryInit();
            NBTTagList inventoryTagList = tags.getTagList(Storage.NBT_TAG_INVENTORY, 10);

            for (int index = 0; index < inventoryTagList.tagCount(); ++index) {
                NBTTagCompound compoundTagAt = inventoryTagList.getCompoundTagAt(index);
                byte slotIndex = compoundTagAt.getByte(Storage.NBT_TAG_SLOT);
                if (slotIndex >= 0 && slotIndex < this.inventory.getSizeInventory()) {
                    this.inventory.setInventorySlotContents(slotIndex, new ItemStack(compoundTagAt));
                } else {
                    WolfArmorMod.getLogger().log(Level.WARN, String.format("[NBT LOAD] Discarded invalid slot information at index %d", slotIndex));
                }
            }
        }

        NBTTagCompound armorTags = tags.getCompoundTag(Storage.NBT_TAG_ARMOR_ITEM);
        if (!armorTags.isEmpty()) {
            @Nonnull ItemStack armorItemStack = new ItemStack(armorTags);
            this.equipArmor(armorItemStack);
        }
    }

    @Override
    public boolean getHasChest() {
        return hasChest;
    }

    @Override
    public void setHasChest(boolean value) {
        hasChest = value;
    }

    @Override
    @Nonnull
    public ItemStack getArmorItemStack() {
        return this.wolfArmorItem;
    }

    @Override
    public void setArmorItemStack(@Nonnull ItemStack value) {
        if(value != wolfArmorItem) {
            this.wolfArmorItem = value;
            //cheeky enchantment workaround / hack
            wolf.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, value);
            wolf.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
            wolf.setItemStackToSlot(EntityEquipmentSlot.HEAD, value);
            wolf.setDropChance(EntityEquipmentSlot.HEAD, 0.0F);
            wolf.setItemStackToSlot(EntityEquipmentSlot.CHEST, value);
            wolf.setDropChance(EntityEquipmentSlot.CHEST, 0.0F);
            wolf.setItemStackToSlot(EntityEquipmentSlot.LEGS, value);
            wolf.setDropChance(EntityEquipmentSlot.LEGS, 0.0F);
            wolf.setItemStackToSlot(EntityEquipmentSlot.FEET, value);
            wolf.setDropChance(EntityEquipmentSlot.FEET, 0.0F);
        }
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
    public void setInventoryItem(int index, @Nonnull ItemStack itemStack) {
        if (index >= 0 && index < this.inventory.getSizeInventory()) {
            this.inventory.setInventorySlotContents(index, itemStack);
        } else {
            WolfArmorMod.getLogger().log(Level.ERROR, String.format("Invalid slot: %d", index));
        }
    }

    @Override
    public boolean canEquipItem(@Nonnull ItemStack armorItemStack) {
        return isValidWolfArmor(armorItemStack) && (!this.getHasArmor() || armorItemStack.isEmpty());
    }

    @Override
    public void equipArmor(@Nonnull ItemStack armorItemStack) {
        if (canEquipItem(armorItemStack)) {
            this.inventory.setInventorySlotContents(INVENTORY_SLOT_ARMOR, armorItemStack);
        }
    }

    @Override
    public boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if (this.wolf.isChild() || !(this.wolf.isTamed() && this.wolf.isOwner(player))) {
            return false;
        }

        if (player.isSneaking()) {
            this.openWolfInventory(player);
            return true;
        }

        @Nonnull ItemStack itemInHand = player.getHeldItem(hand);

        if (!itemInHand.isEmpty()) {

            boolean isWolfChestEnabled = WolfArmorMod.getConfiguration().getIsWolfChestEnabled();
            if (Block.getBlockFromItem(itemInHand.getItem()) == Blocks.CHEST && isWolfChestEnabled && !this.getHasChest()) {
                if(!this.wolf.getEntityWorld().isRemote) {
                    this.playEquipSound(itemInHand); //Plays chicken pop sound
                    this.setHasChest(true);
                    if (!player.capabilities.isCreativeMode) {
                        itemInHand.shrink(1);
                    }
                    if (player instanceof EntityPlayerMP) {
                        ((WolfArmorTrigger) Criteria.EQUIP_WOLF_CHEST).trigger((EntityPlayerMP) player, this.wolf);
                    }
                }
                return true;
            }

            if (isValidWolfArmor(itemInHand)) {
                this.openWolfInventory(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void dropEquipment(boolean killedByPlayer, int lootingModifier) {
        if (this.getHasArmor()) {
            @Nonnull ItemStack armorItemStack = this.getArmorItemStack();
            if (!armorItemStack.isEmpty()) {
                this.wolf.entityDropItem(armorItemStack, 0);
                this.equipArmor(ItemStack.EMPTY);
            }
        }
        if (this.getHasChest()) {
            this.wolf.entityDropItem(new ItemStack(Blocks.CHEST, 1), 0);
            this.dropInventoryContents();
        }
    }

    @Override
    public void dropInventoryContents() {
        for (int slotIndex = INVENTORY_SLOT_CHEST_START; slotIndex < INVENTORY_SLOT_CHEST_START + INVENTORY_SLOT_CHEST_LENGTH; ++slotIndex) {
            @Nonnull ItemStack stackInSlot = this.inventory.getStackInSlot(slotIndex);
            if (!stackInSlot.isEmpty()) {
                this.wolf.entityDropItem(stackInSlot, 0);
                this.inventory.setInventorySlotContents(slotIndex, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void damageArmor(float damage) {
        if (this.getHasArmor()) {
            ItemStack stackInSlot = this.inventory.getStackInSlot(INVENTORY_SLOT_ARMOR);
            if (!stackInSlot.isEmpty()) {
                stackInSlot.damageItem((int) Math.ceil(damage), this.wolf);
                if (stackInSlot.getCount() == 0) {
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
        return capability == Capabilities.CAPABILITY_WOLF_ARMOR ? (T) this : null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == Capabilities.CAPABILITY_WOLF_ARMOR;
    }
}
