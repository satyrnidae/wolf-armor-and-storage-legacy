package com.attributestudios.wolfarmor;

import com.attributestudios.wolfarmor.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

/**
 * WolfArmorMod main class
 */
@SuppressWarnings("WeakerAccess")
@Mod(modid = WolfArmorMod.MOD_ID,
     name = WolfArmorMod.MOD_NAME,
     version = WolfArmorMod.MOD_VERSION,
     guiFactory = "com.attributestudios.wolfarmor.client.gui.config.WolfArmorGuiFactory")
public class WolfArmorMod {
    //region Fields

    public static final String MOD_NAME = "Wolf Armor and Storage";
    public static final String MOD_ID = "wolfarmor";
    public static final String MOD_VERSION = "1.3.2";

    @Mod.Instance(WolfArmorMod.MOD_ID)
    public static WolfArmorMod instance;

    @SidedProxy(clientSide = "com.attributestudios.wolfarmor.client.ClientProxy",
            serverSide = "com.attributestudios.wolfarmor.common.CommonProxy")
    public static CommonProxy proxy;

    private static LogHelper logger;

    private static WolfArmorConfiguration configuration;

    //endregion Fields

    //region Public / Protected Methods

    /**
     * Handles pre-initialization tasks
     * @param preInitializationEvent The pre-initialization event
     */
    @Mod.EventHandler
    public void preInit(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        logger = new LogHelper(preInitializationEvent.getModLog());
        configuration = new WolfArmorConfiguration(preInitializationEvent);

        proxy.preInit(preInitializationEvent);
    }

    /**
     * Handles initialization tasks
     * @param initializationEvent The initialization event
     */
    @Mod.EventHandler
    public void init(@Nonnull FMLInitializationEvent initializationEvent) {
        proxy.init(initializationEvent);
    }

    /**
     * Handles post-initialization events
     * @param postInitializationEvent The post initialization event
     */
    @Mod.EventHandler
    public void postInit(@Nonnull FMLPostInitializationEvent postInitializationEvent) {
        proxy.postInit(postInitializationEvent);
    }

    //endregion Public / Protected Methods

    //region Accessors / Mutators

    /**
     * Gets the mod logger
     * @return The logger
     */
    @Nonnull
    public static LogHelper getLogger() {
        return logger;
    }

    /**
     * Gets the configuration settings
     * @return The configuration settings
     */
    @Nonnull
    public static WolfArmorConfiguration getConfiguration() {
        return configuration;
    }

    //endregion Accessors / Mutators
}
