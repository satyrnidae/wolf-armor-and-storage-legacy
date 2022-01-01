package dev.satyrn.wolfarmor.api.entity;

import dev.satyrn.wolfarmor.api.util.CreatureFoodStats;

import javax.annotation.Nonnull;

/**
 * Hunger-enabled creature interface
 * @author Isabel Maskrey (satyrnidae)
 * @version 1.0.0
 */
public interface IFoodStatsCreature {
    /**
     * Gets the food stats instance for this creature
     * @return The creature's food stats instance
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    @Nonnull CreatureFoodStats getFoodStats();

    /**
     * Adds levels of exhaustion to the creature's food stats instance
     * @param exhaustion The levels of exhaustion to add
     * @author Isabel Maskrey (satyrnidae)
     * @since 1.0.0
     */
    void addExhaustion(float exhaustion);
}
