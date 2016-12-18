package com.attributestudios.wolfarmor.client.renderer.entity.layer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Backport of render layer functionality from future versions for easier code compliance
 */
@SideOnly(Side.CLIENT)
public interface LayerRenderer<T extends EntityLivingBase> {

    /**
     * Renders this layer
     * @param entityLivingBase The entity to render
     * @param limbSwing The entity's limb swing progress
     * @param limbSwingAmount The entity's limb swing amount
     * @param partialTicks The current game tick
     * @param ageInTicks The entity's age
     * @param netHeadYaw The yaw of the entity's head
     * @param headPitch The pitch of the entity's head
     * @param scale The scale at which to render the layer.
     */
    void doRenderLayer(@Nonnull T entityLivingBase, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);
}
