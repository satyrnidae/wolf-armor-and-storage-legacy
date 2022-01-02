package dev.satyrn.wolfarmor;

import dev.satyrn.wolfarmor.api.common.IProxy;
import dev.satyrn.wolfarmor.api.compatibility.Compatibility;
import dev.satyrn.wolfarmor.api.util.ILogHelper;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.common.network.WolfArmorChannel;
import dev.satyrn.wolfarmor.config.WolfArmorConfig;
import dev.satyrn.wolfarmor.util.LogHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;

/**
 * WolfArmorMod main class
 */
@SuppressWarnings("WeakerAccess")
@Mod(useMetadata = true,
        modid = Resources.MOD_ID,
        name = Resources.MOD_NAME,
        version = WolfArmorMod.MOD_VERSION,
        guiFactory = "dev.satyrn.wolfarmor.client.gui.config.WolfArmorGuiFactory",
        certificateFingerprint = "e94e38a605842477f3ec218e6fcf781f6b3f7f89")
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
    public static WolfArmorMod instance;

    private final ILogHelper logger;
    private final WolfArmorConfig config;
    private WolfArmorChannel channel;

    //endregion Fields

    public WolfArmorMod() { this(new WolfArmorConfig(), new LogHelper());
    }

    @SuppressWarnings("unused")
    WolfArmorMod(WolfArmorConfig configInstance, ILogHelper loggerInstance) {
        logger = loggerInstance;
        config = configInstance;
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
        channel = new WolfArmorChannel();

        Compatibility.register("dev.satyrn.wolfarmor.compatibility.sophisticatedwolves.SophisticatedWolvesProvider");
        Compatibility.register("dev.satyrn.wolfarmor.compatibility.dogslie.LetSleepingDogsLieProvider");
        Compatibility.register("dev.satyrn.wolfarmor.compatibility.ebwizardry.WizardryProvider");

        config.initialize(preInitializationEvent.getModConfigurationDirectory() + "/satyrn");
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

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        LogManager.getLogger(Resources.MOD_ID).warn("Invalid fingerprint detected! This might mean the mod is compromised, or maybe you're just in a dev environment...");
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
        return getInstance().logger;
    }

    /**
     * Gets the configuration settings
     *
     * @return The configuration settings
     */
    @Nonnull
    public static WolfArmorConfig getConfig() {
        return getInstance().config;
    }

    public static WolfArmorChannel getNetworkChannel() { return getInstance().channel; }

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
