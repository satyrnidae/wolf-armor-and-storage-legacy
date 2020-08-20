package dev.satyrn.wolfarmor.common;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.advancements.WolfArmorTrigger;
import dev.satyrn.wolfarmor.api.common.IProxy;
import dev.satyrn.wolfarmor.api.util.Criteria;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.common.event.PotionEventHandler;
import dev.satyrn.wolfarmor.common.loot.LootHandler;
import dev.satyrn.wolfarmor.common.network.WolfArmorGuiHandler;
import dev.satyrn.wolfarmor.common.event.EntityEventHandler;
import dev.satyrn.wolfarmor.common.event.PlayerEventHandler;
import dev.satyrn.wolfarmor.common.event.RegistrationEventHandler;
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
    }

    @Override
    public void registerEntityRenderingHandlers() {}

    @Override
    public void registerItemRenders(@Nonnull FMLInitializationEvent initializationEvent) {}

    @Override
    public void init(@Nonnull FMLInitializationEvent initializationEvent) {
        registerItemRenders(initializationEvent);
        registerItemColorHandlers(initializationEvent);
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
    public void registerLootTables() {
        LootHandler.init();
    }

    @Override
    public void registerEventListeners() {
        MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new RegistrationEventHandler());
        MinecraftForge.EVENT_BUS.register(new PotionEventHandler());
        MinecraftForge.EVENT_BUS.register(WolfArmorMod.getConfig());
    }

    @Override
    public void postInit(@Nonnull FMLPostInitializationEvent postInitializationEvent) {
        this.registerGuiHandlers();
        this.registerLootTables();
    }

    @Override
    public void loadComplete(@Nonnull FMLLoadCompleteEvent loadCompleteEvent) {
        this.registerEntityRenderingHandlers();
    }

    @Override
    public void registerGuiHandlers() {
        NetworkRegistry.INSTANCE.registerGuiHandler(WolfArmorMod.getInstance(), new WolfArmorGuiHandler());
    }

    /**
     * Gets the sided thread from the message context
     * @param context The message context
     * @return The server instance
     * @since 2.1.0
     */
	@Override
	public IThreadListener getThreadFromContext(MessageContext context) {
		return context.getServerHandler().player.getServer();
    }

    /**
     * Gets the sided player instance from the message context
     * @param context The message context
     * @return The server player instance
     * @since 2.1.0
     */
    @Override
    public EntityPlayer getPlayerFromContext(MessageContext context) {
        return context.getServerHandler().player;
    }

    @Override
    public Side getCurrentSide() {
        return Side.SERVER;
    }
}
