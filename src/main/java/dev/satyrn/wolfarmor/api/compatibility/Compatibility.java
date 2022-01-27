package dev.satyrn.wolfarmor.api.compatibility;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.satyrn.wolfarmor.api.compatibility.client.LayerProvider;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Compatibility registry for Wolf Armor and Storage.
 * @author Isabel Maskrey
 * @since 4.5.0-alpha
 */
@SuppressWarnings("unused")
public class Compatibility {
    private static final Map<String, CompatibilityProvider> compatibilityProviders = Maps.newLinkedHashMap();

    @SideOnly(Side.CLIENT)
    private static List<LayerProvider> layerOverrides;

    private static final Logger logger = LogManager.getLogger(Resources.MOD_ID);

    /**
     * Registers a compatibility provider with Wolf Armor and Storage
     * @param providerName Fully-qualified provider class name
     * @return {@code true} if the registration succeeded, {@code false} if not.
     */
    public static synchronized boolean register(@Nonnull String providerName) {
        try {
            Class<?> providerClass = Compatibility.class.getClassLoader().loadClass(providerName);
            if (!(CompatibilityProvider.class.isAssignableFrom(providerClass) && providerClass.isAnnotationPresent(Provider.class))) {
                logger.info("Skipped invalid provider {}.", providerName);
                return false;
            }

            CompatibilityProvider instance = (CompatibilityProvider) Compatibility.class.getClassLoader().loadClass(providerName).newInstance();
            String modId = instance.getModId();

            CompatibilityProvider registeredProvider = compatibilityProviders.get(modId);
            if (registeredProvider != null) {
                if (instance.getPriority() != Provider.Priority.HIGHEST
                        && (registeredProvider.getPriority() >= instance.getPriority()
                        || instance.getPriority() == Provider.Priority.LOWEST)) {
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
        } catch (NoClassDefFoundError | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.info("Skipped registering provider {}: {}", providerName, e.toString());
            return false;
        }
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
    @Nonnull
    public static synchronized LayerRenderer<?> getArmorLayer(@Nonnull RenderLiving<?> renderer) {
        return getArmorLayer(renderer, null);
    }

    /**
     * Gets an armor render layer. Can specify a mod id for a specific layer provider.
     * @param renderer The renderer to which the layer will be added.
     * @param filterSpec The mod ID, or null
     * @return The
     */
    @SideOnly(Side.CLIENT)
    @Nonnull
    public static synchronized LayerRenderer<?> getArmorLayer(@Nonnull RenderLiving<?> renderer, final @Nullable String filterSpec) {
        Optional<LayerProvider> provider = Optional.empty();
        if (filterSpec != null) {
            final @Nonnull String[] filters = filterSpec.split(",");
            provider = layerOverrides.stream().filter(LayerProvider::getProvidesArmorLayer).filter(layerProvider -> {
                boolean matches = true;
                for (final String filter : filters) {
                    if (filter.startsWith("!")) {
                        final String exclude = filter.substring(1, filter.length() - 1);
                        matches &= !Objects.equals(layerProvider.getModId(), exclude);
                    } else {
                        matches &= Objects.equals(layerProvider.getModId(), filter);
                    }
                }
                return matches;
            }).max(new ProviderComparator());
        }

        if (!provider.isPresent()) {
            provider = layerOverrides.stream().filter(LayerProvider::getProvidesArmorLayer).max(new ProviderComparator());
        }

        return provider.<LayerRenderer<?>>map(layerProvider -> layerProvider.getArmorLayer(renderer)).orElse(new LayerWolfArmor(renderer));
    }

    /**
     * Gets a backpack armor layer
     * @param renderer The renderer to which the layer is being added
     * @return The backpack layer renderer
     */
    @SideOnly(Side.CLIENT)
    @Nonnull
    public static synchronized LayerRenderer<?> getBackpackLayer(@Nonnull RenderLiving<?> renderer) {
        return getBackpackLayer(renderer, null);
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public static synchronized LayerRenderer<?> getBackpackLayer(@Nonnull RenderLiving<?> renderer, @Nullable String modId) {
        Optional<LayerProvider> provider = Optional.empty();
        if (modId != null) {
            provider = layerOverrides.stream().filter(LayerProvider::getProvidesBackpackLayer).filter(layerProvider -> {
                if (modId.startsWith("!")) {
                    final String excludeModId = modId.substring(1, modId.length() - 1);
                    return !Objects.equals(layerProvider.getModId(), excludeModId);
                }
                return Objects.equals(layerProvider.getModId(), modId);
            }).max(new ProviderComparator());
        }

        if (!provider.isPresent()) {
            provider = layerOverrides.stream().filter(LayerProvider::getProvidesBackpackLayer).max(new ProviderComparator());
        }

        return provider.<LayerRenderer<?>>map(layerProvider -> layerProvider.getBackpackLayer(renderer)).orElse(new LayerWolfBackpack(renderer));
    }

    public static boolean isModLoaded(String modId) {
        final List<ModContainer> mods = Loader.instance().getActiveModList();
        Optional<ModContainer> provided = mods.stream().filter(mod -> modId.equalsIgnoreCase(mod.getModId())).findFirst();
        return provided.isPresent();
    }

    /**
     * Initializes the provider layers.  Necessary to call during init to override the default wolf models.
     */
    @SideOnly(Side.CLIENT)
    public static void registerLayerProviders() {
        layerOverrides = Lists.newArrayList(new LayerProvider());
        logger.info("Initializing layer providers...");
        compatibilityProviders.values().forEach(provider -> {
            if (isModLoaded(provider.getModId())) {
                logger.info("Loading layer providers for " + provider.getModId() + "...");
                List<LayerProvider> layerProviders = provider.getLayerProviders();
                layerProviders.forEach(Compatibility::registerLayer);
            }
        });
    }

    /**
     * Sets up the compatibility providers
     */
    @SideOnly(Side.CLIENT)
    public static void loadComplete_Client(FMLLoadCompleteEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            if(isModLoaded(provider.getModId())) {
                logger.info("Performing client-side compatibility loadComplete for " + provider.getModId() + "...");
                provider.loadComplete_Client(event);
            }
        });
    }

    public static void loadComplete(FMLLoadCompleteEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            if(isModLoaded(provider.getModId())) {
                logger.info("Performing server-side compatibility loadComplete for " + provider.getModId() + "...");
                provider.loadComplete(event);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void postInit_Client(FMLPostInitializationEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            if(isModLoaded(provider.getModId())) {
                logger.info("Performing client-side compatibility postInit for " + provider.getModId() + "...");
                provider.postInit_Client(event);
            }
        });
    }

    public static void postInit(FMLPostInitializationEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            if(isModLoaded(provider.getModId())) {
                logger.info("Performing server-side compatibility postInit for " + provider.getModId() + "...");
                provider.postInit(event);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void init_Client(FMLInitializationEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            if(isModLoaded(provider.getModId())) {
                logger.info("Performing client-side compatibility init for " + provider.getModId() + "...");
                provider.init_Client(event);
            }
        });
    }

    public static void init(FMLInitializationEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            if(isModLoaded(provider.getModId())) {
                logger.info("Performing server-side compatibility init for " + provider.getModId() + "...");
                provider.init(event);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void preInit_Client(FMLPreInitializationEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            if(isModLoaded(provider.getModId())) {
                logger.info("Performing client-side compatibility preInit for " + provider.getModId() + "...");
                provider.preInit_Client(event);
            }
        });
    }

    public static void preInit(FMLPreInitializationEvent event) {
        compatibilityProviders.values().forEach(provider -> {
            if(isModLoaded(provider.getModId())) {
                logger.info("Performing server-side compatibility preInit for " + provider.getModId() + "...");
                provider.preInit(event);
            }
        });
    }
}