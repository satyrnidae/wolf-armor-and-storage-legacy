package com.attributestudios.wolfarmor;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

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

    //region Common Config Settings

    private static final String SETTING_WOLF_CHEST_ENABLED = "Enable Wolf Chests";

    private static final Boolean DEFAULT_WOLF_CHEST_ENABLED = true;

    //endregion Common Config Settings

    //region Client-Side Config Settings

    private static final String CATEGORY_CLIENT = "client";

    private static final String SETTING_WOLF_ARMOR_RENDER_ENABLED = "Enable Wolf Armor Render Layer";
    private static final String SETTING_WOLF_CHEST_RENDER_ENABLED = "Enable Wolf Chest Render Layer";
    private static final String SETTING_WOLF_ARMOR_DISPLAY_ENABLED = "Enable Wolf Armor Display in GUI";
    private static final String SETTING_WOLF_HEALTH_DISPLAY_ENABLED = "Enable Wolf Health Display in GUI";

    private static final boolean DEFAULT_WOLF_ARMOR_RENDER_ENABLED = true;
    private static final boolean DEFAULT_WOLF_CHEST_RENDER_ENABLED = true;
    private static final boolean DEFAULT_WOLF_ARMOR_DISPLAY_ENABLED = true;
    private static final boolean DEFAULT_WOLF_HEALTH_DISPLAY_ENABLED = true;

    //endregion Client-Side Config Settings

    //endregion Fields

    // region Constructors

    /**
     * Creates and loads wolf armor configuration.
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
     * @param eventArgs The event arguments
     */
    @SubscribeEvent
    public void onConfigChanged(@Nonnull ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(WolfArmorMod.MOD_ID)) {
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

    /**
     * Synchronizes the current config value for the setting "Enable Wolf Armor Render Layer"
     */
    private void syncIsWolfArmorRenderEnabled() {
        isWolfArmorRenderEnabled = getSettingWolfArmorRenderEnabled().getBoolean();
    }

    /**
     * Synchronizes the current config value for the setting "Enable Wolf Chest Render Layer"
     */
    private void syncIsWolfChestRenderEnabled() {
        isWolfChestRenderEnabled = getSettingWolfChestRenderEnabled().getBoolean();
    }

    /**
     * Synchronizes the current config value for the setting "Enable Wolf Armor Display in GUI"
     */
    private void syncIsWolfArmorDisplayEnabled() {
        isWolfArmorDisplayEnabled = getSettingWolfArmorDisplayEnabled().getBoolean();
    }

    /**
     * Synchronizes the current config value for the setting "Enable Wolf Armor Display in GUI"
     */
    private void syncIsWolfHealthDisplayEnabled() {
        isWolfHealthDisplayEnabled = getSettingWolfHealthDisplayEnabled().getBoolean();
    }

    /**
     * Synchronizes config values.
     * @param isClientSide Whether or not client-specific settings should be loaded
     */
    private void syncConfig(boolean isClientSide)
    {
        syncIsWolfChestEnabled();

        if(isClientSide) {
            syncIsWolfArmorRenderEnabled();
            syncIsWolfChestRenderEnabled();
            syncIsWolfArmorDisplayEnabled();
            syncIsWolfHealthDisplayEnabled();
        }

        if(config.hasChanged()) {
            WolfArmorMod.getLogger().debug("Saving configuration...");
            config.save();
            WolfArmorMod.getLogger().debug("Mod configuration saved.");
        }
    }

    /**
     * Gets the configuration file.
     * @param preInitializationEvent The pre-initialization event
     * @return The file path.
     */
    @Nonnull
    private static File getConfigurationFile(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        WolfArmorMod.getLogger().debug("Retrieving config file...");

        File configDir = new File(preInitializationEvent.getModConfigurationDirectory() + "/attributestudios");

        if(!configDir.exists()) {
            WolfArmorMod.getLogger().debug("Creating new top-level mod config directory...");
            //noinspection ResultOfMethodCallIgnored
            configDir.mkdirs();
        }

        File mainConfig = new File(configDir.getPath() + "/" + WolfArmorMod.MOD_ID + ".cfg");
        if(!mainConfig.exists()) {
            WolfArmorMod.getLogger().debug("Configuration file not found. A new configuration file will be created.");
        }

        return mainConfig;
    }

    //endregion Private Methods

    //region Accessors / Mutators

    /**
     * Gets the value of the setting "Enable Wolf Chests"
     * @return True if wolf chests should be rendered, false if not.
     */
    public boolean getIsWolfChestEnabled() {
        return isWolfChestEnabled;
    }

    /**
     * Gets the value of the setting "Enable Wolf Armor Render Layer"
     * @return True if wolf chests should be rendered, false if not.
     */
    public boolean getIsWolfArmorRenderEnabled() {
        return isWolfArmorRenderEnabled;
    }

    /**
     * Gets the value of the setting "Enable Wolf Chest Render Layer"
     * @return True if wolf chests should be rendered, false if not.
     */
    public boolean getIsWolfChestRenderEnabled() {
        return isWolfChestRenderEnabled;
    }

    /**
     * Gets the value of the setting "Enable Wolf Chest Render Layer"
     * @return True if wolf chests should be rendered, false if not.
     */
    public boolean getIsWolfArmorDisplayEnabled() {
        return isWolfArmorDisplayEnabled;
    }

    /**
     * Gets the value of the setting "Enable Wolf Chest Render Layer"
     * @return True if wolf chests should be rendered, false if not.
     */
    public boolean getIsWolfHealthDisplayEnabled() {
        return isWolfHealthDisplayEnabled;
    }

    /**
     * Gets the config setting entry for "Enable Wolf Chests"
     * @return The config property for enabling wolf chests (boolean)
     */
    @Nonnull
    public Property getSettingWolfChestsEnabled() {
        return config.get(Configuration.CATEGORY_GENERAL,
                SETTING_WOLF_CHEST_ENABLED,
                DEFAULT_WOLF_CHEST_ENABLED,
                "Enables or disables wolf backpacks.");
    }

    /**
     * Gets the config setting entry for "Enable Wolf Armor Render Layer"
     * @return The config property for enabling wolf chests (boolean)
     */
    @Nonnull
    public Property getSettingWolfArmorRenderEnabled() {
        return config.get(CATEGORY_CLIENT,
                SETTING_WOLF_ARMOR_RENDER_ENABLED,
                DEFAULT_WOLF_ARMOR_RENDER_ENABLED,
                "Enables or disables rendering of wolf armor.");
    }

    /**
     * Gets the config setting entry for "Enable Wolf Chest Render Layer"
     * @return The config property for enabling wolf chests (boolean)
     */
    @Nonnull
    public Property getSettingWolfChestRenderEnabled() {
        return config.get(CATEGORY_CLIENT,
                SETTING_WOLF_CHEST_RENDER_ENABLED,
                DEFAULT_WOLF_CHEST_RENDER_ENABLED,
                "Enables or disables rendering of wolf backpacks.");
    }

    /**
     * Gets the config setting entry for "Enable Wolf Armor Display in GUI"
     * @return The config property for enabling wolf chests (boolean)
     */
    @Nonnull
    public Property getSettingWolfArmorDisplayEnabled() {
        return config.get(CATEGORY_CLIENT,
                SETTING_WOLF_ARMOR_DISPLAY_ENABLED,
                DEFAULT_WOLF_ARMOR_DISPLAY_ENABLED,
                "Enables or disables displaying a wolf's armor value in the wolf inventory screen.");
    }

    /**
     * Gets the config setting entry for "Enable Wolf Health Display in GUI"
     * @return The config property for enabling wolf chests (boolean)
     */
    @Nonnull
    public Property getSettingWolfHealthDisplayEnabled() {
        return config.get(CATEGORY_CLIENT,
                SETTING_WOLF_HEALTH_DISPLAY_ENABLED,
                DEFAULT_WOLF_HEALTH_DISPLAY_ENABLED,
                "Enables or disables displaying a wolf's health value in the wolf inventory screen.");
    }

    //endregion Accessors / Mutators
}
