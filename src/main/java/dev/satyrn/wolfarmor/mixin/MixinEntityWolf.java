package dev.satyrn.wolfarmor.mixin;

import com.google.common.collect.Multimap;
import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.advancements.WolfArmorTrigger;
import dev.satyrn.wolfarmor.api.common.ItemWolfArmor;
import dev.satyrn.wolfarmor.api.util.Criteria;
import dev.satyrn.wolfarmor.api.util.DataHelper;
import dev.satyrn.wolfarmor.api.IArmoredWolf;
import dev.satyrn.wolfarmor.api.util.Items;
import dev.satyrn.wolfarmor.common.inventory.ContainerWolfInventory;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(EntityWolf.class)
public abstract class MixinEntityWolf extends MixinEntityTameable implements IArmoredWolf, IInventoryChangedListener {

    static {
        DataHelper.HAS_CHEST = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
        DataHelper.ARMOR_ITEM = EntityDataManager.createKey(EntityWolf.class, DataSerializers.ITEM_STACK);
    }

    private ContainerHorseChest inventory;

    @Override
    public int getMaxSizeInventory() {
        return ContainerWolfInventory.MAX_SIZE_INVENTORY;
    }

    @Override
    public void onInventoryChanged(@Nonnull IInventory invBasic) {
        if(!this.getEntityWorld().isRemote) {
            @Nonnull ItemStack armorItemStack = inventory.getStackInSlot(ContainerWolfInventory.INVENTORY_SLOT_ARMOR);
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
            this.inventory.setInventorySlotContents(ContainerWolfInventory.INVENTORY_SLOT_ARMOR, armorItemStack);
        }
    }

    @Override
    public boolean canEquipItem(@Nonnull ItemStack armorItemStack) {
        return armorItemStack.isEmpty() || (!this.getHasArmor() && armorItemStack.getItem() instanceof ItemWolfArmor);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        this.dropEquipment();
    }

    @Override
    public void dropEquipment() {
        if(this.getHasArmor()) {
            @Nonnull ItemStack armorItemStack = this.getArmorItemStack();
            if(!this.getEntityWorld().isRemote) {
                this.entityDropItem(armorItemStack, 0);
            }
            this.equipArmor(ItemStack.EMPTY);
        }

        if(this.getHasChest()) {
            if(!this.getEntityWorld().isRemote) {
                this.entityDropItem(new ItemStack(Blocks.CHEST, 1), 0);
            }
            this.dropInventoryContents();
        }
    }

    @Override
    public void dropInventoryContents() {
        for(int slotIndex = ContainerWolfInventory.INVENTORY_SLOT_CHEST_START; slotIndex <= ContainerWolfInventory.INVENTORY_SLOT_CHEST_LENGTH; ++slotIndex) {
            @Nonnull ItemStack stackInSlot = this.inventory.getStackInSlot(slotIndex);
            if(!stackInSlot.isEmpty()) {
                if(!this.getEntityWorld().isRemote) {
                    this.entityDropItem(stackInSlot, 0);
                }
                this.setInventoryItem(slotIndex, ItemStack.EMPTY);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void damageArmor(float damage) {
        if (this.getHasArmor()) {
            ItemStack stackInSlot = this.inventory.getStackInSlot(ContainerWolfInventory.INVENTORY_SLOT_ARMOR);
            if (!stackInSlot.isEmpty()) {
                stackInSlot.damageItem((int) Math.ceil(damage), (EntityLivingBase) (Object) this);
                if (stackInSlot.getCount() == 0) {
                    @Nonnull ItemStack particleStack = stackInSlot.copy();
                    particleStack.setCount(1);
                    this.equipArmor(ItemStack.EMPTY);
                    this.renderBrokenItemStack(particleStack);
                }
            }
        }
    }

    @Override
    public void dropChest() {
        if(this.getHasChest()) {
            this.dropInventoryContents();
            this.setHasChest(false);
            if (!this.getEntityWorld().isRemote) {
                this.entityDropItem(new ItemStack(Blocks.CHEST, 1), 0);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "attackEntityAsMob", at = @At("HEAD"), cancellable = true)
    private void onAttackEntityAsMob(Entity entityIn, CallbackInfoReturnable<Boolean> cir) {
        float damage = (int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int knockback = 0;

        if(entityIn instanceof EntityLivingBase) {
            damage += EnchantmentHelper.getModifierForCreature(this.getArmorItemStack(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            knockback += EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.KNOCKBACK, (EntityLivingBase)(Object)this);
        }

        boolean atkFlag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)(Object)this), damage);

        if(atkFlag) {
            if(knockback > 0) {
                ((EntityLivingBase)entityIn).knockBack((Entity)(Object)this, knockback * 0.5F, MathHelper.sin(this.rotationYaw * (float)(Math.PI / 180)), -MathHelper.cos(this.rotationYaw * (float)(Math.PI / 180)));
                this.motionX *= 0.6f;
                this.motionZ *= 0.6f;
            }

            int fireAspect = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_ASPECT, (EntityLivingBase)(Object)this);
            if(fireAspect > 0) {
                entityIn.setFire(fireAspect * 4);
            }

            this.applyEnchantments((EntityLivingBase)(Object)this, entityIn);
        }

        cir.setReturnValue(atkFlag);
        cir.cancel();
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
    private void onWriteEntityToNBT(@Nonnull NBTTagCompound compound, CallbackInfo ci) {
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
    private void onReadEntityFromNBT(@Nonnull NBTTagCompound compound, CallbackInfo ci) {
        if(compound.hasKey("ForgeCaps")) {
            NBTTagCompound capabilities = compound.getCompoundTag("ForgeCaps");
            if(capabilities.hasKey("wolfarmor:wolf_armor")) {
                this.processLegacyDataTags(capabilities.getCompoundTag("wolfarmor:wolf_armor"));
                return;
            }
        }

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

    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true)
    private void onProcessInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        if(this.isChild() || !(this.isTamed() && this.isOwner(player))) {
            return;
        }

        if(player.isSneaking()) {
            this.openWolfInventory(player);

            // Return true and cancel
            cir.setReturnValue(true);
            cir.cancel();
        } else {
            @Nonnull ItemStack itemInHand = player.getHeldItem(hand);

            if (!itemInHand.isEmpty()) {
                boolean isWolfChestEnabled = WolfArmorMod.getConfiguration().getIsWolfChestEnabled();
                if (isWolfChestEnabled && !this.getHasChest() && (Block.getBlockFromItem(itemInHand.getItem()) == Blocks.CHEST ||
                        OreDictionary.containsMatch(false, OreDictionary.getOres("chest"), itemInHand))) {
                    if (!this.getEntityWorld().isRemote) {
                        this.playEquipSound(itemInHand);
                        this.setHasChest(true);
                        if (!player.capabilities.isCreativeMode) {
                            itemInHand.shrink(1);
                        }
                        if (player instanceof EntityPlayerMP) {
                            ((WolfArmorTrigger) Criteria.EQUIP_WOLF_CHEST).trigger((EntityPlayerMP) player, (EntityWolf) (Object) this);
                        }
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                } else if (Items.isValidWolfArmor(itemInHand)) {
                    this.openWolfInventory(player);
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
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

    private void processLegacyDataTags(@Nonnull NBTTagCompound compound) {
        WolfArmorMod.getLogger().log(Level.INFO, "[NBT LOAD] Updating capable wolf to mixin wolf...");
        boolean hasChest = compound.getBoolean("hasChest");
        this.setHasChest(hasChest);

        if (hasChest) {
            this.inventoryInit();
            NBTTagList inventoryTagList = compound.getTagList("inventory", 10);

            for (int index = 0; index < inventoryTagList.tagCount(); ++index) {
                NBTTagCompound compoundTagAt = inventoryTagList.getCompoundTagAt(index);
                byte slotIndex = compoundTagAt.getByte("slot");
                if (slotIndex >= 0 && slotIndex < this.inventory.getSizeInventory()) {
                    this.inventory.setInventorySlotContents(slotIndex, new ItemStack(compoundTagAt));
                } else {
                    WolfArmorMod.getLogger().log(Level.WARN, String.format("[NBT LOAD] Discarded invalid slot information at index %d", slotIndex));
                }
            }
        }

        NBTTagCompound armorTags = compound.getCompoundTag("armorItem");
        if (!armorTags.isEmpty()) {
            @Nonnull ItemStack armorItemStack = new ItemStack(armorTags);
            this.equipArmor(armorItemStack);
        }
    }
}
