package dev.satyrn.wolfarmor.fabriclike.client;

import dev.satyrn.wolfarmor.client.DyeableLeatherItemColor;
import dev.satyrn.wolfarmor.item.WolfArmorItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

@Environment(EnvType.CLIENT)
public final class WolfArmorFabricLikeClient {
    public static void init() {
        ColorProviderRegistry.ITEM.register(new DyeableLeatherItemColor(), WolfArmorItems.LEATHER_WOLF_ARMOR.get());
    }
}
