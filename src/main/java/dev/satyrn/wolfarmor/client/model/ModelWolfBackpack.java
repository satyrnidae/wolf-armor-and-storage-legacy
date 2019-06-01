package dev.satyrn.wolfarmor.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static java.lang.Math.PI;

/**
 * Model for wolf backpacks
 */
@SideOnly(Side.CLIENT)
public class ModelWolfBackpack extends ModelBase {
    //region Fields

    private ModelRenderer backpackRightTop;
    private ModelRenderer backpackRightBottom;
    private ModelRenderer backpackLeftTop;
    private ModelRenderer backpackLeftBottom;

    private final static float INITIAL_Z_ROTATION = 0.139626F;

    //endregion Fields

    //region Constructors

    /**
     * Constructs a new wolf backpack model
     *
     * @param scale The scale
     */
    public ModelWolfBackpack(float scale) {
        this.backpackRightTop = new ModelRenderer(this, 0, 0);
        this.backpackRightTop.setTextureSize(16, 32);
        this.backpackRightTop.addBox(3, -2, 0, 2, 2, 5, scale);
        this.backpackRightTop.setRotationPoint(0, 14, 2);

        this.backpackRightBottom = new ModelRenderer(this, 0, 14);
        this.backpackRightBottom.setTextureSize(16, 32);
        this.backpackRightBottom.addBox(3, 0, 0, 3, 4, 5, scale);
        this.backpackRightBottom.setRotationPoint(0, 14, 2);

        this.backpackLeftTop = new ModelRenderer(this, 0, 7);
        this.backpackLeftTop.setTextureSize(16, 32);
        this.backpackLeftTop.addBox(-5, -2, 0, 2, 2, 5, scale);
        this.backpackLeftTop.setRotationPoint(0, 14, 2);
        this.backpackLeftTop.mirror = true;

        this.backpackLeftBottom = new ModelRenderer(this, 0, 23);
        this.backpackLeftBottom.setTextureSize(16, 32);
        this.backpackLeftBottom.addBox(-6, 0, 0, 3, 4, 5, scale);
        this.backpackLeftBottom.setRotationPoint(0, 14, 2);
        this.backpackLeftBottom.mirror = true;

        setRotation(this.backpackRightTop, 0, 0, -INITIAL_Z_ROTATION);
        setRotation(this.backpackRightBottom, 0, 0, -INITIAL_Z_ROTATION);
        setRotation(this.backpackLeftTop, 0, 0, INITIAL_Z_ROTATION);
        setRotation(this.backpackLeftBottom, 0, 0, INITIAL_Z_ROTATION);
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Sets various angles and then renders the model.
     *
     * @param entity          The entity that this model is rendering
     * @param limbSwing       The entity's limb swing progress
     * @param limbSwingAmount The entity's limb swing amount
     * @param ageInTicks      The entity's age in ticks
     * @param netHeadYaw      The entity's head yaw
     * @param headPitch       The entity's head pitch
     * @param scale           The scale to render at.
     */
    @Override
    public void render(@Nonnull Entity entity,
                       float limbSwing,
                       float limbSwingAmount,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch,
                       float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        if (this.isChild) {
            GlStateManager.pushMatrix();
            {
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.translate(0, 24 * scale, 0);
                this.backpackRightTop.renderWithRotation(scale);
                this.backpackRightBottom.renderWithRotation(scale);
                this.backpackLeftTop.renderWithRotation(scale);
                this.backpackLeftBottom.renderWithRotation(scale);
            }
            GlStateManager.popMatrix();
        } else {
            this.backpackRightTop.renderWithRotation(scale);
            this.backpackRightBottom.renderWithRotation(scale);
            this.backpackLeftTop.renderWithRotation(scale);
            this.backpackLeftBottom.renderWithRotation(scale);
        }
    }

    /**
     * Sets up animations for the entity
     *
     * @param entity          the entity
     * @param limbSwing       The entity's limb swing progress
     * @param limbSwingAmount The entity's limb swing progress amount
     * @param partialTickTime The world's partial ticks
     */
    @Override
    public void setLivingAnimations(@Nonnull EntityLivingBase entity,
                                    float limbSwing,
                                    float limbSwingAmount,
                                    float partialTickTime) {
        if (entity instanceof EntityWolf) {
            EntityWolf entityWolfArmored = (EntityWolf) entity;

            float rotationPointY = 14;
            float rotationPointZ = 2;

            float rotateAngleX = 0;

            if (entityWolfArmored.isSitting()) {
                rotationPointY = 18;
                rotationPointZ = 0;
                rotateAngleX = (float) (PI / -4);
            }

            this.backpackRightTop.setRotationPoint(0, rotationPointY, rotationPointZ);
            this.backpackRightBottom.setRotationPoint(0, rotationPointY, rotationPointZ);
            this.backpackLeftTop.setRotationPoint(0, rotationPointY, rotationPointZ);
            this.backpackLeftBottom.setRotationPoint(0, rotationPointY, rotationPointZ);

            setRotation(backpackRightTop,
                    rotateAngleX,
                    0,
                    entityWolfArmored.getShakeAngle(partialTickTime, -0.16F) - INITIAL_Z_ROTATION + MathHelper.cos(limbSwing * 1.2F) * 0.15F * limbSwingAmount);
            setRotation(backpackRightBottom,
                    rotateAngleX, 0,
                    entityWolfArmored.getShakeAngle(partialTickTime, -0.16F) - INITIAL_Z_ROTATION + MathHelper.cos(limbSwing * 1.2F) * 0.15F * limbSwingAmount);
            setRotation(backpackLeftTop,
                    rotateAngleX,
                    0,
                    entityWolfArmored.getShakeAngle(partialTickTime, -0.16F) + INITIAL_Z_ROTATION + MathHelper.sin(limbSwing * 1.2F) * 0.15F * limbSwingAmount);
            setRotation(backpackLeftBottom,
                    rotateAngleX,
                    0,
                    entityWolfArmored.getShakeAngle(partialTickTime, -0.16F) + INITIAL_Z_ROTATION + MathHelper.sin(limbSwing * 1.2F) * 0.15F * limbSwingAmount);
        }
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Sets the rotation of the model renderer
     *
     * @param model     The model renderer
     * @param xRotation X-axis rotation in radians
     * @param yRotation Y-axis rotation in radians
     * @param zRotation Z-axis rotation in radians
     */
    @SuppressWarnings("SameParameterValue")
    private static void setRotation(@Nonnull ModelRenderer model,
                                    float xRotation,
                                    float yRotation,
                                    float zRotation) {
        model.rotateAngleX = xRotation;
        model.rotateAngleY = yRotation;
        model.rotateAngleZ = zRotation;
    }

    //endregion Private Methods
}
