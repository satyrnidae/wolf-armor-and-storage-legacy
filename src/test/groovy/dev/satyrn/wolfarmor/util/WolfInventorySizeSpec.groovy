package dev.satyrn.wolfarmor.util

import net.minecraft.nbt.NBTTagByteArray
import spock.lang.Specification

class WolfInventorySizeSpec extends Specification {
    def 'Columns and rows should be set from the constructor'() {
        setup:
        def expectedRows = (byte)3
        def expectedCols = (byte)2
        def size = new WolfInventorySize(expectedCols, expectedRows)
        expect:
        size.getColumns() == expectedCols
        size.getRows() == expectedRows
    }

    def 'Should serialize to an NBT Tag'() {
        setup:
        def expectedRows = (byte)3
        def expectedCols = (byte)2
        def size = new WolfInventorySize(expectedCols, expectedRows)
        def tags = size.serializeNBT()
        expect:
        tags.byteArray[0] == expectedRows
        tags.byteArray[1] == expectedCols
    }

    def 'Should deserialize from NBT tag'() {
        setup:
        def expectedRows = (byte)3
        def expectedCols = (byte)2
        def value = new byte[2]
        value[0] = expectedCols
        value[1] = expectedRows
        def nbt = new NBTTagByteArray(value)
        def size = new WolfInventorySize()
        size.deserializeNBT(nbt)
        expect:
        size.getRows() == expectedRows
        size.getColumns() == expectedCols
    }

    def 'Should return a properly formatted string'() {
        setup:
        def expectedRows = (byte)3
        def expectedCols = (byte)2
        def expectedValue = "${expectedCols}x${expectedRows}"
        def size = new WolfInventorySize(expectedCols, expectedRows)
        expect:
        size.toString() == expectedValue
    }

    def 'Should parse from string value'() {
        setup:
        def expectedRows = (byte)3
        def expectedCols = (byte)2
        def expectedValue = "${expectedCols}x${expectedRows}"
        def size = WolfInventorySize.parseWolfInventorySize(expectedValue)
        expect:
        size.getRows() == expectedRows
        size.getColumns() == expectedCols
    }

    def 'Should throw IllegalArgumentException when set row value too small'() {
        setup:
        def expectedRows = (byte)0
        def size = new WolfInventorySize()
        when:
        size.setRows(expectedRows)
        then:
        def ex = thrown IllegalArgumentException
        ex.message == 'The desired row count \'0\' was outside the bounds [1..3]'
    }

    def 'Should throw IllegalArgumentException when init row value too small'() {
        setup:
        def expectedRows = (byte)0
        when:
        new WolfInventorySize((byte)2,expectedRows)
        then:
        def ex = thrown IllegalArgumentException
        ex.message == 'The desired row count \'0\' was outside the bounds [1..3]'
    }

    def 'Should throw IllegalArgumentException when set column value too small'() {
        setup:
        def expectedCols = (byte)0
        def size = new WolfInventorySize()
        when:
        size.setColumns(expectedCols)
        then:
        def ex = thrown IllegalArgumentException
        ex.message == 'The desired column count \'0\' was outside the bounds [1..5]'
    }

    def 'Should throw IllegalArgumentException when init column value too small'() {
        setup:
        def expectedCols = (byte)0
        when:
        new WolfInventorySize(expectedCols, (byte)2)
        then:
        def ex = thrown IllegalArgumentException
        ex.message == 'The desired column count \'0\' was outside the bounds [1..5]'
    }

    def 'Should throw IllegalArgumentException when input string invalid'() {
        setup:
        def invalidString = 'this is not a size'
        when:
        WolfInventorySize.parseWolfInventorySize(invalidString)
        then:
        def ex = thrown IllegalArgumentException
        ex.message == "The input string was not in the proper format."
    }

    def 'Should throw IllegalArgumentException when input string row count out of bounds'() {
        setup:
        def invalidString = '3,9'
        when:
        WolfInventorySize.parseWolfInventorySize(invalidString)
        then:
        def ex = thrown IllegalArgumentException
        ex.message == "The input string was not in the proper format."
    }

    def 'Should throw IllegalArgumentException when input string column count out of bounds'() {
        setup:
        def invalidString = '9,3'
        when:
        WolfInventorySize.parseWolfInventorySize(invalidString)
        then:
        def ex = thrown IllegalArgumentException
        ex.message == "The input string was not in the proper format."
    }
}
