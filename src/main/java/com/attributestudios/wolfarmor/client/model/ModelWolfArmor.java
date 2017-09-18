package com.attributestudios.wolfarmor.client.model;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.ReflectionCache;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Models wolf armor overlays since the base ModelWolf does not support scaling
 */
@SideOnly(Side.CLIENT)
public class ModelWolfArmor extends ModelWolf {
    //region Fields

    private static final String WOLF_TAIL_SRG = "field_78180_g";
    private static final String WOLF_MANE_SRG = "field_78186_h";

    //endregion Fields

    //region Constructors

    /**
     * Create a wolf armor model with the specified scale.
     *
     * @param scale The scale of the model
     */
    public ModelWolfArmor(float scale) {
        this.wolfHeadMain = new ModelRenderer(this, 0, 0);
        this.wolfHeadMain.addBox(-2, -3, -2, 6, 6, 4, scale);
        this.wolfHeadMain.setRotationPoint(-1, 13.5F, -7);
        this.wolfBody = new ModelRenderer(this, 18, 14);
        this.wolfBody.addBox(-3, -2, -3, 6, 9, 6, scale);
        this.wolfBody.setRotationPoint(0, 14, 2);
        this.wolfLeg1 = new ModelRenderer(this, 0, 18);
        this.wolfLeg1.addBox(0, 0, -1, 2, 8, 2, scale);
        this.wolfLeg1.setRotationPoint(-2.5F, 16, 7);
        this.wolfLeg2 = new ModelRenderer(this, 0, 18);
        this.wolfLeg2.addBox(0, 0, -1, 2, 8, 2, scale);
        this.wolfLeg2.setRotationPoint(0.5F, 16, 7);
        this.wolfLeg3 = new ModelRenderer(this, 0, 18);
        this.wolfLeg3.addBox(0, 0, -1, 2, 8, 2, scale);
        this.wolfLeg3.setRotationPoint(-2.5F, 16, -4);
        this.wolfLeg4 = new ModelRenderer(this, 0, 18);
        this.wolfLeg4.addBox(0, 0, -1, 2, 8, 2, scale);
        this.wolfLeg4.setRotationPoint(0.5F, 16, -4);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(-2, -5, 0, 2, 2, 1, scale);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(2, -5, 0, 2, 2, 1, scale);
        this.wolfHeadMain.setTextureOffset(0, 10).addBox(-0.5F, 0, -5, 3, 3, 4, scale);

        reflectAdditionalMembers(scale);
    }

    //endregion Constructors

    //region Private Methods

    /**
     * Set up members that aren't directly accessible from subclasses
     *
     * @param scale The scale of the model.
     */
    private void reflectAdditionalMembers(float scale) {
        ModelRenderer wolfMane = new ModelRenderer(this, 21, 0);
        wolfMane.addBox(-3, -3, -3, 8, 6, 7, scale);
        wolfMane.setRotationPoint(-1, 14, 2);
        ModelRenderer wolfTail = new ModelRenderer(this, 9, 18);
        wolfTail.addBox(0, 0, -1, 2, 8, 2, scale);
        wolfTail.setRotationPoint(-1, 12, 8);

        try {
            ReflectionCache.getField(ModelWolf.class, WOLF_TAIL_SRG, "wolfTail").set(this, wolfTail);
            ReflectionCache.getField(ModelWolf.class, WOLF_MANE_SRG, "wolfMane").set(this, wolfMane);
        } catch (IllegalAccessException ex) {
            WolfArmorMod.getLogger().fatal(ex);
            throw new RuntimeException("Failed to initialize non-public members in ModelWolfArmor.reflectAdditionalMembers", ex);
        }
    }

    //endregion Private Methods
}
