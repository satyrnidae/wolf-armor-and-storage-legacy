package dev.satyrn.wolfarmor.common.network.packets;

import dev.satyrn.wolfarmor.common.network.MessageBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

/**
 * Removes active potion effects on the client side
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.6.0
 */
public class RemovePotionEffectMessage extends MessageBase.ClientMessageBase<RemovePotionEffectMessage> {
    private int entityId;
    private Potion potion;

    public RemovePotionEffectMessage() {}

    public RemovePotionEffectMessage(int entityId, Potion effect) {
        this.entityId = entityId;
        this.potion = effect;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.entityId = buffer.readVarInt();
        this.potion = Potion.getPotionById(buffer.readUnsignedByte());
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeVarInt(this.entityId);
        buffer.writeByte(Potion.getIdFromPotion(this.potion));
    }

    @Override
    protected IMessage process(EntityPlayer player, Side side) {
        Entity entity = player.getEntityWorld().getEntityByID(this.entityId);
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).removeActivePotionEffect(this.potion);
        }
        return null;
    }
}
