package dev.satyrn.wolfarmor.api.entity;

public interface ExtendedLivingEntity {
    /**
     * Checks if the entity's current health is less than its max.
     * @return <c>true</c> if the entity's health is lower than its max health
     */
    default boolean wolfarmor$isHurt() {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
