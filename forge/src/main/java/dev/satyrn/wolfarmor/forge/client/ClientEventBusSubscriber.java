package dev.satyrn.wolfarmor.forge.client;

import dev.satyrn.wolfarmor.WolfArmorCommon;
import dev.satyrn.wolfarmor.client.DyeableLeatherItemColor;
import dev.satyrn.wolfarmor.item.WolfArmorItems;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = WolfArmorCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    @SuppressWarnings("deprecation")
    static void onClientSetup(FMLClientSetupEvent client) {
        Minecraft.getInstance().getItemColors().register(new DyeableLeatherItemColor(), WolfArmorItems.LEATHER_WOLF_ARMOR.get());
    }
}
