package dev.satyrn.wolfarmor.config

import net.minecraft.nbt.NBTBase
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class SettingSpec extends Specification {

    def createSetting(Object defaultValue) {
        return new Setting(defaultValue) {
            @Override void loadConfiguration() {}
            @Override void saveConfiguration() {}
            @Override Object readTag(NBTBase tag) { return null }
            @Override NBTBase writeTag(Object value) { return null }
        }
    }

    @SuppressWarnings("GroovyAccessibility")
    def 'Setting should set default value in constructor'() {
        setup:
        def defaultValue = 'default value for setting'
        def setting = createSetting(defaultValue)
        expect:
        defaultValue == setting.defaultValue;
    }

    def 'getDefaultValue should return the default value'() {
        setup:
        def defaultValue = 'a default value for you'
        def setting = createSetting(defaultValue)
        expect:
        defaultValue == setting.getDefaultValue();
    }

    def 'getValue should return the default value if not synchronized and value is not set'() {
        setup:
        def defaultValue = 10298745
        def setting = createSetting(defaultValue)
        expect:
        defaultValue == setting.getValue();
    }

    def 'setName should be fluent'() {
        setup:
        def setting = createSetting(1234567890)
        def setting2 = setting.setName('setting name')
        expect:
        setting == setting2
    }

    @SuppressWarnings("GroovyAccessibility")
    def 'setName should set the name field'() {
        setup:
        def newName = 'This is the new setting name'
        def setting = createSetting(null)
                .setName(newName)
        expect:
        newName == setting.name
    }

    def 'setCategory should be fluent'() {
        setup:
        def setting = createSetting('heebie-jeebies')
        def setting2 = setting.setCategory('odd words')
        expect:
        setting == setting2
    }

    @SuppressWarnings("GroovyAccessibility")
    def 'setCategory should set the category'() {
        setup:
        def category = 'uncles who are your uncles and also named bob'
        def setting = createSetting('you have no uncles named bob').setCategory(category)
        expect:
        category == setting.category
    }

    def 'setValue should be fluent'() {
        setup:
        def setting = createSetting(new Object())
        def setting2 = setting.setValue(13)
        expect:
        setting == setting2
    }
}
