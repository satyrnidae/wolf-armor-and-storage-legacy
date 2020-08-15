package dev.satyrn.wolfarmor.common.event;

import dev.satyrn.wolfarmor.api.entity.IFoodStatsCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles events for potions
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.6.0
 */
public class PotionEventHandler {

    /**
     * Handles the pre-effect event
     * @param event The pre-potion effect event
     * @since 3.6.0
     */
    @SubscribeEvent
    public void onPotionEffectPreEvent(PotionEffectEvent event) {
        IFoodStatsCreature creacher;
        if (event.getEntity() instanceof IFoodStatsCreature) {
            creacher = (IFoodStatsCreature) event.getEntity();
            EntityLivingBase entity = event.getEntity();

            if (event.getPotion() == MobEffects.HUNGER) {
                creacher.addExhaustion(0.005F * (event.getAmplifier() + 1F));
                event.setCanceled(true);
            } else if (event.getPotion() == MobEffects.SATURATION) {
                if (!entity.getEntityWorld().isRemote) {
                    creacher.getFoodStats().addStats(event.getAmplifier() + 1, 1.0F);
                }
                event.setCanceled(true);
            }
        }
    }
}
