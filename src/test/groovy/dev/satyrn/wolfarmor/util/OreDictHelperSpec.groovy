package dev.satyrn.wolfarmor.util

import dev.satyrn.wolfarmor.tests.AllTestsUtil
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import org.testng.IExpectedExceptionsHolder
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class OreDictHelperSpec extends Specification {

    def setupSpec() {
        AllTestsUtil.init()
    }

    def 'Should return true when exact item is non-strict matched'() {
        setup:
            def inputItem = new ItemStack(Blocks.DIRT)
            OreDictionary.registerOre('testOre', inputItem)
        expect:
            OreDictHelper.isOre(false, 'testOre', inputItem)
    }

    def 'Should return false when ore type is not in dictionary'() {
        setup:
            def inputItem = new ItemStack(Blocks.DIRT)
            OreDictionary.registerOre('testOre', inputItem)
        expect:
            !OreDictHelper.isOre(false, 'otherOre', inputItem)
    }

    def 'Should return true on a non-strict wildcard match'() {
        setup:
            def inputItem = new ItemStack(Blocks.DIRT)
            def wildcardItem = inputItem.copy()
            wildcardItem.setItemDamage(Short.MAX_VALUE)
            OreDictionary.registerOre('testWildcardOre', wildcardItem)
        expect:
            OreDictHelper.isOre(false, 'testWildcardOre', inputItem)
    }

    def 'Should return false on a strict wildcard match'() {
        setup:
            def inputItem = new ItemStack(Blocks.DIRT)
            def wildcardItem = inputItem.copy()
            wildcardItem.setItemDamage(Short.MAX_VALUE)
            OreDictionary.registerOre('testWildcardOre', wildcardItem)
        expect:
            !OreDictHelper.isOre(true, 'testWildcardOre', inputItem)
    }

    def 'Should return true on a strict non-wildcard match'() {
        setup:
            def inputItem = new ItemStack(Blocks.DIRT)
            OreDictionary.registerOre('testOre', inputItem)
        expect:
            OreDictHelper.isOre(true, 'testOre', inputItem)
    }
/*
    def 'Should check if item exists in ore dictionary'() {
        expect: 'Should return true when matched'
            OreDictHelper.isOre(false, 'testOre', inputItem)
        and: 'Should return false when not matched'
            !OreDictHelper.isOre(false, 'notThisOre', inputItem)
        and: 'Should return true on a non-strict wildcard match'
            OreDictHelper.isOre(false, 'wildcardOre', inputItem)
        and: 'Should return false on a strict wildcard match'
            !OreDictHelper.isOre(true, 'wildcardOre', inputItem)
        and: 'Should return true on a strict non-wildcard match'
            OreDictHelper.isOre(true, 'testOre', inputItem)
    }
 */
}