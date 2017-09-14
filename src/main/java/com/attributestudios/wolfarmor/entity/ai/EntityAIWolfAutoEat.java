package com.attributestudios.wolfarmor.entity.ai;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.network.PacketHandler;
import com.attributestudios.wolfarmor.common.network.packets.WolfEatMessage;
import com.attributestudios.wolfarmor.common.network.packets.WolfHealMessage;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;

public class EntityAIWolfAutoEat extends EntityAIBase implements IInventoryChangedListener {
    private final EntityWolf entity;
    private IWolfArmorCapability capability;

    private NonNullList<ItemStack> inventoryContents = NonNullList.create();
    private ItemStack eatingFood = ItemStack.EMPTY;

    private int eatCooldown;
    private int foodEatTime;
    private boolean hasHealedSinceLastReset;

    public EntityAIWolfAutoEat(@Nonnull EntityWolf entity) {
        this.entity = entity;
        this.capability = this.entity.getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
        this.inventoryInit();
    }

    private void inventoryInit() {
        if(capability != null) {
            capability.getInventory().removeInventoryChangeListener(this);
            capability.getInventory().addInventoryChangeListener(this);
            refreshInventoryContents(capability.getInventory());
        }
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

        return capability.getHasChest() &&
                !entity.getIsInvulnerable() &&
                entity.hurtTime == 0 &&
                (entity.getMaxHealth() - entity.getHealth()) >= 1.0F;
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
                    PacketHandler.getChannel().sendToAllAround(
                        new WolfEatMessage(entity.getEntityId(), eatingFood),
                        new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 60));
                    this.entity.playSound(SoundEvents.ENTITY_GENERIC_EAT,
                            0.5F,
                            (this.entity.getRNG().nextFloat() - this.entity.getRNG().nextFloat()) * 0.2F + 1);
                }
            } else if (!hasHealedSinceLastReset) {
                hasHealedSinceLastReset = true;
                PacketHandler.getChannel().sendToAllAround(
                    new WolfHealMessage(entity.getEntityId()),
                    new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 60));
                this.entity.heal((float) ((ItemFood) eatingFood.getItem()).getHealAmount(eatingFood));
                this.eatingFood.shrink(1);
            }
        }
    }

    @Override
    public void resetTask() {
        foodEatTime = 0;
        hasHealedSinceLastReset = false;
        eatingFood = ItemStack.EMPTY;

        inventoryInit();
    }

    @Override
    public void onInventoryChanged(@Nonnull IInventory invBasic) {
        this.refreshInventoryContents(invBasic);
    }

    private void refreshInventoryContents(IInventory invBasic) {
        this.inventoryContents.clear();
        for(int slotIndex = CapabilityWolfArmor.INVENTORY_SLOT_CHEST_START;
            slotIndex < CapabilityWolfArmor.INVENTORY_SLOT_CHEST_START + CapabilityWolfArmor.INVENTORY_SLOT_CHEST_LENGTH;
            ++slotIndex) {
            this.inventoryContents.add(invBasic.getStackInSlot(slotIndex));
        }
    }

    @Nonnull
    private ItemStack getMostEfficientFood() {
        final float healthDiff = this.entity.getMaxHealth() - this.entity.getHealth();

        return this.inventoryContents.stream()
                .filter(itemStack -> !itemStack.isEmpty() &&
                        entity.isBreedingItem(itemStack) &&
                        itemStack.getItem() instanceof ItemFood)
                .min(Comparator.comparing(itemStack -> {
                    ItemFood food = (ItemFood) itemStack.getItem();
                    return Math.abs(healthDiff - food.getHealAmount(itemStack));
                }))
                .orElse(ItemStack.EMPTY);
    }
}