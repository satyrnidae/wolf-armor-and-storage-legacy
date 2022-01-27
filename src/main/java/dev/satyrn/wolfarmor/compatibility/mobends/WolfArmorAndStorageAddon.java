package dev.satyrn.wolfarmor.compatibility.mobends;

import dev.satyrn.wolfarmor.compatibility.mobends.mutators.ArmoredWolfMutator;
import goblinbob.mobends.core.addon.AddonAnimationRegistry;
import goblinbob.mobends.core.addon.IAddon;
import goblinbob.mobends.standard.client.renderer.entity.mutated.WolfRenderer;
import goblinbob.mobends.standard.data.WolfData;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WolfArmorAndStorageAddon implements IAddon {
    @Override
    public void registerContent(AddonAnimationRegistry registry) {
        registry.registerNewEntity(EntityWolf.class, WolfData::new, ArmoredWolfMutator::new, new WolfRenderer<>(),
                "wolfHeadMain", "wolfBody", "wolfLeg1", "wolfLeg2", "wolfLeg3", "wolfLeg4", "wolfTail", "wolfMane");
    }

    @Override
    public String getDisplayName() {
        return "wolfarmor";
    }
}
