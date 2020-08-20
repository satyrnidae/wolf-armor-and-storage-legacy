package dev.satyrn.wolfarmor.util;

import net.minecraft.nbt.NBTTagByteArray;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a wolf's inventory size in the settings
 */
public final class WolfInventorySize implements INBTSerializable<NBTTagByteArray> {
    private static final int ROW = 1;
    private static final int COL = 0;
    private static final int MIN = 0;
    private static final int MAX = 1;
    private static final int MATCH_ROW = 2;
    private static final int MATCH_COL = 1;


    private static final byte [][] DIMS = { { 1, 5 }, { 1, 3 } };
    byte rows = DIMS[ROW][MIN];
    byte columns = DIMS[COL][MIN];

    public WolfInventorySize() { }

    public WolfInventorySize(byte columns, byte rows) {
        this.setColumns(columns);
        this.setRows(rows);
    }

    /**
     * Gets the number of columns in the pack
     * @return The number of columns in the pack
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Sets the number of columns in the pack
     * @param value The number of columns in the pack
     * @exception IllegalArgumentException The value of value was outside of the range allowed
     */
    public void setColumns(byte value) {
        if (value < DIMS[COL][MIN] || value > DIMS[COL][MAX]) {
           throw new IllegalArgumentException(
                   String.format("The desired column count '%d' was outside the bounds [%d..%d]",
                           value, DIMS[COL][MIN], DIMS[COL][MAX]));
        }
        this.columns = value;
    }

    /**
     * Gets the number of rows in the pack
     * @return The number of rows in the pack
     */
    public byte getRows() {
        return this.rows;
    }

    /**
     * Sets the number of rows in the pack
     * @param value The number of rows in the pack
     * @exception IllegalArgumentException The
     */
    public void setRows(byte value) {
        if (value < DIMS[ROW][MIN] || value > DIMS[ROW][MAX]) {
            throw new IllegalArgumentException(
                    String.format("The desired row count '%d' was outside the bounds [%d..%d]",
                            value, DIMS[ROW][MIN], DIMS[ROW][MAX])
            );
        }
        this.rows = value;
    }

    @Override
    public NBTTagByteArray serializeNBT() {
        byte[] dimensions = { this.columns, this.rows };
        return new NBTTagByteArray(dimensions);
    }

    @Override
    public void deserializeNBT(NBTTagByteArray nbtTagByteArray) {
        byte[] dimensions = nbtTagByteArray.getByteArray();
        this.setColumns(dimensions[COL]);
        this.setRows(dimensions[ROW]);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return  a string representation of the object.
     */
    @Override
    public String toString() {
        return String.format("%dx%d", this.columns, this.rows);
    }

    /**
     * Parses a string representation of this object
     * @param s The string value
     * @return A new WolfInventorySize object
     * @throws IllegalArgumentException The input string was not in the proper format.
     */
    public static WolfInventorySize parseWolfInventorySize(String s) {
        Pattern pattern = Pattern.compile(
                String.format("([%d-%d])x([%d-%d])", DIMS[COL][MIN], DIMS[COL][MAX], DIMS[ROW][MIN], DIMS[ROW][MAX]));
        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    String.format("The input string was not in the proper format."));
        }
        return new WolfInventorySize(Byte.parseByte(matcher.group(MATCH_COL)),
            Byte.parseByte(matcher.group(MATCH_ROW)));
    }
}
