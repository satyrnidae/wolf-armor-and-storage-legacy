package dev.satyrn.wolfarmor.fabric.client;

import dev.satyrn.wolfarmor.fabriclike.client.WolfArmorFabricLikeClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class WolfArmorFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        WolfArmorFabricLikeClient.init();
    }
}
