package com.attributestudios.wolfarmor.common.network.packets;

import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.network.packets.AbstractMessage.AbstractClientMessage;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class WolfEquippedArmorMessage extends AbstractClientMessage<WolfEquippedArmorMessage> {

    private int entityId;
    private ItemStack armorItem;

    public WolfEquippedArmorMessage(int entityId, ItemStack armorItem) {
        this.entityId = entityId;
        this.armorItem = armorItem;
    }

	@Override
	protected void read(PacketBuffer buffer) throws IOException {
        this.entityId = buffer.readInt();
        this.armorItem = buffer.readItemStack();
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(this.entityId);
        buffer.writeItemStack(this.armorItem);
	}

	@Override
	protected IMessage process(EntityPlayer player, Side side) {
        World world = player.getEntityWorld();
        Entity entity = world.getEntityByID(this.entityId);
        if(entity != null) {
            IWolfArmorCapability capability = entity.getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
            if(capability != null) {
                capability.setArmorItemStack(this.armorItem);
            }
        }
        return null;
	}

}