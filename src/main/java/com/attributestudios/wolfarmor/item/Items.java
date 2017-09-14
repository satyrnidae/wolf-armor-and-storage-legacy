package com.attributestudios.wolfarmor.item;

import com.attributestudios.wolfarmor.api.item.IWolfArmorMaterial;
import com.attributestudios.wolfarmor.api.util.Definitions;

public abstract class Items {
    public static final ItemWolfArmor LEATHER_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(IWolfArmorMaterial.CLOTH)
                    .setUnlocalizedName("wolfarmor.leatherWolfArmor")
                    .setRegistryName(Definitions.ResourceLocations.Items.LEATHER_WOLF_ARMOR);

    public static final ItemWolfArmor CHAINMAIL_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(IWolfArmorMaterial.CHAINMAIL)
                    .setUnlocalizedName("wolfarmor.chainWolfArmor")
                    .setRegistryName(Definitions.ResourceLocations.Items.CHAINMAIL_WOLF_ARMOR);

    public static final ItemWolfArmor IRON_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(IWolfArmorMaterial.IRON)
                    .setUnlocalizedName("wolfarmor.ironWolfArmor")
                    .setRegistryName(Definitions.ResourceLocations.Items.IRON_WOLF_ARMOR);

    public static final ItemWolfArmor GOLD_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(IWolfArmorMaterial.GOLD)
                    .setUnlocalizedName("wolfarmor.goldWolfArmor")
                    .setRegistryName(Definitions.ResourceLocations.Items.GOLD_WOLF_ARMOR);

    public static final ItemWolfArmor DIAMOND_WOLF_ARMOR =
            (ItemWolfArmor) new ItemWolfArmor(IWolfArmorMaterial.DIAMOND)
                    .setUnlocalizedName("wolfarmor.diamondWolfArmor")
                    .setRegistryName(Definitions.ResourceLocations.Items.DIAMOND_WOLF_ARMOR);
}
