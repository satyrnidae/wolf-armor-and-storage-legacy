package dev.satyrn.wolfarmor.compatibility.dogslie;

import dev.satyrn.wolfarmor.api.compatibility.Provider;
import dev.satyrn.wolfarmor.api.compatibility.client.LayerProvider;
import dev.satyrn.wolfarmor.compatibility.dogslie.layer.DogsLieBackpackLayer;
import dev.satyrn.wolfarmor.compatibility.dogslie.layer.DogsLieWolfArmorLayer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
@Provider(value = "dogslie", priority = 255)
public class LetSleepingDogsLieLayerProvider extends LayerProvider {
    /**
     * Checks whether the provider provides an armor layer.
     *
     * @return {@code false} by default, if not respecified as {@code true} this provider will not be used for armor
     * layers
     * @since 4.5.0-alpha
     */
    @Override
    public boolean getProvidesArmorLayer() {
        return true;
    }

    /**
     * Checks whether the provider provides a backpack layer
     *
     * @return {@code false} by default, if not respecified as {@code true} this provider will not be used for armor
     * layers
     * @since 4.5.0-alpha
     */
    @Override
    public boolean getProvidesBackpackLayer() {
        return true;
    }

    /**
     * Returns the custom armor layer for this provider
     *
     * @param entityRenderer The entity renderer
     * @return A new layer renderer
     * @since 4.5.0-alpha
     */
    @Override
    public LayerRenderer<?> getArmorLayer(@Nonnull RenderLiving<?> entityRenderer) {
        return new DogsLieWolfArmorLayer(entityRenderer);
    }

    /**
     * Returns the custom backpack layer for this provider
     *
     * @param entityRenderer The entity renderer
     * @return A new layer renderer
     * @since 4.5.0-alpha
     */
    @Nullable
    @Override
    public LayerRenderer<?> getBackpackLayer(@Nonnull RenderLiving<?> entityRenderer) {
        return new DogsLieBackpackLayer(entityRenderer);
    }
}
