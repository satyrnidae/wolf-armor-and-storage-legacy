package dev.satyrn.wolfarmor.common.network.packets;

import dev.satyrn.wolfarmor.api.entity.IFoodStatsCreature;
import dev.satyrn.wolfarmor.api.util.CreatureFoodStats;
import dev.satyrn.wolfarmor.common.network.MessageBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class UpdateFoodStatsMessage extends MessageBase.ClientMessageBase<UpdateFoodStatsMessage> {
    private int entityId;
    private int foodLevel;
    private float foodSaturationLevel;

    public UpdateFoodStatsMessage() { }

    public UpdateFoodStatsMessage(int entityId, int foodLevel, float foodSaturationLevel) {
        this.entityId = entityId;
        this.foodLevel = foodLevel;
        this.foodSaturationLevel = foodSaturationLevel;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.entityId = buffer.readInt();
        this.foodLevel = buffer.readInt();
        this.foodSaturationLevel = buffer.readFloat();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.foodLevel);
        buffer.writeFloat(this.foodSaturationLevel);
    }

    @Override
    protected IMessage process(EntityPlayer player, Side side) {
        World world = player.getEntityWorld();
        Entity entity = world.getEntityByID(this.entityId);
        if (entity != null && IFoodStatsCreature.class.isAssignableFrom(entity.getClass())) {
            CreatureFoodStats foodStats = ((IFoodStatsCreature) entity).getFoodStats();
            foodStats.setFoodLevel(this.foodLevel);
            foodStats.setSaturationLevel(this.foodSaturationLevel);
        }
        return null;
    }
}
