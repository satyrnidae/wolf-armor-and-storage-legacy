package dev.satyrn.wolfarmor.entity.ai;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.IArmoredWolf;
import dev.satyrn.wolfarmor.common.inventory.ContainerWolfInventory;
import dev.satyrn.wolfarmor.common.network.PacketHandler;
import dev.satyrn.wolfarmor.common.network.packets.WolfEatMessage;
import dev.satyrn.wolfarmor.common.network.packets.WolfHealMessage;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;

public class EntityAIWolfAutoEat extends EntityAIBase implements IInventoryChangedListener {
    private final EntityWolf entity;
    private final IArmoredWolf armoredWolf;

    private NonNullList<ItemStack> inventoryContents = NonNullList.create();
    private ItemStack eatingFood = ItemStack.EMPTY;

    private int eatCooldown;
    private int foodEatTime;
    private boolean hasHealedSinceLastReset;

    public EntityAIWolfAutoEat(@Nonnull EntityWolf entity) {
        this.entity = entity;
        this.armoredWolf = (IArmoredWolf)entity;
        this.inventoryInit();
    }

    private void inventoryInit() {
        armoredWolf.getInventory().removeInventoryChangeListener(this);
        armoredWolf.getInventory().addInventoryChangeListener(this);
        refreshInventoryContents(armoredWolf.getInventory());
    }


    @Override
    public boolean shouldExecute() {
        if (!WolfArmorMod.getConfiguration().getIsWolfChestEnabled() ||
                !WolfArmorMod.getConfiguration().getShouldWolvesEatWhenDamaged()) {
            return false;
        }

        return armoredWolf.getHasChest() &&
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
        if(!eatingFood.isEmpty() && eatingFood.getItem() instanceof ItemFood) {
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
        for(int slotIndex = ContainerWolfInventory.INVENTORY_SLOT_CHEST_START;
            slotIndex < ContainerWolfInventory.INVENTORY_SLOT_CHEST_START + ContainerWolfInventory.INVENTORY_SLOT_CHEST_LENGTH;
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