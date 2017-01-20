package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.ReflectionCache;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Contains all forge subscribed events for entities
 */
public class WolfArmorEntityEventHandler {
    //region Public / Protected Methods

    /**
     * Handles the OnEntityJoinWorld event
     *
     * @param event The event data
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityJoinWorld(@Nonnull EntityJoinWorldEvent event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            Entity entity = event.getEntity();

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
                event.setCanceled(true);
            }
        }
    }

    //endregion Public / Protected Methods
}
