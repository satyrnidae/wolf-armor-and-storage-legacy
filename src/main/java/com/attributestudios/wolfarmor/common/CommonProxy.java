package com.attributestudios.wolfarmor.common;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.IProxy;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.network.WolfArmorGuiHandler;
import com.attributestudios.wolfarmor.common.network.WolfArmorPacketHandler;
import com.attributestudios.wolfarmor.common.network.packets.WolfAutoHealMessage;
import com.attributestudios.wolfarmor.event.EntityEventHandler;
import com.attributestudios.wolfarmor.event.PlayerEventHandler;
import com.attributestudios.wolfarmor.event.RegistrationEventHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommonProxy implements IProxy {

    @Override
    public void preInit(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        registerEventListeners();
        registerCriteriaTriggers();
    }

    @Override
    public void registerEntityRenderingHandlers() {}

    @Override
    public void registerItemRenders(@Nonnull FMLInitializationEvent initializationEvent) {}

    @Override
    public void init(@Nonnull FMLInitializationEvent initializationEvent) {
        registerItemRenders(initializationEvent);
        registerItemColorHandlers(initializationEvent);
        registerCapabilities();
        registerRecipes();
    }

    @Override
    public void registerItemColorHandlers(@Nonnull FMLInitializationEvent initializationEvent) {}

    @Override
    public void registerCriteriaTriggers() {
        Method method = ReflectionCache.getMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);

        if(method == null) {
            throw new RuntimeException("Failed to register criteria: method not found", ReflectionCache.getLastError());
        }

        try {
            method.invoke(null, com.attributestudios.wolfarmor.api.definitions.CriteriaTriggers.EQUIP_WOLF_ARMOR);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to register criteria: Unable to access method.", e);
        }
    }

    @Deprecated
    @Override
    public void registerRecipes() {
        //TODO maybe
    }

    @Override
    public void registerPackets() {
        WolfArmorPacketHandler.INSTANCE.registerMessage(WolfAutoHealMessage.WolfAutoHealMessageHandler.class, WolfAutoHealMessage.class, 0, Side.CLIENT);
    }

    @Override
    public void registerEventListeners() {
        MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new RegistrationEventHandler());
    }

    @Override
    public void postInit(@Nonnull FMLPostInitializationEvent postInitializationEvent) {
        this.registerGuiHandlers();
        this.registerPackets();
    }

    private boolean IsRendererInitialized = false;

    @Override
    public void serverAboutToStart(@Nonnull FMLServerAboutToStartEvent serverAboutToStartEvent) {
        if (!IsRendererInitialized) {
            IsRendererInitialized = true;
            this.registerEntityRenderingHandlers();
        }
    }

    public void registerGuiHandlers() {
        NetworkRegistry.INSTANCE.registerGuiHandler(WolfArmorMod.instance, new WolfArmorGuiHandler());
    }

    public void registerCapabilities() {
        CapabilityWolfArmor.register();
    }
}
