package dev.satyrn.wolfarmor.util


import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class OreDictHelperSpec extends Specification {

    ItemStack inputItem

    def setup() {
        dev.satyrn.wolfarmor.tests.AllTestsUtil.init();

        inputItem = new ItemStack(Blocks.DIRT)
        GroovySpy(OreDictionary, global: true)
        OreDictionary.registerOre('testOre', inputItem)
        ItemStack wildcard = new ItemStack(Blocks.DIRT, 1, 32767)
        OreDictionary.registerOre('wildcardOre', wildcard)
    }

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
}