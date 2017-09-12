package com.attributestudios.wolfarmor.api.definitions;

import com.attributestudios.wolfarmor.api.definitions.Resources;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;

public final class Items {
    private Items() {}

    public static final ItemWolfArmor LEATHER_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(ItemWolfArmor.WolfArmorMaterial.CLOTH)
                    .setUnlocalizedName("wolfarmor.leatherWolfArmor")
                    .setRegistryName(Resources.ITEM_LEATHER_WOLF_ARMOR);

    public static final ItemWolfArmor CHAINMAIL_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(ItemWolfArmor.WolfArmorMaterial.CHAINMAIL)
                    .setUnlocalizedName("wolfarmor.chainWolfArmor")
                    .setRegistryName(Resources.ITEM_CHAINMAIL_WOLF_ARMOR);

    public static final ItemWolfArmor IRON_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(ItemWolfArmor.WolfArmorMaterial.IRON)
                    .setUnlocalizedName("wolfarmor.ironWolfArmor")
                    .setRegistryName(Resources.ITEM_IRON_WOLF_ARMOR);

    public static final ItemWolfArmor GOLD_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(ItemWolfArmor.WolfArmorMaterial.GOLD)
                    .setUnlocalizedName("wolfarmor.goldWolfArmor")
                    .setRegistryName(Resources.ITEM_GOLD_WOLF_ARMOR);

    public static final ItemWolfArmor DIAMOND_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(ItemWolfArmor.WolfArmorMaterial.DIAMOND)
                    .setUnlocalizedName("wolfarmor.diamondWolfArmor")
                    .setRegistryName(Resources.ITEM_DIAMOND_WOLF_ARMOR);
}
