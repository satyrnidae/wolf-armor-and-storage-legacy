package com.attributestudios.wolfarmor.compatibility;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.compatibility.loader.SophisticatedWolvesLoader;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.function.Consumer;

public abstract class CompatibilityHelper {
    private static final HashMap<String, ICompatabilityLoader> loaders = Maps.newHashMap();

    static {
        registerHelper(SophisticatedWolvesLoader.class);
    }

    public interface ICompatabilityLoader {
        String getModId();
        void init() throws Exception;
        void preInit() throws Exception;
        void postInit() throws Exception;
        void loadComplete() throws Exception;
    }

    /**
     * Registers a compatibility load helper with the class.
     * @param loaderClass The class to load.
     */
    @SuppressWarnings("SameParameterValue")
    private static void registerHelper(Class<? extends ICompatabilityLoader> loaderClass) {
        try {
            ICompatabilityLoader loader = loaderClass.newInstance();
            String modId = loader.getModId();
            if(loaders.containsKey(modId)) {
                throw new IllegalStateException(String.format("Mod ID %s already registered; loader registration skipped.", modId));
            }
            loaders.put(modId, loader);
        } catch(InstantiationException | IllegalAccessException | IllegalStateException e) {
            WolfArmorMod.getLogger().error(e);
        }
    }

    public static void init() {
        loaders.values().forEach(loader -> {
            try {
                loader.init();
            } catch(Exception e) {
                WolfArmorMod.getLogger().error(String.format("Skipped init compatibility for mod %s: %s", loader.getModId(), e.getMessage()));
            }
        });
    }

    public static void preInit() {
        loaders.values().forEach(loader -> {
            try {
                loader.preInit();
            } catch(Exception e) {
                WolfArmorMod.getLogger().error(String.format("Skipped pre-init compatibility for mod %s: %s", loader.getModId(), e.getMessage()));
            }
        });
    }

    public static void postInit() {
        loaders.values().forEach(loader -> {
            try {
                loader.postInit();
            } catch(Exception e) {
                WolfArmorMod.getLogger().error(String.format("Skipped post-init compatibility for mod %s: %s", loader.getModId(), e.getMessage()));
            }
        });
    }

    public static void loadComplete() {
        loaders.values().forEach(loader -> {
            try {
                loader.loadComplete();
            } catch(Exception e) {
                WolfArmorMod.getLogger().error(String.format("Skipped load complete compatibility for mod %s: %s", loader.getModId(), e.getMessage()));
            }
        });
    }
}
