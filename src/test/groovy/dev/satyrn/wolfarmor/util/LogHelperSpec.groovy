package dev.satyrn.wolfarmor.util

import dev.satyrn.wolfarmor.util.LogHelper
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LoggingException
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class LogHelperSpec extends Specification {

    def 'If logger is null throw LoggingException'() {
        setup:
        def it = new LogHelper(null);

        when:
        it.log(Level.FATAL, 'This should throw an exception')

        then:
        thrown LoggingException
    }

    def 'Should call logger with level and message'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.log(Level.FATAL, 'This is a log message')

        then:
        1 * logger.log(Level.FATAL, 'This is a log message')
    }

    def 'Should log exception with singular level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.log(Level.FATAL, new Exception('This is the message'))

        then:
        (1.._) * logger.log(Level.FATAL, _)
        0 * logger.log(!Level.FATAL, _)
    }

    def 'Should log exception message and stack trace with separate levels'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.log(Level.FATAL, new Exception('This is the message'), Level.DEBUG)

        then:
        1 * logger.log(Level.FATAL, 'This is the message')
        0 * logger.log(!Level.FATAL, 'This is the message')
        (1.._) * logger.log(Level.DEBUG, !'This is the message')
    }

    def 'Should log an object as its string representation'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)
        def object = new Object()

        when:
        it.log(Level.FATAL, object)

        then:
        1 * logger.log(Level.FATAL, object.toString())
    }

    def 'It should log info at info level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.info('This is a test message')

        then:
        1 * logger.log(Level.INFO, _)
        0 * logger.log(!Level.INFO, _)
    }

    def 'It should log warning at warning level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.warning('This is a test message')

        then:
        1 * logger.log(Level.WARN, _)
        0 * logger.log(!Level.WARN, _)
    }

    def 'It should log warning exception message at warning level and stack at debug level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)
        def exception = new Exception('This is a test message')

        when:
        it.warning(exception)

        then:
        1 * logger.log(Level.WARN, 'This is a test message')
        0 * logger.log(!Level.WARN, 'This is a test message')
        (1.._) * logger.log(Level.DEBUG, !'This is a test message')
    }

    def 'It should log error at error level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.error('This is a test message')

        then:
        1 * logger.log(Level.ERROR, _)
        0 * logger.log(!Level.ERROR, _)
    }

    def 'It should log error exception message at error level and stack at debug level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)
        def exception = new Exception('This is a test message')

        when:
        it.error(exception)

        then:
        1 * logger.log(Level.ERROR, 'This is a test message')
        0 * logger.log(!Level.ERROR, 'This is a test message')
        (1.._) * logger.log(Level.DEBUG, !'This is a test message')
    }

    def 'It should log fatal at fatal level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.fatal('This is a test message')

        then:
        1 * logger.log(Level.FATAL, _)
        0 * logger.log(!Level.FATAL, _)
    }

    def 'It should log fatal exception message at fatal level and stack at debug level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)
        def exception = new Exception('This is a test message')

        when:
        it.fatal(exception)

        then:
        1 * logger.log(Level.FATAL, 'This is a test message')
        0 * logger.log(!Level.FATAL, 'This is a test message')
        (1.._) * logger.log(Level.DEBUG, !'This is a test message')
    }

    def 'It should log debug at debug level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.debug('This is a test message')

        then:
        1 * logger.log(Level.DEBUG, _)
        0 * logger.log(!Level.DEBUG, _)
    }

    def 'Should debug log an object as its string representation at debug level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)
        def object = new Object()

        when:
        it.debug(object)

        then:
        1 * logger.log(Level.DEBUG, object.toString())
        0 * logger.log(!Level.DEBUG, _)
    }

    def 'It should trace at trace level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)

        when:
        it.trace('This is a test message')

        then:
        1 * logger.log(Level.TRACE, _)
        0 * logger.log(!Level.TRACE, _)
    }

    def 'Should trace an object as its string representation at trace level'() {
        setup:
        def logger = Mock(Logger)
        def it = new LogHelper(logger)
        def object = new Object()

        when:
        it.trace(object)

        then:
        1 * logger.log(Level.TRACE, object.toString())
        0 * logger.log(!Level.TRACE, _)
    }
}
