package com.attributestudios.wolfarmor.item.crafting;

import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Wolf Armor dye recipes
 */
@SuppressWarnings("WeakerAccess")
public class RecipeWolfArmorDyes implements IRecipe {
    //region Public / Protected Methods

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     * @param inventoryCrafting The crafting grid
     * @param world The world
     */
    @Override
    public boolean matches(@Nonnull InventoryCrafting inventoryCrafting, @Nonnull World world) {
        ItemStack armorItem = null;
        ArrayList<ItemStack> dyes = new ArrayList<ItemStack>();

        for(int slotIndex = 0; slotIndex < inventoryCrafting.getSizeInventory(); slotIndex++) {
            ItemStack stackInSlot = inventoryCrafting.getStackInSlot(slotIndex);

            if(stackInSlot != null) {
                if(stackInSlot.getItem() instanceof ItemWolfArmor) {
                    ItemWolfArmor wolfArmorItem = (ItemWolfArmor)stackInSlot.getItem();

                    if(!wolfArmorItem.getMaterial().getIsDyeable() || armorItem != null) {
                        return false;
                    }

                    armorItem = stackInSlot;
                }
                else {
                    if(getDyeEquivalent(stackInSlot) < 0) {
                        return false;
                    }

                    dyes.add(stackInSlot);
                }
            }
        }

        return armorItem != null && !dyes.isEmpty();
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Whether or not a given stack has an equivalent dye entry
     * @param stack The dye item stack
     * @return The quivalent dye metadata value for the given item stack
     */
    private int getDyeEquivalent(@Nullable ItemStack stack) {
        int dyeEquivalent = -1;

        if(stack != null) {
            int[] oreIds = OreDictionary.getOreIDs(stack);

            for (int oreId : oreIds) {
                String oreName = OreDictionary.getOreName(oreId);

                List<ItemStack> ores = OreDictionary.getOres(oreName);

                for (ItemStack oreStack : ores) {
                    if (oreStack.getItem() == Items.DYE && oreStack.getItemDamage() != Short.MAX_VALUE) { // a dye but not just any dye
                        dyeEquivalent = oreStack.getItemDamage();
                        break;
                    }
                }
            }
        }

        return dyeEquivalent;
    }

    //endregion Private Methods

    //region Accessors / Mutators

    /**
     * Returns an Item that is the result of this recipe
     *
     * @param inventoryCrafting The crafting grid
     */
    @Override
    @Nullable
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventoryCrafting) {
        ItemStack stack = null;
        int[] color = new int[3];
        int rgb;
        int rgbMax = 0;
        int count = 0;

        ItemWolfArmor itemArmor = null;

        for(int slotIndex = 0; slotIndex < inventoryCrafting.getSizeInventory(); slotIndex++) {
            ItemStack stackInSlot = inventoryCrafting.getStackInSlot(slotIndex);

            if(stackInSlot != null) {
                if(stackInSlot.getItem() instanceof ItemWolfArmor) {
                    itemArmor = (ItemWolfArmor)stackInSlot.getItem();

                    if(!itemArmor.getMaterial().getIsDyeable() || stack != null) {
                        return null;
                    }

                    stack = stackInSlot.copy();
                    stack.stackSize = 1;

                    if(itemArmor.getHasColor(stackInSlot)) {
                        rgb = itemArmor.getColor(stackInSlot);
                        float[] existingColor = new float[]{
                            (rgb >> 16 & 0xFF) / 255F,
                            (rgb >> 8 & 0xFF) / 255F,
                            (rgb & 0xFF)     / 255F
                        };
                        rgbMax = (int)(rgbMax + Math.max(existingColor[0], Math.max(existingColor[1], existingColor[2])) * 255F);
                        color[0] = (int)(color[0] + existingColor[0] * 255F);
                        color[1] = (int)(color[1] + existingColor[1] * 255F);
                        color[2] = (int)(color[2] + existingColor[2] * 255F);
                        count++;
                    }
                }
                else {
                    int dyeEquivalent = getDyeEquivalent(stackInSlot);

                    if(dyeEquivalent < 0) {
                        return null;
                    }

                    float[] fleeceColor = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(dyeEquivalent));
                    int[] dye = new int[] {
                        (int)(fleeceColor[0] * 255),
                        (int)(fleeceColor[1] * 255),
                        (int)(fleeceColor[2] * 255)
                    };
                    rgbMax += Math.max(dye[0], Math.max(dye[1], dye[2]));
                    color[0] += dye[0];
                    color[1] += dye[1];
                    color[2] += dye[2];
                    count++;
                }
            }
        }

        if(itemArmor == null) {
            return null;
        }

        int redAvg = color[0] / count;
        int greenAvg = color[1] / count;
        int blueAvg = color[2] / count;
        float rgbAvg = rgbMax / (float)count;
        float rgbAvgMax = Math.max(redAvg, Math.max(greenAvg, blueAvg));
        int red = (int)(redAvg * rgbAvg / rgbAvgMax);
        int green = (int)(greenAvg * rgbAvg / rgbAvgMax);
        int blue = (int)(blueAvg * rgbAvg / rgbAvgMax);

        rgb = (((red << 8) + green) << 8) + blue;
        itemArmor.setColor(stack, rgb);

        return stack;
    }

    /**
     * Returns the size of the recipe area
     */
    @Override
    @Nonnegative
    public int getRecipeSize() {
        return 10;
    }

    /**
     * Gets the recipe output
     * @return nothing
     */
    @Override
    @Nullable
    public ItemStack getRecipeOutput() {
        return null;
    }

    /**
     * Gets the items remaining in the crafting table
     * @param inv The inventory
     * @return The remaining items
     */
    @Override
    @Nonnull
    public ItemStack[] getRemainingItems(@Nonnull InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    //endregion Accessors / Mutators
}
