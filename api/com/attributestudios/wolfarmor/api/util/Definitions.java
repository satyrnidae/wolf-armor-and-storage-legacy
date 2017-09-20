package com.attributestudios.wolfarmor.api.util;

import com.attributestudios.wolfarmor.api.util.annotation.ApiHelper;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public abstract class Definitions {
    public static final String MOD_NAME = "Wolf Armor and Storage";
    public static final String MOD_ID = "wolfarmor";
    public static final String MOD_VERSION = "1.12.2-2.1.0-ALPHA";

    public static abstract class ResourceLocations {
        public static abstract class Entities {
            @Deprecated
            public static final ResourceLocation WOLF_ARMORED =
                    new ResourceLocation(MOD_ID, "wolf_armored");
        }

        public static abstract class Items {
            public static final ResourceLocation LEATHER_WOLF_ARMOR =
                    new ResourceLocation(MOD_ID, "leather_wolf_armor");
            public static final ResourceLocation CHAINMAIL_WOLF_ARMOR =
                    new ResourceLocation(MOD_ID, "chainmail_wolf_armor");
            public static final ResourceLocation IRON_WOLF_ARMOR =
                    new ResourceLocation(MOD_ID, "iron_wolf_armor");
            public static final ResourceLocation GOLD_WOLF_ARMOR =
                    new ResourceLocation(MOD_ID, "gold_wolf_armor");
            public static final ResourceLocation DIAMOND_WOLF_ARMOR =
                    new ResourceLocation(MOD_ID, "diamond_wolf_armor");
        }

        public static abstract class Recipes {
            public static final ResourceLocation LEATHER_ARMOR_DYES =
                    new ResourceLocation(MOD_ID, "leather_wolf_armor_dyed");
        }

        public static abstract class CriteriaTriggers {
            public static final ResourceLocation EQUIP_WOLF_ARMOR =
                    new ResourceLocation(MOD_ID, "equip_wolf_armor");
        }

        public static abstract class Capabilities {
            public static final ResourceLocation WOLF_ARMOR =
                    new ResourceLocation(MOD_ID, "wolf_armor");
        }

        public static abstract class LootTables {
            public static final ResourceLocation DUNGEON_INJECT =
                    new ResourceLocation(MOD_ID, "chests/inject/dungeon");
        }

        public static abstract class Textures {
            public static final ResourceLocation TEXTURE_WOLF_BACKPACK =
                    new ResourceLocation(MOD_ID, "textures/models/wolf_pack.png");
        }
    }

    @ApiHelper
    public static abstract class Recipes{
        @ObjectHolder(MOD_ID + ":leather_wolf_armor_dyed")
        @ApiHelper
        public static IRecipe LEATHER_ARMOR_DYES = null;
    }

    @ApiHelper
    public static abstract class Entities {
        @ObjectHolder(MOD_ID + ":wolf_armored")
        @Deprecated
        @ApiHelper
        public static EntityEntry WOLF_ARMORED = null;
    }

    @ApiHelper
    public static abstract class Items {
        @ObjectHolder(MOD_ID + ":leather_wolf_armor")
        @ApiHelper
        public static Item LEATHER_WOLF_ARMOR = null;

        @ObjectHolder(MOD_ID + ":chainmail_wolf_armor")
        @ApiHelper
        public static Item CHAINMAIL_WOLF_ARMOR = null;

        @ObjectHolder(MOD_ID + ":iron_wolf_armor")
        @ApiHelper
        public static Item IRON_WOLF_ARMOR = null;

        @ObjectHolder(MOD_ID + ":gold_wolf_armor")
        @ApiHelper
        public static Item GOLD_WOLF_ARMOR = null;

        @ObjectHolder(MOD_ID + ":diamond_wolf_armor")
        @ApiHelper
        public static Item DIAMOND_WOLF_ARMOR = null;
    }
}
