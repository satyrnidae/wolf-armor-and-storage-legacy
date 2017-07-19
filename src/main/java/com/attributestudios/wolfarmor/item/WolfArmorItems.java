package com.attributestudios.wolfarmor.item;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.item.ItemWolfArmor.WolfArmorMaterial;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Defines and contains static references to all mod items
 */
public final class WolfArmorItems {
    //region Fields

    public static final ItemWolfArmor LEATHER_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.CLOTH).setUnlocalizedName("wolfarmor.leatherWolfArmor");
    public static final ItemWolfArmor CHAINMAIL_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.CHAINMAIL).setUnlocalizedName("wolfarmor.chainWolfArmor");
    public static final ItemWolfArmor IRON_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.IRON).setUnlocalizedName("wolfarmor.ironWolfArmor");
    public static final ItemWolfArmor GOLDEN_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.GOLD).setUnlocalizedName("wolfarmor.goldWolfArmor");
    public static final ItemWolfArmor DIAMOND_WOLF_ARMOR = (ItemWolfArmor) new ItemWolfArmor(WolfArmorMaterial.DIAMOND).setUnlocalizedName("wolfarmor.diamondWolfArmor");

    //endregion Fields

    //region Constructors

    /**
     * Cannot instantiate a utility class
     */
    private WolfArmorItems() {
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Initializes items
     */
    public static void init() {
        GameRegistry.register(LEATHER_WOLF_ARMOR.setRegistryName(WolfArmorMod.MOD_ID, "leather_wolf_armor"));
        GameRegistry.register(CHAINMAIL_WOLF_ARMOR.setRegistryName(WolfArmorMod.MOD_ID, "chain_wolf_armor"));
        GameRegistry.register(IRON_WOLF_ARMOR.setRegistryName(WolfArmorMod.MOD_ID, "iron_wolf_armor"));
        GameRegistry.register(GOLDEN_WOLF_ARMOR.setRegistryName(WolfArmorMod.MOD_ID, "gold_wolf_armor"));
        GameRegistry.register(DIAMOND_WOLF_ARMOR.setRegistryName(WolfArmorMod.MOD_ID, "diamond_wolf_armor"));
    }

    /**
     * Registers item models with the model loader
     *
     * @param item     The item
     * @param metadata The item metadata
     */
    @SuppressWarnings("SameParameterValue")
    @SideOnly(Side.CLIENT)
    public static void registerItemModel(@Nullable Item item, int metadata) {
        if (item != null) {
            ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    //endregion Public / Protected Methods
}
