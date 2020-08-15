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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
            .setName("chest.enabled")
            .setComment("Enables or disables wolf chests")
            .setRequiresWorldReload()
            .setSynchronizes(true);

    private final Setting<WolfInventorySize> chestSize =
            new WolfInventorySizeSetting(new WolfInventorySize((byte)3, (byte)2))
                    .setCategory(Configuration.CATEGORY_GENERAL)
                    .setName("chest.size")
                    .setComment("Sets the horizontal and vertical size of the wolf chest")
                    .setSynchronizes(true)
                    .setRequiresWorldReload()
                    .setConfigWidgetClassName("dev.satyrn.wolfarmor.client.gui.config.WolfInventorySizeWidget");

    public BehaviorCategory behavior;
    public static final class BehaviorCategory {
        private static final String CATEGORY = "behavior";

        final Setting<Boolean> howlAtMoonEnabled = new BooleanSetting(false)
                .setCategory(CATEGORY)
                .setName("howl_at_moon.enabled")
                .setComment("Enables or disables untamed wolves howling at the moon")
                .setSynchronizes(true);

        final Setting<Boolean> autoHealEnabled = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("auto_heal.enabled")
                .setComment("Enables or disables wolves automatically eating food when their health is low")
                .setSynchronizes(true);

        final Setting<WolfFoodStatsLevel> useFoodStats = new WolfFoodStatsSetting()
                .setCategory(CATEGORY)
                .setName("food.saturation_healing")
                .setComment("Sets whether or not to enable hunger for wolves. Valid values are disabled, heal, and full. \"Heal\" will override the default " +
                        "heal functionality and heal wolves w/ saturation.  \"Full\" will cause unfed wolves to lose health until the untamed wolf health "
                        + "level is reached, or if starvation is enabled, until it dies.")
                .setRequiresWorldReload()
                .setSynchronizes(true);

        final Setting<Boolean> wolvesCanStarve = new BooleanSetting(false)
                .setCategory(CATEGORY)
                .setName("food.enable_starvation")
                .setComment("Sets whether or not wolves can starve. Don't use this on a server unless you're endlessly filling up your wolves' inventories.")
                .setRequiresWorldReload()
                .setSynchronizes(true);
    }

    public ClientCategory client;
    public static final class ClientCategory {
        private static final String CATEGORY = Configuration.CATEGORY_CLIENT;

        final Setting<Boolean> armorRendered = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("render.armor_model")
                .setComment("Enables or disables wolf armor layer rendering. Disable if you have an unsupported model mod");

        final Setting<Boolean> chestRendered = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("render.chest_model")
                .setComment("Enables or disables wolf chest layer rendering. Disable if you have an unsupported model mod");

        final Setting<Boolean> healthInGui = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("inventory.display_health")
                .setComment("Enables or disables displaying the wolf health amount in the inventory");

        final Setting<Boolean> armorInGui = new BooleanSetting(true)
                .setCategory(CATEGORY)
                .setName("inventory.display_armor")
                .setComment("Enables or disables displaying the wolf armor amount in the inventory");

        final Setting<Boolean> wolfStatsRendered = new BooleanSetting(false)
                .setCategory(CATEGORY)
                .setName("render.stats")
                .setComment("Enables or disables health, armor, and food stat rendering when looking at one of your tamed wolves.");
    }

    private Configuration configuration;
    private final Map<String, Setting<?>> settings = Maps.newLinkedHashMap();

    @SideOnly(Side.CLIENT)
    private boolean connected;

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
    public Collection<Setting<?>> getSettings() { return this.settings.values(); }
    public Setting<?> getSetting(String fullName) { return this.settings.get(fullName); }

    public void load() {
        if (this.settings.size() == 0) return;
        this.configuration.load();

        this.getSettings().forEach(setting -> setting.loadFromConfiguration(this.configuration));
    }

    public void save() {
        this.getSettings().forEach(setting -> setting.saveToConfiguration(this.configuration));
        this.configuration.save();
    }

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

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.connected = true;
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (!this.connected) return;
        this.getSettings().forEach(Setting::onDisconnect);
        this.connected = false;
    }

    /**
     * Checks if wolf chests are enabled or disabled
     *
     * @return <c>true</c> if wolf chests are enabled, <c>false</c> if they are disabled.
     */
    public boolean getChestEnabled() { return this.chestEnabled.getCurrentValue(); }

    /**
     * Checks if untamed wolves should howl at the full moon when untamed
     *
     * @return <c>true</c> if they should howl, <c>false</c> otherwise
     */
    public boolean getHowlAtMoonEnabled() { return this.behavior.howlAtMoonEnabled.getCurrentValue(); }

    /**
     * Checks whether or not wolves should eat food present in their inventory
     *
     * @return <c>true</c> if wolves should auto-heal, <c>false</c> if not
     */
    public boolean getAutoHealEnabled() { return this.behavior.autoHealEnabled.getCurrentValue(); }

    /**
     * Checks if the armor layers should be rendered
     *
     * @return <c>true</c> if the armor layers should be rendered, <c>false</c> otherwise
     */
    public boolean getArmorRendered() { return this.client.armorRendered.getCurrentValue(); }

    /**
     * Checks if the chest model should be rendered
     *
     * @return <c>true</c> if the chests should be rendered; otherwise, <c>false</c>
     */
    public boolean getChestRendered() { return this.client.chestRendered.getCurrentValue(); }

    /**
     * Checks if the wolf's health should be displayed in the GUI
     *
     * @return <c>true</c> if the health should be displayed; otherwise, <c>false</c>
     */
    public boolean getHealthInGui() { return this.client.healthInGui.getCurrentValue(); }

    /**
     * Checks if the wolf's equipped armor level should be displayed in the GUI
     *
     * @return <c>true</c> if the armor should be displayed; otherwise, <c>false</c>
     */
    public boolean getArmorInGui() { return this.client.armorInGui.getCurrentValue(); }

    /**
     * Gets the current configured wolf chest size
     *
     * @return The current configured wolf chest size
     */
    public WolfInventorySize getChestSize() { return this.chestSize.getCurrentValue(); }

    public WolfFoodStatsLevel getFoodStatsLevel() { return this.behavior.useFoodStats.getCurrentValue(); }

    public boolean getCanStarve() { return this.behavior.wolvesCanStarve.getCurrentValue(); }

    public boolean getStatsRendered() { return this.client.wolfStatsRendered.getCurrentValue(); }

    /**
     * Initializes the config file and directory
     *
     * @param configDirectory The path to the directory where the config file is stored
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
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onConfigChanged(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if (!event.getModID().equals(Resources.MOD_ID)) return;

        if (this.configuration.hasChanged()) {
            if (event.isWorldRunning()) {
                WolfArmorMod.getNetworkChannel().sendToAllWhere(ConfigSyncMessage.create(), WolfArmorConfig::isNotServerOwner);
            }
            this.save();
        }
    }

    private static boolean isNotServerOwner(EntityPlayer player) {
        return player.getName().equalsIgnoreCase(FMLCommonHandler.instance().getMinecraftServerInstance().getServerOwner());
    }
}
