package com.attributestudios.wolfarmor.client.renderer.entity.layer;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.client.model.ModelWolfBackpack;
import com.attributestudios.wolfarmor.client.renderer.entity.RenderWolfArmored;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A layer renderer for wolf backpacks.
 */
public class LayerWolfBackpack implements LayerRenderer<EntityWolf> {
    //region Fields

    private ModelWolfBackpack modelWolfBackpack;
    private RenderWolfArmored renderer;

    private static final ResourceLocation TEXTURE_WOLF_BACKPACK = new ResourceLocation(WolfArmorMod.MOD_ID, "textures/models/wolf_pack.png");

    //endregion Fields

    //region Constructors

    /**
     * Creates a new layer renderer for armored wolf backpacks.
     * @param renderer The parent renderer.
     */
    public LayerWolfBackpack(@Nonnull RenderWolfArmored renderer) {
        this.renderer = renderer;

        this.modelWolfBackpack = new ModelWolfBackpack(0.0F);
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Renders the layer.
     * @param entityWolf The wolf to render.  If it is not am EntityWolfArmored, the layer will not render.
     * @param limbSwing The entity's limb swing progress.
     * @param limbSwingAmount The entity's limb swing progress amount.
     * @param partialTicks The current game tick.
     * @param ageInTicks The entity's age.
     * @param netHeadYaw The yaw of the entity's head.
     * @param headPitch The pitch of the entity's head.
     * @param scale The scale at which to render the layer.
     */
    @Override
    public void doRenderLayer(@Nullable EntityWolf entityWolf,
                              float limbSwing,
                              float limbSwingAmount,
                              float partialTicks,
                              float ageInTicks,
                              float netHeadYaw,
                              float headPitch,
                              float scale) {

        if(WolfArmorMod.getConfiguration().getIsWolfChestRenderEnabled()) {
            if (entityWolf instanceof EntityWolfArmored) {
                EntityWolfArmored entityWolfArmored = (EntityWolfArmored) entityWolf;

                if (entityWolfArmored.getHasChest()) {

                    this.renderer.setupModelAttributes(entityWolfArmored, partialTicks, this.modelWolfBackpack);
                    this.modelWolfBackpack.setLivingAnimations(entityWolfArmored, limbSwing, limbSwingAmount, partialTicks);

                    this.renderer.bindTexture(TEXTURE_WOLF_BACKPACK);

                    GL11.glColor4f(1, 1, 1, 1);

                    if (!entityWolfArmored.isInvisible()) {
                        this.modelWolfBackpack.render(entityWolfArmored, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    } else {
                        GL11.glPushMatrix();
                        {
                            GL11.glColor4f(1, 1, 1, 0.15F);
                            GL11.glDepthMask(false);
                            {
                                GL11.glEnable(GL11.GL_BLEND);
                                {
                                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
                                            GL11.GL_ONE_MINUS_SRC_ALPHA);

                                    this.modelWolfBackpack.render(entityWolfArmored,
                                            limbSwing,
                                            limbSwingAmount,
                                            ageInTicks,
                                            netHeadYaw,
                                            headPitch,
                                            scale);
                                }
                                GL11.glDisable(GL11.GL_BLEND);
                            }
                            GL11.glDepthMask(true);
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
        }
    }

    //endregion Public / Protected Methods
}
