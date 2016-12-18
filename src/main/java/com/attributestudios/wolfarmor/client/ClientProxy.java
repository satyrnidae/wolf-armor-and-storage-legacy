package com.attributestudios.wolfarmor.client;

import com.attributestudios.wolfarmor.client.renderer.entity.RenderWolfArmored;
import com.attributestudios.wolfarmor.common.CommonProxy;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.attributestudios.wolfarmor.item.WolfArmorItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            RenderingRegistry.registerEntityRenderingHandler(EntityWolfArmored.class, new IRenderFactory<EntityWolfArmored>() {
                @Override
                @Nonnull
                public Render<? super EntityWolfArmored> createRenderFor(@Nonnull RenderManager manager) {
                    return new RenderWolfArmored(manager, new ModelWolf(), 0.5F);
                }
            });

    }

    /**
     * Registers item renderers for this mod.
     * @param initializationEvent the initialization event
     */
    @Override
    protected void registerItemRenderers(@Nonnull FMLPreInitializationEvent initializationEvent) {
        WolfArmorItems.registerItemModel(WolfArmorItems.LEATHER_WOLF_ARMOR, 0);
        WolfArmorItems.registerItemModel(WolfArmorItems.CHAINMAIL_WOLF_ARMOR, 0);
        WolfArmorItems.registerItemModel(WolfArmorItems.IRON_WOLF_ARMOR, 0);
        WolfArmorItems.registerItemModel(WolfArmorItems.GOLDEN_WOLF_ARMOR, 0);
        WolfArmorItems.registerItemModel(WolfArmorItems.DIAMOND_WOLF_ARMOR, 0);
    }

    @Override
    protected void registerItemColorHandlers(@Nonnull FMLInitializationEvent initializationEvent) {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(@Nonnull ItemStack stack, int tintIndex) {
                return tintIndex > 0 ? -1 : ((ItemWolfArmor)stack.getItem()).getColor(stack);
            }
        }, WolfArmorItems.LEATHER_WOLF_ARMOR);
    }

    //endregion Public / Protected Methods
}
