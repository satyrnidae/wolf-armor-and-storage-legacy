package dev.satyrn.wolfarmor.config;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unused") // TODO: Remove when these are actually used.
public abstract class Setting<T> {
    private final T defaultValue;

    private T value;
    private String category;
    private String name;

    private boolean isSynchronizedSetting;
    private boolean isCurrentlySynchronized;
    private T synchronizedValue;

    public Setting(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Sets the name of the setting.  This will be used for localization client-side, and also determine the
     * configuration group that the setting will appear under in the UI and in the configuration file.
     * @param value The new value of the name field
     * @return self
     */
    @Nonnull
    public Setting<T> setName(String value) {
        this.name = value;
        return this;
    }

    /**
     * Gets the setting's name
     * @return The setting's name
     */
    public String getName() { return this.name; }

    /**
     * Sets the configuration category name.  This determines in which group the config will be classified.
     * @param value The new value for the configuration category
     * @return self
     */
    @Nonnull
    public Setting<T> setCategory(String value) {
        this.category = value;
        return this;
    }

    /**
     * Gets the setting's category
     * @return The setting's category
     */
    public String getCategory() { return this.category; }

    /**
     * Gets the setting's category concatenated with its name.
     * @return The full name of the setting, consisting of its category and name concatenated with a period.
     */
    public String getFullSettingName() { return String.format("%s.%s", this.category, this.name); }

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
     * Gets the calculated value of the setting.
     * @return If synchronized, the synchronized value will be returned.  Otherwise, the standard value will be
     * returned. If either the synchronized or standard values are null, the default value is returned.
     */
    @Nullable
    public T getValue() {
        if (this.isSynchronizedSetting && this.isCurrentlySynchronized) {
            return this.synchronizedValue == null ? this.defaultValue : this.synchronizedValue;
        }
        else {
            return this.value == null ? this.defaultValue : this.value;
        }
    }

    /**
     * Flags this setting as either synchronized between server and client, or unsynchronized.
     * @param value <c>true</c> to mark synchronized, <c>false</c> for unsynchronized values.
     * @return self
     */
    @Nonnull
    public Setting<T> setIsSynchronizedSetting(boolean value) {
        this.isSynchronizedSetting = value;
        return this;
    }

    /**
     * Gets a flag indicating whether or not this setting's value is synchronized from the host to the client instance
     * @return <c>true</c> if synchronization is enabled, otherwise <c>false</c>.
     */
    public boolean getIsSynchronizedSetting() { return this.isSynchronizedSetting; }

    /**
     * Gets the default value of the setting, regardless of synchronization or current value.
     * @return The default value of the setting.
     */
    @Nullable
    public T getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * The synchronized value for the
     * @return
     */
    public T getSynchronizedValue() { return this.isSynchronizedSetting ? this.synchronizedValue : null;}

    /**
     * Flag indicating whether or not the setting is currently synchronized to a server's setting value
     * @return If the setting is not a synchronized setting, this getter always returns <c>false</c>. However, for
     * synchronized settings, it will return <c>true</c> if the setting is in a synchronized state.
     */
    public boolean getIsCurrentlySynchronized() { return this.isSynchronizedSetting && this.isCurrentlySynchronized; }

    /**
     * Loads the setting from the configuration file
     */
    public abstract void loadConfiguration();

    /**
     * Saves the value for the setting to the configuration file
     */
    public abstract void saveConfiguration();

    /**
     * Reads the synchronized setting value from an NBT object
     * @param tag The tag
     */
    public void readSynchronized(NBTBase tag) {
        this.isCurrentlySynchronized = true;
        this.synchronizedValue = this.readTag(tag);
    }

    /**
     * Writes the value to an NBTBase tag
     * @return The tag
     */
    public NBTBase writeSynchronized() { return this.writeTag(this.value); }

    /**
     * Loads the setting value from an NBT object.
     * @param tag The NBTBase to read
     */
    public abstract T readTag(NBTBase tag);

    /**
     * Writes the value to a new NBT tag object.
     * @param value The value to write to the NBT tag.
     * @return The newly constructed NBT tag object.
     */
    public abstract NBTBase writeTag(T value);
}
