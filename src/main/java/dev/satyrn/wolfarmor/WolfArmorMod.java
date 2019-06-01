package dev.satyrn.wolfarmor;

import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.api.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

/**
 * WolfArmorMod main class
 */
@SuppressWarnings("WeakerAccess")
@Mod(useMetadata = true,
        modid = Resources.MOD_ID,
        name = Resources.MOD_NAME,
        version = WolfArmorMod.MOD_VERSION,
        guiFactory = "dev.satyrn.wolfarmor.client.gui.config.WolfArmorGuiFactory")
public class WolfArmorMod {
    //region Fields

    /**
     * The version for the mod
     */
    public static final String MOD_VERSION = "${version}";

    @SidedProxy(clientSide = "dev.satyrn.wolfarmor.client.ClientProxy",
                serverSide = "dev.satyrn.wolfarmor.common.CommonProxy")
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

    @Mod.EventHandler
    public void loadComplete(@Nonnull FMLLoadCompleteEvent loadCompleteEvent) {
        proxy.loadComplete(loadCompleteEvent);
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
