package com.attributestudios.wolfarmor.entity.ai;

import com.attributestudios.wolfarmor.WolfArmorMod;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Allows wolves to howl at the moon
 */
public class EntityAIWolfHowl extends EntityAIBase {
    private final EntityWolf entity;
    private final Random random;
    private int soundTimer;

    /**
     * Initializes a new wolf howl AI instance
     * @param entity The wolf entity.
     */
    public EntityAIWolfHowl(EntityWolf entity) {
        this.entity = entity;
        this.random = entity.getRNG();
    }

    /**
     * The AI will be allowed to execute if the following conditions are met:
     * - Howling wild wolves is enabled
     * - The wolf is not tamed
     * - The current moon phase is 0 (full moon)
     * - It is not daytime
     * @return <tt>true</tt> if the above conditions are met.
     */
    @Override
    public boolean shouldExecute() {
        if (!WolfArmorMod.getConfiguration().getAreHowlingUntamedWolvesEnabled()) {
            return false;
        }

        World entityWorld = entity.getEntityWorld();
        int phase = entityWorld.provider.getMoonPhase(entityWorld.getWorldTime());

        return !entity.isTamed() && phase == 0 && !entityWorld.isDaytime();
    }

    /**
     * Sets the time to the next wolf howl.
     */
    @Override
    public void startExecuting() {
        soundTimer = 300 + random.nextInt(600)  + random.nextInt(600) + random.nextInt(600) + random.nextInt(600);
    }

    /**
     * The task executes until the timer runs out, then resets.
     * @return <tt>true</tt> if the sound timer is greater than 0.
     */
    @Override
    public boolean shouldContinueExecuting() {

        World entityWorld = entity.getEntityWorld();
        int phase = entityWorld.provider.getMoonPhase(entityWorld.getWorldTime());

        if(entity.isTamed() || phase != 0 || entityWorld.isDaytime()) {
            return false;
        }

        return soundTimer > 0;
    }

    /**
     * While the sound timer is greater than zero, the task subtracts one from the value each tick
     */
    @Override
    public void updateTask() {
        if (soundTimer > 0) {
            soundTimer--;
        }
    }

    /**
     * Plays the wolf howl sound.
     */
    @Override
    public void resetTask() {
        World entityWorld = entity.getEntityWorld();
        int phase = entityWorld.provider.getMoonPhase(entityWorld.getWorldTime());

        if (!entity.isTamed() && phase == 0 && !entityWorld.isDaytime()) {
            this.entity.playSound(SoundEvents.ENTITY_WOLF_HOWL, 5.0F, 0.75f + (random.nextFloat() * (entity.isChild() ? 1.25F : 0.25F)));
        }
    }
}
