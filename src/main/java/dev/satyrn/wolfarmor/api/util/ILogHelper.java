package dev.satyrn.wolfarmor.api.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public interface ILogHelper {

    /**
     * Initialized the logger instance
     * @param logger The logger to use
     */
    void initializeLogger(Logger logger);

    /**
     * Log a message to the mod logger at the specified level
     *
     * @param level   The level of the message
     * @param message The message
     */
    void log(@Nonnull Level level, @Nonnull String message);

    /**
     * Logs an exception to the mod logger at the specified level
     *
     * @param level The level of the exception
     * @param ex    The exception to log
     */
    void log(@Nonnull Level level, @Nonnull Throwable ex);

    /**
     * Logs an exception to the mod logger at the specified level, and logs its stack trace at stackTraceLevel
     *
     * @param level           The logging level to log the exception message at
     * @param ex              The exception
     * @param stackTraceLevel The logging level to log the stack trace at
     */
    void log(@Nonnull Level level, @Nonnull Throwable ex, @Nonnull Level stackTraceLevel);

    /**
     * Logs an object to the mod logger at the specified level
     *
     * @param level The level of the exception
     * @param obj   The object to log
     */
    void log(@Nonnull Level level, @Nonnull Object obj);

    /**
     * Logs a message at the INFO level
     *
     * @param message The message to log
     */
    void info(@Nonnull String message);

    /**
     * Logs a message at the WARN level
     *
     * @param message The message to log
     */
    void warning(@Nonnull String message);

    /**
     * Logs an exception at the WARN level
     *
     * @param ex The exception to log
     */
    void warning(@Nonnull Exception ex);

    /**
     * Logs a message at the ERROR level
     *
     * @param message The message to log
     */
    void error(@Nonnull String message);

    /**
     * Logs an exception at the ERROR level
     *
     * @param ex The exception to log
     */
    void error(@Nonnull Exception ex);

    /**
     * Logs a message at the FATAL level
     *
     * @param message The message to log
     */
    void fatal(@Nonnull String message);

    /**
     * Logs an exception at the FATAL level
     *
     * @param ex The exception to log
     */
    void fatal(@Nonnull Throwable ex);

    /**
     * Logs a message at the DEBUG level
     *
     * @param message The message to log
     */
    void debug(@Nonnull String message);

    /**
     * Logs an object at the DEBUG level
     *
     * @param obj The object to log
     */
    void debug(@Nonnull Object obj);

    /**
     * Logs a message at the TRACE level
     *
     * @param message The message to log
     */
    void trace(@Nonnull String message);

    /**
     * Logs an object at the TRACE level
     *
     * @param obj The object to log
     */
    void trace(@Nonnull Object obj);
}
