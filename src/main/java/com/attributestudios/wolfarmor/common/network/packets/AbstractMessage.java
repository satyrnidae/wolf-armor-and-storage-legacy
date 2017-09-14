package com.attributestudios.wolfarmor.common.network.packets;

import java.io.IOException;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.util.Definitions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class AbstractMessage<T extends AbstractMessage<T>> implements IMessage, IMessageHandler<T, IMessage> {

    protected abstract void read(PacketBuffer buffer) throws IOException;

    protected abstract void write(PacketBuffer buffer) throws IOException;

    protected abstract IMessage process(EntityPlayer player, Side side);

    protected boolean isSideValid(Side side) {
        return true;
    }

    protected boolean isMainThreadRequired() {
        return true;
    }

	@Override
	public IMessage onMessage(T message, MessageContext ctx) {
		if(!isSideValid(ctx.side)) {
            throw new RuntimeException(String.format("The side %s is not valid for packet %s", ctx.side.name(), message.getClass().getSimpleName()));
        }
        
        return this.isMainThreadRequired() ? enqueue(message, ctx) : message.process(WolfArmorMod.getProxy().getPlayerFromContext(ctx), ctx.side);
	}

    private IMessage enqueue(T message, MessageContext context) {
        IThreadListener thread = WolfArmorMod.getProxy().getThreadFromContext(context);
        thread.addScheduledTask(new Runnable() {
            public void run() {
                message.process(WolfArmorMod.getProxy().getPlayerFromContext(context), context.side);
            }
        });
        return null;
    }

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
            read(new PacketBuffer(buf));
        }
        catch(IOException ex) {
            throw new RuntimeException(String.format("The packet %s failed to deserialize.", this.getClass().getSimpleName()), ex);
        }
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
            write(new PacketBuffer(buf));
        }
        catch (IOException ex) {
            throw new RuntimeException(String.format("The packet %s failed to serialize.", this.getClass().getSimpleName()), ex);
        }
	}

    public static abstract class AbstractClientMessage<T extends AbstractClientMessage<T>> extends AbstractMessage<T> {
        @Override
        protected boolean isSideValid(Side side) {
            return side.isClient();
        }
    }

    public static abstract class AbstractServerMessage<T extends AbstractServerMessage<T>> extends AbstractMessage<T> {
        @Override
        protected boolean isSideValid(Side side) {
            return side.isServer();
        }
    }
}