package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import javax.annotation.Nonnull;

/**
 * Contains all forge subscribed events
 */
public class WolfArmorEntityEventHandler {
    //region Public / Protected Methods

    /**
     * Handles the OnEntityJoinWorld event
     * @param event The event data
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityJoinWorld(@Nonnull EntityJoinWorldEvent event) {
        if(!event.world.isRemote) {
            if(event.entity.getClass() == EntityWolf.class) {
                EntityWolfArmored replaceEntity = new EntityWolfArmored(event.world);
                replaceEntity.copyDataFrom(event.entity, true);
                event.world.spawnEntityInWorld(replaceEntity);
                event.entity.setDead();
                event.setCanceled(true);
            }
        }
    }

    //endregion Public / Protected Methods
}
