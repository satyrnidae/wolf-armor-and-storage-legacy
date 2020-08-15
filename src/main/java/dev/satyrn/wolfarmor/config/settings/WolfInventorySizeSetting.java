package dev.satyrn.wolfarmor.config.settings;

import dev.satyrn.wolfarmor.api.config.settings.ValueSetting;
import dev.satyrn.wolfarmor.util.WolfInventorySize;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;

/**
 * A value setting for a
 */
public class WolfInventorySizeSetting extends ValueSetting<WolfInventorySize> {
    /**
     * Instantiates a new wolf inventory size setting
     * @param defaultValue The default value
     */
    public WolfInventorySizeSetting(WolfInventorySize defaultValue) {
        super(defaultValue);
        this.setPropertyType(Property.Type.STRING);
    }
    /**
     * Loads the setting value from an NBT object.
     *
     * @param tag The NBTBase to read
     */
    @Override
    public WolfInventorySize readTag(NBTBase tag) {
        WolfInventorySize wolfInvSize = new WolfInventorySize();
        wolfInvSize.deserializeNBT((NBTTagByteArray)tag);
        return wolfInvSize;
    }

    /**
     * Writes the value to a new NBT tag object.
     *
     * @param value The value to write to the NBT tag.
     * @return The newly constructed NBT tag object.
     */
    @Override
    public NBTBase writeTag(WolfInventorySize value) {
        return value.serializeNBT();
    }

    /**
     * Parses a string value into the setting value type
     *
     * @param value The value to parse
     * @return The parsed setting value
     */
    @Nonnull
    @Override
    public WolfInventorySize parse(String value) {
        return WolfInventorySize.parseWolfInventorySize(value);
    }
}
