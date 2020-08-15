package dev.satyrn.wolfarmor.mixin;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(EntityAnimal.class)
public abstract class MixinEntityAnimal extends MixinEntityAgeable {
    @Shadow public abstract void setInLove(@Nullable EntityPlayer player);

    @Shadow protected abstract void consumeItemFromStack(EntityPlayer player, ItemStack stack);

    @Shadow public abstract boolean isInLove();

    /**
     * Shadows whether or not the item stack can be used to breed the entity
     * @param stack The item stack to check
     * @return <c>true</c> if the item can be used to breed the entity
     * @since 1.1.0
     */
    @Shadow public abstract boolean isBreedingItem(ItemStack stack);
}
