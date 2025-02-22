package dev.satyrn.wolfarmor.mixins.entity;

import dev.satyrn.wolfarmor.api.entity.ExtendedTamableAnimal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin extends Animal implements OwnableEntity, ExtendedTamableAnimal {

    protected TamableAnimalMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Accessor("DATA_FLAGS_ID")
    public static @NotNull EntityDataAccessor<Byte> wolfarmor$getDATA_FLAGS_ID() {
        return null;
    }

    @Override
    public boolean wolfarmor$getFlag(int flagId) {
        return (this.getEntityData().get(wolfarmor$getDATA_FLAGS_ID()) & flagId) != 0;
    }

    @Override
    public void wolfarmor$setFlag(int flagId, boolean value) {
        byte flags = this.getEntityData().get(wolfarmor$getDATA_FLAGS_ID());
        if (value) {
            this.getEntityData().set(wolfarmor$getDATA_FLAGS_ID(), (byte) (flags | flagId));
        } else {
            this.getEntityData().set(wolfarmor$getDATA_FLAGS_ID(), (byte) (flags & ~flagId));
        }
    }
}
