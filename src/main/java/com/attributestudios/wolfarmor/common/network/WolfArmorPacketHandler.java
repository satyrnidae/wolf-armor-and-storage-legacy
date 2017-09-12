package com.attributestudios.wolfarmor.common.network;

import com.attributestudios.wolfarmor.WolfArmorMod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class WolfArmorPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(WolfArmorMod.MOD_ID);
}
