package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.api.IWolfArmorMaterial;
import com.attributestudios.wolfarmor.api.util.Resources;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.attributestudios.wolfarmor.item.crafting.RecipeWolfArmorDyes;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

/**
 * Hanldes forge registration events.
 */
public class RegistrationEventHandler {

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ItemWolfArmor(IWolfArmorMaterial.CLOTH).setUnlocalizedName("wolfarmor.leatherWolfArmor").setRegistryName(Resources.ITEM_LEATHER_WOLF_ARMOR),
                new ItemWolfArmor(IWolfArmorMaterial.CHAINMAIL).setUnlocalizedName("wolfarmor.chainWolfArmor").setRegistryName(Resources.ITEM_CHAINMAIL_WOLF_ARMOR),
                new ItemWolfArmor(IWolfArmorMaterial.IRON).setUnlocalizedName("wolfarmor.ironWolfArmor").setRegistryName(Resources.ITEM_IRON_WOLF_ARMOR),
                new ItemWolfArmor(IWolfArmorMaterial.GOLD).setUnlocalizedName("wolfarmor.goldWolfArmor").setRegistryName(Resources.ITEM_GOLD_WOLF_ARMOR),
                new ItemWolfArmor(IWolfArmorMaterial.DIAMOND).setUnlocalizedName("wolfarmor.diamondWolfArmor").setRegistryName(Resources.ITEM_DIAMOND_WOLF_ARMOR)
        );
    }

    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(new EntityEntry(EntityWolfArmored.class, "wolfarmor.wolf_armored").setRegistryName(Resources.ENTITY_WOLF_ARMORED_LOCATION));
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeWolfArmorDyes().setRegistryName(Resources.RECIPE_LEATHER_ARMOR_DYES));
    }
}
