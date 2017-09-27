package com.attributestudios.wolfarmor.entity.ai;

import com.attributestudios.wolfarmor.WolfArmorMod;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;


public class EntityAIWolfHowl extends EntityAIBase {
    private final EntityWolf entity;
    private final Random random;
    private int soundTimer;

    public EntityAIWolfHowl(EntityWolf entity) {
        this.entity = entity;
        this.random = entity.getRNG();
        soundTimer = random.nextInt(600) + random.nextInt(120);
    }

    @Override
    public boolean shouldExecute() {
        if (!WolfArmorMod.getConfiguration().getAreHowlingUntamedWolvesEnabled()) {
            return false;
        }

        World entityWorld = entity.getEntityWorld();
        int phase = entityWorld.getMoonPhase();

        return !entity.isTamed() && phase == 0 && !entityWorld.isDaytime();
    }

    @Override
    public void startExecuting() {
        this.entity.playSound(SoundEvents.ENTITY_WOLF_HOWL, 1, 0.75f + random.nextFloat() * (entity.isChild() ? 2 : 1));
    }

    @Override
    public boolean shouldContinueExecuting() {
        return soundTimer > 0;
    }

    @Override
    public void updateTask() {
        if (soundTimer > 0) {
            soundTimer--;
        }
    }

    public void resetTask() {
        soundTimer = 600 + random.nextInt(600);
    }
}
