package dev.satyrn.wolfarmor.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.satyrn.wolfarmor.WolfArmorCommon;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(WolfArmorCommon.MOD_ID)
public class WolfArmorForge {

    public WolfArmorForge(@NotNull FMLJavaModLoadingContext modLoadingContext) {
        // Submit our event bus to let Architectury API register our content on the right time.
        var modEventBus = modLoadingContext.getModEventBus();
        EventBuses.registerModEventBus(WolfArmorCommon.MOD_ID, modEventBus);

        // Run our common setup.
        WolfArmorCommon.init();
    }
}
