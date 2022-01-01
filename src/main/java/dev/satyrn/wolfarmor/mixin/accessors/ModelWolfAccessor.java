package dev.satyrn.wolfarmor.mixin.accessors;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
@Mixin(ModelWolf.class)
public interface ModelWolfAccessor {
    @Accessor
    ModelRenderer getWolfTail();
    @Accessor
    void setWolfTail(@Nonnull final ModelRenderer value);
    @Accessor
    ModelRenderer getWolfMane();
    @Accessor
    void setWolfMane(@Nonnull final ModelRenderer value);
}
