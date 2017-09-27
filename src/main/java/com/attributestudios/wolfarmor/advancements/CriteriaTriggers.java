package com.attributestudios.wolfarmor.advancements;

import com.attributestudios.wolfarmor.api.util.Definitions;
import com.attributestudios.wolfarmor.api.util.annotation.DynamicallyUsed;

@DynamicallyUsed
public abstract class CriteriaTriggers {
    public static final EquipWolfArmorTrigger EQUIP_WOLF_ARMOR =
            new EquipWolfArmorTrigger(Definitions.ResourceLocations.CriteriaTriggers.EQUIP_WOLF_ARMOR);
}
