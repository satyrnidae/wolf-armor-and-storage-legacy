package dev.satyrn.wolfarmor.tags;

import dev.satyrn.wolfarmor.WolfArmorCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class WolfArmorTags {
    public static final TagKey<Item> WOLF_CHESTS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(WolfArmorCommon.MOD_ID, "items/wolf_chests"));

    private WolfArmorTags() {}
}
