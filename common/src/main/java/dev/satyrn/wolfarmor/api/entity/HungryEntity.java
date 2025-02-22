package dev.satyrn.wolfarmor.api.entity;

import dev.satyrn.wolfarmor.api.food.EntityFoodData;
import org.jetbrains.annotations.NotNull;

public interface HungryEntity {
    @NotNull
    EntityFoodData wolfarmor$getFoodData();

    void wolfarmor$addExhaustion(float exhaustion);
}
