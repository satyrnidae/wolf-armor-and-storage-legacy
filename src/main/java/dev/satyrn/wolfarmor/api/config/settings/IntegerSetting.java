package dev.satyrn.wolfarmor.api.config.settings;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;

// This file is based on the configuration system implemented in the Wearable Backpacks mod, (c) 2014-2019 copygirl.
// Licensed under MIT.  Please see THIRDPARTY for license and notices related to the use of this code.
/**
 * An integer setting value
 */
public class IntegerSetting extends ValueSetting<Integer> {
    /**
     * Instantiates a new integer setting value with a property type of integer
     *
     * @param defaultValue The default value
     */
    public IntegerSetting(Integer defaultValue) {
        super(defaultValue);
        this.setPropertyType(Property.Type.INTEGER);
    }

    /**
     * Loads the setting value from an NBT object.
     *
     * @param tag The NBTBase to read
     */
    @Override
    public Integer readTag(NBTBase tag) {
        return ((NBTTagInt)tag).getInt();
    }

    /**
     * Writes the value to a new NBT tag object.
     *
     * @param value The value to write to the NBT tag.
     * @return The newly constructed NBT tag object.
     */
    @Override
    public NBTBase writeTag(Integer value) {
        return new NBTTagInt(value);
    }

    /**
     * Parses a string value into the setting value type
     *
     * @param value The value to parse
     * @return The parsed setting value
     */
    @Nonnull
    @Override
    public Integer parse(String value) {
        return Integer.parseInt(value);
    }
}
