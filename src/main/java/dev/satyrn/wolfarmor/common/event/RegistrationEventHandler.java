package dev.satyrn.wolfarmor.common.event;

import dev.satyrn.wolfarmor.api.item.WolfArmorMaterials;
import dev.satyrn.wolfarmor.item.ItemWolfArmor;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.item.WolfArmorMaterial;
import dev.satyrn.wolfarmor.item.crafting.RecipeWolfArmorDyes;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Hanldes forge registration events.
 */
public class RegistrationEventHandler {

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        WolfArmorMaterial.initializeMaterials();

        event.getRegistry().registerAll(
                new ItemWolfArmor(WolfArmorMaterials.CLOTH).setUnlocalizedName("wolfarmor.leatherWolfArmor").setRegistryName(Resources.ITEM_LEATHER_WOLF_ARMOR),
                new ItemWolfArmor(WolfArmorMaterials.CHAINMAIL).setUnlocalizedName("wolfarmor.chainWolfArmor").setRegistryName(Resources.ITEM_CHAINMAIL_WOLF_ARMOR),
                new ItemWolfArmor(WolfArmorMaterials.IRON).setUnlocalizedName("wolfarmor.ironWolfArmor").setRegistryName(Resources.ITEM_IRON_WOLF_ARMOR),
                new ItemWolfArmor(WolfArmorMaterials.GOLD).setUnlocalizedName("wolfarmor.goldWolfArmor").setRegistryName(Resources.ITEM_GOLD_WOLF_ARMOR),
                new ItemWolfArmor(WolfArmorMaterials.DIAMOND).setUnlocalizedName("wolfarmor.diamondWolfArmor").setRegistryName(Resources.ITEM_DIAMOND_WOLF_ARMOR)
        );
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeWolfArmorDyes().setRegistryName(Resources.RECIPE_LEATHER_ARMOR_DYES));
    }
}
