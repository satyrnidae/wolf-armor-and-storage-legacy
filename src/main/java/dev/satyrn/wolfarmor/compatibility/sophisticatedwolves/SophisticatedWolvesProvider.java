package dev.satyrn.wolfarmor.compatibility.sophisticatedwolves;

import dev.satyrn.wolfarmor.api.compatibility.Compatibility;
import dev.satyrn.wolfarmor.api.compatibility.CompatibilityProvider;
import dev.satyrn.wolfarmor.api.compatibility.Provider;
import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@Provider("sophisticatedwolves")
public class SophisticatedWolvesProvider extends CompatibilityProvider {
    /**
     * Method which is called on loadComplete which can register custom renderers
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void setupClient() {
        @Nonnull final String className = "sophisticated_wolves.entity.EntitySophisticatedWolf";
        @Nonnull final Map<Class<? extends Entity>, Render<? extends Entity>> pendingRenderers = new HashMap<>();
        RenderingRegistry.loadEntityRenderers(pendingRenderers);

        try {
            Class<?> entitySophisticatedWolfClass = SophisticatedWolvesProvider.class.getClassLoader().loadClass(className);
            RenderLiving<?> render = (RenderLiving<?>) pendingRenderers.get(entitySophisticatedWolfClass);
            if (render == null) {
                logger.warn("Renderer for {} could not be found.", className);
            } else {
                render.addLayer(Compatibility.getArmorLayer(render, "!dogslie"));
                render.addLayer(Compatibility.getBackpackLayer(render, "!dogslie"));
            }
        } catch (ClassNotFoundException e) {
            logger.warn("Failed to load class {}: {}", className, e.toString());
        }
    }
}
