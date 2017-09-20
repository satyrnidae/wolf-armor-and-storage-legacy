package com.attributestudios.wolfarmor.item.crafting;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Helper class to assist with OreDict defined dyes
 */
abstract class OreDictHelper {
    private static final String[] DYE_ORE_NAMES;

    /**
     * Convert EnumDyeColor unlocalized names to oredict names
     */
    static {
        DYE_ORE_NAMES = Arrays.stream(EnumDyeColor.values())
            .map(dyeColor -> String.format("dye%s%s", dyeColor.getUnlocalizedName().substring(0,1), dyeColor.getUnlocalizedName().substring(1)))
            .toArray(String[]::new);
    }

    /**
     * Returns true if the color index is greater than or equal to zero
     * @param stack The stack to check
     */
    public static boolean isValidDye(@Nonnull ItemStack stack) {
        return getColorIndexFromStack(stack) >= 0;
    }

    /**
     * Returns the index of the dye name in the DYE_ORE_NAMES list
     */
    private static int getColorIndexFromStack(@Nonnull ItemStack stack) {
        if(stack.isEmpty()) return -1;
        return Arrays.stream(OreDictionary.getOreIDs(stack))
            .mapToObj(OreDictionary::getOreName)
            .mapToInt(oreName -> ArrayUtils.indexOf(DYE_ORE_NAMES, oreName))
            .filter(id -> id >= 0)
            .findFirst()
            .orElse(-1);
    }

    /**
     * Gets the enum color from the stack
     */
    @Nonnull
    public static Optional<EnumDyeColor> getColorFromStack(@Nonnull ItemStack stack) {
        return isValidDye(stack) ? 
            Optional.of(EnumDyeColor.byMetadata(stack.getMetadata())) : 
            Optional.empty();
    }
}