package dev.satyrn.wolfarmor.compatibility.sophisticatedwolves;

import dev.satyrn.wolfarmor.api.compatibility.Compatibility;
import dev.satyrn.wolfarmor.api.compatibility.ICompatibilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.fml.relauncher.Side;
import sophisticated_wolves.entity.EntitySophisticatedWolf;

public class SophisticatedWolvesProvider implements ICompatibilityProvider {
    /**
     * Gets the mod ID for which this instance is providing compatibility
     * @return {@code sophisticatedwolves}
     */
    @Override
    public String getModId() {
        return "sophisticatedwolves";
    }

    /**
     * Method which is called on loadComplete which can register custom renderers
     */
    @Override
    public void setup(Side side) {
        if (side != Side.CLIENT) return;

        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        @SuppressWarnings("unchecked")
        RenderLiving<? extends EntityWolf> renderer =
                (RenderLiving<? extends EntityWolf>)manager.entityRenderMap.get(EntitySophisticatedWolf.class);

        if (renderer != null) {
            LayerRenderer<?> layerArmor = Compatibility.getArmorLayer(renderer);
            LayerRenderer<?> layerBackpack = Compatibility.getBackpackLayer(renderer);

            renderer.addLayer(layerArmor);
            renderer.addLayer(layerBackpack);
        }
    }
}
