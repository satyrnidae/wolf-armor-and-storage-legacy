package dev.satyrn.wolfarmor.mixin.accessors;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SideOnly(Side.CLIENT)
@Mixin(Render.class)
public interface RenderAccessor {
    @Invoker
    boolean callCanRenderName(Entity entity);
}
