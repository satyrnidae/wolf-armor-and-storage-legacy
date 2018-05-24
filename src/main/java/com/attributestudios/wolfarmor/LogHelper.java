package com.attributestudios.wolfarmor;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LoggingException;

import javax.annotation.Nonnull;

/**
 * Provides logging utilities
 */
public class LogHelper {
    private Logger logger;

    /**
     * Creates a log helper for the specified logger.
     */
    @SuppressWarnings("WeakerAccess")
    public LogHelper(@Nonnull Logger logger) {
        this.logger = logger;
    }

    /**
     * Log a message to the mod logger at the specified level
     *
     * @param level   The level of the message
     * @param message The message
     */
    @SuppressWarnings("WeakerAccess")
    public void log(@Nonnull Level level, @Nonnull String message) {
        if (logger == null) {
            throw new LoggingException("Logger has not been initialized");
        }

        logger.log(level, message);
    }

    /**
     * Logs an exception to the mod logger at the specified level
     *
     * @param level The level of the exception
     * @param ex    The exception to log
     */
    @SuppressWarnings("WeakerAccess")
    public void log(@Nonnull Level level, @Nonnull Throwable ex) {
        log(level, ex, level);
    }

    /**
     * Logs an exception to the mod logger at the specified level, and logs its stack trace at stackTraceLevel
     *
     * @param level           The logging level to log the exception message at
     * @param ex              The exception
     * @param stackTraceLevel The logging level to log the stack trace at
     */
    @SuppressWarnings("WeakerAccess")
    public void log(@Nonnull Level level, @Nonnull Throwable ex, @Nonnull Level stackTraceLevel) {
        log(level, ex.getMessage());

        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            log(stackTraceLevel, stackTraceElement);
        }
    }

    /**
     * Logs an object to the mod logger at the specified level
     *
     * @param level The level of the exception
     * @param obj   The object to log
     */
    @SuppressWarnings("WeakerAccess")
    public void log(@Nonnull Level level, @Nonnull Object obj) {
        log(level, obj.toString());
    }

    /**
     * Logs a message at the INFO level
     *
     * @param message The message to log
     */
    @SuppressWarnings("unused")
    public void info(@Nonnull String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message at the WARN level
     *
     * @param message The message to log
     */
    @SuppressWarnings("unused")
    public void warning(@Nonnull String message) {
        log(Level.WARN, message);
    }

    /**
     * Logs an exception at the WARN level
     *
     * @param ex The exception to log
     */
    @SuppressWarnings("unused")
    public void warning(@Nonnull Exception ex) {
        log(Level.WARN, ex, Level.DEBUG);
    }

    /**
     * Logs a message at the ERROR level
     *
     * @param message The message to log
     */
    @SuppressWarnings("unused")
    public void error(@Nonnull String message) {
        log(Level.ERROR, message);
    }

    /**
     * Logs an exception at the ERROR level
     *
     * @param ex The exception to log
     */
    public void error(@Nonnull Exception ex) {
        log(Level.ERROR, ex);
    }

    /**
     * Logs a message at the FATAL level
     *
     * @param message The message to log
     */
    @SuppressWarnings("unused")
    public void fatal(@Nonnull String message) {
        log(Level.FATAL, message);
    }

    /**
     * Logs an exception at the FATAL level
     *
     * @param ex The exception to log
     */
    public void fatal(@Nonnull Throwable ex) {
        log(Level.FATAL, ex);
    }

    /**
     * Logs a message at the DEBUG level
     *
     * @param message The message to log
     */
    @SuppressWarnings("WeakerAccess")
    public void debug(@Nonnull String message) {
        log(Level.DEBUG, message);
    }

    /**
     * Logs an object at the DEBUG level
     *
     * @param obj The object to log
     */
    @SuppressWarnings("unused")
    public void debug(@Nonnull Object obj) {
        log(Level.DEBUG, obj);
    }

    /**
     * Logs a message at the TRACE level
     *
     * @param message The message to log
     */
    @SuppressWarnings("unused")
    public void trace(@Nonnull String message) {
        log(Level.TRACE, message);
    }

    /**
     * Logs an object at the TRACE level
     *
     * @param obj The object to log
     */
    @SuppressWarnings("unused")
    public void trace(@Nonnull Object obj) {
        log(Level.TRACE, obj);
    }
}
