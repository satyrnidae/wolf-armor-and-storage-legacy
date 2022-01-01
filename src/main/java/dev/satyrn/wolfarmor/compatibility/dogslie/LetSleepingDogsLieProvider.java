package dev.satyrn.wolfarmor.compatibility.dogslie;

import dev.satyrn.wolfarmor.api.compatibility.CompatibilityProvider;
import dev.satyrn.wolfarmor.api.compatibility.client.LayerProvider;
import dev.satyrn.wolfarmor.compatibility.dogslie.client.LetSleepingDogsLieLayerProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LetSleepingDogsLieProvider extends CompatibilityProvider {
    /**
     * The mod ID which is handled by this provider
     *
     * @return The mod ID which is provided
     * @since 4.5.0-alpha
     */
    public String getModId() {
        return "dogslie";
    }

    /**
     * Method called during init and will allow overrides of the default wolf model
     *
     * @return A list of LayerProviders to override
     * @since 4.5.0-alpha
     */
    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public List<LayerProvider> getLayerProviders() {
        LayerProvider letSleepingDogsLie = new LetSleepingDogsLieLayerProvider();
        List<LayerProvider> providers = new ArrayList<>();
        providers.add(letSleepingDogsLie);
        return providers;
    }
}
