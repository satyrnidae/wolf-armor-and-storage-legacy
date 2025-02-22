package dev.satyrn.wolfarmor.api.food;

import dev.satyrn.wolfarmor.WolfArmorCommon;
import dev.satyrn.wolfarmor.api.entity.ExtendedLivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EntityFoodData {
    private int foodLevel = 20;
    private float saturationLevel = 5.0F;
    private float exhaustionLevel;
    private int tickTimer;
    private int lastFoodLevel = 20;

    // Food damage and starvation are difficulty-dependent now
    public void eat(final int level, final float saturation) {
        this.foodLevel = Math.min(level + this.foodLevel, 20);
        this.saturationLevel = Math.min(this.saturationLevel + (level * saturation * 2F), this.foodLevel);
    }

    public void eat(final @NotNull Item item, final @NotNull ItemStack stack) {
        if (item.isEdible()) {
            final @NotNull var foodProperties = Objects.requireNonNull(item.getFoodProperties());
            this.eat(foodProperties.getNutrition(), foodProperties.getSaturationModifier());
        }
    }

    public void tick(final @NotNull LivingEntity entity) {
        final @NotNull var difficulty = entity.getLevel().getDifficulty();
        this.lastFoodLevel = this.foodLevel;
        if (this.exhaustionLevel > 4F) {
            this.exhaustionLevel -= 4F;
            if (this.saturationLevel > 0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1F, 0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        final boolean isRegenEnabled = entity.getLevel().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        final boolean isEntityStarvationEnabled = entity.getLevel().getGameRules().getBoolean(WolfArmorCommon.RULE_ENTITY_STARVATION);
        if (isRegenEnabled && this.saturationLevel > 0F && ((ExtendedLivingEntity)entity).wolfarmor$isHurt() && this.foodLevel >= 20) {
            this.tickTimer++;
            if (this.tickTimer >= 10) {
                float healAmount = Math.min(this.saturationLevel, 6F);
                entity.heal(healAmount / 6F);
                this.addExhaustion(healAmount);
                this.tickTimer = 0;
            }
        } else if (isRegenEnabled && this.foodLevel >= 18 && ((ExtendedLivingEntity)entity).wolfarmor$isHurt()) {
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                entity.heal(1F);
                this.addExhaustion(6F);
                this.tickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                if (isEntityStarvationEnabled && (entity.getHealth() > 10F || difficulty == Difficulty.HARD || entity.getHealth() > 1F && difficulty == Difficulty.NORMAL)) {
                    entity.hurt(DamageSource.STARVE, 1.0F);
                }

                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }
    }

    public void readAdditionalSaveData(final @NotNull CompoundTag compoundTag) {
        if (compoundTag.contains("foodLevel", 99)) {
            this.foodLevel = compoundTag.getInt("foodLevel");
            this.tickTimer = compoundTag.getInt("foodTickTimer");
            this.saturationLevel = compoundTag.getFloat("foodSaturationLevel");
            this.exhaustionLevel = compoundTag.getFloat("foodExhaustionLevel");
        }
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("foodLevel", this.foodLevel);
        compoundTag.putInt("foodTickTimer", this.tickTimer);
        compoundTag.putFloat("foodSaturationLevel", this.saturationLevel);
        compoundTag.putFloat("foodExhaustionLevel", this.exhaustionLevel);
    }

    /**
     * Gets the current food level.
     * @return The food level.
     */
    public int getFoodLevel() {
        return this.foodLevel;
    }

    public int getLastFoodLevel() {
        return this.lastFoodLevel;
    }

    public boolean needsFood() {
        return this.foodLevel < 20;
    }

    public void addExhaustion(float exhaustion) {
        this.exhaustionLevel = Math.min(this.exhaustionLevel + exhaustion, 40F);
    }

    public float getExhaustionLevel() {
        return this.exhaustionLevel;
    }

    public float getSaturationLevel() {
        return this.saturationLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public void setSaturation(float saturationLevel) {
        this.saturationLevel = saturationLevel;
    }

    public void setExhaustion(float exhaustionLevel) {
        this.exhaustionLevel = exhaustionLevel;
    }
}
