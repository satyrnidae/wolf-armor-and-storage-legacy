package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.capabilities.IWolfArmor;

import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Contains all forge subscribed events for entities
 */
public class WolfArmorEntityEventHandler {
    //region Public / Protected Methods

    /**
     * Converts old EntityWolfArmored back to EntityWolf to leverage capabilities system
     * @param event The event data
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityJoinWorld(@Nonnull EntityJoinWorldEvent event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            Entity entity = event.getEntity();

            if (entity.getClass() == EntityWolfArmored.class) {
                WolfArmorMod.getLogger().warning("Replacing EntityWolfArmored with new capable wolf");

                EntityWolfArmored entityWolfArmored = (EntityWolfArmored)entity;
                EntityWolf entityWolf = new EntityWolf(world);
                @SuppressWarnings("ConstantConditions") IWolfArmor wolfArmorCapability = entityWolf.getCapability(CapabilityWolfArmor.WOLF_ARMOR, null);

                IInventory wolfInventory = entityWolfArmored.getInventory();
                IInventory capabilityInventory = wolfArmorCapability.getInventory();
                for(int i = 0; i < wolfInventory.getSizeInventory(); i++) {
                    ItemStack stack = wolfInventory.getStackInSlot(i);

                    capabilityInventory.setInventorySlotContents(i, stack);
                }

                if(entityWolfArmored.getHasArmor()) {
                    wolfArmorCapability.setArmorItemStack(entityWolfArmored.getArmorItemStack());
                }

                if(entityWolfArmored.getHasChest()) {
                    wolfArmorCapability.setHasChest(true);
                }

                NBTTagCompound compound = new NBTTagCompound();
                entityWolfArmored.writeToNBT(compound);

                entityWolf.copyLocationAndAnglesFrom(entityWolfArmored);
                entityWolf.readEntityFromNBT(compound);

                world.spawnEntityInWorld(entityWolf);
                entityWolfArmored.setDead();
                event.setCanceled(true);
            }
        }
    }

    //attach the armor capability to the wolf entity
    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity eventObject = event.getObject();
        if (eventObject instanceof EntityWolf &&
                !(eventObject instanceof EntityWolfArmored)) {
            //Now, attach the armor ability here
            event.addCapability(new ResourceLocation(WolfArmorMod.MOD_ID, "wolf_armor"), new CapabilityWolfArmor((EntityWolf) eventObject));
        }

    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public void onWolfHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityWolf) {
            @Nullable IWolfArmor wolfArmor = event.getEntity().getCapability(CapabilityWolfArmor.WOLF_ARMOR, null);
            if(wolfArmor != null) {
                wolfArmor.damageArmor(event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public void onWolfDropItems(LivingDropsEvent event) {
        if (event.getEntity() instanceof EntityWolf) {
            IWolfArmor wolfArmor = event.getEntity().getCapability(CapabilityWolfArmor.WOLF_ARMOR, null);
            DamageSource Source = event.getSource();
            wolfArmor.dropEquipment(Source != null && Source.getEntity() instanceof EntityPlayer, event.getLootingLevel());
        }
    }

    @SubscribeEvent
    public void onPlayerInteractWithWolf(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityWolf) {
            IWolfArmor wolfArmor = event.getTarget().getCapability(CapabilityWolfArmor.WOLF_ARMOR, null);
            EntityPlayer player = event.getEntityPlayer();
            EnumHand hand = EnumHand.MAIN_HAND;
            ItemStack stack = player.getHeldItemMainhand();
            if (stack == null && (stack = player.getHeldItemOffhand()) != null) {
                hand = EnumHand.OFF_HAND;
            }
            //use for open gui
            if (wolfArmor.processInteract(player, hand, stack)) {
                event.setCanceled(true);
            }
        }
    }
    //endregion Public / Protected Methods
}
