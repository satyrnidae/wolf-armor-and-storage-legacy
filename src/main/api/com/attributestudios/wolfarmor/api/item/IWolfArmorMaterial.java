package com.attributestudios.wolfarmor.api.item;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

public interface IWolfArmorMaterial {
    /**
     * Armor material for cloth wolf armor.
     */
    IWolfArmorMaterial CLOTH = new Impl("leather", 80, 6D, 15, true, 0xA06540, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, true, Items.LEATHER, 0.0F);
    /**
     * Armor material for chainmail wolf armor.
     */
    IWolfArmorMaterial CHAINMAIL = new Impl("chainmail", 180, 12D, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, Items.IRON_NUGGET, 0.0F);
    /**
     * Armor material for iron wolf armor.
     */
    IWolfArmorMaterial IRON = new Impl("iron", 240, 15D, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, Items.IRON_INGOT, 0.0F);
    /**
     * Armor material for gold wolf armor.
     */
    IWolfArmorMaterial GOLD = new Impl("gold", 112, 12D, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, Items.GOLD_INGOT, 0.0F);
    /**
     * Armor material for diamond wolf armor.
     */
    IWolfArmorMaterial DIAMOND = new Impl("diamond", 528, 20D, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, Items.DIAMOND, 2.0F);
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

    @Immutable
    class Impl implements IWolfArmorMaterial {
        private final String name;
        private final int durability;
        private final double damageReductionAmount;
        private final int enchantability;
        private final boolean canBeDyed;
        private final int defaultColor;
        private final SoundEvent equipSound;
        private final boolean hasOverlay;
        private final Item repairItem;
        private final float toughness;

        Impl(@Nonnull String name,
             @Nonnegative int durability,
             @Nonnegative double damageReductionAmount,
             @Nonnegative int enchantability,
             @Nonnull SoundEvent equipSound,
             @Nullable Item repairItem,
             @Nonnegative float toughness) {
            this(name, durability, damageReductionAmount, enchantability, false, 0xFFFFFF, equipSound, false, repairItem, toughness);
        }

        Impl(@Nonnull String name,
             @Nonnegative int durability,
             @Nonnegative double damageReductionAmount,
             @Nonnegative int enchantability,
             boolean canBeDyed,
             int defaultColor,
             @Nonnull SoundEvent equipSound,
             boolean hasOverlay,
             @Nullable Item repairItem,
             @Nonnegative float toughness) {
            this.name = name;
            this.durability = durability;
            this.damageReductionAmount = damageReductionAmount;
            this.enchantability = enchantability;
            this.canBeDyed = canBeDyed;
            this.defaultColor = defaultColor;
            this.equipSound = equipSound;
            this.hasOverlay = hasOverlay;
            this.repairItem = repairItem;
            this.toughness = toughness;
        }

        @Override
        @Nonnull
        public String getName() {
            return this.name;
        }

        @Override
        @Nonnegative
        public int getDurability() {
            return this.durability;
        }

        @Override
        @Nonnegative
        public double getDamageReductionAmount() {
            return this.damageReductionAmount;
        }

        @Override
        @Nonnegative
        public int getEnchantability() {
            return this.enchantability;
        }

        @Override
        public boolean getCanBeDyed() {
            return this.canBeDyed;
        }

        @Override
        public int getDefaultColor() {
            return this.defaultColor;
        }

        @Override
        @Nonnull
        public SoundEvent getEquipSound() {
            return this.equipSound;
        }

        @Override
        public boolean getHasOverlay() {
            return this.hasOverlay;
        }

        @Override
        @Nullable
        public Item getRepairItem() {
            return this.repairItem;
        }

        @Override
        @Nonnegative
        public float getToughness() {
            return this.toughness;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Impl impl = (Impl) o;

            return durability == impl.durability && Double.compare(impl.damageReductionAmount, damageReductionAmount) == 0 && enchantability == impl.enchantability && canBeDyed == impl.canBeDyed && defaultColor == impl.defaultColor && hasOverlay == impl.hasOverlay && Float.compare(impl.toughness, toughness) == 0 && name.equals(impl.name) && equipSound.equals(impl.equipSound) && (repairItem != null ? repairItem.equals(impl.repairItem) : impl.repairItem == null);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = name.hashCode();
            result = 31 * result + durability;
            temp = Double.doubleToLongBits(damageReductionAmount);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + enchantability;
            result = 31 * result + (canBeDyed ? 1 : 0);
            result = 31 * result + defaultColor;
            result = 31 * result + equipSound.hashCode();
            result = 31 * result + (hasOverlay ? 1 : 0);
            result = 31 * result + (repairItem != null ? repairItem.hashCode() : 0);
            result = 31 * result + (toughness != +0.0f ? Float.floatToIntBits(toughness) : 0);
            return result;
        }
    }
}
