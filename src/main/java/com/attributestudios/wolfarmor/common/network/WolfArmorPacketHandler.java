package com.attributestudios.wolfarmor.common.network;

import com.attributestudios.wolfarmor.api.util.Definitions;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class WolfArmorPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE =
            NetworkRegistry.INSTANCE.newSimpleChannel(Definitions.MOD_ID);
}
