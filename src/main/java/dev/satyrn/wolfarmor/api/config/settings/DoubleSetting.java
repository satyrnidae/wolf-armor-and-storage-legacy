package dev.satyrn.wolfarmor.api.config.settings;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;

// This file is based on the configuration system implemented in the Wearable Backpacks mod, (c) 2014-2019 copygirl.
// Licensed under MIT.  Please see THIRDPARTY for license and notices related to the use of this code.
/**
 * A double setting value
 */
public class DoubleSetting extends ValueSetting<Double> {
    /**
     * Instantiates a new setting with a property type of DOUBLE
     * @param defaultValue The default value
     */
    public DoubleSetting(Double defaultValue) {
        super(defaultValue);
        this.setPropertyType(Property.Type.DOUBLE);
    }

    /**
     * Loads the setting value from an NBT object.
     *
     * @param tag The NBTBase to read
     */
    @Override
    public Double readTag(NBTBase tag) {
        return ((NBTTagDouble)tag).getDouble();
    }

    /**
     * Writes the value to a new NBT tag object.
     *
     * @param value The value to write to the NBT tag.
     * @return The newly constructed NBT tag object.
     */
    @Override
    public NBTBase writeTag(Double value) {
        return new NBTTagDouble(value);
    }

    /**
     * Parses a string value into the setting value type
     *
     * @param value The value to parse
     * @return The parsed setting value
     */
    @Nonnull
    @Override
    public Double parse(String value) {
        return Double.parseDouble(value);
    }
}
