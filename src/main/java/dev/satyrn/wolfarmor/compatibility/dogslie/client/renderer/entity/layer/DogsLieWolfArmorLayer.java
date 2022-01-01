package dev.satyrn.wolfarmor.compatibility.dogslie.client.renderer.entity.layer;

import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import dev.satyrn.wolfarmor.compatibility.dogslie.client.model.DogsLieWolfArmorModel;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class DogsLieWolfArmorLayer extends LayerWolfArmor {

    /**
     * Creates a new Wolf Armor layer renderer
     *
     * @param renderer The parent renderer.
     */
    public DogsLieWolfArmorLayer(@Nonnull RenderLiving<?> renderer) {
        super(renderer);
    }

    /**
     * Initializes the armor layers.
     */
    @Override
    protected void initArmor() {
        DogsLieWolfArmorModel innerLayer = new DogsLieWolfArmorModel(0.1F);
        DogsLieWolfArmorModel outerLayer = new DogsLieWolfArmorModel(0.2F);

        this.setArmorInnerLayer(innerLayer);
        this.setArmorOuterLayer(outerLayer);
    }
}
