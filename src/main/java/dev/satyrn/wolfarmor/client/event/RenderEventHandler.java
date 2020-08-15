package dev.satyrn.wolfarmor.client.event;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.client.renderer.WolfRenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Handles client-side rendering events
 * @author Isabel Maskrey (satyrnidae)
 * @since 3.6.0
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT)
public class RenderEventHandler {
    private Method canRenderName;
    private boolean triedSetMethod;

    /**
     * Reflects and caches Render&lt;T&gt;.canRenderName()
     * @return The cached method, or null if reflection failed
     */
    private Method getCanRenderName() {
        if (!this.triedSetMethod) {
            try {
                this.canRenderName = ObfuscationReflectionHelper.findMethod(Render.class, "func_177070_b",
                        boolean.class, Entity.class);
                this.canRenderName.setAccessible(true);
            } catch (ReflectionHelper.UnableToFindMethodException ex) {
                WolfArmorMod.getLogger().error(ex);
            }
            this.triedSetMethod = true;
        }
        return this.canRenderName;
    }

    /**
     * Handles post-living-render events and renders wolves' stats above their heads if enabled
     * @param event The event instance
     * @since 3.6.0
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRenderLiving(RenderLivingEvent.Post<EntityWolf> event) {
        EntityWolf wolf;
        if (!WolfArmorMod.getConfig().getStatsRendered()
                || event == null
                || !(event.getEntity() instanceof EntityWolf)) {
            return;
        }

        wolf = (EntityWolf)event.getEntity();
        RenderLivingBase<EntityWolf> renderer = event.getRenderer();

        if (renderer.getRenderManager().pointedEntity == wolf) {
            double distanceFromViewport = wolf.getDistanceSq(renderer.getRenderManager().renderViewEntity);

            if (distanceFromViewport <= (double) (64 * 64)) {
                boolean isSneaking = wolf.isSneaking();
                float playerViewX = renderer.getRenderManager().playerViewX;
                float playerViewY = renderer.getRenderManager().playerViewY;
                boolean isThirdPerson = renderer.getRenderManager().options.thirdPersonView == 2;
                float heightOffset = wolf.height + 0.5F - (isSneaking ? 0.25F : 0F);

                // If reflection fails we will still offset the stats display as though we knew the nameplate was being
                // displayed.
                boolean showNameplate = true;
                if (this.getCanRenderName() != null) {
                    try {
                        showNameplate = (boolean) this.getCanRenderName().invoke(renderer, wolf);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        WolfArmorMod.getLogger().error(ex);
                    }
                }

                WolfRenderHelper.drawStats(wolf, showNameplate, event.getX(),
                        event.getY() + heightOffset, event.getZ(), playerViewX, playerViewY, isThirdPerson,
                        isSneaking);
            }
        }
    }
}
