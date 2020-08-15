package dev.satyrn.wolfarmor.common.event;

import dev.satyrn.wolfarmor.api.entity.IFoodStatsCreature;
import dev.satyrn.wolfarmor.entity.ai.EntityAIWolfEatFromPack;
import dev.satyrn.wolfarmor.entity.ai.EntityAIWolfHowl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

/**
 * Contains all forge subscribed events for entities
 */
public class EntityEventHandler {
    //region Public / Protected Methods

    @SubscribeEvent
    public void onAttachEntityAI(@Nonnull EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityWolf) {
            EntityWolf entity = (EntityWolf) event.getEntity();
            entity.tasks.addTask(8, new EntityAIWolfHowl(entity));
            entity.tasks.addTask(1, new EntityAIWolfEatFromPack(entity));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDamage(LivingDamageEvent event) {
        IFoodStatsCreature creacher;
        if (event.isCanceled() || !(event.getEntity() instanceof IFoodStatsCreature)) {
            return;
        }
        creacher = (IFoodStatsCreature)event.getEntity();
        if (event.getAmount() != 0F) {
            creacher.addExhaustion(event.getSource().getHungerDamage());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        IFoodStatsCreature creacher;
        if (event.isCanceled() || !(event.getEntity() instanceof IFoodStatsCreature)) {
            return;
        }
        Entity entity = event.getEntity();
        creacher = (IFoodStatsCreature)event.getEntity();
        if(entity.isSprinting()) {
            creacher.addExhaustion(0.2F);
        } else {
            creacher.addExhaustion(0.05F);
        }
    }

    //endregion Public / Protected Methods
}
