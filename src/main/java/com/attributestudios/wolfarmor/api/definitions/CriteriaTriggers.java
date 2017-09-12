package com.attributestudios.wolfarmor.api.definitions;

import com.attributestudios.wolfarmor.advancements.EquipWolfArmorTrigger;
import com.attributestudios.wolfarmor.api.util.DynamicallyUsed;

@DynamicallyUsed
public final class CriteriaTriggers {
    private CriteriaTriggers() {}

    public static final EquipWolfArmorTrigger EQUIP_WOLF_ARMOR = new EquipWolfArmorTrigger(Resources.TRIGGER_EQUIP_WOLF_ARMOR);
}
