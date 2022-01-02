package dev.satyrn.wolfarmor.api.util;

import dev.satyrn.wolfarmor.api.IWolfArmorCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Add the old API files under Attribute Studios namespace to fix Sophisticated
 * Wolves compatibility.
 * @author Isabel Maskrey
 * @deprecated 3.0. Unused!
 */
@Deprecated
public abstract class Capabilities {

    /**
     * The wolf armor capability instance.
     */
    @CapabilityInject(IWolfArmorCapability.class)
    public static Capability<IWolfArmorCapability> CAPABILITY_WOLF_ARMOR;
}
