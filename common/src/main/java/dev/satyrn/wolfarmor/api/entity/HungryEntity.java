package dev.satyrn.wolfarmor.api.entity;

import dev.satyrn.wolfarmor.api.food.EntityFoodData;
import org.jetbrains.annotations.NotNull;

public interface HungryEntity {
    default @NotNull EntityFoodData wolfarmor$getFoodData() {
        throw new UnsupportedOperationException("Not Implemented");
    }

    default void wolfarmor$addExhaustion(float exhaustion) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
