package com.attributestudios.wolfarmor;

import com.attributestudios.wolfarmor.api.util.Resources;
import com.attributestudios.wolfarmor.api.IProxy;
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
@Mod(modid = Resources.MOD_ID,
        name = Resources.MOD_NAME,
        version = WolfArmorMod.MOD_VERSION,
        guiFactory = "com.attributestudios.wolfarmor.client.gui.config.WolfArmorGuiFactory",
        dependencies = "after: sophisticatedwolves")
public class WolfArmorMod {
    //region Fields

    public static final String MOD_VERSION = "1.12.2-2.1.0-RELEASE";

    @SidedProxy(clientSide = "com.attributestudios.wolfarmor.client.ClientProxy",
                serverSide = "com.attributestudios.wolfarmor.common.CommonProxy")
    public static IProxy proxy;

    @Mod.Instance(Resources.MOD_ID)
    private static WolfArmorMod instance;

    private static LogHelper logger;

    private static WolfArmorConfiguration configuration;

    //endregion Fields

    //region Public / Protected Methods

    /**
     * Gets the sided proxy.
     *
     * @return The sided proxy.
     */
    public static IProxy getProxy() {
        return proxy;
    }

    /**
     * Gets the mod instance.
     *
     * @return The mod instance.
     */
    public static WolfArmorMod getInstance() {
        return instance;
    }

    /**
     * Handles pre-initialization tasks
     *
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
     *
     * @param initializationEvent The initialization event
     */
    @Mod.EventHandler
    public void init(@Nonnull FMLInitializationEvent initializationEvent) {
        proxy.init(initializationEvent);
    }

    /**
     * Handles post-initialization events
     *
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
     *
     * @return The logger
     */
    @Nonnull
    public static LogHelper getLogger() {
        return logger;
    }

    /**
     * Gets the configuration settings
     *
     * @return The configuration settings
     */
    @Nonnull
    public static WolfArmorConfiguration getConfiguration() {
        return configuration;
    }

    //endregion Accessors / Mutators
}
