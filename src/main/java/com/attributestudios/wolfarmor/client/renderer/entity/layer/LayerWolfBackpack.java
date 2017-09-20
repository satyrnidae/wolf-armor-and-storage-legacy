package com.attributestudios.wolfarmor.client.renderer.entity.layer;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.util.Definitions.ResourceLocations.Textures;
import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.client.model.ModelWolfBackpack;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * A layer renderer for wolf backpacks.
 */
@SideOnly(Side.CLIENT)
public class LayerWolfBackpack implements LayerRenderer<EntityWolf> {
    //region Fields

    private ModelWolfBackpack modelWolfBackpack;
    private final RenderLiving renderer;

    //endregion Fields

    //region Constructors

    /**
     * Creates a new layer renderer for armored wolf backpacks.
     *
     * @param renderer The parent renderer.
     */
    public LayerWolfBackpack(@Nonnull RenderLiving renderer) {
        this.renderer = renderer;

        this.modelWolfBackpack = new ModelWolfBackpack(0.0F);
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Renders the layer.
     *
     * @param entityWolf      The wolf to render.  If it is not am EntityWolfArmored, the layer will not render.
     * @param limbSwing       The entity's limb swing progress.
     * @param limbSwingAmount The entity's limb swing progress amount.
     * @param partialTicks    The current game tick.
     * @param ageInTicks      The entity's age.
     * @param netHeadYaw      The yaw of the entity's head.
     * @param headPitch       The pitch of the entity's head.
     * @param scale           The scale at which to render the layer.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void doRenderLayer(@Nonnull EntityWolf entityWolf,
                              float limbSwing,
                              float limbSwingAmount,
                              float partialTicks,
                              float ageInTicks,
                              float netHeadYaw,
                              float headPitch,
                              float scale) {

        if (!WolfArmorMod.getConfiguration().getIsWolfChestRenderEnabled()) {
            return;
        }
        IWolfArmorCapability wolfArmor = entityWolf.getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
        if (wolfArmor != null && wolfArmor.getHasChest()) {
            this.modelWolfBackpack.setModelAttributes(renderer.getMainModel());
            this.modelWolfBackpack.setLivingAnimations(entityWolf, limbSwing, limbSwingAmount, partialTicks);

            this.renderer.bindTexture(Textures.TEXTURE_WOLF_BACKPACK);

            GlStateManager.color(1, 1, 1, 1);

            if (!entityWolf.isInvisible()) {
                this.modelWolfBackpack.render(entityWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            } else {
                GlStateManager.pushMatrix();
                {
                    GlStateManager.color(1, 1, 1, 0.15F);
                    GlStateManager.depthMask(false);
                    {
                        GlStateManager.enableBlend();
                        {
                            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                            this.modelWolfBackpack.render(entityWolf,
                                    limbSwing,
                                    limbSwingAmount,
                                    ageInTicks,
                                    netHeadYaw,
                                    headPitch,
                                    scale);

                        }
                        GlStateManager.disableBlend();
                    }
                    GlStateManager.depthMask(true);
                }
                GlStateManager.popMatrix();
            }
        }
    }

    //endregion Public / Protected Methods

    //region Accessors / Mutators

    /**
     * Whether or not textures should be combined.
     *
     * @return false.
     */
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    //endregion Accessors / Mutators
}
