package dev.satyrn.wolfarmor.common.network;

import java.io.IOException;

import dev.satyrn.wolfarmor.WolfArmorMod;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Base packet and packet handler implementation
 * @author Isabel Maskrey (satyrnidae)
 * @param <T> The packet type
 */
public abstract class MessageBase<T extends MessageBase<T>> implements IMessage, IMessageHandler<T, IMessage> {
    /**
     * Reads the packet data from the buffer
     * @param buffer The packet data buffer
     * @throws IOException if the data read fails
     */
    protected abstract void read(PacketBuffer buffer) throws IOException;

    /**
     * Writes the packet data to the buffer
     * @param buffer The packet data buffer
     * @throws IOException if the data read fails
     */
    protected abstract void write(PacketBuffer buffer) throws IOException;

    /**
     * Processes the message
     * @param player The sided player instance
     * @param side The side on which the message is being processed
     * @return Any replies to be sent
     */
    @Nullable protected abstract IMessage process(EntityPlayer player, Side side);

    /**
     * Checks whether the given side is valid or not
     * @param side The side which is being checked
     * @return <c>true</c> if the packet should be processed on this side
     */
    protected boolean isSideValid(Side side) { return true;  }

    /**
     * Gets whether or not this message should be executed on the world thread instead of the network thread
     * @return <c>true</c> if the message should be processed on the world thread
     * @deprecated from version 3.6.0 onwards, use {@link #isScheduled()} instead
     */
    @Deprecated protected boolean isMainThreadRequired() { return this.isScheduled(); }

    /**
     * Gets whether or not this message should be executed on the world thread instead of the network thread
     * @apiNote Replaces {@link #isMainThreadRequired()} from version 3.6.0 onwards.
     * @return <c>true</c> if the message should be processed on the world thread
     * @since 3.6.0
     */
    protected boolean isScheduled() { return true; }

    /**
     * Handles the received packet
     * @param message The message instance
     * @param context The message context
     * @return Any replies to be sent
     */
	@Override
	public final IMessage onMessage(T message, MessageContext context) {
		if (!this.isSideValid(context.side)) {
            throw new RuntimeException(
                    String.format("The side %s is not valid for packet %s", context.side.name(),
                            message.getClass().getSimpleName()));
        }

		if (this.isScheduled()) {
		    this.enqueue(message, context);
		    return null;
        }

        return message.process(WolfArmorMod.getProxy().getPlayerFromContext(context), context.side);
	}

    /**
     * Enqueues processing the message on the main thread
     * @param message The message to enque
     * @param context The message context
     * @since 3.6.0
     */
    private void enqueue(T message, MessageContext context) {
        IThreadListener thread = WolfArmorMod.getProxy().getThreadFromContext(context);
        thread.addScheduledTask(new Runnable() {
            public void run() {
                message.process(WolfArmorMod.getProxy().getPlayerFromContext(context), context.side);
            }
        });
    }

    /**
     * Reads the packet data from the buffer
     * @param buf The packet data buffer
     * @throws RuntimeException if the data read fails
     */
	@Override
	public final void fromBytes(ByteBuf buf) {
		try {
            read(new PacketBuffer(buf));
        }
        catch(IOException ex) {
            throw new RuntimeException(String.format("The packet %s failed to deserialize.", this.getClass().getSimpleName()), ex);
        }
	}

    /**
     * Writes the packet data to the buffer
     * @param buf The packet data buffer
     * @throws RuntimeException if the data read fails
     */
	@Override
	public final void toBytes(ByteBuf buf) {
		try {
            write(new PacketBuffer(buf));
        }
        catch (IOException ex) {
            throw new RuntimeException(String.format("The packet %s failed to serialize.", this.getClass().getSimpleName()), ex);
        }
	}

    /**
     * Base client packet and packet handler implementation
     * @author Isabel Maskrey (satyrnidae)
     * @param <T> The packet type
     */
    public static abstract class ClientMessageBase<T extends ClientMessageBase<T>> extends MessageBase<T> {
        /**
         * Checks whether the given side is valid or not
         * @param side The side which is being checked
         * @return <c>true</c> if running on the client side
         */
        @Override
        protected final boolean isSideValid(Side side) {
            return side.isClient();
        }
    }

    /**
     * Base server packet and packet handler implementation
     * @author Isabel Maskrey (satyrnidae)
     * @param <T> The packet type
     */
    public static abstract class ServerMessageBase<T extends ServerMessageBase<T>> extends MessageBase<T> {
        /**
         * Checks whether the given side is valid or not
         * @param side The side which is being checked
         * @return <c>true</c> if running on the server side
         */
        @Override
        protected final boolean isSideValid(Side side) {
            return side.isServer();
        }
    }
}