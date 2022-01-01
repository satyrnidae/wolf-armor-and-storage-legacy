package dev.satyrn.wolfarmor.api.util;

import net.minecraftforge.fml.common.registry.EntityEntry;
import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Resources.MOD_ID)
public abstract class Entities {

    /**
     * The entity entry for the armored wolf.
     * @deprecated Since API-1.0
     */
    @ObjectHolder("wolf_armored")
    @Deprecated
    public static final EntityEntry WOLF_ARMORED = null;
}
