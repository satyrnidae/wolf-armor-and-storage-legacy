package dev.satyrn.wolfarmor.api.util;

import net.minecraft.advancements.ICriterionTrigger;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Resources.MOD_ID)
public abstract class Criteria {

    @SuppressWarnings("rawtypes")
    public static ICriterionTrigger EQUIP_WOLF_ARMOR;

    @SuppressWarnings("rawtypes")
    public static ICriterionTrigger EQUIP_WOLF_CHEST;
}
