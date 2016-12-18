package com.attributestudios.wolfarmor.client.renderer.entity;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerRenderer;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import com.attributestudios.wolfarmor.common.ReflectionCache;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Handles rendering for EntityWolfArmored
 */
@SideOnly(Side.CLIENT)
public class RenderWolfArmored extends RenderWolf {
    //region Fields

    private static final String TIMER_SRG = "field_71428_T";
    private static Timer timer;
    private ArrayList<LayerRenderer<EntityWolf>> renderLayers = new ArrayList<LayerRenderer<EntityWolf>>();

    //endregion Fields

    //region Constructors

    /**
     * Creates a new renderer
     * @param modelMain The main model
     * @param modelRenderPass The render pass model
     * @param shadowSize The shadow size for the entity
     */
    public RenderWolfArmored(@Nonnull ModelBase modelMain,
                             @Nonnull ModelBase modelRenderPass,
                             float shadowSize) {
        super(modelMain, modelRenderPass, shadowSize);

        renderLayers.add(new LayerWolfArmor(this));
        renderLayers.add(new LayerWolfBackpack(this));
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Renders the entity
     * @param entity The entity
     * @param posX The entity X position
     * @param posY The entity Y position
     * @param posZ The entity Z position
     * @param entityYaw The entity yaw
     * @param partialTicks The partial ticks
     */
    @Override
    public void doRender(@Nonnull EntityLivingBase entity,
                         double posX,
                         double posY,
                         double posZ,
                         float entityYaw,
                         float partialTicks) {
        super.doRender(entity, posX, posY, posZ, entityYaw, partialTicks);
    }

    /**
     * Renders the entity models
     * @param entity The entity to render
     * @param limbSwing The limb swing
     * @param limbSwingAmount The limb swing progress
     * @param ageInTicks The entity age in ticks
     * @param netHeadYaw The entity head yaw
     * @param headPitch The entity head pitch
     * @param scaleFactor The scale factor
     */
    @Override
    protected void renderModel(@Nonnull EntityLivingBase entity,
                               float limbSwing,
                               float limbSwingAmount,
                               float ageInTicks,
                               float netHeadYaw,
                               float headPitch,
                               float scaleFactor)
    {
        super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        this.renderModelLayers((EntityWolfArmored)entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }

    /**
     * Public access to texture binding.
     * @param resourceLocation The texture resource location to bind.
     */
    @Override
    public void bindTexture(@Nullable ResourceLocation resourceLocation) {
        super.bindTexture(resourceLocation);
    }

    /**
     * Sets up the model attributes.
     * @param entityLivingBase The entity
     * @param model The model
     */
    public void setupModelAttributes(@Nonnull EntityLivingBase entityLivingBase, float partialTicks, ModelBase model) {
        model.onGround = this.renderSwingProgress(entityLivingBase, partialTicks);
        model.isRiding = entityLivingBase.isRiding();
        model.isChild = entityLivingBase.isChild();
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Renders custom model layers for the wolf
     * @param entity The wolf
     * @param limbSwing The limb swing
     * @param limbSwingAmount The limb swing amount
     * @param ageInTicks The entity age
     * @param netHeadYaw The entity head yaw
     * @param headPitch The entity head pitch
     * @param scaleFactor The scale factor
     */
    private void renderModelLayers(@Nonnull EntityWolfArmored entity,
                                   float limbSwing,
                                   float limbSwingAmount,
                                   float ageInTicks,
                                   float netHeadYaw,
                                   float headPitch,
                                   float scaleFactor)
    {
        float partialTicks = getRenderPartialTicks();

        for(LayerRenderer<EntityWolf> layer : renderLayers) {
            GL11.glPushMatrix();
            {
                layer.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            }
            GL11.glPopMatrix();
        }
    }

    //endregion Private Methods

    //region Accessors / Mutators

    /**
     * Gets the timer partial ticks
     * @return The partial ticks
     */
    private static float getRenderPartialTicks() {
        if(RenderWolfArmored.timer == null)
        {
            try {
                RenderWolfArmored.timer = (Timer) ReflectionCache.getField(Minecraft.class, TIMER_SRG, "timer").get(Minecraft.getMinecraft());
            }
            catch(IllegalAccessException ex) {
                WolfArmorMod.getLogger().error(ex);
                RenderWolfArmored.timer = new Timer(20);
            }
        }

        return RenderWolfArmored.timer.renderPartialTicks;
    }

    //endregion Accessors / Mutators
}
