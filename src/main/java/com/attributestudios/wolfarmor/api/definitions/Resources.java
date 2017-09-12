package com.attributestudios.wolfarmor.api.definitions;

import com.attributestudios.wolfarmor.WolfArmorMod;
import net.minecraft.util.ResourceLocation;

public final class Resources {
    private Resources() {}

    @Deprecated
    public static final ResourceLocation ENTITY_WOLF_ARMORED =
            new ResourceLocation(WolfArmorMod.MOD_ID, "wolf_armored");

    public static final ResourceLocation ITEM_LEATHER_WOLF_ARMOR =
            new ResourceLocation(WolfArmorMod.MOD_ID, "leather_wolf_armor");
    public static final ResourceLocation ITEM_CHAINMAIL_WOLF_ARMOR =
            new ResourceLocation(WolfArmorMod.MOD_ID, "chainmail_wolf_armor");
    public static final ResourceLocation ITEM_IRON_WOLF_ARMOR =
            new ResourceLocation(WolfArmorMod.MOD_ID, "iron_wolf_armor");
    public static final ResourceLocation ITEM_GOLD_WOLF_ARMOR =
            new ResourceLocation(WolfArmorMod.MOD_ID, "gold_wolf_armor");
    public static final ResourceLocation ITEM_DIAMOND_WOLF_ARMOR =
            new ResourceLocation(WolfArmorMod.MOD_ID, "diamond_wolf_armor");

    public static final ResourceLocation RECIPE_LEATHER_ARMOR_DYES =
            new ResourceLocation(WolfArmorMod.MOD_ID, "leather_wolf_armor_dyed");

    public static final ResourceLocation TRIGGER_EQUIP_WOLF_ARMOR =
            new ResourceLocation(WolfArmorMod.MOD_ID, "equip_wolf_armor");

    public static final ResourceLocation CAPABILITY_WOLF_ARMOR =
            new ResourceLocation(WolfArmorMod.MOD_ID, "wolf_armor");
}
