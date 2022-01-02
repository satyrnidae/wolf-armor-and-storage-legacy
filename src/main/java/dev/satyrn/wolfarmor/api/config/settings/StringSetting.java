package dev.satyrn.wolfarmor.api.config.settings;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;

// This file is based on the configuration system implemented in the Wearable Backpacks mod, (c) 2014-2019 copygirl.
// Licensed under MIT.  Please see THIRDPARTY for license and notices related to the use of this code.
/**
 * A string setting value
 */
public class StringSetting extends ValueSetting<String> {
    /**
     * Instantiates a new string setting with the specified detailed value, and property type set to STRING.
     * @param defaultValue The default value
     */
    public StringSetting(String defaultValue) {
        super(defaultValue);
        this.setPropertyType(Property.Type.STRING);
    }

    /**
     * Loads the setting value from an NBT object.
     *
     * @param tag The NBTBase to read
     */
    @Override
    public String readTag(NBTBase tag) {
        return ((NBTTagString)tag).getString();
    }

    /**
     * Writes the value to a new NBT tag object.
     *
     * @param value The value to write to the NBT tag.
     * @return The newly constructed NBT tag object.
     */
    @Override
    public NBTBase writeTag(String value) {
        return new NBTTagString(value);
    }

    /**
     * Parses a string value into the setting value type
     * @param value The value to parse
     * @return The parsed setting value
     */
    @Nonnull
    @Override
    public String parse(String value) {
        return value;
    }
}
