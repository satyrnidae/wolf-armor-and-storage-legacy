package dev.satyrn.wolfarmor.entity.ai;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.config.WolfArmorConfig;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;

/**
 * Allows a wolf to path to and eat items
 * @author Isabel Maskrey
 * @since 3.6.0
 */
public class EntityAIWolfEatMeatItem extends EntityAIBase {
    private final EntityWolf entity;
    private final IArmoredWolf armoredWolf;
    private final WolfArmorConfig config;

    public EntityAIWolfEatMeatItem(EntityWolf entity) {
        this.entity = entity;
        this.armoredWolf = (IArmoredWolf)entity;
        this.config = WolfArmorMod.getConfig();
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     * @since 3.6.0
     */
    @Override
    public boolean shouldExecute() {
        return !this.entity.getIsInvulnerable()
                && this.entity.hurtTime == 0
                && this.config.getChestEnabled()
                && this.config.getAutoHealEnabled()
                && this.armoredWolf.getHasChest();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     * @since 3.6.0
     */
    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
