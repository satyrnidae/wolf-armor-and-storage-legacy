package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.ReflectionCache;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
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
    //region Fields

    private static final String COPY_DATA_FROM_OLD_SRG = "func_180432_n";

    //endregion Fields

    //region Public / Protected Methods

    /**
     * Handles the OnEntityJoinWorld event
     *
     * @param event The event data
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityJoinWorld(@Nonnull EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote) {
            if (event.getEntity().getClass() == EntityWolf.class) {
                EntityWolfArmored replaceEntity = new EntityWolfArmored(event.getWorld());

                try {
                    Method copyDataFromOld = ReflectionCache.getMethod(Entity.class, replaceEntity, new String[] {COPY_DATA_FROM_OLD_SRG, "copyDataFromOld"}, Entity.class);

                    if(copyDataFromOld != null) {
                        copyDataFromOld.invoke(replaceEntity, event.getEntity());
                    }
                } catch (IllegalAccessException ex) {
                    WolfArmorMod.getLogger().fatal(ex);
                    throw new RuntimeException("Reflection failed in WolfArmorEntityEventHandler: invoke failed with IllegalAccessException", ex);
                }
                catch (InvocationTargetException ex) {
                    WolfArmorMod.getLogger().fatal(ex);
                    throw new RuntimeException("Reflection failed in WolfArmorEntityEventHandler: invoke failed with InvocationTargetException", ex);
                }

                event.getWorld().spawnEntityInWorld(replaceEntity);
                event.getEntity().setDead();
                event.setCanceled(true);
            }
        }
    }

    //endregion Public / Protected Methods
}
