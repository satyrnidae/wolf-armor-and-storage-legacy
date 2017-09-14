package com.attributestudios.wolfarmor.item.crafting;

import com.attributestudios.wolfarmor.api.util.Definitions;
import net.minecraft.item.crafting.IRecipe;

public abstract class Recipes {
    public static final IRecipe LEATHER_ARMOR_DYES = new RecipeWolfArmorDyes()
            .setRegistryName(Definitions.ResourceLocations.Recipes.LEATHER_ARMOR_DYES);
}
