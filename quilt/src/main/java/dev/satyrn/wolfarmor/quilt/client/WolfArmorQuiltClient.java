package dev.satyrn.wolfarmor.quilt.client;

import dev.satyrn.wolfarmor.fabriclike.client.WolfArmorFabricLikeClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@ClientOnly
public final class WolfArmorQuiltClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        // Run the Fabric-like setup.
        WolfArmorFabricLikeClient.init();
    }
}
