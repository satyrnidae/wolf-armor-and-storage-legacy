package dev.satyrn.wolfarmor.mixins.entity;

import dev.satyrn.wolfarmor.api.entity.ExtendedLivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ExtendedLivingEntity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract float getMaxHealth();

    @Override
    public boolean wolfarmor$isHurt() {
        return this.getHealth() > 0F && this.getHealth() < this.getMaxHealth();
    }
}
