package com.attributestudios.wolfarmor.client;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.item.Items;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import com.attributestudios.wolfarmor.common.CommonProxy;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.attributestudios.wolfarmor.api.util.annotation.DynamicallyUsed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Loads client-specific mod data
 */
@SideOnly(Side.CLIENT)
@DynamicallyUsed
public class ClientProxy extends CommonProxy {
    //region Public / Protected Methods

    @Override
    public void registerEntityRenderingHandlers() {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        RenderWolf renderWolf = (RenderWolf) renderManager.entityRenderMap.get(EntityWolf.class);

        renderWolf.addLayer(new LayerWolfArmor(renderWolf));
        renderWolf.addLayer(new LayerWolfBackpack(renderWolf));

        if (Loader.isModLoaded("SophisticatedWolves")) {
            RenderLiving sRender = null;
            try {
                Class clazz = Class.forName("sophisticated_wolves.entity.EntitySophisticatedWolf");
                if(EntityWolf.class.isAssignableFrom(clazz)) {
                    Render<? extends Entity> renderer = renderManager.entityRenderMap.get(clazz);
                    if(renderer != null && RenderLiving.class.isAssignableFrom(renderer.getClass())) {
                        sRender = (RenderLiving)renderer;
                    }
                }
            } catch (ClassNotFoundException e) {
                WolfArmorMod.getLogger().warning(e);
            }
            if (sRender != null) {
                sRender.addLayer(new LayerWolfArmor(sRender));
                sRender.addLayer(new LayerWolfBackpack(sRender));
            }
        }
    }

    /**
     * Registers item renders for this mod.
     *
     * @param initializationEvent the initialization event
     */
    @Override
    public void registerItemRenders(@Nonnull FMLInitializationEvent initializationEvent) {
        registerItemModel(Items.LEATHER_WOLF_ARMOR);
        registerItemModel(Items.CHAINMAIL_WOLF_ARMOR);
        registerItemModel(Items.IRON_WOLF_ARMOR);
        registerItemModel(Items.GOLD_WOLF_ARMOR);
        registerItemModel(Items.DIAMOND_WOLF_ARMOR);
    }

    @Override
    public void registerItemColorHandlers(@Nonnull FMLInitializationEvent initializationEvent) {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(@Nonnull ItemStack stack, int tintIndex) {
                return tintIndex > 0 ? -1 : ((ItemWolfArmor) stack.getItem()).getColor(stack);
            }
        }, Items.LEATHER_WOLF_ARMOR);
    }

    /**
     * Registers item models with the model loader
     *  @param item     The item
     *
     */
    @SideOnly(Side.CLIENT)
    private static void registerItemModel(@Nullable Item item) {
        if (item != null && item.getRegistryName() != null) {
            ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName().toString(), "inventory");
            ModelLoader.registerItemVariants(item, item.getRegistryName());
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, resource);
        }
    }

    //endregion Public / Protected Methods
}
