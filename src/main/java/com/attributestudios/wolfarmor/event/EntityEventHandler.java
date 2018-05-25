package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.api.util.Capabilities;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.entity.ai.EntityAIWolfAutoEat;
import com.attributestudios.wolfarmor.entity.ai.EntityAIWolfHowl;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
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
                WolfArmorMod.getLogger().warning("Replacing EntityWolfArmored with new capable wolf");

                EntityWolfArmored entityWolfArmored = (EntityWolfArmored) entity;
                EntityWolf entityWolf = new EntityWolf(world);
                IWolfArmorCapability wolfArmorCapability = entityWolf.getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
                if(wolfArmorCapability == null) {
                    throw new RuntimeException("Failed to replace entity: Capabilities were not properly registered!");
                }

                IInventory wolfInventory = entityWolfArmored.getInventory();
                IInventory capabilityInventory = wolfArmorCapability.getInventory();
                for (int i = 0; i < wolfInventory.getSizeInventory(); i++) {
                    ItemStack stack = wolfInventory.getStackInSlot(i);

                    capabilityInventory.setInventorySlotContents(i, stack);
                }

                if (entityWolfArmored.getHasArmor()) {
                    wolfArmorCapability.setArmorItemStack(entityWolfArmored.getArmorItemStack());
                }

                if (entityWolfArmored.getHasChest()) {
                    wolfArmorCapability.setHasChest(true);
                }

                NBTTagCompound compound = new NBTTagCompound();
                entityWolfArmored.writeToNBT(compound);

                entityWolf.copyLocationAndAnglesFrom(entityWolfArmored);
                entityWolf.readEntityFromNBT(compound);

                world.spawnEntity(entityWolf);
                entityWolfArmored.setDead();
                event.setCanceled(true);
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
