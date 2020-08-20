package dev.satyrn.wolfarmor.client.renderer.entity.layer;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.client.model.ModelWolfBackpack;
import net.minecraft.client.model.ModelBase;
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

    protected ModelBase modelWolfBackpack;
    private final RenderLiving<?> renderer;

    //endregion Fields

    //region Constructors

    /**
     * Creates a new layer renderer for armored wolf backpacks.
     *
     * @param renderer The parent renderer.
     */
    public LayerWolfBackpack(@Nonnull RenderLiving<?> renderer) {
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
    @Override
    public void doRenderLayer(@Nonnull EntityWolf entityWolf,
                              float limbSwing,
                              float limbSwingAmount,
                              float partialTicks,
                              float ageInTicks,
                              float netHeadYaw,
                              float headPitch,
                              float scale) {

        if (!WolfArmorMod.getConfig().getChestRendered()) {
            return;
        }
        IArmoredWolf wolfArmor = (IArmoredWolf)entityWolf;
        if (wolfArmor.getHasChest()) {
            this.modelWolfBackpack.setModelAttributes(renderer.getMainModel());
            this.modelWolfBackpack.setLivingAnimations(entityWolf, limbSwing, limbSwingAmount, partialTicks);

            this.renderer.bindTexture(Resources.TEXTURE_WOLF_BACKPACK);

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
