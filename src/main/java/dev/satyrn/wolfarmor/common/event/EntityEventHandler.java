package dev.satyrn.wolfarmor.common.event;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.IArmoredWolf;
import dev.satyrn.wolfarmor.entity.ai.EntityAIWolfAutoEat;
import dev.satyrn.wolfarmor.entity.ai.EntityAIWolfHowl;
import dev.satyrn.wolfarmor.entity.passive.EntityWolfArmored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * Contains all forge subscribed events for entities
 */
public class EntityEventHandler {
    //region Public / Protected Methods

    /**
     * Converts old EntityWolfArmored back to EntityWolf to leverage capabilities system
     *
     * @param event The event data
     */
    @Deprecated
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityWolfArmoredJoinedWorld(@Nonnull EntityJoinWorldEvent event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            Entity entity = event.getEntity();

            if (entity.getClass() == EntityWolfArmored.class) {
                WolfArmorMod.getLogger().warning("Replacing EntityWolfArmored with new mixed wolf");

                EntityWolfArmored entityWolfArmored = (EntityWolfArmored) entity;
                EntityWolf entityWolf = new EntityWolf(world);
                IArmoredWolf armoredWolf = (IArmoredWolf)entityWolf;
                if(armoredWolf == null) {
                    throw new RuntimeException("Failed to replace entity: Mixins were not properly registered!");
                }

                IInventory wolfInventory = entityWolfArmored.getInventory();
                IInventory capabilityInventory = armoredWolf.getInventory();
                for (int i = 0; i < wolfInventory.getSizeInventory(); i++) {
                    ItemStack stack = wolfInventory.getStackInSlot(i);

                    capabilityInventory.setInventorySlotContents(i, stack);
                }

                if (entityWolfArmored.getHasArmor()) {
                    armoredWolf.setArmorItemStack(entityWolfArmored.getArmorItemStack());
                }

                if (entityWolfArmored.getHasChest()) {
                    armoredWolf.setHasChest(true);
                }

                NBTTagCompound compound = new NBTTagCompound();
                entityWolfArmored.writeToNBT(compound);

                entityWolf.copyLocationAndAnglesFrom(entityWolfArmored);
                entityWolf.readEntityFromNBT(compound);

                world.spawnEntity(entityWolf);
                entityWolfArmored.setDead();
                event.setCanceled(true);
            }
            else if(entity instanceof EntityWolf){
                EntityWolf wolf = (EntityWolf) entity;

                Arrays.fill(wolf.inventoryHandsDropChances, 0.0F);
                Arrays.fill(wolf.inventoryArmorDropChances, 0.0F);
            }
        }
    }

    @SubscribeEvent
    public void onAttachEntityAI(@Nonnull EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityWolf) {
            EntityWolf entity = (EntityWolf) event.getEntity();
            entity.tasks.addTask(8, new EntityAIWolfHowl(entity));
            entity.tasks.addTask(1, new EntityAIWolfAutoEat(entity));
        }
    }

    //endregion Public / Protected Methods
}
