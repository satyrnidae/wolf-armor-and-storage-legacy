package dev.satyrn.wolfarmor.compatibility.sophisticatedwolves;

import dev.satyrn.wolfarmor.api.compatibility.CompatibilityProvider;
import dev.satyrn.wolfarmor.api.compatibility.Provider;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sophisticated_wolves.RenderSophisticatedWolf;
import sophisticated_wolves.entity.EntitySophisticatedWolf;

import java.util.HashMap;
import java.util.Map;

@Provider("sophisticatedwolves")
public class SophisticatedWolvesProvider extends CompatibilityProvider {

    private static final Logger logger = LogManager.getLogger(Resources.MOD_ID);

    /**
     * Method which is called on loadComplete which can register custom renderers
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void setupClient() {
        // This feels awful but works great so... whatever?
        final Map<Class<? extends Entity>, Render<? extends Entity>> oldEntityRenderersPendingLoad = new HashMap<>();
        RenderingRegistry.loadEntityRenderers(oldEntityRenderersPendingLoad);
        RenderSophisticatedWolf renderer =
                (RenderSophisticatedWolf) oldEntityRenderersPendingLoad.get(EntitySophisticatedWolf.class);

        if (renderer != null) {
            LayerRenderer<?> layerArmor = new LayerWolfArmor(renderer);
            LayerRenderer<?> layerBackpack = new LayerWolfBackpack(renderer);

            renderer.addLayer(layerArmor);
            renderer.addLayer(layerBackpack);
        } else {
            logger.warn("Unable to find renderer for EntitySophisticatedWolf!");
        }
    }
}
