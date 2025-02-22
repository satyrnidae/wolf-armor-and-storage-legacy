package dev.satyrn.wolfarmor.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class DyeableLeatherItemColor implements ItemColor {
    @Override
    public int getColor(ItemStack itemStack, int i) {
        return i > 0 ? 0xFFFFFFFF :((DyeableLeatherItem) itemStack.getItem()).getColor(itemStack);
    }
}
