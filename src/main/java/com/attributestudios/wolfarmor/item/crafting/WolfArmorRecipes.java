package com.attributestudios.wolfarmor.item.crafting;

import com.attributestudios.wolfarmor.item.WolfArmorItems;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Handles recipes for wolf armor
 */
public class WolfArmorRecipes {
    //region Public / Protected Methods

    public static void init() {

        //TODO: Enable / disable crafting in configuration

        GameRegistry.addRecipe(
                new ShapedOreRecipe(WolfArmorItems.LEATHER_WOLF_ARMOR,
                        "IHI",
                        "ILI",
                        "ILI",
                        'I', "leather",
                        'H', Items.LEATHER_HELMET,
                        'L', Items.LEATHER_BOOTS));
        GameRegistry.addRecipe(
                new ShapedOreRecipe(WolfArmorItems.CHAINMAIL_WOLF_ARMOR,
                        "IHI",
                        "ILI",
                        "ILI",
                        'I', "ingotIron",
                        'H', Items.CHAINMAIL_HELMET,
                        'L', Items.CHAINMAIL_BOOTS));
        GameRegistry.addRecipe(
                new ShapedOreRecipe(WolfArmorItems.IRON_WOLF_ARMOR,
                        "IHI",
                        "ILI",
                        "ILI",
                        'I', "ingotIron",
                        'H', Items.IRON_HELMET,
                        'L', Items.IRON_BOOTS));
        GameRegistry.addRecipe(
                new ShapedOreRecipe(WolfArmorItems.GOLDEN_WOLF_ARMOR,
                        "IHI",
                        "ILI",
                        "ILI",
                        'I', "ingotGold",
                        'H', Items.GOLDEN_HELMET,
                        'L', Items.GOLDEN_BOOTS));
        GameRegistry.addRecipe(
                new ShapedOreRecipe(WolfArmorItems.DIAMOND_WOLF_ARMOR,
                        "IHI",
                        "ILI",
                        "ILI",
                        'I', "gemDiamond",
                        'H', Items.DIAMOND_HELMET,
                        'L', Items.DIAMOND_BOOTS));

        GameRegistry.addRecipe(new RecipeWolfArmorDyes());
    }

    //endregion Public / Protected Methods
}
