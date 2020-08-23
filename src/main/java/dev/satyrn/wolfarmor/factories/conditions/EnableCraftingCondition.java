package dev.satyrn.wolfarmor.factories.conditions;

import com.google.gson.JsonObject;
import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.config.WolfArmorConfig;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

/**
 * Condition factory that will return true if and only if the config setting
 * {@link WolfArmorConfig#getEnableCrafting()} is enabled.
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.7.3
 */
@SuppressWarnings("unused")
public class EnableCraftingCondition implements IConditionFactory {
    /**
     * Returns a supplier which reads the setting from the configuration
     * @param jsonContext The JSON context (unused)
     * @param jsonObject The JSON object (unused)
     * @return The value of {@link WolfArmorConfig#getEnableCrafting()}
     */
    @Override
    public BooleanSupplier parse(JsonContext jsonContext, JsonObject jsonObject) {
        return () -> WolfArmorMod.getConfig().getEnableCrafting();
    }
}
