package dev.satyrn.wolfarmor.common.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Occurs when a potion effect is applied to an entity
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.6.0
 */
public class PotionEffectEvent extends Event {
    private final Potion potion;
    private final EntityLivingBase entity;
    private final int amplifier;

    /**
     * Initializes a new PotionEffectEvent
     * @param potion The potion which fired the event
     * @param entity The targeted entity
     * @param amplifier The potion level
     * @since 3.6.0
     */
    public PotionEffectEvent(Potion potion, EntityLivingBase entity, int amplifier) {
        this.potion = potion;
        this.entity = entity;
        this.amplifier = amplifier;
    }

    /**
     * Gets the potion which fired the event
     * @return The potion which fired the event
     * @since 3.6.0
     */
    public Potion getPotion() { return this.potion; }

    /**
     * Gets the targeted entity
     * @return The targeted entity
     * @since 3.6.0
     */
    public EntityLivingBase getEntity() { return this.entity; }

    /**
     * Gets the potion level
     * @return The potion level
     * @since 3.6.0
     */
    public int getAmplifier() { return this.amplifier; }

    /**
     * Returns whether or not the event can be canceled
     * @return <c>true</c>
     * @since 3.6.0
     */
    @Override
    public boolean isCancelable() { return true; }
}
