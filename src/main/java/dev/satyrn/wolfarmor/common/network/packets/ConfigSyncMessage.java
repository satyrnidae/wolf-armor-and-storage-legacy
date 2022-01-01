package dev.satyrn.wolfarmor.common.network.packets;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.config.settings.Setting;
import dev.satyrn.wolfarmor.common.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigSyncMessage extends MessageBase.ClientMessageBase<ConfigSyncMessage> {

    private NBTTagCompound data;

    public ConfigSyncMessage() { }

    private ConfigSyncMessage(NBTTagCompound data) {
        this.data = data;
    }

    public static ConfigSyncMessage create() {
        NBTTagCompound configData = new NBTTagCompound();
        for (Setting<?> setting : WolfArmorMod.getConfig().getSettings()) {
            if (setting.getSynchronizes()) {
                configData.setTag(setting.getFullName(), setting.writeSynchronized());
            }
        }
        return new ConfigSyncMessage(configData);
    }

    @Override
    protected void read(PacketBuffer buffer) {
        try {
            this.data = buffer.readCompoundTag();
        } catch (Exception ex) {
            this.data = null;
        }
    }

    @Override
    protected void write(PacketBuffer buffer) {
        buffer.writeCompoundTag(this.data);
    }

    @Override
    protected IMessage process(EntityPlayer player, Side side) {
        if (this.data != null) {
            WolfArmorMod.getConfig().sync(this.data);
        }
        return null;
    }
}
