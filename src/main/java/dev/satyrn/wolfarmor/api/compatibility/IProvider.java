package dev.satyrn.wolfarmor.api.compatibility;

/**
 * Provider interface
 * @author Isabel Maskrey (satyrnidae)
 * @since 4.5.0-alpha
 */
public interface IProvider {
    /**
     * Gets the provider priority. Defaults to {@code Short.MIN_VALUE}
     * @return The provider priority
     * @since 4.5.0-alpha
     */
    default short getPriority() { return Priority.LOWEST; }
}
