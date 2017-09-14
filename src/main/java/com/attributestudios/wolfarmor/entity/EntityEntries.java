package com.attributestudios.wolfarmor.entity;

import com.attributestudios.wolfarmor.api.util.Definitions.ResourceLocations.Entities;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import net.minecraftforge.fml.common.registry.EntityEntry;

public final class EntityEntries {
    private EntityEntries() {}

    @Deprecated
    public static final EntityEntry ENTITY_WOLF_ARMORED =
            new EntityEntry(EntityWolfArmored.class, "wolfarmor.wolf_armored")
                .setRegistryName(Entities.WOLF_ARMORED);
}
