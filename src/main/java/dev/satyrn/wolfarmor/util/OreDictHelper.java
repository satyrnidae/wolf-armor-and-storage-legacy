package dev.satyrn.wolfarmor.util;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public abstract class OreDictHelper {

    public static boolean isOre(boolean strict, String oreName, ItemStack... inputs) {
        List<ItemStack> matchedOreItems = OreDictionary.getOres(oreName);
        ItemStack[] dictTargets = matchedOreItems.toArray(new ItemStack[0]);

        NonNullList<ItemStack> inputList = NonNullList.create();
        inputList.addAll(Lists.newArrayList(inputs));

        return OreDictionary.containsMatch(strict, inputList, dictTargets);
    }
}
