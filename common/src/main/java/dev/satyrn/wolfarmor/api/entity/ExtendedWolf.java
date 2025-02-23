package dev.satyrn.wolfarmor.api.entity;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ExtendedWolf extends ExtendedTamableAnimal, HungryEntity {
    default int wolfarmor$getInventorySize() {
        throw new UnsupportedOperationException("Not Implemented");
    };

    default boolean wolfarmor$isArmor(final @NotNull ItemStack stack) {
        throw new UnsupportedOperationException("Not Implemented");
    };

    default boolean wolfarmor$hasInventoryChanged(final @NotNull Container container) {
        throw new UnsupportedOperationException("Not Implemented");
    };

    default boolean wolfarmor$getHasChest() {
        throw new UnsupportedOperationException("Not Implemented");
    };

    default void wolfarmor$setHasChest(boolean value) {
        throw new UnsupportedOperationException("Not Implemented");
    };

    default @NotNull ItemStack wolfarmor$getChestItem() {
        throw new UnsupportedOperationException("Not Implemented");
    };

    default void wolfarmor$setChestItem(final @NotNull ItemStack itemStack) {
        throw new UnsupportedOperationException("Not Implemented");
    };

    default @NotNull ItemStack wolfarmor$getArmor() {
        throw new UnsupportedOperationException("Not Implemented");
    };

    default boolean wolfarmor$equipArmor(final @NotNull ItemStack itemStack) {
        throw new UnsupportedOperationException("Not Implemented");
    };
}
