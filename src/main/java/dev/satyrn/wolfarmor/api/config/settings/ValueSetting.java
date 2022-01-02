package dev.satyrn.wolfarmor.api.config.settings;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;
import java.util.Objects;

// This file is based on the configuration system implemented in the Wearable Backpacks mod, (c) 2014-2019 copygirl.
// Licensed under MIT.  Please see THIRDPARTY for license and notices related to the use of this code.
/**
 * Abstract implementation for settings which are not arrays
 * @param <T> The setting type
 */
public abstract class ValueSetting<T> extends Setting<T> {
    public ValueSetting(T defaultValue) {
        super(defaultValue);
    }

    public void loadFromConfiguration(@Nonnull Configuration config) {
        this.setValue(this.parse(this.getConfigurationProperty(config).getString()));
    }

    public void saveToConfiguration(@Nonnull Configuration config) {
        this.getConfigurationProperty(config).set(Objects.toString(this.getValue()));
    }

    /**
     * Gets the configuration property for the setting
     * @param config The configuration from which to load the property
     * @return The property object
     */
    @Nonnull
    public Property getConfigurationProperty(@Nonnull Configuration config) {
        return config.get(this.getCategory(),
                this.getName(),
                Objects.toString(this.getDefaultValue()),
                this.getComment(),
                this.getPropertyType());
    }
}
