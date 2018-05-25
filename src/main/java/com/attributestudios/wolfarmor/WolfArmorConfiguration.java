package com.attributestudios.wolfarmor;

import com.attributestudios.wolfarmor.api.util.Resources;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Configuration settings for WolfArmor
 */
public final class WolfArmorConfiguration {
    //region Fields

    private Configuration config;
    private boolean isWolfChestEnabled = DEFAULT_WOLF_CHEST_ENABLED;
    private boolean isWolfArmorRenderEnabled = DEFAULT_WOLF_ARMOR_RENDER_ENABLED;
    private boolean isWolfChestRenderEnabled = DEFAULT_WOLF_CHEST_RENDER_ENABLED;
    private boolean isWolfHealthDisplayEnabled = DEFAULT_WOLF_HEALTH_DISPLAY_ENABLED;
    private boolean isWolfArmorDisplayEnabled = DEFAULT_WOLF_ARMOR_DISPLAY_ENABLED;
    private boolean areHowlingUntamedWolvesEnabled = DEFAULT_HOWLING_UNTAMED_WOLVES_ENABLED;
    private boolean shouldWolvesEatWhenDamaged = DEFAULT_WOLVES_EAT_FOOD_IN_INVENTORY;

    //region Common Config Settings

    private static final String SETTING_WOLF_CHEST_ENABLED = "config.wolfarmor.general.enableChests";

    private static final Boolean DEFAULT_WOLF_CHEST_ENABLED = true;

    //endregion Common Config Settings

    //region Client-Side Config Settings

    private static final String SETTING_WOLF_ARMOR_RENDER_ENABLED = "config.wolfarmor.client.enableWolfArmorRender";
    private static final String SETTING_WOLF_CHEST_RENDER_ENABLED = "config.wolfarmor.client.enableWolfChestRender";
    private static final String SETTING_WOLF_ARMOR_DISPLAY_ENABLED = "config.wolfarmor.client.enableWolfArmorDisplay";
    private static final String SETTING_WOLF_HEALTH_DISPLAY_ENABLED = "config.wolfarmor.client.enableWolfHealthDisplay";

    private static final boolean DEFAULT_WOLF_ARMOR_RENDER_ENABLED = true;
    private static final boolean DEFAULT_WOLF_CHEST_RENDER_ENABLED = true;
    private static final boolean DEFAULT_WOLF_ARMOR_DISPLAY_ENABLED = true;
    private static final boolean DEFAULT_WOLF_HEALTH_DISPLAY_ENABLED = true;

    //endregion Client-Side Config Settings

    //region Behavioral Config Settings

    private static final String CATEGORY_BEHAVIOR = "behavior";

    private static final String SETTING_HOWLING_UNTAMED_WOLVES_ENABLED = "config.wolfarmor.behavior.enableHowlingUntamedWolves";
    private static final String SETTING_WOLVES_EAT_FOOD_IN_INVENTORY = "config.wolfarmor.behavior.shouldWolvesEatWhenDamaged";

    private static final boolean DEFAULT_HOWLING_UNTAMED_WOLVES_ENABLED = false;
    private static final boolean DEFAULT_WOLVES_EAT_FOOD_IN_INVENTORY = true;

    //endregion Behavioral Config Settings

    //endregion Fields

    // region Constructors

    /**
     * Creates and loads wolf armor configuration.
     *
     * @param preInitializationEvent The pre-initialization event.
     */
    @SuppressWarnings("WeakerAccess")
    public WolfArmorConfiguration(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        config = new Configuration(getConfigurationFile(preInitializationEvent));

        WolfArmorMod.getLogger().debug("Loading mod configuration...");
        config.load();
        WolfArmorMod.getLogger().debug("Mod configuration loaded.");

        syncConfig(preInitializationEvent.getSide() == Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(this);
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Handles config changed events
     *
     * @param eventArgs The event arguments
     */
    @SubscribeEvent
    public void onConfigChanged(@Nonnull ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(Resources.MOD_ID)) {
            syncConfig(FMLCommonHandler.instance().getSide() == Side.CLIENT);
        }
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Synchronizes the current config value for the setting "Enable Wolf Chests"
     */
    private void syncIsWolfChestEnabled() {
        isWolfChestEnabled = getSettingWolfChestsEnabled().getBoolean();
    }

    private void syncIsWolfArmorRenderEnabled() {
        isWolfArmorRenderEnabled = getSettingWolfArmorRenderEnabled().getBoolean();
    }

    private void syncIsWolfChestRenderEnabled() {
        isWolfChestRenderEnabled = getSettingWolfChestRenderEnabled().getBoolean();
    }

    private void syncIsWolfArmorDisplayEnabled() {
        isWolfArmorDisplayEnabled = getSettingWolfArmorDisplayEnabled().getBoolean();
    }

    private void syncIsWolfHealthDisplayEnabled() {
        isWolfHealthDisplayEnabled = getSettingWolfHealthDisplayEnabled().getBoolean();
    }

    private void syncAreHowlingUntamedWolvesEnabled() {
        areHowlingUntamedWolvesEnabled = getSettingHowlingUntamedWolvesEnabled().getBoolean();
    }

    private void syncShouldWolvesEatWhenDamaged() {
        shouldWolvesEatWhenDamaged = getSettingWolvesEatWhenDamaged().getBoolean();
    }

    /**
     * Synchronizes config values.
     *
     * @param isClientSide Whether or not client-specific settings should be loaded
     */
    private void syncConfig(boolean isClientSide) {
        syncIsWolfChestEnabled();

        if (isClientSide) {
            syncIsWolfArmorRenderEnabled();
            syncIsWolfChestRenderEnabled();
            syncIsWolfArmorDisplayEnabled();
            syncIsWolfHealthDisplayEnabled();
        }

        syncAreHowlingUntamedWolvesEnabled();
        syncShouldWolvesEatWhenDamaged();

        if (config.hasChanged()) {
            WolfArmorMod.getLogger().debug("Saving configuration...");
            config.save();
            WolfArmorMod.getLogger().debug("Mod configuration saved.");
        }
    }

    /**
     * Gets the configuration file.
     *
     * @param preInitializationEvent The pre-initialization event
     * @return The file path.
     */
    @Nonnull
    private static File getConfigurationFile(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        WolfArmorMod.getLogger().debug("Retrieving config file...");

        File configDir = new File(preInitializationEvent.getModConfigurationDirectory() + "/attributestudios");

        if (!configDir.exists()) {
            WolfArmorMod.getLogger().debug("Creating new top-level mod config directory...");
            //noinspection ResultOfMethodCallIgnored
            configDir.mkdirs();
        }

        File mainConfig = new File(configDir.getPath() + "/" + Resources.MOD_ID + ".cfg");
        if (!mainConfig.exists()) {
            WolfArmorMod.getLogger().debug("Configuration file not found. A new configuration file will be created.");
        }

        return mainConfig;
    }

    //endregion Private Methods

    //region Accessors / Mutators

    //region Generic Accessors

    public boolean getIsWolfChestEnabled() {
        return isWolfChestEnabled;
    }

    public boolean getIsWolfArmorRenderEnabled() {
        return isWolfArmorRenderEnabled;
    }

    public boolean getIsWolfChestRenderEnabled() {
        return isWolfChestRenderEnabled;
    }

    public boolean getIsWolfArmorDisplayEnabled() {
        return isWolfArmorDisplayEnabled;
    }

    public boolean getIsWolfHealthDisplayEnabled() {
        return isWolfHealthDisplayEnabled;
    }

    public boolean getAreHowlingUntamedWolvesEnabled() {
        return areHowlingUntamedWolvesEnabled;
    }

    public boolean getShouldWolvesEatWhenDamaged() { return shouldWolvesEatWhenDamaged; }

    //endregion Generic Accessors

    //region Property Accessors

    @Nonnull
    public Property getSettingWolfChestsEnabled() {
        return config.get(Configuration.CATEGORY_GENERAL,
                SETTING_WOLF_CHEST_ENABLED,
                DEFAULT_WOLF_CHEST_ENABLED,
                "Enables or disables wolf backpacks.");
    }

    @Nonnull
    public Property getSettingWolfArmorRenderEnabled() {
        return config.get(Configuration.CATEGORY_CLIENT,
                SETTING_WOLF_ARMOR_RENDER_ENABLED,
                DEFAULT_WOLF_ARMOR_RENDER_ENABLED,
                "Enables or disables rendering of wolf armor.");
    }

    @Nonnull
    public Property getSettingWolfChestRenderEnabled() {
        return config.get(Configuration.CATEGORY_CLIENT,
                SETTING_WOLF_CHEST_RENDER_ENABLED,
                DEFAULT_WOLF_CHEST_RENDER_ENABLED,
                "Enables or disables rendering of wolf backpacks.");
    }

    @Nonnull
    public Property getSettingWolfArmorDisplayEnabled() {
        return config.get(Configuration.CATEGORY_CLIENT,
                SETTING_WOLF_ARMOR_DISPLAY_ENABLED,
                DEFAULT_WOLF_ARMOR_DISPLAY_ENABLED,
                "Enables or disables displaying a wolf's armor value in the wolf inventory screen.");
    }

    @Nonnull
    public Property getSettingWolfHealthDisplayEnabled() {
        return config.get(Configuration.CATEGORY_CLIENT,
                SETTING_WOLF_HEALTH_DISPLAY_ENABLED,
                DEFAULT_WOLF_HEALTH_DISPLAY_ENABLED,
                "Enables or disables displaying a wolf's health value in the wolf inventory screen.");
    }

    @Nonnull
    public Property getSettingHowlingUntamedWolvesEnabled() {
        return config.get(CATEGORY_BEHAVIOR,
                SETTING_HOWLING_UNTAMED_WOLVES_ENABLED,
                DEFAULT_HOWLING_UNTAMED_WOLVES_ENABLED,
                "Enables or disables untamed wolves howling at the full moon.");
    }

    @Nonnull
    public Property getSettingWolvesEatWhenDamaged() {
        return config.get(CATEGORY_BEHAVIOR,
                SETTING_WOLVES_EAT_FOOD_IN_INVENTORY,
                DEFAULT_WOLVES_EAT_FOOD_IN_INVENTORY,
                "Enables or disables wolves eating food in their inventories when their health is low.");
    }

    //endregion Property Accessors

    //endregion Accessors / Mutators
}
