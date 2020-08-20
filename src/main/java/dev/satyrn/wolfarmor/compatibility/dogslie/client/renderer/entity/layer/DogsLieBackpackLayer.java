package dev.satyrn.wolfarmor.compatibility.dogslie.client.renderer.entity.layer;

import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import dev.satyrn.wolfarmor.compatibility.dogslie.client.model.DogsLieWolfBackpackModel;
import net.minecraft.client.renderer.entity.RenderLiving;

import javax.annotation.Nonnull;

public class DogsLieBackpackLayer extends LayerWolfBackpack {
    /**
     * Creates a new layer renderer for armored wolf backpacks.
     *
     * @param renderer The parent renderer.
     */
    public DogsLieBackpackLayer(@Nonnull RenderLiving<?> renderer) {
        super(renderer);
        this.modelWolfBackpack = new DogsLieWolfBackpackModel(0);
    }
}
