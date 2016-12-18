package com.attributestudios.wolfarmor.item;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.item.ItemWolfArmor.WolfArmorMaterial;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Defines and contains static references to all mod items
 */
public final class WolfArmorItems {
    //region Fields

    public static final ItemWolfArmor LEATHER_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.CLOTH).setUnlocalizedName(WolfArmorMod.MOD_ID + ".leatherWolfArmor").setTextureName(WolfArmorMod.MOD_ID + ":leather_wolf_armor");
    public static final ItemWolfArmor CHAINMAIL_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.CHAINMAIL).setUnlocalizedName(WolfArmorMod.MOD_ID + ".chainWolfArmor").setTextureName(WolfArmorMod.MOD_ID + ":chain_wolf_armor");
    public static final ItemWolfArmor IRON_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.IRON).setUnlocalizedName(WolfArmorMod.MOD_ID + ".ironWolfArmor").setTextureName(WolfArmorMod.MOD_ID + ":iron_wolf_armor");
    public static final ItemWolfArmor GOLDEN_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.GOLD).setUnlocalizedName(WolfArmorMod.MOD_ID + ".goldWolfArmor").setTextureName(WolfArmorMod.MOD_ID + ":gold_wolf_armor");
    public static final ItemWolfArmor DIAMOND_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.DIAMOND).setUnlocalizedName(WolfArmorMod.MOD_ID + ".diamondWolfArmor").setTextureName(WolfArmorMod.MOD_ID + ":diamond_wolf_armor");

    //endregion Fields

    //region Constructors

    /**
     * Cannot instantiate a utility class
     */
    private WolfArmorItems() {}

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Initializes items
     */
    public static void init() {
        GameRegistry.registerItem(LEATHER_WOLF_ARMOR, "leather_wolf_armor", WolfArmorMod.MOD_ID);
        GameRegistry.registerItem(CHAINMAIL_WOLF_ARMOR, "chain_wolf_armor", WolfArmorMod.MOD_ID);
        GameRegistry.registerItem(IRON_WOLF_ARMOR, "iron_wolf_armor", WolfArmorMod.MOD_ID);
        GameRegistry.registerItem(GOLDEN_WOLF_ARMOR, "gold_wolf_armor", WolfArmorMod.MOD_ID);
        GameRegistry.registerItem(DIAMOND_WOLF_ARMOR, "diamond_wolf_armor", WolfArmorMod.MOD_ID);
    }

    //endregion Public / Protected Methods
}
