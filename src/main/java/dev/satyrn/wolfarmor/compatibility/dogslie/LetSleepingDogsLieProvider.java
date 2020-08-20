package dev.satyrn.wolfarmor.compatibility.dogslie;

import dev.satyrn.wolfarmor.api.compatibility.ICompatibilityProvider;
import dev.satyrn.wolfarmor.api.compatibility.client.LayerProvider;
import dev.satyrn.wolfarmor.compatibility.dogslie.client.LetSleepingDogsLieLayerProvider;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class LetSleepingDogsLieProvider implements ICompatibilityProvider {
    /**
     * The mod ID which is handled by this provider
     *
     * @return The mod ID which is provided
     * @since 4.5.0-alpha
     */
    @Override
    public String getModId() {
        return "dogslie";
    }

    /**
     * Method which is called on loadComplete which can register custom renderers
     *
     * @param side The distribution this is running on
     * @since 4.5.0-alpha-2
     */
    @Override
    public void setup(Side side) { }

    /**
     * Method called during init and will allow overrides of the default wolf model
     *
     * @return A list of LayerProviders to override
     * @since 4.5.0-alpha
     */
    @Override
    public List<LayerProvider> getLayerProviders() {
        LayerProvider letSleepingDogsLie = new LetSleepingDogsLieLayerProvider();
        List<LayerProvider> providers = new ArrayList<>();
        providers.add(letSleepingDogsLie);
        return providers;
    }
}
