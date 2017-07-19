package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.capabilities.IWolfArmor;
import com.attributestudios.wolfarmor.entity.ai.EntityAIWolfHowl;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
     *
     * @param event The event data
     */
    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
<<<<<<< HEAD
    public void onEntityJoinWorld(@Nonnull EntityJoinWorldEvent event) {
=======
    public void onReplaceEntityWolfArmored(@Nonnull EntityJoinWorldEvent event) {
>>>>>>> origin/1.10.2-testing
        World world = event.getWorld();
        if (!world.isRemote) {
            Entity entity = event.getEntity();

<<<<<<< HEAD
            if (entity.getClass() == EntityWolf.class) {
                EntityWolfArmored entityWolfArmored = new EntityWolfArmored(world);

                try {
                    entityWolfArmored.copyLocationAndAnglesFrom(entity);

                    Method writeEntityToNBT = ReflectionCache.getMethod(Entity.class, entity, new String[] {"func_70014_b", "writeEntityToNBT"}, NBTTagCompound.class);

                    if(writeEntityToNBT != null) {
                        try {
                            NBTTagCompound compound = new NBTTagCompound();
                            writeEntityToNBT.invoke(entity, compound);
                            entityWolfArmored.readEntityFromNBT(compound);
                        } catch(InvocationTargetException ex) {
                            WolfArmorMod.getLogger().warning("NBT data for spawned wolf has been lost! InvocationTargetException: " + ex.getMessage());
                        }
                    }
                } catch (IllegalAccessException ex) {
                    WolfArmorMod.getLogger().fatal(ex);
                    throw new RuntimeException("Reflection failed in WolfArmorEntityEventHandler: invoke failed (" + ex.getMessage() +")", ex);
                }

                world.spawnEntity(entityWolfArmored);
                entity.setDead();
=======
            if (entity.getClass() == EntityWolfArmored.class) {
                WolfArmorMod.getLogger().warning("Replacing EntityWolfArmored with new capable wolf");

                EntityWolfArmored entityWolfArmored = (EntityWolfArmored) entity;
                EntityWolf entityWolf = new EntityWolf(world);
                @SuppressWarnings("ConstantConditions") IWolfArmor wolfArmorCapability = entityWolf.getCapability(CapabilityWolfArmor.WOLF_ARMOR, null);

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

                world.spawnEntityInWorld(entityWolf);
                entityWolfArmored.setDead();
>>>>>>> origin/1.10.2-testing
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onAttachEntityAI(@Nonnull EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityWolf) {
            EntityWolf entity = (EntityWolf) event.getEntity();
            entity.tasks.addTask(8, new EntityAIWolfHowl(entity));
        }
    }

    //attach the armor capability to the wolf entity
    @SuppressWarnings("deprecation")
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
            if (wolfArmor != null) {
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
