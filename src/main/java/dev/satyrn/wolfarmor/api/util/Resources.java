package dev.satyrn.wolfarmor.api.util;

import net.minecraft.util.ResourceLocation;

/**
 * Contains definitions for constant strings and resource locations
 */
public abstract class Resources {

    /**
     * The name of the mod.
     */
    public static final String MOD_NAME = "${modname}";

    /**
     * The mod ID of the mod.
     */
    public static final String MOD_ID = "${modid}";

    /**
     * Resource location for the wolf backpacks.
     */
    public static final ResourceLocation TEXTURE_WOLF_BACKPACK = new ResourceLocation(MOD_ID, "textures/models/wolf_pack.png");

    /**
     * Resource location for the dungeon chest injection loot table.
     */
    public static final ResourceLocation LOOT_TABLE_DUNGEON_CHEST_INJECT = new ResourceLocation(MOD_ID, "chests/inject/dungeon");

    /**
     * Resource location for the armor up achievement
     */
    public static final ResourceLocation ADVANCEMENT_ARMOR_UP = new ResourceLocation(MOD_ID, "husbandry/armor_up");

    /**
     * Resource location for the wolf pack achievement
     */
    public static final ResourceLocation ADVANCEMENT_WOLF_PACK = new ResourceLocation(MOD_ID, "husbandry/wolf_pack");

    /**
     * Resource location for the wolf armor capability
     */
    public static final ResourceLocation CAPABILITY_WOLF_ARMOR = new ResourceLocation(MOD_ID, "wolf_armor");

    /**
     * Resource location for the wolf armor equip criteria trigger
     */
    public static final ResourceLocation CRITERIA_TRIGGER_EQUIP_WOLF_ARMOR = new ResourceLocation(MOD_ID, "equip_wolf_armor");

    /**
     * Resource location for the wolf chest equip criteria trigger
     */
    public static final ResourceLocation CRITERIA_TRIGGER_EQUIP_WOLF_CHEST = new ResourceLocation(MOD_ID, "equip_wolf_backpack");

    /**
     * Resource location for the leather armor dyeing recipe.
     */
    public static final ResourceLocation RECIPE_LEATHER_ARMOR_DYES = new ResourceLocation(MOD_ID, "leather_wolf_armor_dyed");

    /**
     * Resource location for the diamond wolf armor item.
     */
    public static final ResourceLocation ITEM_DIAMOND_WOLF_ARMOR = new ResourceLocation(MOD_ID, "diamond_wolf_armor");

    /**
     * Resource location for the gold wolf armor item
     */
    public static final ResourceLocation ITEM_GOLD_WOLF_ARMOR = new ResourceLocation(MOD_ID, "gold_wolf_armor");

    /**
     * Resource location for the iron wolf armor item
     */
    public static final ResourceLocation ITEM_IRON_WOLF_ARMOR = new ResourceLocation(MOD_ID, "iron_wolf_armor");

    /**
     * Resource location for the chainmail wolf armor item
     */
    public static final ResourceLocation ITEM_CHAINMAIL_WOLF_ARMOR = new ResourceLocation(MOD_ID, "chainmail_wolf_armor");

    /**
     * Resource location for the leather wolf armor item
     */
    public static final ResourceLocation ITEM_LEATHER_WOLF_ARMOR = new ResourceLocation(MOD_ID, "leather_wolf_armor");

    /**
     * Resource location for the armored wolf entity
     * @deprecated Since API-1.0
     */
    @Deprecated
    public static final ResourceLocation ENTITY_WOLF_ARMORED_LOCATION = new ResourceLocation(MOD_ID, "wolf_armored");

}
