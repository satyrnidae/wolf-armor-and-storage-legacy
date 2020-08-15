package dev.satyrn.wolfarmor.common.network;

import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.common.network.packets.*;
import dev.satyrn.wolfarmor.common.network.MessageBase.ClientMessageBase;
import dev.satyrn.wolfarmor.common.network.MessageBase.ServerMessageBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.function.Predicate;

public class WolfArmorChannel extends SimpleNetworkWrapper {
    private static byte ID = 0;

    public WolfArmorChannel() {
        super(Resources.MOD_ID);

        this.registerMessage(WolfEatMessage.class);
        this.registerMessage(WolfHealMessage.class);
        this.registerMessage(WolfDropChestMessage.class);
        this.registerMessage(ConfigSyncMessage.class);
        this.registerMessage(UpdateFoodStatsMessage.class);
        this.registerMessage(UpdatePotionEffectMessage.class);
        this.registerMessage(RemovePotionEffectMessage.class);
    }

    private <T extends MessageBase<T> & IMessageHandler<T, IMessage>>void registerMessage(Class<T> clazz) {
        if(ClientMessageBase.class.isAssignableFrom(clazz)) {
            this.registerMessage(clazz, clazz, ID++, Side.CLIENT);
            return;
        }
        if(ServerMessageBase.class.isAssignableFrom(clazz)) {
            this.registerMessage(clazz, clazz, ID++, Side.SERVER);
            return;
        }
        this.registerMessage(clazz, clazz, ID, Side.CLIENT);
        this.registerMessage(clazz, clazz, ID++, Side.SERVER);
    }

    public void sendToAllWhere(IMessage message, Predicate<EntityPlayer> filter) {
        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (filter.test(player)) {
                sendTo(message, (EntityPlayerMP) player);
            }
        }
    }
}
