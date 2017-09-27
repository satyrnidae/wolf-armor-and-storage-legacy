package com.attributestudios.wolfarmor.client.gui.config;

import com.attributestudios.wolfarmor.api.util.annotation.DynamicallyUsed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * GUI Factory for the WolfArmor Mod
 */
@SideOnly(Side.CLIENT)
@DynamicallyUsed
public class WolfArmorGuiFactory implements IModGuiFactory {
    //region Public / Protected Methods

    /**
     * Called when instantiated to initialize with the active minecraft instance.
     *
     * @param minecraftInstance the instance
     */
    @Override
    public void initialize(@Nonnull Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen guiScreen) {
        return new WolfArmorGuiConfig(guiScreen);
    }

    @Override
    @Nullable
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    //endregion Public / Protected Methods
}
