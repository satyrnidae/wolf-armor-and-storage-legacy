package dev.satyrn.wolfarmor.api.compatibility;

/**
 * Default provider priorities
 * @author Isabel Maskrey (satyrnidae)
 * @since 4.5.0-alpha
 */
public final class Priority {
    private Priority() {}

    /**
     * This provider has the highest priority
     * @since 4.5.0-alpha
     */
    public static final short HIGHEST = Short.MAX_VALUE;

    /**
     * This provider has the lowest priority
     * @since 4.5.0-alpha
     */
    public static final short LOWEST = Short.MIN_VALUE;
}
