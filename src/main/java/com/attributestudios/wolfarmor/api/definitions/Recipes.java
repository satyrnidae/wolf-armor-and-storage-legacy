package com.attributestudios.wolfarmor.api.definitions;

import com.attributestudios.wolfarmor.item.crafting.RecipeWolfArmorDyes;
import net.minecraft.item.crafting.IRecipe;

public final class Recipes {
    private Recipes() {}

    public static final IRecipe LEATHER_ARMOR_DYES = new RecipeWolfArmorDyes()
            .setRegistryName(Resources.RECIPE_LEATHER_ARMOR_DYES);
}
