package dev.satyrn.wolfarmor.api.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

import javax.annotation.Nonnull;

public class CreatureFoodStats {
    private int foodLevel = 20;
    private float foodSaturationLevel = 5.0F;
    private float foodExhaustionLevel;
    private int foodTimer;

    private final float minimumHealth;
    private final boolean damageEntity;

    /**
     * Creates a default CreatureFoodStats which does not damage the entity.
     */
    public CreatureFoodStats() { this(false); }

    /**
     * Creates a CreatureFoodStats with a minimum health value of 0.
     * @param damageEntity Whether or not damage should be applied to the entity when it is hungry
     */
    public CreatureFoodStats(boolean damageEntity) { this (0, damageEntity); }

    /**
     * Creates a new CreatureFoodStats object.
     * @param minimumHealth The minimum health that the creature can reach from starvation. 0 will kill the entity.
     * @param damageEntity Whether or not damage should be applied to the entity when it is hungry.
     */
    public CreatureFoodStats(float minimumHealth, boolean damageEntity) {
        this.minimumHealth = Math.max(minimumHealth, 0);
        this.damageEntity = damageEntity;
    }

    public int getFoodLevel() { return this.foodLevel; }

    public boolean needFood() { return this.foodLevel < 20; }

    public float getSaturationLevel() { return this.foodSaturationLevel; }

    public void setFoodLevel(int foodLevel) { this.foodLevel = foodLevel; }

    public void setSaturationLevel(float foodSaturationLevel) { this.foodSaturationLevel = foodSaturationLevel; }

    public void addStats(int level, float saturation) {
        this.foodLevel = Math.min(level + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)level * saturation * 2F, (float)this.foodLevel);
    }

    public void addStats(@Nonnull ItemFood food, ItemStack stack) {
        this.addStats(food.getHealAmount(stack), food.getSaturationModifier(stack));
    }

    public void addExhaustion(float exhaustion) {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + exhaustion, 40.0F);
    }

    public void onUpdate(EntityLivingBase entityLiving) {
        // Decrease food exhaustion, saturation, and level
        if (this.foodExhaustionLevel > 4F) {
            this.foodExhaustionLevel -= 4F;
            if (this.foodSaturationLevel > 0F) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1F, 0F);
            } else {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean naturalRegeneration = entityLiving.getEntityWorld().getGameRules().getBoolean("naturalRegeneration");
        boolean canHeal = (entityLiving.getHealth() > 0 && entityLiving.getHealth() < entityLiving.getMaxHealth());
        if (naturalRegeneration && canHeal && this.foodSaturationLevel > 0 && this.foodLevel >= 20) {
            ++this.foodTimer;
            if (this.foodTimer >= 10) {
                float saturation = Math.min(this.foodSaturationLevel, 6F);
                entityLiving.heal(saturation / 6F);
                this.addExhaustion(saturation);
                this.foodTimer = 0;
            }
        } else if (naturalRegeneration && canHeal && this.foodLevel >= 18) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                entityLiving.heal(1F);
                this.addExhaustion(6F);
                this.foodTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTimer;
            if (this.foodTimer > 80) {
                if (this.damageEntity && entityLiving.getHealth() > this.minimumHealth) {
                    entityLiving.attackEntityFrom(DamageSource.STARVE, 1F);
                }
                this.foodTimer = 0;
            }
        } else {
            this.foodTimer = 0;
        }
    }

    public void readNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound.hasKey("foodLevel", 99)) {
            this.foodLevel = nbtTagCompound.getInteger("foodLevel");
            this.foodTimer = nbtTagCompound.getInteger("foodTickTimer");
            this.foodSaturationLevel = nbtTagCompound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = nbtTagCompound.getFloat("foodExhaustionLevel");
        }
    }

    public void writeNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("foodLevel", this.foodLevel);
        nbtTagCompound.setInteger("foodTickTimer", this.foodTimer);
        nbtTagCompound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        nbtTagCompound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }
}
