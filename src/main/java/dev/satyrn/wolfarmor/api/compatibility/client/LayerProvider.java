package dev.satyrn.wolfarmor.api.compatibility.client;

import dev.satyrn.wolfarmor.api.compatibility.IProvider;
import dev.satyrn.wolfarmor.api.util.Resources;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

/**
 * Overrides the armor or backpack layer renders for a wolf.
 * @author Isabel Maskrey (satyrnidae)
 * @since 4.5.0-alpha
 */
@SideOnly(Side.CLIENT)
public class LayerProvider implements IProvider {

    /**
     * Checks whether or not the provider provides an armor layer.
     * @return {@code true} by default, if not overridden as {@code false} this provider will be used for armor
     *         layers
     * @since 4.5.0-alpha
     */
    public boolean getProvidesArmorLayer() { return true; }

    /**
     * Checks whether or not the provider provides a backpack layer
     * @return {@code true} by default, if not overridden as {@code false} this provider will be used for armor
     *         layers
     * @since 4.5.0-alpha
     */
    public boolean getProvidesBackpackLayer() { return true; }

    /**
     * Returns the custom armor layer for this provider
     * @param entityRenderer The entity renderer
     * @return A new layer renderer
     * @since 4.5.0-alpha
     */
    @Nullable
    public LayerRenderer<?> getArmorLayer(@Nonnull RenderLiving<?> entityRenderer) {
        try {
            Class<?> layerClass = this.getClass().getClassLoader().loadClass("dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfArmor" );
            return (LayerRenderer<?>)layerClass.getConstructor(RenderLiving.class).newInstance(entityRenderer);
        } catch (InstantiationException | IllegalArgumentException | IllegalAccessException
                | InvocationTargetException | ExceptionInInitializerError | NoSuchMethodException
                | SecurityException | ClassCastException | ClassNotFoundException ex) {
            LogManager.getLogger(Resources.MOD_ID).error(ex);
            return null;
        }
    }

    /**
     * Returns the custom backpack layer for this provider
     * @param entityRenderer The entity renderer
     * @return A new layer renderer
     * @since 4.5.0-alpha
     */
    @Nullable
    public LayerRenderer<?> getBackpackLayer(@Nonnull RenderLiving<?> entityRenderer) {
        try {
            Class<?> layerClass = LayerProvider.class.getClassLoader().loadClass( "dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack");
            return (LayerRenderer<?>)layerClass.getConstructor(RenderLiving.class).newInstance(entityRenderer);
        } catch (InstantiationException | IllegalArgumentException | IllegalAccessException
                | InvocationTargetException | ExceptionInInitializerError | NoSuchMethodException
                | SecurityException | ClassCastException | ClassNotFoundException ex) {
            LogManager.getLogger(Resources.MOD_ID).error(ex);
            return null;
        }
    }
}
