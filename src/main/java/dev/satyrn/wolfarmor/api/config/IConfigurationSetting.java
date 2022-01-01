/*
 * This file is based on the configuration system implemented in copycore, (c) 2014 copygirl. Licensed under MIT. Please
 * see THIRDPARTY for license and notices related to the use of this code.
 */
package dev.satyrn.wolfarmor.api.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides default implementation methods for configuration setting instances
 * @param <T> The setting type
 * @since 4.2.0
 */
public interface IConfigurationSetting<T> {
    /**
     * Gets the configuration property for the setting
     * @param config The configuration from which to load the property
     * @return The property object
     * @since 4.2.0
     */
    @Nonnull Property getConfigurationProperty(@Nonnull Configuration config);

    /**
     * Gets the setting's category concatenated with its name.
     * @return The full name of the setting, consisting of its category and name concatenated with a period.
     * @since 4.2.0
     */
    @Nonnull String getFullName();

    /**
     * Gets the default value of the setting, regardless of synchronization or current value.
     * @return The default value of the setting.
     * @since 4.2.0
     */
    @Nullable T getDefaultValue();

    /**
     * The synchronized value for the setting
     * @return The synchronized value, or null if the setting is not a synchronized setting.
     * @since 4.2.0
     * @deprecated as of API 4.3.0; replaced by {@link #getSyncedValue()}
     */
    @Deprecated @Nullable default T getSynchronizedValue() { return this.getSyncedValue(); }

    /**
     * The synchronized value for the setting
     * @apiNote This method was renamed in API 4.3.0; replaces {@link #getSynchronizedValue()}
     * @return The synchronized value, or null if the setting is not a synchronized setting.
     * @since 4.3.0
     */
    @Nullable T getSyncedValue();

    /**
     * Gets the calculated value of the setting.
     * @return If synchronized, the synchronized value will be returned.  Otherwise, the standard value will be
     *         returned. If either the synchronized or standard values are null, the default value is returned.
     * @since 4.2.0
     */
    @Nonnull T getCurrentValue();

    /**
     * Sets the value of the configuration setting.
     * @param value The new value for the configuration setting
     * @return self
     * @since 4.2.0
     */
    @Nonnull IConfigurationSetting<T> setValue(@Nullable T value);

    /**
     * Gets the setting's name
     * @return The setting's name
     * @since 4.2.0
     */
    @Nonnull String getName();

    /**
     * Sets the name of the setting.  This will be used for localization client-side, and also determine the
     * configuration group that the setting will appear under in the UI and in the configuration file.
     * @param value The new value of the name field
     * @return self
     * @since 4.2.0
     */
    @Nonnull IConfigurationSetting<T> setName(@Nonnull String value);

    /**
     * Gets the setting's category
     * @return The setting's category
     * @since 4.2.0
     */
    @Nonnull String getCategory();

    /**
     * Sets the configuration category name.  This determines in which group the config will be classified.
     * @param value The new value for the configuration category
     * @return self
     * @since 4.2.0
     */
    @Nonnull IConfigurationSetting<T> setCategory(@Nonnull String value);

    /**
     * Gets the comment value
     * @return The config setting's comment value
     * @since 4.2.0
     */
    @Nonnull String getComment();

    /**
     * Sets the comment for the config entry
     * @param value The comment's value
     * @return self
     * @since 4.2.0
     */
    @Nonnull IConfigurationSetting<T> setComment(@Nonnull String value);

    /**
     * Gets a flag indicating whether or not this setting's value is synchronized from the host to the client instance
     * @return {@code true} if synchronization is enabled, otherwise {@code false}
     * @deprecated as of API 4.3.0, replaced by {@link #getSynchronizes()}
     * @since 4.2.0
     */
    @Deprecated
    default boolean getIsSynchronized() {  return this.getSynchronizes(); }

    /**
     * Flags this setting as either synchronized between server and client, or unsynchronized.
     * @param value {@code true} to mark synchronized, {@code false} for unsynchronized values.
     * @return self
     * @deprecated as of API 4.3.0, replaced by {@link #setSynchronizes(boolean)}
     * @since 4.2.0
     */
    @Deprecated
    default IConfigurationSetting<T> setIsSynchronized(boolean value) { return this.setSynchronizes(value); }

    /**
     * Gets a flag indicating whether or not this setting's value is synchronized from the host to the client instance
     * @apiNote This method was renamed for 4.3.0 of the API from {@link #getIsSynchronized()}
     * @return {@code true} if synchronization is enabled, otherwise {@code false}
     * @since 4.3.0
     */
    boolean getSynchronizes();

    /**
     * Flags this setting as either synchronized between server and client, or unsynchronized.
     * @param value {@code true} to mark synchronized, {@code false} for unsynchronized values.
     * @return self
     */
    @Nonnull IConfigurationSetting<T> setSynchronizes(boolean value);

    /**
     * Loads the setting from the configuration file
     * @param config The configuration file
     */
    void loadFromConfiguration(@Nonnull Configuration config);

    /**
     * Saves the value for the setting to the configuration file
     * @param config The configuration file
     */
    void saveToConfiguration(@Nonnull Configuration config);

    /**
     * Parses a string value into the setting value type
     * @param value The value to parse
     * @return The parsed setting value
     */
    @Nonnull T parse(String value);
}
