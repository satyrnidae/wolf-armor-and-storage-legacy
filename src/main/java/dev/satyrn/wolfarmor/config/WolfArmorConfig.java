/*
 * This file contains code based on the configuration system implemented in copycore, (c) 2014 copygirl. Licensed under
 * MIT. Please see THIRDPARTY for license and notices related to the use of this code.
 */
package dev.satyrn.wolfarmor.config;

import com.google.common.collect.Maps;
import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.config.settings.BooleanSetting;
import dev.satyrn.wolfarmor.api.config.settings.Setting;
import dev.satyrn.wolfarmor.util.WolfFoodStatsLevel;
import dev.satyrn.wolfarmor.config.settings.WolfFoodStatsSetting;
import dev.satyrn.wolfarmor.config.settings.WolfInventorySizeSetting;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.util.WolfInventorySize;
import dev.satyrn.wolfarmor.common.network.packets.ConfigSyncMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

/**
 * Configuration file for the WolfArmor mod.
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.5.13
 */
public class WolfArmorConfig {

    private final Setting<Boolean> chestEnabled = new BooleanSetting(true)
            .setCategory(Configuration.CATEGORY_GENERAL)
            .setName("backpack")
            .setComment("Enables or disables wolf backpacks.")
            .setRequiresWorldReload()
            .setSynchronizes(true);

    private final Setting<WolfInventorySize> chestSize =
            new WolfInventorySizeSetting(new WolfInventorySize((byte)3, (byte)2))
                    .setCategory(Configuration.CATEGORY_GENERAL)
                    .setName("backpack_size")
                    .setComment("Sets the horizontal and vertical size of wolves' backpacks.")
                    .setSynchronizes(true)
                    .setRequiresWorldReload()
                    .setConfigWidgetClassName("dev.satyrn.wolfarmor.client.gui.config.WolfInventorySizeWidget");

    private final Setting<Boolean> enableRecipes = new BooleanSetting(true)
            .setCategory(Configuration.CATEGORY_GENERAL)
            .setName("allow_crafting")
            .setComment("Allows or disallows players to craft the wolf armor items.")
            .setRequiresMinecraftRestart();

    public BehaviorCategory behavior;

    /**
     * This class contains settings related to wolf behavior
     * @author Isabel Maskrey (satyrnidae)
     * @since 3.6.0
     */
    public static final class BehaviorCategory {
        private static final String CATEGORY = "behavior";

        final Setting<Boolean> howlAtMoonEnabled = new BooleanSetting(false)
                .setCategory(CATEGORY)
                .setName("howl_at_moon")
                .setComment("Enables or disables untamed wolves howling at the moon")
                .setSynchronizes(true);

        final Setting<Boolean> autoHealEnabled = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("auto_eat")
                .setComment("Enables or disables wolves automatically eating food when their health is low.")
                .setSynchronizes(true);

        final Setting<WolfFoodStatsLevel> useFoodStats = new WolfFoodStatsSetting()
                .setCategory(CATEGORY)
                .setName("hunger")
                .setComment("Sets whether or not to enable hunger for wolves. Valid values are disabled, heal, and "
                        + "full. \"Heal\" will override the default heal functionality and heal wolves w/ saturation. "
                        + "\"Full\" will cause unfed wolves to lose health until 4 hearts are left, or, if "
                        + "starvation is enabled, until it dies.")
                .setRequiresWorldReload()
                .setSynchronizes(true);

        final Setting<Boolean> wolvesCanStarve = new BooleanSetting(false)
                .setCategory(CATEGORY)
                .setName("starvation")
                .setComment("Sets whether or not wolves can starve to death if hunger is set to \"full\".")
                .setRequiresWorldReload()
                .setSynchronizes(true);
    }

    @SideOnly(Side.CLIENT)
    public ClientCategory client;

    /**
     * This class contains all settings related to display and rendering.  It is client-side only.
     * @author Isabel Maskrey (satyrnidae)
     * @since 3.6.0
     */
    @SideOnly(Side.CLIENT)
    public static final class ClientCategory {
        private static final String CATEGORY = Configuration.CATEGORY_CLIENT;

        final Setting<Boolean> armorRendered = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("armor_model")
                .setComment("Enables or disables wolf armor model rendering. Disable this if you have an unsupported "
                        + " custom model mod, or if rendering the armor causes other issues.");

        final Setting<Boolean> chestRendered = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("backpack_model")
                .setComment("Enables or disables wolf backpack model rendering. Disable if you have an unsupported "
                        + "custom model mod, or if rendering the backpack causes other issues.");

        final Setting<Boolean> statsInGui = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("stats_in_gui")
                .setComment("Enables or disables displaying the wolf health, armor, and food stats in the inventory");

        final Setting<Boolean> wolfStatsRendered = new BooleanSetting(false)
                .setCategory(CATEGORY)
                .setName("stats_in_nameplate")
                .setComment("Enables or disables displaying health, armor, and food stats above tamed wolves' heads.");
    }

    private Configuration configuration;
    private final Map<String, Setting<?>> settings = Maps.newLinkedHashMap();

    @SideOnly(Side.CLIENT)
    private boolean connected;

    /**
     * Loads setting fields from a given class into the settings map
     * @param object The class instance
     * @since 3.6.0
     */
    private void addSettingsFromClass(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (!Setting.class.isAssignableFrom(field.getType())) { continue; }

            Setting<?> setting;
            try {
                setting = (Setting<?>)field.get(object);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }

            this.settings.put(setting.getFullName(), setting);
        }
    }

    /**
     * Returns the settings currently present in the settings map.
     * @return The settings currently registered in the settings map.
     * @since 3.6.0
     */
    public Collection<Setting<?>> getSettings() { return this.settings.values(); }

    /**
     * Gets a specific setting by name from the settings map.
     * @param fullName The full name of the setting.
     * @return A setting with a matching full name that was registered in the setting map.
     * @since 3.6.0
     */
    public Setting<?> getSetting(String fullName) { return this.settings.get(fullName); }

    /**
     * Loads configuration settings from the config file
     * @since 3.5.13
     */
    public void load() {
        if (this.settings.size() == 0) return;
        this.configuration.load();

        this.getSettings().forEach(setting -> setting.loadFromConfiguration(this.configuration));
    }

    /**
     * Saves the configuration settings to disk
     * @since 3.5.13
     */
    public void save() {
        this.getSettings().forEach(setting -> setting.saveToConfiguration(this.configuration));
        this.configuration.save();
    }

    /**
     * Checks if wolf chests are enabled or disabled
     * @return <c>true</c> if wolf chests are enabled, <c>false</c> if they are disabled.
     * @since 3.5.13
     */
    public boolean getChestEnabled() { return this.chestEnabled.getCurrentValue(); }

    /**
     * Checks if untamed wolves should howl at the full moon when untamed
     * @return <c>true</c> if they should howl, <c>false</c> otherwise
     * @since 3.5.13
     */
    public boolean getHowlAtMoonEnabled() { return this.behavior.howlAtMoonEnabled.getCurrentValue(); }

    /**
     * Checks whether or not wolves should eat food present in their inventory
     * @return <c>true</c> if wolves should auto-heal, <c>false</c> if not
     * @since 3.5.13
     */
    public boolean getAutoHealEnabled() { return this.behavior.autoHealEnabled.getCurrentValue(); }

    /**
     * Checks if the armor layers should be rendered
     * @return <c>true</c> if the armor layers should be rendered, <c>false</c> otherwise
     * @since 3.5.13
     */
    @SideOnly(Side.CLIENT)
    public boolean getArmorRendered() { return this.client.armorRendered.getCurrentValue(); }

    /**
     * Checks if the chest model should be rendered
     * @return <c>true</c> if the chests should be rendered; otherwise, <c>false</c>
     * @since 3.5.13
     */
    public boolean getChestRendered() { return this.client.chestRendered.getCurrentValue(); }

    /**
     * Gets the current configured wolf chest size
     * @return The current configured wolf chest size
     * @since 3.5.13
     */
    public WolfInventorySize getChestSize() { return this.chestSize.getCurrentValue(); }

    /**
     * Gets the hunger option level
     * @return See {@link WolfFoodStatsLevel} for potential setitngs
     * @since 3.6.0
     */
    public WolfFoodStatsLevel getFoodStatsLevel() { return this.behavior.useFoodStats.getCurrentValue(); }

    /**
     * Checks whether or not wolves can starve to death.  Requires {@link #getFoodStatsLevel()} to be anything other
     * than disabled.
     * @return Whether or not wolves can starve to death from being unfed
     * @since 3.6.0
     */
    public boolean getCanStarve() { return this.behavior.wolvesCanStarve.getCurrentValue(); }

    /**
     * Checks whether or not tamed wolf stats (health, armor, food) are rendered above their heads.
     * @return Whether or not wolf health, armor, and hunger should be rendered above their heads
     * @since 3.6.0
     */
    @SideOnly(Side.CLIENT)
    public boolean getStatsRendered() { return this.client.wolfStatsRendered.getCurrentValue(); }

    /**
     * Checks whether or not wolf stats should be rendered in the wolf inventory UI
     * @return Whether or not wolf health, armor, and hunger should be rendered in the wolf inventory
     * @since 3.6.1
     */
    @SideOnly(Side.CLIENT)
    public boolean getStatsInGui() { return this.client.statsInGui.getCurrentValue(); }

    /**
     * Checks whether or not the vanilla-based armor recipes are enabled or not
     * @return {@code true} if enabled, otherwise {@code false}
     * @since 3.7.3
     */
    public boolean getEnableCrafting() { return this.enableRecipes.getCurrentValue(); }

    /**
     * Initializes the configuration
     * @param configDirectory The path to the directory where the config file is stored
     * @since 3.5.13
     */
    public void initialize(String configDirectory) {
        File directory = new File(configDirectory);
        if (!directory.exists()) {
            WolfArmorMod.getLogger().debug(
                    String.format("Creating new configuration directory '%s'", configDirectory));
            if (!directory.mkdirs()) {
                WolfArmorMod.getLogger().error("Unable to create new configuration directory!");
                throw new RuntimeException("Failed to create configuration directory");
            }
        }
        File configFile = new File(directory.getPath() + "/" + Resources.MOD_ID + ".cfg");
        if (!configFile.exists()) {
           WolfArmorMod.getLogger().debug("Config file not found; a new configuration file will be initialized.");
        }
        this.configuration = new Configuration(configFile);
        addSettingsFromClass(this);
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!field.getType().getName().endsWith("Category")) continue;
            try {
                field.set(this, field.getType().getConstructors()[0].newInstance());
                addSettingsFromClass(field.get(this));
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        this.load();
        if (this.configuration.hasChanged()) {
            this.save();
        }
    }

    /**
     * Synchronizes the client side configuration file with data from the server
     * @param data The server configuration data
     * @since 3.5.13
     */
    @SideOnly(Side.CLIENT)
    public void sync(@Nonnull NBTTagCompound data) {
        if (this.connected) {
            for (String key : data.getKeySet()) {
                NBTBase tag = data.getTag(key);
                Setting<?> setting = this.getSetting(key);
                if ((setting != null) && setting.getSynchronizes()) {
                    setting.readSynchronized(tag);
                }
            }
        }
    }

    /**
     * Event handler for when a client connects to a server
     * @param event The event detials
     * @since 3.6.0
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.connected = true;
    }

    /**
     * Event handler for when a client disconnects from a server.  Resets each of the client's settings back to their
     * un-synchronized values.
     * @param event The event data
     * @since 3.6.0
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (!this.connected) return;
        this.getSettings().forEach(Setting::onDisconnect);
        this.connected = false;
    }

    /**
     * Event handler for when the configuration is changed or reloaded.  From the server side, sends new config data to
     * the clients. Both sides save the altered config to the disk.
     * @param event The event data
     * @since 3.6.0
     */
    @SubscribeEvent
    public void onConfigChanged(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if (!event.getModID().equals(Resources.MOD_ID)) return;
        if (event.isWorldRunning()) {
            WolfArmorMod.getNetworkChannel().sendToAllWhere(ConfigSyncMessage.create(), WolfArmorConfig::isNotServerOwner);
        }
        this.save();
    }

    /**
     * Synchronizes a player's configuration with the server's when they log in
     * @param event The log-in event data
     * @since 3.6.3
     */
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (isNotServerOwner(event.player) || !(event.player instanceof EntityPlayerMP)) {
            return;
        }
        WolfArmorMod.getNetworkChannel().sendTo(ConfigSyncMessage.create(), (EntityPlayerMP)event.player);
    }

    /**
     * Provider method which ensures that a player entity is not the server owner.
     * @param player The player to check
     * @return Whether or not the player entity is also hosting the server.
     * @since 3.6.0
     */
    private static boolean isNotServerOwner(EntityPlayer player) {
        return player.getName().equalsIgnoreCase(FMLCommonHandler.instance().getMinecraftServerInstance().getServerOwner());
    }
}
