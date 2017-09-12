package com.attributestudios.wolfarmor.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;

public class WolfAutoHealMessage implements IMessage {
    public WolfAutoHealMessage() {}

    private int entityId;
    private int itemId;
    private int metadata;
    private boolean showHappyParticle;
    public WolfAutoHealMessage(@Nonnegative int entityId,
                               @Nonnegative int itemId,
                               @Nonnegative int metadata,
                               boolean showHappyParticle) {
        this.entityId = entityId;
        this.itemId = itemId;
        this.metadata = metadata;
        this.showHappyParticle = showHappyParticle;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeInt(itemId);
        buffer.writeInt(metadata);
        buffer.writeBoolean(showHappyParticle);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        entityId = buffer.readInt();
        itemId = buffer.readInt();
        metadata = buffer.readInt();
        showHappyParticle = buffer.readBoolean();
    }

    public static class WolfAutoHealMessageHandler implements IMessageHandler<WolfAutoHealMessage, IMessage> {
        @Override
        @Nullable
        public IMessage onMessage(WolfAutoHealMessage message, MessageContext ctx) {
            int entityId = message.entityId;
            int itemId = message.itemId;
            int metadata = message.metadata;
            boolean showHappyParticle = message.showHappyParticle;
            @Nonnull World world = Minecraft.getMinecraft().world;

            if(world.isRemote) {
                @Nullable Entity entity = world.getEntityByID(entityId);
                if(entity != null) {
                    if(showHappyParticle) {
                        for(int i = 0; i < 3 + world.rand.nextInt(5); ++i) {
                            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
                                    entity.posX + (double) (world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width,
                                    entity.posY + 0.5D + (double) (world.rand.nextFloat() * entity.height),
                                    entity.posZ + (double) (world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width,
                                    0.0D,
                                    0.0D,
                                    0.0D);
                        }
                    }
                    else {
                        Vec3d vec3d = new Vec3d(((double) world.rand.nextFloat() - 0.5D) * 0.1D,
                                Math.random() * 0.1D + 0.1D,
                                0.0D);
                        vec3d = vec3d.rotatePitch(-entity.rotationPitch * 0.017453292F);
                        vec3d = vec3d.rotateYaw(-entity.rotationYaw * 0.017453292F);
                        double d0 = (double) (-world.rand.nextFloat()) * 0.6D - 0.3D;
                        Vec3d vec3d1 = new Vec3d(((double) world.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
                        vec3d1 = vec3d1.rotatePitch(-entity.rotationPitch * 0.017453292F);
                        vec3d1 = vec3d1.rotateYaw(-entity.rotationYaw * 0.017453292F);
                        vec3d1 = vec3d1.addVector(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ);

                        world.spawnParticle(EnumParticleTypes.ITEM_CRACK,
                                vec3d1.x,
                                vec3d1.y,
                                vec3d1.z,
                                vec3d.x,
                                vec3d.y + 0.05D,
                                vec3d.z,
                                itemId,
                                metadata);
                    }
                }
            }

            return null;
        }
    }
}
