package com.attributestudios.wolfarmor.mixin.entity;

import com.attributestudios.wolfarmor.common.DataHelper;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityWolf.class)
public abstract class MixinEntityWolf extends MixinEntityTameable {
    static {
        DataHelper.CHESTED = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
    }

    @Inject(method="entityInit", at = @At("RETURN"))
    protected void onEntityInit(CallbackInfo ci) {
        this.dataManager.register(DataHelper.CHESTED, false);
    }

    @Override
    protected void damageArmor(float damage) {
        //TODO: Damage Armor
    }
}
