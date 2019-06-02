package dev.satyrn.wolfarmor.mixin;

import com.google.common.collect.Multimap;
import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.ItemWolfArmor;
import dev.satyrn.wolfarmor.api.util.DataHelper;
import dev.satyrn.wolfarmor.api.IArmoredWolf;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(EntityWolf.class)
public abstract class MixinEntityWolf extends MixinEntityTameable implements IArmoredWolf, IInventoryChangedListener {

    private static final int INVENTORY_SLOT_ARMOR = 0;
    private static final int MAX_SIZE_INVENTORY = 7;

    static {
        DataHelper.HAS_CHEST = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
        DataHelper.ARMOR_ITEM = EntityDataManager.createKey(EntityWolf.class, DataSerializers.ITEM_STACK);
    }

    private ContainerHorseChest inventory;

    @Override
    public int getMaxSizeInventory() {
        return MAX_SIZE_INVENTORY;
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        if(!this.getEntityWorld().isRemote) {
            @Nonnull ItemStack armorItemStack = inventory.getStackInSlot(INVENTORY_SLOT_ARMOR);
            this.setArmorItemStack(armorItemStack);

            applyArmorModifiers(this.getEntityAttribute(SharedMonsterAttributes.ARMOR), armorItemStack);
            applyArmorModifiers(this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS), armorItemStack);
        }
    }

    @Override
    public void setArmorItemStack(@Nonnull ItemStack armorItemStack) {
        if(armorItemStack != this.dataManager.get(DataHelper.ARMOR_ITEM)) {
            this.dataManager.set(DataHelper.ARMOR_ITEM, armorItemStack);
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, armorItemStack);
            setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
            setItemStackToSlot(EntityEquipmentSlot.HEAD, armorItemStack);
            setDropChance(EntityEquipmentSlot.HEAD, 0.0F);
            setItemStackToSlot(EntityEquipmentSlot.CHEST, armorItemStack);
            setDropChance(EntityEquipmentSlot.CHEST, 0.0F);
            setItemStackToSlot(EntityEquipmentSlot.LEGS, armorItemStack);
            setDropChance(EntityEquipmentSlot.LEGS, 0.0F);
            setItemStackToSlot(EntityEquipmentSlot.FEET, armorItemStack);
            setDropChance(EntityEquipmentSlot.FEET, 0.0F);
        }
    }

    @Nonnull
    @Override
    public ItemStack getArmorItemStack() {
        return this.dataManager.get(DataHelper.ARMOR_ITEM);
    }

    @Override
    public boolean getHasArmor() {
        return !this.getArmorItemStack().isEmpty();
    }

    @Override
    public boolean getHasChest() {
        return this.dataManager.get(DataHelper.HAS_CHEST);
    }

    @Override
    public void setHasChest(boolean value) {
        this.dataManager.set(DataHelper.HAS_CHEST, value);
    }

    @Nonnull
    @Override
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
    public void equipArmor(@Nonnull ItemStack armorItemStack) {
        if (canEquipItem(armorItemStack)) {
            this.inventory.setInventorySlotContents(INVENTORY_SLOT_ARMOR, armorItemStack);
        }
    }

    @Override
    public boolean canEquipItem(@Nonnull ItemStack armorItemStack) {
        return armorItemStack.isEmpty() || (!this.getHasArmor() && armorItemStack.getItem() instanceof ItemWolfArmor);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        this.inventoryInit();
    }

    @Inject(method = "entityInit", at = @At("RETURN"))
    private void onEntityInit(CallbackInfo ci) {
        this.dataManager.register(DataHelper.HAS_CHEST, false);
        this.dataManager.register(DataHelper.ARMOR_ITEM, ItemStack.EMPTY);
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    private void onWriteEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
        boolean hasChest = this.getHasChest();
        compound.setBoolean("HasChest", hasChest);

        if(hasChest) {
            NBTTagList inventoryItems = new NBTTagList();
            IInventory inventory = this.getInventory();

            for (byte slotIndex = 0; slotIndex < inventory.getSizeInventory(); ++slotIndex) {
                @Nonnull ItemStack stackInSlot = inventory.getStackInSlot(slotIndex);
                if (stackInSlot.isEmpty()) {
                    continue;
                }
                NBTTagCompound stackInSlotTags = new NBTTagCompound();

                stackInSlotTags.setByte("Slot", slotIndex);
                stackInSlot.writeToNBT(stackInSlotTags);

                inventoryItems.appendTag(stackInSlotTags);
            }

            compound.setTag("Inventory", inventoryItems);
        }

        if (this.getHasArmor()) {
            ItemStack armorItemStack = this.getArmorItemStack();
            if (!armorItemStack.isEmpty()) {
                NBTTagCompound armorItemTag = armorItemStack.writeToNBT(new NBTTagCompound());
                compound.setTag("ArmorItem", armorItemTag);
            } else {
                compound.removeTag("ArmorItem");
            }
        }
    }

    @Inject(method = "readEntityFromNBT", at = @At("RETURN"))
    public void onReadEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        boolean hasChest = compound.getBoolean("HasChest");
        this.setHasChest(hasChest);

        if (hasChest) {
            this.inventoryInit();
            NBTTagList inventoryTagList = compound.getTagList("Inventory", 10);

            for (int index = 0; index < inventoryTagList.tagCount(); ++index) {
                NBTTagCompound compoundTagAt = inventoryTagList.getCompoundTagAt(index);
                byte slotIndex = compoundTagAt.getByte("Slot");
                if (slotIndex >= 0 && slotIndex < this.inventory.getSizeInventory()) {
                    this.inventory.setInventorySlotContents(slotIndex, new ItemStack(compoundTagAt));
                } else {
                    WolfArmorMod.getLogger().log(Level.WARN, String.format("[NBT LOAD] Discarded invalid slot information at index %d", slotIndex));
                }
            }
        }

        NBTTagCompound armorTags = compound.getCompoundTag("ArmorItem");
        if (!armorTags.isEmpty()) {
            @Nonnull ItemStack armorItemStack = new ItemStack(armorTags);
            this.equipArmor(armorItemStack);
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

    private void inventoryInit() {
        ContainerHorseChest inventoryExisting = this.inventory;
        this.inventory = new ContainerHorseChest("inventory.wolfarmor.wolf", this.getMaxSizeInventory());

        String customWolfName = this.getCustomNameTag();
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
            this.playSound(sound, 1, (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F + 1);
        }
    }

    private void openWolfInventory(@Nonnull EntityPlayer player) {
        if (!this.getEntityWorld().isRemote) {
            this.getAISit().setSitting(true);
            player.openGui(WolfArmorMod.getInstance(),
                    this.getEntityId(),
                    this.getEntityWorld(),
                    MathHelper.floor(this.posX),
                    MathHelper.floor(this.posY),
                    MathHelper.floor(this.posZ));
        }
    }
}
