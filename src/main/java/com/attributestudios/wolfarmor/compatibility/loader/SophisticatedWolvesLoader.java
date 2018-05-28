package com.attributestudios.wolfarmor.compatibility.loader;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import com.attributestudios.wolfarmor.compatibility.CompatibilityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class SophisticatedWolvesLoader implements CompatibilityHelper.ICompatabilityLoader {

    public static final String MOD_ID = "sophisticatedwolves";

    @ObjectHolder("sophisticatedwolves:textures/entity/brown/wolf.png")
    public static final EntityEntry ENTITY_SOPHISTICATED_WOLF = null;

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @SideOnly(Side.CLIENT)
    private static void registerRenderers() throws Exception {

        Class clazz;

        if(ENTITY_SOPHISTICATED_WOLF != null) {
            clazz = ENTITY_SOPHISTICATED_WOLF.getEntityClass();
        }
        else {
            WolfArmorMod.getLogger().warning("Failed to load SWWolf entity (sophisticatedwolves:textures/entity/brown/wolf.png); falling back to reflection!");

            try{
                ClassLoader loader = SophisticatedWolvesLoader.class.getClassLoader();
                clazz = loader.loadClass("sophisticated_wolves.entity.EntitySophisticatedWolf");
            } catch(ClassNotFoundException ex) {
                throw new Exception("Failed to load SWWolf entity: cannot find class `sophisticated_wolves.entity.EntitySophisticatedWolf`!");
            }
        }

        RenderManager renderer = Minecraft.getMinecraft().getRenderManager();
        RenderLiving renderLiving = (RenderLiving)renderer.entityRenderMap.get(clazz);

        if(renderLiving == null) {
            //throw new NullPointerException(String.format("Failed to load renderer for entity class %s", clazz.getName()));
            try{
                ClassLoader loader = SophisticatedWolvesLoader.class.getClassLoader();
                Class rClass = loader.loadClass("sophisticated_wolves.RenderSophisticatedWolf");
                renderLiving = (RenderLiving)rClass.newInstance();
                RenderingRegistry.registerEntityRenderingHandler(clazz, renderLiving);
            } catch(ClassNotFoundException ex) {
                throw new Exception("Failed to load renderer for entity class sophisticated_wolves.entity.EntitySophisticatedWolf!");
            }
        }
        renderLiving.addLayer(new LayerWolfArmor(renderLiving));
        renderLiving.addLayer(new LayerWolfBackpack(renderLiving));
    }

    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public void init() throws Exception {}

    @Override
    public void preInit() throws Exception {}

    @Override
    public void postInit() throws Exception {}

    @Override
    public void loadComplete() throws Exception {
        if(WolfArmorMod.getProxy().getCurrentSide() == Side.CLIENT) {
            registerRenderers();
        }
    }
}
