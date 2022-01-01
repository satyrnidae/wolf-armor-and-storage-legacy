package dev.satyrn.wolfarmor.api.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Creates render layers for wolf armor and backpacks
 * @author Isabel Maskrey (satyrnidae)
 * @deprecated since 4.5.0, use {@link dev.satyrn.wolfarmor.api.compatibility.Compatibility} instead
 */
@SideOnly(Side.CLIENT)
@Deprecated
public abstract class RenderLayerFactory {

    /**
     * Constructs a new instance of the wolf armor render layer.
     * @param renderLiving The parent renderer.
     * @return <tt>null</tt> if the class load or instantiation failed; otherwise, a new instance of LayerWolfArmor.
     * @deprecated since 4.5.0, use {@link dev.satyrn.wolfarmor.api.compatibility.Compatibility#getArmorLayer(RenderLiving)} instead
     */
    @Nullable
    public static LayerRenderer<?> createArmorLayer(@Nonnull RenderLiving<?> renderLiving) {
        try {
            Class<?> clazz = RenderLayerFactory.class.getClassLoader().loadClass("dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfArmor");
            return (LayerRenderer<?>) clazz.getConstructor(RenderLiving.class).newInstance(renderLiving);
        }
        catch(Exception e) {
            return null;
        }
    }

    /**
     * Constructs a new wolf backpack layer renderer object.
     * @param renderLiving The parent renderer.
     * @return <tt>null</tt> if the class load or instantiation failed; otherwise, a new instance of LayerWolfBackpack.
     * @deprecated since 4.5.0, use {@link dev.satyrn.wolfarmor.api.compatibility.Compatibility#getBackpackLayer(RenderLiving)} instead
     */
    @Nullable
    public static LayerRenderer<?> createBackpackLayer(@Nonnull RenderLiving<?> renderLiving) {
        try {
            Class<?> clazz = RenderLayerFactory.class.getClassLoader().loadClass("dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack");
            return (LayerRenderer<?>) clazz.getConstructor(RenderLiving.class).newInstance(renderLiving);
        } catch(Exception e) {
            return null;
        }
    }

}
