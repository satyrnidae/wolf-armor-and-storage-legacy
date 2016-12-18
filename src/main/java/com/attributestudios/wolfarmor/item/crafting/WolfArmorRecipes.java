package com.attributestudios.wolfarmor.item.crafting;

import com.attributestudios.wolfarmor.item.WolfArmorItems;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
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
                                'I', Items.leather,
                                'H', Items.leather_helmet,
                                'L', Items.leather_boots));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(WolfArmorItems.CHAINMAIL_WOLF_ARMOR,
                                "IHI",
                                "ILI",
                                "ILI",
                                'I', "ingotIron",
                                'H', Items.chainmail_helmet,
                                'L', Items.chainmail_boots));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(WolfArmorItems.IRON_WOLF_ARMOR,
                                "IHI",
                                "ILI",
                                "ILI",
                                'I', "ingotIron",
                                'H', Items.iron_helmet,
                                'L', Items.iron_boots));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(WolfArmorItems.GOLDEN_WOLF_ARMOR,
                                "IHI",
                                "ILI",
                                "ILI",
                                'I', "ingotGold",
                                'H', Items.golden_helmet,
                                'L', Items.golden_boots));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(WolfArmorItems.DIAMOND_WOLF_ARMOR,
                                "IHI",
                                "ILI",
                                "ILI",
                                'I', "gemDiamond",
                                'H', Items.diamond_helmet,
                                'L', Items.diamond_boots));

        GameRegistry.addRecipe(new RecipeWolfArmorDyes());
    }

    //endregion Public / Protected Methods
}
