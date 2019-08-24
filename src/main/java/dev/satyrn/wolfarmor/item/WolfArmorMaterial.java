package dev.satyrn.wolfarmor.item;

import dev.satyrn.wolfarmor.api.item.IWolfArmorMaterial;
import dev.satyrn.wolfarmor.api.item.WolfArmorMaterials;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public class WolfArmorMaterial implements IWolfArmorMaterial {
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

    WolfArmorMaterial(@Nonnull String name,
                     @Nonnegative int durability,
                     @Nonnegative double damageReductionAmount,
                     @Nonnegative int enchantability,
                     @Nonnull SoundEvent equipSound,
                     @Nullable Item repairItem,
                     @Nonnegative float toughness) {
        this(name, durability, damageReductionAmount, enchantability, false, 0xFFFFFF, equipSound, false, repairItem, toughness);
    }

    WolfArmorMaterial(@Nonnull String name,
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

    public static void initializeMaterials() {
        WolfArmorMaterials.CLOTH = new WolfArmorMaterial("leather", 80, 6D, 15, true, 0xA06540, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, true, Items.LEATHER, 0.0F);
        WolfArmorMaterials.CHAINMAIL = new WolfArmorMaterial("chainmail", 180, 12D, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, Items.IRON_NUGGET, 0.0F);
        WolfArmorMaterials.IRON = new WolfArmorMaterial("iron", 240, 15D, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, Items.IRON_INGOT, 0.0F);
        WolfArmorMaterials.GOLD = new WolfArmorMaterial("gold", 112, 12D, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, Items.GOLD_INGOT, 0.0F);
        WolfArmorMaterials.DIAMOND = new WolfArmorMaterial("diamond", 528, 20D, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, Items.DIAMOND, 2.0F);
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

        WolfArmorMaterial implementation = (WolfArmorMaterial) o;

        return durability == implementation.durability && Double.compare(implementation.damageReductionAmount, damageReductionAmount) == 0 && enchantability == implementation.enchantability && canBeDyed == implementation.canBeDyed && defaultColor == implementation.defaultColor && hasOverlay == implementation.hasOverlay && Float.compare(implementation.toughness, toughness) == 0 && name.equals(implementation.name) && equipSound.equals(implementation.equipSound) && (repairItem != null ? repairItem.equals(implementation.repairItem) : implementation.repairItem == null);
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
