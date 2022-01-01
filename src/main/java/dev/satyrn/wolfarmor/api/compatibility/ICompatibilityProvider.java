package dev.satyrn.wolfarmor.api.compatibility;

import dev.satyrn.wolfarmor.api.compatibility.client.LayerProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Wolf armor interoperability provider
 * @author Isabel Maskrey (satyrnidae)
 * @since 4.5.0-alpha
 */
public interface ICompatibilityProvider extends IProvider {
    /**
     * The mod ID which is handled by this provider
     * @return The mod ID which is provided
     * @since 4.5.0-alpha
     */
    String getModId();

    /**
     * Method which is called on loadComplete which can register custom renderers
     * @param side The distribution this is running on
     * @since 4.5.0-alpha-2
     */
    void setup(Side side);

    /**
     * Method called during init and will allow overrides of the default wolf model
     * @return A list of LayerProviders to override
     * @since 4.5.0-alpha
     */
    @SideOnly(Side.CLIENT)
    default List<LayerProvider> getLayerProviders() { return null; }
}