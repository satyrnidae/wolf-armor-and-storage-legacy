package dev.satyrn.wolfarmor.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;

public abstract class DataHelper {

    public static DataParameter<Boolean> HAS_CHEST;

    public static DataParameter<ItemStack> ARMOR_ITEM;

    public static DataParameter<ItemStack> CHEST_TYPE;
}
