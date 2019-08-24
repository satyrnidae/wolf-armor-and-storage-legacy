package dev.satyrn.wolfarmor

import dev.satyrn.wolfarmor.api.common.IProxy
import dev.satyrn.wolfarmor.api.config.IConfiguration
import dev.satyrn.wolfarmor.api.util.ILogHelper
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class WolfArmorModSpec extends Specification {

    def 'GetProxy should return proxy'() {
        setup:
        def proxy = Mock(IProxy)
        WolfArmorMod.proxy = proxy

        expect:
        WolfArmorMod.getProxy() == proxy
    }

    def 'GetInstance should return instance'() {
        setup:
        def instance = new WolfArmorMod(Mock(IConfiguration), Mock(ILogHelper))
        WolfArmorMod.instance = instance

        expect:
        WolfArmorMod.getInstance() == instance
    }

    def 'GetLogger should return logger'() {
        setup:
        def logger = Mock(ILogHelper)
        WolfArmorMod.logger = logger

        expect:
        WolfArmorMod.getLogger() == logger
    }

    def 'GetConfiguration should return configuration'() {
        setup:
        def configuration = Mock(IConfiguration)
        WolfArmorMod.configuration = configuration

        expect:
        WolfArmorMod.getConfiguration() == configuration
    }

    def 'PreInit should call proxy pre-init'() {
        setup:
        def mockProxy = Mock(IProxy)
        def mockEvent = Mock(FMLPreInitializationEvent)
        def mockLogger = Mock(Logger)
        def mockLogHelper = Mock(ILogHelper)
        def mockConfiguration = Mock(IConfiguration)
        def it = new WolfArmorMod(mockConfiguration, mockLogHelper)
        mockEvent.getModLog() >> mockLogger

        WolfArmorMod.instance = it
        WolfArmorMod.proxy = mockProxy

        when:
        it.preInit(mockEvent)

        then:
        1 * mockLogHelper.initializeLogger(mockLogger)
        1 * mockConfiguration.initializeConfig(mockEvent)
        1 * mockProxy.preInit(mockEvent)
    }

    def 'Default constructor should set config and logger'() {
        when:
        new WolfArmorMod();

        then:
        WolfArmorMod.configuration != null
        WolfArmorMod.logger != null
    }

    def 'init should call proxy init'() {
        setup:
        def mockEvent = Mock(FMLInitializationEvent)
        def mockProxy = Mock(IProxy)
        def it = new WolfArmorMod(Mock(IConfiguration), Mock(ILogHelper))
        WolfArmorMod.proxy = mockProxy

        when:
        it.init(mockEvent)

        then:
        1 * mockProxy.init(mockEvent)
    }

    def 'postInit should call proxy postInit'() {
        setup:
        def mockEvent = Mock(FMLPostInitializationEvent)
        def mockProxy = Mock(IProxy)
        def it = new WolfArmorMod(Mock(IConfiguration), Mock(ILogHelper))
        WolfArmorMod.proxy = mockProxy

        when:
        it.postInit(mockEvent)

        then:
        1 * mockProxy.postInit(mockEvent)
    }

    def 'loadComplete should call proxy loadComplete'() {
        setup:
        def mockEvent = Mock(FMLLoadCompleteEvent)
        def mockProxy = Mock(IProxy)
        def it = new WolfArmorMod(Mock(IConfiguration), Mock(ILogHelper))
        WolfArmorMod.proxy = mockProxy

        when:
        it.loadComplete(mockEvent)

        then:
        1 * mockProxy.loadComplete(mockEvent)
    }
}
