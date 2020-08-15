package dev.satyrn.wolfarmor.mixin;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.entity.IFoodStatsCreature;
import dev.satyrn.wolfarmor.common.event.PotionEffectEvent;
import dev.satyrn.wolfarmor.util.WolfFoodStatsLevel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to add a Forge event when a potion attempts to apply its effect to an entity
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.6.0
 */
@Mixin(Potion.class)
public class MixinPotion {
    /**
     * Adds a call to the beginning of the method which fires a cancellable PotionEffectEvent event
     * @param entityLivingBaseIn The targeted entity
     * @param amplifier The effect amplifier
     * @param ci Cancellable callback info
     * @since 3.6.0
     */
    @Inject(method="performEffect", at=@At("HEAD"), cancellable = true)
    private void beforePerformEffect(EntityLivingBase entityLivingBaseIn, int amplifier, CallbackInfo ci) {
        if (!MinecraftForge.EVENT_BUS.post(new PotionEffectEvent((Potion)(Object)this, entityLivingBaseIn, amplifier))) {
            ci.cancel();
        }
    }
}
