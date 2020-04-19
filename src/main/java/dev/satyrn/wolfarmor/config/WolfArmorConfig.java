package dev.satyrn.wolfarmor.config;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.config.IWolfArmorConfig;
import dev.satyrn.wolfarmor.api.config.settings.BooleanSetting;
import dev.satyrn.wolfarmor.api.config.settings.Setting;
import dev.satyrn.wolfarmor.api.config.settings.WolfInventorySizeSetting;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.api.util.WolfInventorySize;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.io.File;

public class WolfArmorConfig implements IWolfArmorConfig {
    private static final String CATEGORY_BEHAVIOR = "behavior";

    private final Setting<Boolean> chestEnabled = new BooleanSetting(true)
            .setCategory(Configuration.CATEGORY_GENERAL)
            .setName("chest.enabled")
            .setComment("Enables or disables wolf chests")
            .setIsSynchronizedSetting(true);

    private final Setting<Boolean> howlAtMoonEnabled = new BooleanSetting(false)
            .setCategory(CATEGORY_BEHAVIOR)
            .setName("howlAtMoon.enabled")
            .setComment("Enables or disables untamed wolves howling at the moon")
            .setIsSynchronizedSetting(true);

    private final Setting<Boolean> autoHealEnabled = new BooleanSetting(true)
            .setCategory(CATEGORY_BEHAVIOR)
            .setName("autoHeal.enabled")
            .setComment("Enables or disables wolves automatically eating food when their health is low")
            .setIsSynchronizedSetting(true);

    private final Setting<WolfInventorySize> chestSize =
            new WolfInventorySizeSetting(new WolfInventorySize(3, 2))
            .setCategory(Configuration.CATEGORY_GENERAL)
            .setName("chest.size")
            .setComment("Sets the horizontal and vertical size of the wolf chest")
            .setIsSynchronizedSetting(true);

    private final Setting<Boolean> armorRendered = new BooleanSetting(true)
            .setCategory(Configuration.CATEGORY_CLIENT)
            .setName("armor.render")
            .setComment("Enables or disables wolf armor layer rendering. Disable if you have an unsupported model mod");

    private final Setting<Boolean> chestRendered = new BooleanSetting(true)
            .setCategory(Configuration.CATEGORY_CLIENT)
            .setName("chest.render")
            .setComment("Enables or disables wolf chest layer rendering. Disable if you have an unsupported model mod");

    private final Setting<Boolean> healthInGui = new BooleanSetting(true)
            .setCategory(Configuration.CATEGORY_CLIENT)
            .setName("gui.showHealth")
            .setComment("Enables or disables displaying the wolf health amount in the inventory");

    private final Setting<Boolean> armorInGui = new BooleanSetting(true)
            .setCategory(Configuration.CATEGORY_CLIENT)
            .setName("gui.showHealth")
            .setComment("Enables or disables displaying the wolf armor amount in the inventory");

    private Configuration configuration;

    /**
     * Checks if wolf chests are enabled or disabled
     *
     * @return <c>true</c> if wolf chests are enabled, <c>false</c> if they are disabled.
     */
    @Override
    public boolean getChestEnabled() { return this.chestEnabled.getCurrentValue(); }

    /**
     * Checks if untamed wolves should howl at the full moon when untamed
     *
     * @return <c>true</c> if they should howl, <c>false</c> otherwise
     */
    @Override
    public boolean getHowlAtMoonEnabled() { return this.howlAtMoonEnabled.getCurrentValue(); }

    /**
     * Checks whether or not wolves should eat food present in their inventory
     *
     * @return <c>true</c> if wolves should auto-heal, <c>false</c> if not
     */
    @Override
    public boolean getAutoHealEnabled() { return this.autoHealEnabled.getCurrentValue(); }

    /**
     * Checks if the armor layers should be rendered
     *
     * @return <c>true</c> if the armor layers should be rendered, <c>false</c> otherwise
     */
    @Override
    public boolean getArmorRendered() { return this.armorRendered.getCurrentValue(); }

    /**
     * Checks if the chest model should be rendered
     *
     * @return <c>true</c> if the chests should be rendered; otherwise, <c>false</c>
     */
    @Override
    public boolean getChestRendered() { return this.chestRendered.getCurrentValue(); }

    /**
     * Checks if the wolf's health should be displayed in the GUI
     *
     * @return <c>true</c> if the health should be displayed; otherwise, <c>false</c>
     */
    @Override
    public boolean getHealthInGui() { return this.healthInGui.getCurrentValue(); }

    /**
     * Checks if the wolf's equipped armor level should be displayed in the GUI
     *
     * @return <c>true</c> if the armor should be displayed; otherwise, <c>false</c>
     */
    @Override
    public boolean getArmorInGui() { return this.armorInGui.getCurrentValue(); }

    /**
     * Gets the current configured wolf chest size
     *
     * @return The current configured wolf chest size
     */
    @Override
    public WolfInventorySize getChestSize() { return this.chestSize.getCurrentValue(); }

    /**
     * Initializes the config file and directory
     *
     * @param configDirectory The path to the directory where the config file is stored
     */
    @Override
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
        this.loadFromConfiguration(FMLClientHandler.instance().getSide());
        if (this.configuration.hasChanged()) {
            this.saveToConfiguration(FMLClientHandler.instance().getSide());
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Loads the settings from the configuration file
     *
     * @param side The thread side which is currently being loaded
     */
    @Override
    public void loadFromConfiguration(Side side) {
        switch (side) {
            case CLIENT:
                this.armorRendered.loadFromConfiguration(this.configuration);
                this.chestRendered.loadFromConfiguration(this.configuration);
                this.healthInGui.loadFromConfiguration(this.configuration);
                this.armorInGui.loadFromConfiguration(this.configuration);
            case SERVER:
                this.chestEnabled.loadFromConfiguration(this.configuration);
                this.howlAtMoonEnabled.loadFromConfiguration(this.configuration);
                this.autoHealEnabled.loadFromConfiguration(this.configuration);
                this.chestSize.loadFromConfiguration(this.configuration);
        }
    }

    /**
     * Saves the current settings to the configuration file
     */
    @Override
    public void saveToConfiguration(Side side) {
        switch (side) {
            case CLIENT:
                this.armorRendered.saveToConfiguration(this.configuration);
                this.chestRendered.saveToConfiguration(this.configuration);
                this.healthInGui.saveToConfiguration(this.configuration);
                this.armorInGui.saveToConfiguration(this.configuration);
            case SERVER:
                this.chestEnabled.saveToConfiguration(this.configuration);
                this.howlAtMoonEnabled.saveToConfiguration(this.configuration);
                this.autoHealEnabled.saveToConfiguration(this.configuration);
                this.chestSize.saveToConfiguration(this.configuration);
        }

        this.configuration.save();
    }

    @SubscribeEvent
    public void onConfigChanged(@Nonnull ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(Resources.MOD_ID)) {
           this.loadFromConfiguration(FMLClientHandler.instance().getSide());

           if (this.configuration.hasChanged()) {
              this.saveToConfiguration(FMLClientHandler.instance().getSide());
           }
        }
    }

}
