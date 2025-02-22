package dev.satyrn.wolfarmor.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import dev.satyrn.wolfarmor.fabriclike.WolfArmorFabricLike;

public final class WolfArmorQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        // Run the Fabric-like setup.
        WolfArmorFabricLike.init();
    }
}
