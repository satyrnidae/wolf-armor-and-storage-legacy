package dev.satyrn.wolfarmor.api.compatibility;

import dev.satyrn.wolfarmor.api.compatibility.client.LayerProvider;
import dev.satyrn.wolfarmor.api.util.Resources;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Wolf armor interoperability provider
 * @author Isabel Maskrey (satyrnidae)
 * @since 4.5.0-alpha
 */

public abstract class CompatibilityProvider extends Provider.ProviderType {
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

    protected final static Logger logger = LogManager.getLogger(Resources.MOD_ID);
}