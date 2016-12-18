package com.attributestudios.wolfarmor.client.gui.config;

import com.attributestudios.wolfarmor.WolfArmorConfiguration;
import com.attributestudios.wolfarmor.WolfArmorMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
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
     * @param parent The parent screen.
     */
    public WolfArmorGuiConfig(GuiScreen parent) {
        super(parent, getConfigurationElements(), WolfArmorMod.MOD_ID, false, false, I18n.format("gui.wolfarmor.config"), I18n.format("gui.wolfarmor.configSubtitle"));
    }

    //endregion Constructors

    //region Private Methods

    /**
     * Creates a list of config entries for display
     * @return A list of config entries
     */
    @Nonnull
    private static List<IConfigElement> getConfigurationElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        WolfArmorConfiguration configuration = WolfArmorMod.getConfiguration();

        // requires world restart
        list.add(new ConfigElement(configuration.getSettingWolfChestsEnabled().setRequiresWorldRestart(true)));

        // does not require world restart
        list.add(new ConfigElement(configuration.getSettingWolfArmorRenderEnabled()));
        list.add(new ConfigElement(configuration.getSettingWolfChestRenderEnabled()));
        list.add(new ConfigElement(configuration.getSettingWolfArmorDisplayEnabled()));
        list.add(new ConfigElement(configuration.getSettingWolfHealthDisplayEnabled()));

        return list;
    }

    //endregion Private Methods
}
