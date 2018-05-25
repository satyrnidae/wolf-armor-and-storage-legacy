package com.attributestudios.wolfarmor.common;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.advancements.EquipWolfArmorTrigger;
import com.attributestudios.wolfarmor.api.IProxy;
import com.attributestudios.wolfarmor.api.util.Criteria;
import com.attributestudios.wolfarmor.api.util.Resources;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.loot.LootHandler;
import com.attributestudios.wolfarmor.common.network.WolfArmorGuiHandler;
import com.attributestudios.wolfarmor.common.network.PacketHandler;
import com.attributestudios.wolfarmor.event.EntityEventHandler;
import com.attributestudios.wolfarmor.event.PlayerEventHandler;
import com.attributestudios.wolfarmor.event.RegistrationEventHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
    }

    @Override
    public void registerItemColorHandlers(@Nonnull FMLInitializationEvent initializationEvent) {}

    @Override
    public void registerCriteriaTriggers() {
        Criteria.EQUIP_WOLF_ARMOR = new EquipWolfArmorTrigger(Resources.CRITERIA_TRIGGER_EQUIP_WOLF_ARMOR);
        CriteriaTriggers.register(Criteria.EQUIP_WOLF_ARMOR);
    }
    @Override
    public void registerPackets() {
        PacketHandler.initialize();
    }

    @Override
    public void registerLootTables() {
        LootHandler.init();
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
        this.registerLootTables();
        this.registerEntityRenderingHandlers();
    }

    @Override
    public void registerGuiHandlers() {
        NetworkRegistry.INSTANCE.registerGuiHandler(WolfArmorMod.getInstance(), new WolfArmorGuiHandler());
    }

    @Override
    public void registerCapabilities() {
        CapabilityWolfArmor.register();
    }

	@Override
	public IThreadListener getThreadFromContext(MessageContext context) {
		return context.getServerHandler().player.getServer();
    }
    
    @Override
    public EntityPlayer getPlayerFromContext(MessageContext context) {
        return context.getServerHandler().player;
    }
}
