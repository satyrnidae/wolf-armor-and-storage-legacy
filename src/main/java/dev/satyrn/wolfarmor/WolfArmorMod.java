package dev.satyrn.wolfarmor;

import dev.satyrn.wolfarmor.api.config.IConfiguration;
import dev.satyrn.wolfarmor.api.util.ILogHelper;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.api.common.IProxy;
import dev.satyrn.wolfarmor.util.LogHelper;
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

    @SuppressWarnings("unused")
    @Mod.Instance(Resources.MOD_ID)
    public static WolfArmorMod instance;

    static ILogHelper logger;

    static IConfiguration configuration;

    //endregion Fields

    public WolfArmorMod() {
        this(new WolfArmorConfiguration(), new LogHelper());
    }

    @SuppressWarnings("unused")
    WolfArmorMod(IConfiguration configurationInstance, ILogHelper loggerInstance) {
        configuration = configurationInstance;
        logger = loggerInstance;
    }

    //region Public / Protected Methods

    /**
     * Handles pre-initialization tasks
     *
     * @param preInitializationEvent The pre-initialization event
     */
    @Mod.EventHandler
    public void preInit(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        logger.initializeLogger(preInitializationEvent.getModLog());
        configuration.initializeConfig(preInitializationEvent);

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
    public static ILogHelper getLogger() {
        return logger;
    }

    /**
     * Gets the configuration settings
     *
     * @return The configuration settings
     */
    @Nonnull
    public static IConfiguration getConfiguration() {
        return configuration;
    }

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

    //endregion Accessors / Mutators
}
