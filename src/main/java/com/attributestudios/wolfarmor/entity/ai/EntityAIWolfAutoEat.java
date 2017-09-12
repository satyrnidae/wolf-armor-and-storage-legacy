package com.attributestudios.wolfarmor.entity.ai;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.network.WolfArmorPacketHandler;
import com.attributestudios.wolfarmor.common.network.packets.WolfAutoHealMessage;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityAIWolfAutoEat extends EntityAIBase implements IInventoryChangedListener {
    private final EntityWolf entity;
    private IWolfArmorCapability capability;

    private NonNullList<ItemStack> favoriteFoods = NonNullList.create();
    private ItemStack eatingFood = ItemStack.EMPTY;

    private int eatCooldown;
    private int foodEatTime;
    private boolean hasHealedSinceLastReset;

    public EntityAIWolfAutoEat(@Nonnull EntityWolf entity) {
        this.entity = entity;
        this.capability = this.entity.getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
    }

    @Override
    public boolean shouldExecute() {
        if (!WolfArmorMod.getConfiguration().getIsWolfChestEnabled() ||
                !WolfArmorMod.getConfiguration().getShouldWolvesEatWhenDamaged()) {
            return false;
        }
        if (capability == null) {
            this.capability = this.entity.getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
            return false;
        }
        capability.getInventory().removeInventoryChangeListener(this);
        capability.getInventory().addInventoryChangeListener(this);

        return capability.getHasChest() && WolfArmorMod.getConfiguration().getShouldWolvesEatWhenDamaged() && entity.getHealth() < entity.getMaxHealth();
    }

    @Override
    public void startExecuting() {
        if(eatCooldown <= 0) {
            @Nonnull ItemStack mostEfficientFood = getMostEfficientFood();
            @Nullable ItemFood itemFood;
            if (mostEfficientFood.isEmpty() || !(mostEfficientFood.getItem() instanceof ItemFood)) {
                return;
            }
            itemFood = (ItemFood) mostEfficientFood.getItem();
            this.eatingFood = mostEfficientFood;
            foodEatTime = itemFood.itemUseDuration;
            eatCooldown = itemFood.itemUseDuration + entity.getRNG().nextInt(itemFood.itemUseDuration);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return eatCooldown > 0;
    }

    @Override
    public void updateTask() {
        --eatCooldown;

        if(eatingFood != ItemStack.EMPTY) {
            if (foodEatTime > 0) {
                if (--foodEatTime % 4 == 0) {
                    this.sendParticleUpdate(false);
                    this.entity.playSound(SoundEvents.ENTITY_GENERIC_EAT,
                            0.5F,
                            (this.entity.getRNG().nextFloat() - this.entity.getRNG().nextFloat()) * 0.2F + 1);
                }
            } else if (!hasHealedSinceLastReset) {
                hasHealedSinceLastReset = true;
                sendParticleUpdate(true);
                this.entity.heal((float) ((ItemFood) eatingFood.getItem()).getHealAmount(eatingFood));
                this.eatingFood.shrink(1);
            }
        }
    }

    private void sendParticleUpdate(boolean showHappyParticles) {
        WolfArmorPacketHandler.INSTANCE.sendToAllAround(new WolfAutoHealMessage(this.entity.getEntityId(),
                        Item.getIdFromItem(eatingFood.getItem()),
                        eatingFood.getMetadata(),
                        showHappyParticles),
                new NetworkRegistry.TargetPoint(this.entity.dimension,
                        this.entity.posX,
                        this.entity.posY,
                        this.entity.posZ,
                        32));
    }

    @Override
    public void resetTask() {
        foodEatTime = 0;
        hasHealedSinceLastReset = false;
        eatingFood = ItemStack.EMPTY;
    }

    @Override
    public void onInventoryChanged(@Nonnull IInventory invBasic) {
        this.favoriteFoods = NonNullList.create();
        for(int slotIndex = CapabilityWolfArmor.INVENTORY_SLOT_CHEST_START;
            slotIndex < CapabilityWolfArmor.INVENTORY_SLOT_CHEST_START + CapabilityWolfArmor.INVENTORY_SLOT_CHEST_LENGTH;
            ++slotIndex) {
            @Nonnull ItemStack stackInSlot = invBasic.getStackInSlot(slotIndex);
            if(this.entity.isBreedingItem(stackInSlot)) {
                favoriteFoods.add(stackInSlot);
            }
        }
    }

    private ItemStack getMostEfficientFood() {
        float healthDiff = this.entity.getMaxHealth() - this.entity.getHealth();

        float healDiff = Float.MAX_VALUE;
        @Nonnull ItemStack mostEfficient = ItemStack.EMPTY;
        for(@Nonnull ItemStack stack : this.favoriteFoods) {
            if(!(stack.getItem() instanceof ItemFood)) {
                continue;
            }
            ItemFood foodItem = (ItemFood) stack.getItem();
            float foodHealAmount = Math.abs(healthDiff - foodItem.getHealAmount(stack));
            if(foodHealAmount < healDiff) {
                healDiff = foodHealAmount;
                mostEfficient = stack;
            }
            if(Math.abs(healDiff) < 0.01F) {
                break;
            }
        }

        return mostEfficient;
    }
}