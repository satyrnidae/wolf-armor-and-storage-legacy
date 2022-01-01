package dev.satyrn.wolfarmor.api.compatibility;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.satyrn.wolfarmor.api.compatibility.client.LayerProvider;
import dev.satyrn.wolfarmor.api.util.Resources;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Compatibility registry for Wolf Armor and Storage.
 * @author Isabel Maskrey
 * @since 4.5.0-alpha
 */
@SuppressWarnings("unused")
public class Compatibility {
    private static final Map<String, ICompatibilityProvider> compatibilityProviders = Maps.newLinkedHashMap();

    @SideOnly(Side.CLIENT)
    private static List<LayerProvider> layerOverrides;

    private static final Logger logger = LogManager.getLogger(Resources.MOD_ID);

    /**
     * Registers a compatibility provider with Wolf Armor and Storage
     * @param provider Supplier for the provider instance
     * @return {@code true} if the registration succeeded, {@code false} if not.
     */
    public static synchronized boolean register(@Nonnull Supplier<ICompatibilityProvider> provider) {
        ICompatibilityProvider instance = provider.get();
        String modId = instance.getModId();

        ICompatibilityProvider registeredProvider = compatibilityProviders.get(modId);
        if (registeredProvider != null) {
            if (instance.getPriority() != Priority.HIGHEST
                    && (registeredProvider.getPriority() >= instance.getPriority()
                    || instance.getPriority() == Priority.LOWEST)) {
                logger.warn("Skipped registering provider {} for {}: the provider {} is already registered for this mod",
                        instance.getClass().getSimpleName(), modId, registeredProvider.getClass().getSimpleName());
                return false;
            }

            logger.warn("Replacing provider {} with higher priority provider {} for mod {}",
                    registeredProvider.getClass().getSimpleName(), instance.getClass().getSimpleName(),
                    modId);
            compatibilityProviders.replace(modId, instance);
            return true;
        }

        compatibilityProviders.put(modId, instance);
        logger.info("Registered new provider {} for mod {}", instance.getClass().getSimpleName(), modId);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static synchronized void registerLayer(@Nonnull LayerProvider provider) {
        if (!layerOverrides.contains(provider)) {
            layerOverrides.add(provider);
        }
    }

    /**
     * Gets an armor render layer
     * @param renderer The renderer to which the layer is being added
     * @return The armor layer renderer
     */
    @SideOnly(Side.CLIENT)
    @Nullable
    public static synchronized LayerRenderer<?> getArmorLayer(@Nonnull RenderLiving<?> renderer) {
        Optional<LayerProvider> provider = layerOverrides.stream().filter(LayerProvider::getProvidesArmorLayer).max(new PriorityComparator());

        return provider.<LayerRenderer<?>>map(layerProvider -> layerProvider.getArmorLayer(renderer)).orElse(null);
    }

    /**
     * Gets a backpack armor layer
     * @param renderer The renderer to which the layer is being added
     * @return The backpack layer renderer
     */
    @SideOnly(Side.CLIENT)
    @Nullable
    public static synchronized LayerRenderer<?> getBackpackLayer(@Nonnull RenderLiving<?> renderer) {
        Optional<LayerProvider> provider = layerOverrides.stream().filter(LayerProvider::getProvidesBackpackLayer).max(new PriorityComparator());

        return provider.<LayerRenderer<?>>map(layerProvider -> layerProvider.getBackpackLayer(renderer)).orElse(null);
    }

    private static boolean isModLoaded(String modId) {
        final List<ModContainer> mods = Loader.instance().getActiveModList();
        Optional<ModContainer> provided = mods.stream().filter(mod -> modId.equalsIgnoreCase(mod.getModId())).findFirst();
        return provided.isPresent();
    }

    /**
     * Initializes the provider layers.  Necessary to call during init to override the default wolf models
     * @param event The FML initialization event
     */
    public static void postInit(@Nonnull FMLPostInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            layerOverrides = Lists.newArrayList(new LayerProvider());
            logger.info("Initializing layer providers...");
            compatibilityProviders.values().forEach(provider -> {
                if (isModLoaded(provider.getModId())) {
                    List<LayerProvider> layerProviders = provider.getLayerProviders();
                    layerProviders.forEach(Compatibility::registerLayer);
                }
            });
        }
    }

    /**
     * Sets up the compatibility providers
     * @param event The load complete event
     */
    public static void loadComplete(@Nonnull FMLLoadCompleteEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            logger.info("Setting up compatibility providers...");
            if(isModLoaded(provider.getModId())) {
                provider.setup(event.getSide());
            }
        });
    }
}