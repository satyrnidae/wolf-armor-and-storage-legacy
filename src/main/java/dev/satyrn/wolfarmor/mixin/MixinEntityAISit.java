package dev.satyrn.wolfarmor.mixin;

import dev.satyrn.wolfarmor.api.entity.IFoodStatsCreature;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityTameable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Enables exhaustion for FoodStats creatures on sit / stand
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.6.0
 */
@Mixin(EntityAISit.class)
public class MixinEntityAISit {
    @Shadow private boolean isSitting;
    private IFoodStatsCreature creacher;

    /**
     * Alters the end of the EntityAISit constructor to set the food stat creature field.
     * @param entityIn The entity that the task is bound to, and potentially the IFoodStatsCreature
     * @param ci Callback info
     * @since 3.6.0
     */
    @Inject(method = "<init>", at=@At("TAIL"))
    private void onInit(EntityTameable entityIn, CallbackInfo ci) {
        if (entityIn instanceof IFoodStatsCreature) {
            this.creacher = (IFoodStatsCreature)entityIn;
        }
    }

    /**
     * Alters the start of the sitting event to apply exhaustion dependent on whether the entity was sitting or not.
     * @param sitting The new value for isSitting
     * @param ci Callback info
     * @since 3.6.0
     */
    @Inject(method = "setSitting", at=@At("HEAD"))
    private void onSetSitting(boolean sitting, CallbackInfo ci) {
        if (this.creacher != null && this.isSitting != sitting) {
            this.creacher.addExhaustion(0.01F * (this.isSitting ? 5F : 1F));
        }
    }
}
