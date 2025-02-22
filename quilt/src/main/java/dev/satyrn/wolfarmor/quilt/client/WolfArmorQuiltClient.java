package dev.satyrn.wolfarmor.quilt.client;

import dev.satyrn.wolfarmor.fabriclike.client.WolfArmorFabricLikeClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public final class WolfArmorQuiltClient implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        // Run the Fabric-like setup.
        WolfArmorFabricLikeClient.init();
    }
}
