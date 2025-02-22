package dev.satyrn.wolfarmor.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.satyrn.wolfarmor.WolfArmorCommon;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

/**
 * Contains all of the registered items in the mod.
 */
public final class WolfArmorItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(WolfArmorCommon.MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<WolfArmorItem> LEATHER_WOLF_ARMOR = ITEMS.register("leather_wolf_armor", () -> new DyeableWolfArmorItem(ArmorMaterials.LEATHER, new Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistrySupplier<WolfArmorItem> CHAINMAIL_WOLF_ARMOR = ITEMS.register("chainmail_wolf_armor", () -> new WolfArmorItem(ArmorMaterials.CHAIN, new Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistrySupplier<WolfArmorItem> IRON_WOLF_ARMOR = ITEMS.register("iron_wolf_armor", () -> new WolfArmorItem(ArmorMaterials.IRON, new Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistrySupplier<WolfArmorItem> GOLDEN_WOLF_ARMOR = ITEMS.register("golden_wolf_armor", () -> new WolfArmorItem(ArmorMaterials.GOLD, new Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistrySupplier<WolfArmorItem> DIAMOND_WOLF_ARMOR = ITEMS.register("diamond_wolf_armor", () -> new WolfArmorItem(ArmorMaterials.DIAMOND, new Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistrySupplier<WolfArmorItem> NETHERITE_WOLF_ARMOR = ITEMS.register("netherite_wolf_armor", () -> new WolfArmorItem(ArmorMaterials.NETHERITE, new Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant()));

    public static void init() {
        ITEMS.register();
    }

    private WolfArmorItems() {}
}
