package dev.satyrn.wolfarmor.mixin;

import com.google.common.collect.Multimap;
import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.advancements.WolfArmorTrigger;
import dev.satyrn.wolfarmor.api.item.IItemWolfArmor;
import dev.satyrn.wolfarmor.common.network.MessageBase;
import dev.satyrn.wolfarmor.common.network.packets.RemovePotionEffectMessage;
import dev.satyrn.wolfarmor.common.network.packets.UpdatePotionEffectMessage;
import dev.satyrn.wolfarmor.config.WolfArmorConfig;
import dev.satyrn.wolfarmor.util.WolfFoodStatsLevel;
import dev.satyrn.wolfarmor.common.network.packets.UpdateFoodStatsMessage;
import dev.satyrn.wolfarmor.item.ItemWolfArmor;
import dev.satyrn.wolfarmor.api.util.Criteria;
import dev.satyrn.wolfarmor.api.util.DataHelper;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.api.util.Items;
import dev.satyrn.wolfarmor.common.inventory.ContainerWolfInventory;
import dev.satyrn.wolfarmor.api.util.CreatureFoodStats;
import dev.satyrn.wolfarmor.util.OreDictHelper;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Mixes-in armored wolf functionality to the base wolf entity
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.0.20
 */
@Mixin(EntityWolf.class)
public abstract class MixinEntityWolf extends MixinEntityTameable implements IArmoredWolf {
    private int entityXpCooldown;
    private ContainerHorseChest inventory;
    private CreatureFoodStats foodStats;
    private WolfArmorConfig config;
    private SimpleNetworkWrapper connection;

    // Static ctor injection to add new data manager keys before any sub-classes can add their own.  Prevents nasty ID conflicts,
    // and lessens the number of packets we need to write or send.
    static {
        DataHelper.HAS_CHEST = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
        DataHelper.ARMOR_ITEM = EntityDataManager.createKey(EntityWolf.class, DataSerializers.ITEM_STACK);
        DataHelper.CHEST_TYPE = EntityDataManager.createKey(EntityWolf.class, DataSerializers.ITEM_STACK);
    }

    /**
     * Injects into the constructor to set the config instance, the food stats, and initilialize the inventory
     * @param ci The callback info
     * @since 3.0.20
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructed(CallbackInfo ci) {
        this.config = WolfArmorMod.getConfig();
        this.connection = WolfArmorMod.getNetworkChannel();

        float minimumHealth = this.config.getCanStarve() ? 0F : 8F;
        boolean damageEntity = this.config.getFoodStatsLevel() == WolfFoodStatsLevel.FULL;

        this.foodStats = new CreatureFoodStats(minimumHealth, damageEntity);
        this.inventoryInit();
    }

    /**
     * Gets the food stats instance for this creature
     * @return The creature's food stats instance
     * @since 3.6.0
     */
    @Override
    @Nonnull public CreatureFoodStats getFoodStats() { return this.foodStats; }

    /**
     * Adds levels of exhaustion to the creature's food stats instance
     * @param exhaustion The levels of exhaustion to add
     * @since 3.6.0
     */
    @Override
    public void addExhaustion(float exhaustion) {
        if (this.config.getFoodStatsLevel() != WolfFoodStatsLevel.DISABLED
                && this.foodStats != null
                && !this.getEntityWorld().isRemote) {
            this.foodStats.addExhaustion(exhaustion);
        }
    }

    /**
     * Callback to update the server-side wolf entity when the contianer is modified client-side
     * @param invBasic The inventory instance
     * @since 3.0.20
     */
    @Override
    public void onInventoryChanged(@Nonnull IInventory invBasic) {
        if(this.getEntityWorld().isRemote) {
            return;
        }

        @Nonnull ItemStack armorItemStack = invBasic.getStackInSlot(ContainerWolfInventory.INVENTORY_SLOT_ARMOR);

        this.setArmorItemStack(armorItemStack);

        this.applyArmorModifiers(this.getEntityAttribute(SharedMonsterAttributes.ARMOR), armorItemStack);
        this.applyArmorModifiers(this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS), armorItemStack);
    }

    /**
     * Gets the type of the wolf backpack as an item
     * @return The chest type of the wolf backpack
     * @since 3.0.20
     */
    @Override
    public Item getChestType() {
        ItemStack chestType = this.dataManager.get(DataHelper.CHEST_TYPE);
        return chestType.isEmpty() ? null : chestType.getItem();
    }

    /**
     * Sets the chest type of the wolf backpack to the specified item stack
     * @param stack An item stack which describes the backpack chest type
     * @since 3.0.20
     */
    @Override
    public void setChestType(@Nonnull ItemStack stack) {
        ItemStack chestType = stack.copy();
        if(!chestType.isEmpty())
            chestType.setCount(1);
        this.dataManager.set(DataHelper.CHEST_TYPE, chestType);
    }

    /**
     * Checks if the wolf has a backpack
     * @return <c>true</c> if the wolf has a backpack; otherwise, <c>false</c>
     * @since 3.0.20
     */
    @Override
    public boolean getHasChest() { return this.dataManager.get(DataHelper.HAS_CHEST); }

    /**
     * Sets the wolf to either have or not have an backpack
     * @param hasChest <c>true</c> to add a wolf backpack, <c>false</c> to remove it
     * @since 3.0.20
     */
    @Override
    public void setHasChest(boolean hasChest) {
        this.dataManager.set(DataHelper.HAS_CHEST, hasChest);
        if(!hasChest) {
            this.dataManager.set(DataHelper.CHEST_TYPE, ItemStack.EMPTY);
        }
    }

    /**
     * Gets the maximum size of a wolf's inventory
     * @return The maximum configured size of a wolf's inventory
     * @since 3.0.20
     */
    @Override
    public int getMaxSizeInventory() { return 1 + (this.config.getChestSize().getColumns() * this.config.getChestSize().getRows()); }

    /**
     * Gets the inventory instance
     * @return The wolf's inventory instance

     * @since 3.0.20
     */
    @Override
    @Nonnull public InventoryBasic getInventory() { return this.inventory; }

    /**
     * Gets the armor item stack from the data manager
     * @return The current armor item stack
     * @since 3.0.20
     */
    @Override
    @Nonnull public ItemStack getArmorItemStack() {
        return this.dataManager.get(DataHelper.ARMOR_ITEM);
    }

    /**
     * Sets the wolf's armor item stack in its data manager
     * @param armorItemStack The armor item stack
     * @since 3.0.20
     */
    @Override
    public void setArmorItemStack(@Nonnull ItemStack armorItemStack) {
        if(armorItemStack != this.dataManager.get(DataHelper.ARMOR_ITEM)) {
            this.dataManager.set(DataHelper.ARMOR_ITEM, armorItemStack);

            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, armorItemStack);

            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, armorItemStack);
            this.setItemStackToSlot(EntityEquipmentSlot.CHEST, armorItemStack);
            this.setItemStackToSlot(EntityEquipmentSlot.LEGS, armorItemStack);
            this.setItemStackToSlot(EntityEquipmentSlot.FEET, armorItemStack);

            Arrays.fill(this.inventoryArmorDropChances, 0.0F);
            Arrays.fill(this.inventoryHandsDropChances, 0.0F);
        }
    }

    /**
     * Checks if the wolf currently has armor equipped
     * @return <c>true</c> if the armor item is set; otherwise, <c>false</c>
     * @since 3.0.20
     */
    @Override
    public boolean getHasArmor() {
        return !this.getArmorItemStack().isEmpty();
    }

    /**
     * Checks if a specific item stack can be equipped by the wolf as armor
     * @param armorItemStack The armor item stack
     * @return <c>true</c> if the armor item can be equipped; otherwise, <c>false</c>.
     * @since 3.0.20
     */
    @Override
    public boolean canEquipItem(@Nonnull ItemStack armorItemStack) {
        return armorItemStack.isEmpty() || (!this.getHasArmor() && armorItemStack.getItem() instanceof ItemWolfArmor);
    }

    /**
     * Equips an item stack as armor, if possible
     * @param armorItemStack The armor item stack to equip, provided canEquipItem is <c>true</c>
     * @since 3.0.20
     */
    @Override
    public void equipArmor(@Nonnull ItemStack armorItemStack) {
        if (canEquipItem(armorItemStack)) {
            this.inventory.setInventorySlotContents(ContainerWolfInventory.INVENTORY_SLOT_ARMOR, armorItemStack);
        }
    }

    /**
     * Sets the inventory item in the specified slot
     * @param index The index of the slot to alter in the inventory
     * @param itemStack The item stack to insert into the specified slot
     * @since 3.0.20
     */
    @Override
    public void setInventoryItem(int index, @Nonnull ItemStack itemStack) {
        if (index >= 0 && index < this.inventory.getSizeInventory()) {
            this.inventory.setInventorySlotContents(index, itemStack);
        } else {
            WolfArmorMod.getLogger().log(Level.ERROR, String.format("Invalid slot: %d", index));
        }
    }

    /**
     * Drops the wolf's equipped armor
     * @since 3.0.20
     */
    @Override
    public void dropEquipment() {
        if(this.getHasArmor()) {
            @Nonnull ItemStack armorItemStack = this.getArmorItemStack();
            if(!this.getEntityWorld().isRemote) {
                this.entityDropItem(armorItemStack, 0);
            }
            this.equipArmor(ItemStack.EMPTY);
        }
    }

    /**
     * Drops the wolf's backpack as a chest
     * @since 3.0.20
     */
    @Override
    public void dropChest() {
        if(this.getHasChest()) {
            if (!this.getEntityWorld().isRemote) {
                Item chestItem = this.getChestType();
                this.entityDropItem(new ItemStack(chestItem == null ? Item.getItemFromBlock(Blocks.CHEST) : chestItem, 1), 0);
            }
            this.setHasChest(false);
        }
    }

    /**
     * Drops all of the items in the wolf's backpack
     * @since 3.0.20
     */
    @Override
    public void dropInventoryContents() {
        for(int slotIndex = ContainerWolfInventory.INVENTORY_SLOT_CHEST_START; slotIndex <= this.getMaxSizeInventory() - 1; ++slotIndex) {
            @Nonnull ItemStack stackInSlot = this.inventory.getStackInSlot(slotIndex);
            if(!stackInSlot.isEmpty()) {
                if(!this.getEntityWorld().isRemote) {
                    this.entityDropItem(stackInSlot, 0);
                }
                this.setInventoryItem(slotIndex, ItemStack.EMPTY);
            }
        }
    }

    /**
     * Shadows whether or not the wolf is angry
     * @return <c>true</c> if the wolf is angry; otherwise, <c>false</c>
     * @since 3.6.0
     */
    @Shadow public abstract boolean isAngry();

    /**
     * Damages the wolf's equipped armor when the wolf takes damage
     * @param damage The damage taken by the wolf
     * @implNote This method is intrinsically overwritten in EntityWolf
     * @since 3.6.0
     */
    @Intrinsic
    @Override
    protected void damageArmor(float damage) {
        super.damageArmor(damage);
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

    /**
     * Moves the entity through the world
     * @param strafe The relative motion in the X axis
     * @param vertical The relative motion in the Y axis
     * @param forward The relative motion in the Z axis
     * @implNote This method is intrinsically overwritten in EntityWolf

     * @since 3.6.0
     */
    @Intrinsic
    @Override
    public void travel(float strafe, float vertical, float forward) {
        double initX = this.posX;
        double initY = this.posY;
        double initZ = this.posZ;

        super.travel(strafe, vertical, forward);

        this.addMovementStat(this.posX - initX, this.posY - initY, this.posZ - initZ);
    }

    /**
     * Called when a new potion effect is applied to the entity
     * @param effect The new potion effect applied to the entity
     * @implNote This method is intrinsically overwritten in EntityWolf
     * @author Isabel Maskrey
     * @since 3.6.0
     */
    @Intrinsic
    @Override
    protected void onNewPotionEffect(PotionEffect effect) {
        super.onNewPotionEffect(effect);
        if (!this.getEntityWorld().isRemote) {
            this.dispatchPotionEffectMessage(effect, false);
        }
    }

    /**
     * Called when a potion effect is altered
     * @param effect The effect which changed
     * @param sendUpdate if <c>true</c>, the client side will be notified of the change
     * @implNote This method is intrinsically overwritten in EntityWolf

     * @since 3.6.0
     */
    @Intrinsic
    @Override
    protected void onChangedPotionEffect(PotionEffect effect, boolean sendUpdate) {
        super.onChangedPotionEffect(effect, sendUpdate);
        if (sendUpdate && !this.getEntityWorld().isRemote) {
            this.dispatchPotionEffectMessage(effect, false);
        }
    }

    /**
     * Called when a previously active potion effect ends.
     * @param effect The effect which ended
     * @implNote This method is intrinsically overwritten in EntityWolf

     * @since 3.6.0
     */
    @Intrinsic
    @Override
    protected void onFinishedPotionEffect(PotionEffect effect) {
        super.onFinishedPotionEffect(effect);
        if (!this.getEntityWorld().isRemote) {
            this.dispatchPotionEffectMessage(effect, true);
        }
    }

    /**
     * Called when the entity dies.
     * @param damageSource
     */
    @Intrinsic
    @Override
    public void onDeath(DamageSource damageSource) {
        this.dropEquipment();
        this.dropChest();
        this.dropInventoryContents();
        super.onDeath(damageSource);
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
            this.foodStats.addExhaustion(0.1F);
        }

        cir.setReturnValue(atkFlag);
        cir.cancel();
    }

    @Inject(method = "entityInit", at = @At("RETURN"))
    private void onEntityInit(CallbackInfo ci) {
        this.dataManager.register(DataHelper.HAS_CHEST, false);
        this.dataManager.register(DataHelper.ARMOR_ITEM, ItemStack.EMPTY);
        this.dataManager.register(DataHelper.CHEST_TYPE, ItemStack.EMPTY);
    }

    @Inject(method = "onUpdate", at=@At("TAIL"))
    private void onUpdate(CallbackInfo ci) {
        // since we can't cast ourselves to EntityLivingBase we need to call food tick via reflection.
        if (this.foodStats != null && this.isTamed() && !this.getEntityWorld().isRemote &&
                this.config.getFoodStatsLevel() != WolfFoodStatsLevel.DISABLED) {
            //noinspection ConstantConditions this is actually fine it's a mixin
            this.foodStats.onUpdate((EntityLivingBase)(Object)this);
            this.connection.sendToAll(new UpdateFoodStatsMessage(this.getEntityId(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
        }
        --this.entityXpCooldown;
    }

    @Inject(method="onLivingUpdate", at=@At("TAIL"))
    private void onLivingUpdate(CallbackInfo ci) {
        if (!this.isDead && !this.getEntityWorld().isRemote) {
            @Nonnull ItemStack enchantedItem = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, (EntityLivingBase) (Object) this);
            if (!enchantedItem.isEmpty() && enchantedItem.isItemDamaged()) {
                AxisAlignedBB aabb;
                if (this.isRiding() && !this.getRidingEntity().isDead) {
                    aabb = this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox());
                } else {
                    aabb = this.getEntityBoundingBox();
                }

                List<Entity> collidedEntities = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity((Entity) (Object) this, aabb);

                for (Entity entity : collidedEntities) {
                    if (entity instanceof EntityXPOrb && !entity.isDead) {
                        EntityXPOrb xpOrb = (EntityXPOrb)entity;
                        if (xpOrb.delayBeforeCanPickup == 0 && this.entityXpCooldown == 0) {
                            float ratio = enchantedItem.getItem().getXpRepairRatio(enchantedItem);
                            int repairAmount = Math.min(this.xpRoundAverage(xpOrb.xpValue * ratio), enchantedItem.getItemDamage());
                            xpOrb.xpValue -= this.xpRoundAverage(xpOrb.xpValue / ratio);
                            enchantedItem.setItemDamage(enchantedItem.getItemDamage() - repairAmount);
                            this.entityXpCooldown = 2;
                        }
                        if (xpOrb.xpValue == 0) {
                            xpOrb.setDead();
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "getAmbientSound", at=@At("HEAD"), cancellable = true)
    private void onGetAmbientSound(CallbackInfoReturnable<SoundEvent> ci) {
        if (this.isAngry() || !this.isTamed() || this.config.getFoodStatsLevel() == WolfFoodStatsLevel.DISABLED || this.foodStats == null) {
            return;
        }

        if (this.getRNG().nextInt(3) == 0 && (this.foodStats.getFoodLevel() + this.foodStats.getSaturationLevel() <= 18F)) {
            ci.setReturnValue(SoundEvents.ENTITY_WOLF_WHINE);
            ci.cancel();
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    private void onWriteEntityToNBT(@Nonnull NBTTagCompound compound, CallbackInfo ci) {
        boolean hasChest = this.getHasChest();
        compound.setBoolean("HasChest", hasChest);

        if(hasChest) {
            ItemStack chestInSlot = this.dataManager.get(DataHelper.CHEST_TYPE);
            NBTTagCompound chestItem = new NBTTagCompound();
            chestInSlot.writeToNBT(chestItem);
            compound.setTag("ChestType", chestItem);

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

        if(this.foodStats != null) {
            this.foodStats.writeNBT(compound);
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

            ItemStack chestTypeStack = new ItemStack(compound.getCompoundTag("ChestType"));
            this.dataManager.set(DataHelper.CHEST_TYPE, chestTypeStack);

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
        if (!armorTags.hasNoTags()) {
            @Nonnull ItemStack armorItemStack = new ItemStack(armorTags);
            this.equipArmor(armorItemStack);
        }

        if (this.foodStats != null) {
            this.foodStats.readNBT(compound);
        }
    }

    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true)
    private void onProcessInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        // Handle sneaking player (Open wolf inventory)
        if(player.isSneaking() && this.isOwner(player) && this.isTamed() && !this.isChild()) {
            this.openWolfInventory(player);
            cir.setReturnValue(true);
            cir.cancel();
            return;
        }

        @Nonnull ItemStack itemStack = player.getHeldItem(hand);
        if (!itemStack.isEmpty()) {
            if (this.config.getFoodStatsLevel() != WolfFoodStatsLevel.DISABLED && this.foodStats != null) {
                if (itemStack.getItem() instanceof ItemFood) {
                    ItemFood foodItem = (ItemFood) itemStack.getItem();
                    cir.setReturnValue(false);
                    if (foodItem.isWolfsFavoriteMeat() || this.isBreedingItem(itemStack)) {
                        if (foodItem.isWolfsFavoriteMeat() && this.foodStats.needFood()) {
                            this.foodStats.addStats(foodItem, itemStack);
                            this.consumeItemFromStack(player, itemStack);
                            cir.setReturnValue(true);
                        } else if (this.isBreedingItem(itemStack)) {
                            if (this.getGrowingAge() == 0 && !this.isInLove()) {
                                this.consumeItemFromStack(player, itemStack);
                                this.setInLove(player);
                                cir.setReturnValue(true);
                            } else if (this.isChild()) {
                                this.consumeItemFromStack(player, itemStack);
                                this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.1F), true);
                                cir.setReturnValue(true);
                            }
                        } else {
                            cir.setReturnValue(false);
                        }
                    } else {
                        if (this.isOwner(player) && !this.world.isRemote) {
                            this.setSitting(!this.isSitting());
                            this.isJumping = false;
                            this.getNavigator().clearPath();
                            this.setAttackTarget((EntityLivingBase)null);
                        }
                    }
                    cir.cancel();
                    return;
                }
            }

            if (this.isTamed() && this.isOwner(player) && !this.isChild()) {
                if (this.config.getChestEnabled() && !this.getHasChest() &&
                        OreDictHelper.isOre(false, "chestWood", itemStack)) {
                    if (!this.getEntityWorld().isRemote) {
                        ItemStack chestItem = itemStack.copy();
                        chestItem.setCount(1);
                        this.playEquipSound(chestItem);
                        this.setHasChest(true);
                        this.setChestType(chestItem);

                        this.consumeItemFromStack(player, itemStack);

                        if (player instanceof EntityPlayerMP) {
                            ((WolfArmorTrigger) Criteria.EQUIP_WOLF_CHEST).trigger((EntityPlayerMP) player, (EntityWolf) (Object) this);
                        }
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }

                if (Items.isValidWolfArmor(itemStack)) {
                    if (this.getArmorItemStack().isEmpty()) {
                        ItemStack toEquip = itemStack.copy();
                        toEquip.setCount(1);
                        this.equipArmor(toEquip);
                        this.consumeItemFromStack(player, itemStack);
                    } else {
                        this.openWolfInventory(player);
                    }
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
        instance.removeModifier(IItemWolfArmor.ARMOR_UUID);
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemWolfArmor)) {
            return;
        }
        ItemWolfArmor armorItem = (ItemWolfArmor) stack.getItem();
        Multimap<String, AttributeModifier> map = armorItem.getAttributeModifiers(EntityEquipmentSlot.CHEST, stack);
        if (map.containsKey(instance.getAttribute().getName())) {
            map.get(instance.getAttribute().getName()).forEach(instance::applyModifier);
        }
    }

    private void addMovementStat(double deltaX, double deltaY, double deltaZ) {
        if (!this.isRiding()) {
            if (this.isInsideOfMaterial(Material.WATER))  {
                int magnitude3d = Math.round(MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 100F);
                if (magnitude3d > 0) {
                    this.addExhaustion(0.01F * magnitude3d * 0.01F);
                }
            } else if (this.isInWater()) {
                int magnitude2d = Math.round(MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 100F);
                if (magnitude2d > 0) {
                    this.addExhaustion(0.01F * magnitude2d * 0.01F);
                }
            } else if (this.onGround) {
                int magnitude2d = Math.round(MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 100F);
                if (magnitude2d > 0 && this.isSprinting()) {
                    this.addExhaustion(0.1F * magnitude2d * 0.01F);
                }
            }
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
        if (OreDictHelper.isOre(false, "chestWood", itemStack)) {
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
            this.dataManager.set(DataHelper.CHEST_TYPE, new ItemStack(Blocks.CHEST, 1));
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
        if (!armorTags.hasNoTags()) {
            @Nonnull ItemStack armorItemStack = new ItemStack(armorTags);
            this.equipArmor(armorItemStack);
        }
    }

    private void dispatchPotionEffectMessage(PotionEffect effect, boolean removeEffect) {
        MessageBase<?> message = removeEffect
                ? new RemovePotionEffectMessage(this.getEntityId(), effect.getPotion())
                : new UpdatePotionEffectMessage(this.getEntityId(), effect);
        this.connection.sendToAll(message);
    }

    private int xpRoundAverage(float value) {
        float floor = MathHelper.floor(value);
        return (int) floor + (Math.random() < value - floor ? 1 : 0);
    }
}
