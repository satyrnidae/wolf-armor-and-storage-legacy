package dev.satyrn.wolfarmor.coremod;


import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("@MCVERSION@")
@IFMLLoadingPlugin.TransformerExclusions("dev.satyrn.wolfarmor.core")
public class WolfArmorCore implements IFMLLoadingPlugin {

    public WolfArmorCore() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.wolfarmor.core.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) { }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
