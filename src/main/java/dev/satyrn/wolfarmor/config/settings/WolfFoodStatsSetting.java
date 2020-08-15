package dev.satyrn.wolfarmor.config.settings;

import dev.satyrn.wolfarmor.api.config.settings.ValueSetting;
import dev.satyrn.wolfarmor.util.WolfFoodStatsLevel;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class WolfFoodStatsSetting extends ValueSetting<WolfFoodStatsLevel> {

    public WolfFoodStatsSetting() {
        super(WolfFoodStatsLevel.DISABLED);
    }

    @Override
    public void saveToConfiguration(@Nonnull Configuration config) {
        this.getConfigurationProperty(config).set(Objects.toString(this.getValue()).toLowerCase());
    }

    /**
     * Loads the setting value from an NBT object.
     *
     * @param tag The NBTBase to read
     */
    @Override
    public WolfFoodStatsLevel readTag(NBTBase tag) {
        return this.parse(((NBTTagString)tag).getString());
    }

    /**
     * Writes the value to a new NBT tag object.
     *
     * @param value The value to write to the NBT tag.
     * @return The newly constructed NBT tag object.
     */
    @Override
    public NBTBase writeTag(WolfFoodStatsLevel value) {
        return new NBTTagString(value.name());
    }

    /**
     * Parses a string value into the setting value type
     *
     * @param value The value to parse
     * @return The parsed setting value
     */
    @Nonnull
    @Override
    public WolfFoodStatsLevel parse(String value) {
        Optional<WolfFoodStatsLevel> stats = Arrays.stream(WolfFoodStatsLevel.values()).filter(option -> option.name().equalsIgnoreCase(value.trim())).findAny();
        return stats.orElse(WolfFoodStatsLevel.DISABLED);
    }
}
