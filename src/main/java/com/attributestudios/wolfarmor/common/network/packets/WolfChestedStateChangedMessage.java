package com.attributestudios.wolfarmor.common.network.packets;

import java.io.IOException;

import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.network.packets.AbstractMessage.AbstractClientMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class WolfChestedStateChangedMessage extends AbstractClientMessage<WolfChestedStateChangedMessage> {

    private int entityId;
    private boolean isChested;

    public WolfChestedStateChangedMessage(int entityId, boolean isChested) {
        this.entityId = entityId;
        this.isChested = isChested;
    }

	@Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.entityId = buffer.readInt();
        this.isChested = buffer.readBoolean();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.isChested);
    }

    @Override
    protected IMessage process(EntityPlayer player, Side side) {
        World world = player.getEntityWorld();
        Entity entity = world.getEntityByID(this.entityId);
        if(entity != null) {
            IWolfArmorCapability capability = entity.getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
            if(capability != null) {
                capability.setHasChest(this.isChested);
            }
        }
        return null;
    }

}