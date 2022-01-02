package dev.satyrn.wolfarmor.api.item;

import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IWolfArmorMaterial {
    /**
     * The maximum attainable armor value with vanilla wolf armor types.
     */
    double MAX_VANILLA_ARMOR_VALUE = 20D;

    /**
     * Gets the name for this wolf armor material.
     *
     * @return The name of the wolf armor material.
     */
    @Nonnull
    String getName();

    @Nonnegative
    int getDurability();

    @Nonnegative
    double getDamageReductionAmount();

    @Nonnegative
    int getEnchantability();

    boolean getCanBeDyed();

    int getDefaultColor();

    @Nonnull
    SoundEvent getEquipSound();

    boolean getHasOverlay();

    @Nullable
    Item getRepairItem();

    @Nonnegative
    float getToughness();
}
