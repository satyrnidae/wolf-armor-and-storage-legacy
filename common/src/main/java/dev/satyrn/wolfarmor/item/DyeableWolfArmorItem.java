package dev.satyrn.wolfarmor.item;

import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;

/**
 * It's the same as {@link WolfArmorItem}, but dyeable.
 */
public class DyeableWolfArmorItem extends WolfArmorItem implements DyeableLeatherItem {
    /**
     * Creates a new dyeable wolf armor item with the properties of the specified armor material.
     *
     * @param material   The armor material. This determines armor enchantability, knockback/damage resistance, toughness,
     *                   etc.
     * @param properties The item properties.
     */
    public DyeableWolfArmorItem(ArmorMaterial material, Properties properties) {
        super(material, properties);
    }
}
