package com.attributestudios.wolfarmor.api.definitions;

import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import net.minecraftforge.fml.common.registry.EntityEntry;

public final class EntityEntries {
    private EntityEntries() {}

    @Deprecated
    public static final EntityEntry ENTITY_WOLF_ARMORED =
            new EntityEntry(EntityWolfArmored.class, "wolfarmor.wolf_armored")
                .setRegistryName(Resources.ENTITY_WOLF_ARMORED);
}
