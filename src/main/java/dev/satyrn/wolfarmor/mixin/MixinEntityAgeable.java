package dev.satyrn.wolfarmor.mixin;

import net.minecraft.entity.EntityAgeable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAgeable.class)
public abstract class MixinEntityAgeable extends MixinEntityCreature {
    @Shadow public abstract void ageUp(int growthSeconds, boolean updateForcedAge);

    @Shadow public abstract int getGrowingAge();

    @Shadow public abstract boolean isChild();
}
