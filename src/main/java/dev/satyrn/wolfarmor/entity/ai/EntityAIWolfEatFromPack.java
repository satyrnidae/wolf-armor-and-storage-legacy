package dev.satyrn.wolfarmor.entity.ai;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.api.util.CreatureFoodStats;
import dev.satyrn.wolfarmor.config.WolfArmorConfig;
import dev.satyrn.wolfarmor.util.WolfFoodStatsLevel;
import dev.satyrn.wolfarmor.common.inventory.ContainerWolfInventory;
import dev.satyrn.wolfarmor.common.network.packets.WolfEatMessage;
import dev.satyrn.wolfarmor.common.network.packets.WolfHealMessage;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;

/**
 * @author Isabel Maskrey (satyrnidae)
 */
public class EntityAIWolfEatFromPack extends EntityAIBase implements IInventoryChangedListener {
    private final EntityWolf entity;
    private final IArmoredWolf armoredWolf;

    private final NonNullList<ItemStack> inventoryContents = NonNullList.create();
    private final WolfArmorConfig config;
    private final SimpleNetworkWrapper connection;
    private ItemStack eatingFood = ItemStack.EMPTY;

    private int eatCooldown;
    private int foodEatTime;
    private boolean hasHealedSinceLastReset;

    public EntityAIWolfEatFromPack(@Nonnull EntityWolf entity) {
        this.entity = entity;
        this.armoredWolf = (IArmoredWolf)entity;
        this.config = WolfArmorMod.getConfig();
        this.connection = WolfArmorMod.getNetworkChannel();
        this.inventoryInit();
    }

    private void inventoryInit() {
        armoredWolf.getInventory().removeInventoryChangeListener(this);
        armoredWolf.getInventory().addInventoryChangeListener(this);
        refreshInventoryContents(armoredWolf.getInventory());
    }


    @Override
    public boolean shouldExecute() {
        return !this.entity.getIsInvulnerable()
                && this.entity.hurtTime == 0
                && this.config.getChestEnabled()
                && this.config.getAutoHealEnabled()
                && this.armoredWolf.getHasChest();
    }

    @Override
    public void startExecuting() {
        if(eatCooldown <= 0) {
            @Nonnull ItemStack mostEfficientFood = getMostEfficientFood();
            if (!mostEfficientFood.isEmpty() && mostEfficientFood.getItem() instanceof ItemFood) {
                @Nullable ItemFood foodItem = (ItemFood)mostEfficientFood.getItem();

                float damageAmount = (this.entity.getMaxHealth() - this.entity.getHealth());
                float healedAmount = foodItem.getHealAmount(mostEfficientFood);
                CreatureFoodStats foodStats = this.config.getFoodStatsLevel() != WolfFoodStatsLevel.DISABLED ? this.armoredWolf.getFoodStats() : null;

                boolean isHurtOrHungry = damageAmount >= 1.0F || (foodStats != null && foodStats.needFood());
                boolean wasteNotWantNot = healedAmount <= damageAmount || (foodStats != null && healedAmount <= (20 - foodStats.getFoodLevel()));

                if (isHurtOrHungry && wasteNotWantNot) {
                    this.eatingFood = mostEfficientFood;
                    foodEatTime = foodItem.itemUseDuration;
                    eatCooldown = foodEatTime + entity.getRNG().nextInt(foodEatTime);
                }
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return eatCooldown > 0;
    }

    @Override
    public void updateTask() {
        --eatCooldown;
        if(!eatingFood.isEmpty() && eatingFood.getItem() instanceof ItemFood) {
            if (foodEatTime > 0) {
                if (--foodEatTime % 4 == 0) {
                    TargetPoint targetPoint = new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 80);
                    this.connection.sendToAllAround(new WolfEatMessage(entity.getEntityId(), eatingFood), targetPoint);
                    this.entity.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F, (this.entity.getRNG().nextFloat() - this.entity.getRNG().nextFloat()) * 0.2F + 1);
                }
            } else if (!hasHealedSinceLastReset) {
                CreatureFoodStats foodStats = this.armoredWolf.getFoodStats();
                boolean creatureFoodStatsEnabled = this.config.getFoodStatsLevel() != WolfFoodStatsLevel.DISABLED;
                if (foodStats != null || !creatureFoodStatsEnabled) {
                    hasHealedSinceLastReset = true;
                    TargetPoint targetPoint = new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 80);
                    this.connection.sendToAllAround(new WolfHealMessage(entity.getEntityId()), targetPoint);

                    if (creatureFoodStatsEnabled) {
                        foodStats.addStats((ItemFood)this.eatingFood.getItem(), this.eatingFood);
                    } else {
                        this.entity.heal((float) ((ItemFood) eatingFood.getItem()).getHealAmount(eatingFood));
                    }
                    this.eatingFood.shrink(1);
                }
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
        for(int slotIndex = ContainerWolfInventory.INVENTORY_SLOT_CHEST_START;
            slotIndex < ContainerWolfInventory.INVENTORY_SLOT_CHEST_START + this.armoredWolf.getMaxSizeInventory() - 1;
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