package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.item.Items;
import com.attributestudios.wolfarmor.entity.EntityEntries;
import com.attributestudios.wolfarmor.item.crafting.Recipes;
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
                Items.LEATHER_WOLF_ARMOR,
                Items.CHAINMAIL_WOLF_ARMOR,
                Items.IRON_WOLF_ARMOR,
                Items.GOLD_WOLF_ARMOR,
                Items.DIAMOND_WOLF_ARMOR
        );
    }

    @Deprecated
    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntries.ENTITY_WOLF_ARMORED);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(Recipes.LEATHER_ARMOR_DYES);
    }
}
