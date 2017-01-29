package com.attributestudios.wolfarmor.client.gui.config;

import com.attributestudios.wolfarmor.WolfArmorConfiguration;
import com.attributestudios.wolfarmor.WolfArmorMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration GUI for the Wolf Armor mod.
 */
@SideOnly(Side.CLIENT)
public class WolfArmorGuiConfig extends GuiConfig {
    //region Constructors

    /**
     * Creates a new Wolf Armor configuration GUI.
     *
     * @param parent The parent screen.
     */
    public WolfArmorGuiConfig(@Nonnull GuiScreen parent) {
        super(parent,
                getConfigurationElements(),
                WolfArmorMod.MOD_ID,
                false,
                false,
                I18n.format("gui.wolfarmor.config"),
                I18n.format("gui.wolfarmor.config.subtitle"));
    }

    //endregion Constructors

    //region Private Methods

    /**
     * Creates a list of config entries for display
     *
     * @return A list of config entries
     */
    @Nonnull
    private static List<IConfigElement> getConfigurationElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        WolfArmorConfiguration configuration = WolfArmorMod.getConfiguration();

        ConfigCategory general = new ConfigCategory(I18n.format("gui.wolfarmor.config.category.general"));
        {
            Property wolfChestsEnabled = configuration.getSettingWolfChestsEnabled();
            wolfChestsEnabled.setRequiresWorldRestart(true);
            wolfChestsEnabled.setComment(I18n.format(wolfChestsEnabled.getName() + ".comment"));
            general.put(wolfChestsEnabled.getName(), wolfChestsEnabled);
        }

        ConfigCategory behavior = new ConfigCategory(I18n.format("gui.wolfarmor.config.category.behavior"));
        {
            Property howlingUntamedWolvesEnabled = configuration.getSettingHowlingUntamedWolvesEnabled();
            howlingUntamedWolvesEnabled.setComment(I18n.format(howlingUntamedWolvesEnabled.getName() + ".comment"));
            behavior.put(howlingUntamedWolvesEnabled.getName(), howlingUntamedWolvesEnabled);
        }

        ConfigCategory client = new ConfigCategory(I18n.format("gui.wolfarmor.config.category.client"));
        {
            ConfigCategory render = new ConfigCategory(I18n.format("gui.wolfarmor.config.category.client.render"), client);
            {
                Property wolfArmorRenderEnabled = configuration.getSettingWolfArmorRenderEnabled();
                wolfArmorRenderEnabled.setComment(I18n.format(wolfArmorRenderEnabled.getName() + ".comment"));
                render.put(wolfArmorRenderEnabled.getName(), wolfArmorRenderEnabled);

                Property wolfChestRenderEnabled = configuration.getSettingWolfChestRenderEnabled();
                wolfChestRenderEnabled.setComment(I18n.format(wolfChestRenderEnabled.getName() + ".comment"));
                render.put(wolfChestRenderEnabled.getName(), wolfChestRenderEnabled);
            }

            ConfigCategory gui = new ConfigCategory(I18n.format("gui.wolfarmor.config.category.client.gui"), client);
            {
                Property wolfArmorDisplayEnabled = configuration.getSettingWolfArmorDisplayEnabled();
                wolfArmorDisplayEnabled.setComment(I18n.format(wolfArmorDisplayEnabled.getName() + ".comment"));
                gui.put(wolfArmorDisplayEnabled.getName(), wolfArmorDisplayEnabled);

                Property wolfHealthDisplayEnabled = configuration.getSettingWolfHealthDisplayEnabled();
                wolfHealthDisplayEnabled.setComment(I18n.format(wolfHealthDisplayEnabled.getName() + ".comment"));
                gui.put(wolfHealthDisplayEnabled.getName(), wolfHealthDisplayEnabled);
            }
        }

        // requires world restart
        list.add(new ConfigElement(general));

        // does not require world restart
        list.add(new ConfigElement(behavior));
        list.add(new ConfigElement(client));


        return list;
    }

    //endregion Private Methods
}
