package com.attributestudios.wolfarmor.mixin.entity;

import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.common.DataHelper;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityWolf.class)
public abstract class MixinEntityWolf extends MixinEntityTameable implements IWolfArmorCapability {
    static {
        DataHelper.CHESTED = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
    }
}
