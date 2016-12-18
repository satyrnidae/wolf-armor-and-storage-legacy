package com.attributestudios.wolfarmor.client;

import com.attributestudios.wolfarmor.client.renderer.entity.RenderWolfArmored;
import com.attributestudios.wolfarmor.common.CommonProxy;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelWolf;

import javax.annotation.Nonnull;

/**
 * Loads client-specific mod data
 */
@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    //region Public / Protected Methods

    /**
     * Registers entity renderers for this mod.
     * @param preInitializationEvent The pre-initialization event
     */
    @Override
    protected void registerEntityRenderingHandlers(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
            RenderingRegistry.registerEntityRenderingHandler(EntityWolfArmored.class,
                    new RenderWolfArmored(new ModelWolf(), new ModelWolf(), 0.5F));
    }

    //endregion Public / Protected Methods
}
