package com.attributestudios.wolfarmor.common.network.packets;

import com.attributestudios.wolfarmor.api.util.annotation.DynamicallyUsed;
import com.attributestudios.wolfarmor.common.network.MessageBase.ClientMessageBase;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class WolfEatMessage extends ClientMessageBase<WolfEatMessage> {

    private int entityId;
    private ItemStack foodItem;

    @DynamicallyUsed
    public WolfEatMessage() {}

    public WolfEatMessage(int entityId, ItemStack foodItem) {
        this.entityId = entityId;
        this.foodItem = foodItem;
    }

	@Override
	protected void read(PacketBuffer buffer) throws IOException {
        this.entityId = buffer.readInt();
        this.foodItem = buffer.readItemStack();
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(this.entityId);
        buffer.writeItemStack(this.foodItem);
	}

	@Override
	protected IMessage process(EntityPlayer player, Side side) {
        World world = player.getEntityWorld();
        Entity entity = player.getEntityWorld().getEntityByID(this.entityId);
        if(entity != null) {
            Vec3d velocity = new Vec3d((world.rand.nextFloat() - 0.5D) * 0.1D, world.rand.nextFloat() * 0.1D + 0.1D, 0.0D);
            velocity = velocity.rotatePitch(-entity.rotationPitch * (float)(Math.PI / 180));
            velocity = velocity.rotateYaw(-entity.rotationYaw * (float)(Math.PI / 180));
            Vec3d position = new Vec3d(world.rand.nextFloat() - 0.5D, -world.rand.nextFloat() * 0.6D - 0.3D, 0.6D);
            position = position.rotatePitch(-entity.rotationPitch * (float)(Math.PI / 180));
            position = position.rotateYaw(-entity.rotationYaw * (float)(Math.PI / 180));
            position = position.addVector(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);

            if(foodItem.getHasSubtypes()) {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK,
                        position.x,
                        position.y,
                        position.z,
                        velocity.x,
                        velocity.y + 0.05D,
                        velocity.z,
                        Item.getIdFromItem(foodItem.getItem()), foodItem.getMetadata());
            }
            else {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK,
                        position.x,
                        position.y,
                        position.z,
                        velocity.x,
                        velocity.y + 0.05D,
                        velocity.z,
                        Item.getIdFromItem(foodItem.getItem()));
            }
        }

		return null;
	}


}