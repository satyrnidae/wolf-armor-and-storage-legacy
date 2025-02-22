package dev.satyrn.wolfarmor;

import dev.satyrn.wolfarmor.item.WolfArmorItems;
import net.minecraft.world.level.GameRules;

public final class WolfArmorCommon {
    public static final String MOD_ID = "wolfarmor";

    public static GameRules.Key<GameRules.BooleanValue> RULE_ENTITY_STARVATION;

    public static void init() {
        WolfArmorItems.init();

        RULE_ENTITY_STARVATION = GameRules.register("doAnimalStarvation", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
    }
}
