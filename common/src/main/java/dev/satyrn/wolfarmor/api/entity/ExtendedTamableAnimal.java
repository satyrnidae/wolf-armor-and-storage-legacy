package dev.satyrn.wolfarmor.api.entity;

public interface ExtendedTamableAnimal {
    default boolean wolfarmor$getFlag(int flagId) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    default void wolfarmor$setFlag(int flagId, boolean value) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
