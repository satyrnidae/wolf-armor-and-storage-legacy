package com.attributestudios.wolfarmor.common;

import dev.satyrn.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.advancements.WolfArmorTrigger;
import dev.satyrn.wolfarmor.api.IProxy;
import dev.satyrn.wolfarmor.api.util.Criteria;
import dev.satyrn.wolfarmor.api.util.Resources;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.loot.LootHandler;
import com.attributestudios.wolfarmor.common.network.WolfArmorGuiHandler;
import com.attributestudios.wolfarmor.common.network.PacketHandler;
import com.attributestudios.wolfarmor.compatibility.CompatibilityHelper;
import com.attributestudios.wolfarmor.common.event.EntityEventHandler;
import com.attributestudios.wolfarmor.common.event.PlayerEventHandler;
import com.attributestudios.wolfarmor.common.event.RegistrationEventHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

public class CommonProxy implements IProxy {

    @Override
    public void preInit(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        registerEventListeners();
        registerCriteriaTriggers();
        CompatibilityHelper.preInit();
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
        CompatibilityHelper.init();
    }

    @Override
    public void registerItemColorHandlers(@Nonnull FMLInitializationEvent initializationEvent) {}

    @Override
    public void registerCriteriaTriggers() {
        Criteria.EQUIP_WOLF_ARMOR = new WolfArmorTrigger(Resources.CRITERIA_TRIGGER_EQUIP_WOLF_ARMOR);
        Criteria.EQUIP_WOLF_CHEST = new WolfArmorTrigger(Resources.CRITERIA_TRIGGER_EQUIP_WOLF_CHEST);
        CriteriaTriggers.register(Criteria.EQUIP_WOLF_ARMOR);
        CriteriaTriggers.register(Criteria.EQUIP_WOLF_CHEST);
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
        CompatibilityHelper.postInit();
    }

    @Override
    public void loadComplete(@Nonnull FMLLoadCompleteEvent loadCompleteEvent) {
        CompatibilityHelper.loadComplete();
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

    @Override
    public Side getCurrentSide() {
        return Side.SERVER;
    }
}
