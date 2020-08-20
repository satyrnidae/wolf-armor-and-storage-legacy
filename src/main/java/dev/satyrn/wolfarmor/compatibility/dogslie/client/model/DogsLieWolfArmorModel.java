package dev.satyrn.wolfarmor.compatibility.dogslie.client.model;

import me.ichun.letsleepingdogslie.common.model.ModelWolf;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Models wolf armor overlays since the base ModelWolf does not support scaling
 */
@SideOnly(Side.CLIENT)
public class DogsLieWolfArmorModel extends ModelWolf {
    /**
     * Create a wolf armor model with the specified scale.
     *
     * @param scale The scale of the model
     */
    public DogsLieWolfArmorModel(float scale) {
        this.wolfHeadMain = new ModelRenderer(this, 0, 0);
        this.wolfHeadMain.addBox(-2, -3, -2, 6, 6, 4, scale);
        this.wolfHeadMain.setRotationPoint(-1, 13.5F, -7);
        this.wolfBody = new ModelRenderer(this, 18, 14);
        this.wolfBody.addBox(-3, -2, -3, 6, 9, 6, scale);
        this.wolfBody.setRotationPoint(0, 14, 2);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(-2, -5, 0, 2, 2, 1, scale);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(2, -5, 0, 2, 2, 1, scale);
        this.wolfHeadMain.setTextureOffset(0, 10).addBox(-0.5F, 0, -5, 3, 3, 4, scale);
        this.wolfTail = new ModelRenderer(this, 9, 18);
        this.wolfTail.addBox(0, 0, -1, 2, 8, 2, scale);
        this.wolfTail.setRotationPoint(-1, 12, 8);

        // Below were altered to match Let Sleeping Dogs Lie
        this.wolfMane = new ModelRenderer(this, 21, 0);
        this.wolfMane.addBox(-4, -3, -3, 8, 6, 7, scale);
        this.wolfMane.setRotationPoint(0, 14, 2);
        this.wolfLeg1 = new ModelRenderer(this, 0, 18);
        this.wolfLeg1.addBox(-1, 0, -1, 2, 8, 2, scale);
        this.wolfLeg1.setRotationPoint(-1.5F, 16, 7);
        this.wolfLeg2 = new ModelRenderer(this, 0, 18);
        this.wolfLeg2.addBox(-1, 0, -1, 2, 8, 2, scale);
        this.wolfLeg2.setRotationPoint(1.5F, 16, 7);
        this.wolfLeg3 = new ModelRenderer(this, 0, 18);
        this.wolfLeg3.addBox(-1, 0, -1, 2, 8, 2, scale);
        this.wolfLeg3.setRotationPoint(-1.5F, 16, -4);
        this.wolfLeg4 = new ModelRenderer(this, 0, 18);
        this.wolfLeg4.addBox(-1, 0, -1, 2, 8, 2, scale);
        this.wolfLeg4.setRotationPoint(1.5F, 16, -4);
    }
}
