/*
 * This file is based on the configuration system implemented in copycore, (c) 2014 copygirl. Licensed under MIT. Please
 * see THIRDPARTY for license and notices related to the use of this code.
 */
package dev.satyrn.wolfarmor.api.config.settings;

import dev.satyrn.wolfarmor.api.config.IConfigurationSetting;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The base implementation of a setting entry.
 * @param <T> The setting value type.  Can be any class as long as the value may be stored as a string.
 */
public abstract class Setting<T> implements IConfigurationSetting<T> {
    private final T defaultValue;

    private T value;
    private T syncedValue;

    private boolean synchronizes;
    private boolean isSynchronized;

    private String category = "";
    private String name = "";
    private String comment = "";

    private Property.Type propertyType = Property.Type.STRING;
    private ReloadAction reloadAction = ReloadAction.NONE;
    private String configWidgetClassName = "";

    public Setting(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the setting's name
     * @return The setting's name
     */
    @Override
    @Nonnull public String getName() { return this.name; }

    /**
     * Sets the name of the setting.  This will be used for localization client-side, and also determine the
     * configuration group that the setting will appear under in the UI and in the configuration file.
     * @param value The new value of the name field
     * @return self
     */
    @Nonnull
    public Setting<T> setName(@Nonnull String value) {
        this.name = value;
        return this;
    }

    /**
     * Gets the setting's category
     * @return The setting's category
     */
    @Nonnull
    public String getCategory() { return this.category; }

    /**
     * Sets the configuration category name.  This determines in which group the config will be classified.
     * @param value The new value for the configuration category
     * @return self
     */
    @Nonnull
    public Setting<T> setCategory(@Nonnull String value) {
        this.category = value;
        return this;
    }

    /**
     * Gets the comment value
     * @return The config setting's comment value
     */
    @Nonnull
    public String getComment() { return this.comment; }

    /**
     * Sets the comment for the config entry
     * @param value The comment's value
     * @return self
     */
    @Nonnull
    public Setting<T> setComment(@Nonnull String value) {
        this.comment = value;
        return this;
    }

    /**
     * Gets the config property base type
     * @return The property base type
     */
    @Nonnull
    protected Property.Type getPropertyType() { return this.propertyType; }

    /**
     * Sets the configuration property base type
     * @param value The config property type
     */
    protected void setPropertyType(@Nonnull Property.Type value) { this.propertyType = value; }

    /**
     * Gets the actual value, instead of the default or synchronized values
     * @return The actual value
     */
    @Nullable
    protected T getValue() { return this.value; }

    /**
     * Sets the value of the configuration setting.
     * @param value The new value for the configuration setting
     * @return self
     */
    @Nonnull
    public Setting<T> setValue(T value) {
        this.value = value;
        return this;
    }


    /**
     * Gets a flag indicating whether or not this setting's value is synchronized from the host to the client instance
     * @return {@code true} if synchronization is enabled, otherwise {@code false}.
     */
    public boolean getSynchronizes() { return this.synchronizes; }

    /**
     * Flags this setting as either synchronized between server and client, or unsynchronized.
     * @param value {@code true} to mark synchronized, {@code false} for unsynchronized values.
     * @return self
     */
    @Nonnull
    public Setting<T> setSynchronizes(boolean value) {
        this.synchronizes = value;
        return this;
    }

    /**
     * Gets the default value of the setting, regardless of synchronization or current value.
     * @return The default value of the setting.
     */
    public T getDefaultValue() { return this.defaultValue; }

    /**
     * The synchronized value for the setting
     * @return The synchronized value, or null if the setting is not a synchronized setting.
     */
    public T getSyncedValue() { return this.synchronizes ? this.syncedValue : null;}

    /**
     * Flag indicating whether or not the setting is currently synchronized to a server's setting value
     * @return If the setting is not a synchronized setting, this getter always returns {@code false}. However, for
     * synchronized settings, it will return {@code true} if the setting is in a synchronized state.
     */
    public boolean getIsCurrentlySynchronized() { return this.synchronizes && this.isSynchronized; }

    /**
     * Gets the setting's category concatenated with its name.
     * @return The full name of the setting, consisting of its category and name concatenated with a period.
     */
    @Nonnull
    public String getFullName() { return String.format("%s.%s", this.category, this.name); }

    /**
     * Gets the calculated value of the setting.
     * @return If synchronized, the synchronized value will be returned.  Otherwise, the standard value will be
     * returned. If either the synchronized or standard values are null, the default value is returned.
     */
    @Nonnull
    public T getCurrentValue() {
        if (this.synchronizes && this.isSynchronized) {
            return this.syncedValue == null ? this.defaultValue : this.syncedValue;
        }
        else {
            return this.value == null ? this.defaultValue : this.value;
        }
    }

    /**
     * Reads the synchronized setting value from an NBT object
     * @param tag The tag
     */
    public void readSynchronized(NBTBase tag) {
        this.isSynchronized = true;
        this.syncedValue = this.readTag(tag);
    }

    /**
     * Writes the value to an NBTBase tag
     * @return The tag
     */
    public NBTBase writeSynchronized() { return this.writeTag(this.value); }

    /**
     * Sets the reload action required to alter this setting
     * @param reloadAction The action the user will need to take after altering this setting
     * @return self
     */
    public Setting<T> setReloadAction(ReloadAction reloadAction) {
        this.reloadAction = reloadAction;
        return this;
    }

    /**
     * Gets the reload action required to alter this setting
     * @return The reload action
     */
    public ReloadAction getReloadAction() {
        return this.reloadAction;
    }

    /**
     * Require the world to be reloaded in order to apply the setting.
     * @return self
     */
    public Setting<T> setRequiresWorldReload() { return this.setReloadAction(ReloadAction.WORLD); }

    /**
     * Require the game to be restarted in order to apply the setting.
     * @return self
     */
    public Setting<T> setRequiresMinecraftRestart() { return this.setReloadAction(ReloadAction.MINECRAFT); }

    /**
     * Check whether or not the world has to be reloaded in order to update the setting.
     * @return {@code true} if the world must be reloaded in order to update the setting.
     */
    public boolean getRequiresWorldReload() { return this.reloadAction == ReloadAction.WORLD; }

    /**
     * Checks whether or not the game has to be reloaded in order to update the setting.
     * @return {@code true} if the game must be reloaded in order to update the setting.
     */
    public boolean getRequiresMinecraftRestart() { return this.reloadAction == ReloadAction.MINECRAFT; }

    /**
     * Sets the class name for the configuration UI widget which will be used to alter the setting's value
     * @param configWidgetClassName The class name for the configuration UI widget
     * @return self
     */
    public Setting<T> setConfigWidgetClassName(String configWidgetClassName) {
        this.configWidgetClassName = configWidgetClassName;
        return this;
    }

    /**
     * Gets the class name of the configuration UI widget which will be used to edit this setting's value
     * @return The class name of the configuration UI widget which will be used to edit this setting's value
     */
    public String getConfigWidgetClassName() { return this.configWidgetClassName; }

    /**
     * Loads the setting value from an NBT object.
     * @param tag The NBTBase to read
     * @return The setting type derived from the NBT
     */
    public abstract T readTag(NBTBase tag);

    /**
     * Writes the value to a new NBT tag object.
     * @param value The value to write to the NBT tag.
     * @return The newly constructed NBT tag object.
     */
    public abstract NBTBase writeTag(T value);

    public void onDisconnect() {
        this.isSynchronized = false;
        this.syncedValue = null;
    }

    /**
     * The action required to update a setting's value
     */
    public enum ReloadAction {
        /**
         * Can be updated while the world is running
         */
        NONE,
        /**
         * Requires the user to exit any loaded worlds and re-enter
         */
        WORLD,
        /**
         * Requires the user to restart the game entirely
         */
        MINECRAFT
    }
}
