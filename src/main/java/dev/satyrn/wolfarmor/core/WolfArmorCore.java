package dev.satyrn.wolfarmor.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Coremod implementation to load mixins.
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.0.20
 */
@IFMLLoadingPlugin.MCVersion("${mcversion}")
@IFMLLoadingPlugin.TransformerExclusions("dev.satyrn.wolfarmor.core")
public class WolfArmorCore implements IFMLLoadingPlugin {

    public WolfArmorCore() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.wolfarmor.core.json");
    }

    @Override public String[] getASMTransformerClass() { return new String[0]; }

    @Override public String getModContainerClass() { return null; }

    @Override @Nullable public String getSetupClass() { return null; }

    @Override public void injectData(Map<String, Object> data) { }

    @Override public String getAccessTransformerClass() { return null; }
}
