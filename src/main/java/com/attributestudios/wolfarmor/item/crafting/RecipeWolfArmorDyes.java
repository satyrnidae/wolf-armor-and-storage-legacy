package com.attributestudios.wolfarmor.item.crafting;

import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Wolf Armor dye recipes
 */
public class RecipeWolfArmorDyes extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
        @Nonnull ItemStack armorItemStack = ItemStack.EMPTY;
        @Nonnull final List<ItemStack> dyes = Lists.newArrayList();

        for(int slotIndex = 0; slotIndex < inv.getSizeInventory(); ++slotIndex) {
            @Nonnull ItemStack stackInSlot = inv.getStackInSlot(slotIndex);
            if(stackInSlot.isEmpty()) {
               continue;
            }
            if(stackInSlot.getItem() instanceof ItemWolfArmor) {
                @Nullable ItemWolfArmor armorItem = (ItemWolfArmor)stackInSlot.getItem();
                if(!armorItem.getMaterial().getCanBeDyed() || !armorItemStack.isEmpty()) {
                    return false;
                }
                armorItemStack = stackInSlot;
                continue;
            }

            if(!OreDictHelper.isValidDye(stackInSlot)) {
                return false;
            }
            dyes.add(stackInSlot);
        }

        return !armorItemStack.isEmpty() && !dyes.isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        @Nonnull ItemStack result = ItemStack.EMPTY;
        int[] rgbColor = new int[3];
        int colorMultiplier = 0;
        int colorCount = 0;
        @Nullable ItemWolfArmor armorItem = null;

        for(int slotIndex = 0; slotIndex < inv.getSizeInventory(); ++slotIndex) {
            @Nonnull ItemStack stackInSlot = inv.getStackInSlot(slotIndex);
            if(stackInSlot.isEmpty()) {
                continue;
            }

            // Get current armor color and mult into rgbColor array
            if(stackInSlot.getItem() instanceof ItemWolfArmor) {
                armorItem = (ItemWolfArmor)stackInSlot.getItem();
                if(!armorItem.getMaterial().getCanBeDyed() || !result.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                result = stackInSlot.copy();
                result.setCount(1);

                if(armorItem.getHasColor(result)) {
                    int armorColor = armorItem.getColor(result);
                    float rArmor = (float)(armorColor >> 16 & 0xff) / 255.0F;
                    float gArmor = (float)(armorColor >> 8 & 0xff) / 255.0F;
                    float bArmor = (float)(armorColor & 0xff) / 255.0F;
                    colorMultiplier = (int)((float)colorMultiplier + Math.max(rArmor, Math.max(gArmor, bArmor)) * 255.0F);
                    rgbColor[0] = (int)((float)rgbColor[0] + rArmor * 255.0F);
                    rgbColor[1] = (int)((float)rgbColor[1] + gArmor * 255.0F);
                    rgbColor[2] = (int)((float)rgbColor[2] + bArmor * 255.0F);
                    ++colorCount;
                }
                continue;
            }

            Optional<EnumDyeColor> dyeColorOptional = OreDictHelper.getColorFromStack(stackInSlot);
            if(!dyeColorOptional.isPresent()) {
                return ItemStack.EMPTY;
            }

            float[] dyeColorMultipliers = dyeColorOptional.get().getColorComponentValues();
            int rDye = (int)(dyeColorMultipliers[0] * 255.0F);
            int gDye = (int)(dyeColorMultipliers[1] * 255.0F);
            int bDye = (int)(dyeColorMultipliers[2] * 255.0F);
            colorMultiplier += Math.max(rDye, Math.max(gDye, bDye));
            rgbColor[0] += rDye;
            rgbColor[1] += gDye;
            rgbColor[2] += bDye;
            ++colorCount;
        }
        if(armorItem == null) {
            return ItemStack.EMPTY;
        }

        int rAvg = rgbColor[0] / colorCount;
        int gAvg = rgbColor[1] / colorCount;
        int bAvg = rgbColor[2] / colorCount;
        float colorMultiplierAvg = (float)colorMultiplier / (float)colorCount;
        float maxColorValue = (float)Math.max(rAvg, Math.max(gAvg, bAvg));
        rAvg = (int)((float)rAvg * colorMultiplierAvg / maxColorValue);
        gAvg = (int)((float)gAvg * colorMultiplierAvg / maxColorValue);
        bAvg = (int)((float)bAvg * colorMultiplierAvg / maxColorValue);
        int finalColor = (((rAvg << 8) + gAvg) << 8) + bAvg;
        armorItem.setColor(result, finalColor);
        return result;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() { return ItemStack.EMPTY; }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    @Override
    public boolean isHidden() { return true; }

    @Override
    public boolean canFit(int width, int height) { return width * height >= 2; }
}