package dev.satyrn.wolfarmor.common.network.packets;

import dev.satyrn.wolfarmor.api.IWolfArmorCapability;
import dev.satyrn.wolfarmor.api.util.Capabilities;
import dev.satyrn.wolfarmor.common.network.MessageBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class WolfArmorCapabilityDataMessage extends MessageBase<WolfArmorCapabilityDataMessage> {

    private int entityId;
    private boolean hasChest;
    private ItemStack armorItemStack;

    public WolfArmorCapabilityDataMessage() {}

    public WolfArmorCapabilityDataMessage(int entityId, boolean hasChest, ItemStack armorItemStack) {
         this.entityId = entityId;
         this.hasChest = hasChest;
         this.armorItemStack = armorItemStack;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.entityId = buffer.readInt();
        this.hasChest = buffer.readBoolean();
        this.armorItemStack = buffer.readItemStack();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.hasChest);
        buffer.writeItemStack(this.armorItemStack);
    }

    @Override
    protected IMessage process(EntityPlayer player, Side side) {
        World world = player.getEntityWorld();
        Entity target = world.getEntityByID(entityId);
        if(target instanceof EntityWolf) {
            IWolfArmorCapability capability = target.getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
            if (capability != null) {
                capability.setArmorItemStack(this.armorItemStack);
                capability.setHasChest(this.hasChest);
            }
        }

        return null;
    }
}
