package dev.satyrn.wolfarmor.api.entity;

import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ExtendedWolf extends ContainerListener, HasCustomInventoryScreen, ExtendedTamableAnimal, HungryEntity {
    int wolfarmor$getInventorySize();

    boolean wolfarmor$isArmor(final @NotNull ItemStack stack);

    boolean wolfarmor$hasInventoryChanged(final @NotNull Container container);

    boolean wolfarmor$getHasChest();

    void wolfarmor$setHasChest(boolean value);

    @NotNull ItemStack wolfarmor$getChestItem();

    void wolfarmor$setChestItem(final @NotNull ItemStack itemStack);

    @NotNull ItemStack wolfarmor$getArmor();

    void wolfarmor$setArmor(final @NotNull ItemStack itemStack);
}
