package dev.satyrn.wolfarmor.compatibility.ebwizardry;

import dev.satyrn.wolfarmor.api.compatibility.Compatibility;
import dev.satyrn.wolfarmor.api.compatibility.CompatibilityProvider;
import dev.satyrn.wolfarmor.api.compatibility.Provider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@Provider("ebwizardry")
public class WizardryProvider extends CompatibilityProvider {
    /**
     * Method which is called on loadComplete which can perform client-side setup.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void loadComplete_Client(FMLLoadCompleteEvent event) {
        @Nonnull final RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        @Nonnull final String className = "electroblob.wizardry.entity.living.EntitySpiritWolf";

        try {
            Class<?> spiritWolfClass = WizardryProvider.class.getClassLoader().loadClass(className);
            RenderLiving<?> renderWolf = (RenderLiving<?>) manager.entityRenderMap.get(spiritWolfClass);

            if (renderWolf == null) {
                logger.warn("Renderer for {} could not be found.", className);
            } else {
                renderWolf.addLayer(Compatibility.getArmorLayer(renderWolf, "dogslie"));
                renderWolf.addLayer(Compatibility.getBackpackLayer(renderWolf, "dogslie"));
            }
        } catch (ClassNotFoundException e) {
            logger.warn("Failed to load class {}: {}", className, e.toString());
        }
    }
}
