package dev.satyrn.wolfarmor.common.network.packets;

import java.io.IOException;

import dev.satyrn.wolfarmor.common.network.MessageBase.ClientMessageBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Handles auto-eating wolf heal events
 * @author Isabel Maskrey (satyrnidae)
 * @since 2.1.0
 */
public class WolfHealMessage extends ClientMessageBase<WolfHealMessage> {
    private int entityId;

    /**
     * Initializes an empty WolfHealMessage
     * @since 2.1.0
     */
    public WolfHealMessage() {}

    /**
     * Initializes a new WolfHealMessage with the given entityId
     * @param entityId The ID of the entity around which to spawn the heal particles
     * @since 2.1.0
     */
    public WolfHealMessage(int entityId) {
        this.entityId = entityId;
    }

    /**
     * Reads the entity ID from the buffer
     * @param buffer The packet data buffer
     * @since 2.1.0
     */
	@Override
	protected void read(PacketBuffer buffer) {
        this.entityId = buffer.readVarInt();
    }

    /**
     * Writes the entity ID to the buffer
     * @param buffer The packet data buffer
     * @since 2.1.0
     */
	@Override
	protected void write(PacketBuffer buffer) {
        buffer.writeVarInt(this.entityId);
    }

    /**
     * Spawns happy particles around the wolf
     * @param player The sided player instance
     * @param side The side on which the message is being processed
     * @return Nothing, as this message is processed on the world thread and no reply can be sent
     * @since 2.1.0
     */
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
