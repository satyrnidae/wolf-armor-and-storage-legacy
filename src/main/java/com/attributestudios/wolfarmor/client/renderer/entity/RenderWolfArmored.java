package com.attributestudios.wolfarmor.client.renderer.entity;

import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Handles rendering for EntityWolfArmored
 */
@SideOnly(Side.CLIENT)
public class RenderWolfArmored extends RenderWolf {
    //region Constructors

    /**
     * Initializes a new renderer for an armored wolf.
     * @param renderManager The render manager
     * @param modelMain The main model
     * @param shadowSize The shadow size for the entity.
     */
    public RenderWolfArmored(@Nonnull RenderManager renderManager,
                             @Nonnull ModelBase modelMain,
                             float shadowSize) {
        super(renderManager, modelMain, shadowSize);

        this.addLayer(new LayerWolfArmor(this));
        this.addLayer(new LayerWolfBackpack(this));
    }

    //endregion Constructors
}
