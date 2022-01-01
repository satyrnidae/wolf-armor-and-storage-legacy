package dev.satyrn.wolfarmor.api.compatibility;

import dev.satyrn.wolfarmor.api.compatibility.client.LayerProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Wolf armor interoperability provider
 * @author Isabel Maskrey (satyrnidae)
 * @since 4.5.0-alpha
 */
public abstract class CompatibilityProvider implements IProvider {
    /**
     * The mod ID which is handled by this provider
     * @return The mod ID which is provided
     * @since 4.5.0-alpha
     */
    public abstract String getModId();

    /**
     * Method which is called on loadComplete which can register custom renderers
     * @since 4.5.0-alpha-2
     */
    @SideOnly(Side.CLIENT)
    public void setupClient() { }

    /**
     * Method which is called on loadComplete which can perform non-client setup.
     */
    public void setup() { }

    /**
     * Method called during init and will allow overrides of the default wolf model
     * @return A list of LayerProviders to override
     * @since 4.5.0-alpha
     */
    @Nonnull
    @SideOnly(Side.CLIENT)
    public List<LayerProvider> getLayerProviders() { return new ArrayList<>(); }
}