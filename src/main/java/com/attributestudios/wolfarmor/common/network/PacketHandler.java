package com.attributestudios.wolfarmor.common.network;

import com.attributestudios.wolfarmor.api.util.Definitions;
import com.attributestudios.wolfarmor.common.network.packets.AbstractMessage;
import com.attributestudios.wolfarmor.common.network.packets.WolfChestedStateChangedMessage;
import com.attributestudios.wolfarmor.common.network.packets.WolfEatMessage;
import com.attributestudios.wolfarmor.common.network.packets.WolfEquippedArmorMessage;
import com.attributestudios.wolfarmor.common.network.packets.WolfHealMessage;
import com.attributestudios.wolfarmor.common.network.packets.AbstractMessage.AbstractClientMessage;
import com.attributestudios.wolfarmor.common.network.packets.AbstractMessage.AbstractServerMessage;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public abstract class PacketHandler {
    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Definitions.MOD_ID);
    private static byte ID = 0;
    public static final void initialize() {
        registerMessage(WolfChestedStateChangedMessage.class);
        registerMessage(WolfEatMessage.class);
        registerMessage(WolfHealMessage.class);
        registerMessage(WolfEquippedArmorMessage.class);
    }

    private static final <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>>void registerMessage(Class<T> clazz) {
        if(AbstractClientMessage.class.isAssignableFrom(clazz)) {
            CHANNEL.registerMessage(clazz, clazz, ID++, Side.CLIENT);
            return;
        }
        if(AbstractServerMessage.class.isAssignableFrom(clazz)) {
            CHANNEL.registerMessage(clazz, clazz, ID++, Side.SERVER);
            return;
        }
        CHANNEL.registerMessage(clazz, clazz, ID, Side.CLIENT);
        CHANNEL.registerMessage(clazz, clazz, ID++, Side.SERVER);
    }

    public static SimpleNetworkWrapper getChannel() {
        return CHANNEL;
    }
}
