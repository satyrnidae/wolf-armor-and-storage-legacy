package com.attributestudios.wolfarmor.common.network.packets;

import java.io.IOException;

import com.attributestudios.wolfarmor.common.network.packets.AbstractMessage.AbstractClientMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class WolfHealMessage extends AbstractClientMessage<WolfHealMessage> {

    private int entityId;

    public WolfHealMessage(int entityId) {
        this.entityId = entityId;
    }

	@Override
	protected void read(PacketBuffer buffer) throws IOException { 
        this.entityId = buffer.readInt();
    }

	@Override
	protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(this.entityId);
    }

	@Override
	protected IMessage process(EntityPlayer player, Side side) {
        World world = player.getEntityWorld();
        Entity entity = world.getEntityByID(this.entityId);
        if(entity != null) {
            for(int particleIndex = 0; particleIndex < 3 + world.rand.nextInt(5); ++particleIndex) {
                world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, 
                    entity.posX + world.rand.nextFloat() * entity.width * 2 - entity.width,
                    entity.posY + 0.5D + world.rand.nextFloat() * entity.height,
                    entity.posZ + world.rand.nextFloat() * entity.width * 2 - entity.width,
                    0, 0, 0);
            }
        }
        return null;
	}    
}
