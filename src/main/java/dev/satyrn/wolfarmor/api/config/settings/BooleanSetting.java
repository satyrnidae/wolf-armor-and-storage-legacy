package dev.satyrn.wolfarmor.api.config.settings;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;

// This file is based on the configuration system implemented in the Wearable Backpacks mod, (c) 2014-2019 copygirl.
// Licensed under MIT.  Please see THIRDPARTY for license and notices related to the use of this code.
/**
 * A boolean setting value
 */
public class BooleanSetting extends ValueSetting<Boolean> {
    /**
     * Instantiates a new boolean setting
     * @param defaultValue The default value
     */
    public BooleanSetting(boolean defaultValue) {
        super(defaultValue);
        this.setPropertyType(Property.Type.BOOLEAN);
    }

    /**
     * Loads the setting value from an NBT object.
     *
     * @param tag The NBTBase to read
     */
    @Override
    public Boolean readTag(NBTBase tag) {
        return ((NBTTagByte)tag).getByte() != 0;
    }

    /**
     * Writes the value to a new NBT tag object.
     *
     * @param value The value to write to the NBT tag.
     * @return The newly constructed NBT tag object.
     */
    @Override
    public NBTBase writeTag(Boolean value) {
        return new NBTTagByte((byte)(this.getValue() ? 1 : 0));
    }

    /**
     * Parses a string value into the setting value type
     * @param value The value to parse
     * @return The parsed setting value
     */
    @Nonnull
    @Override
    public Boolean parse(String value) {
        switch(value.toLowerCase()) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw new IllegalArgumentException(String.format("The string '%s' is not a valid boolean.", value));
        }
    }
}
