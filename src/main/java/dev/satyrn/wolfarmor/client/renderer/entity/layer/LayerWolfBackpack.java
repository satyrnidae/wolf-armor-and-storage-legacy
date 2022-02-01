package dev.satyrn.wolfarmor.client.renderer.entity.layer;

import com.google.common.collect.Maps;
import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.client.model.ModelWolfBackpack;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * A layer renderer for wolf backpacks.
 */
@SideOnly(Side.CLIENT)
public class LayerWolfBackpack implements LayerRenderer<EntityWolf> {
    //region Fields
    protected ModelBase modelWolfBackpack;
    private final RenderLiving<?> renderer;
    private static final Map<String, ResourceLocation> WOLF_PACK_TEXTURE_MAP = Maps.newHashMap();


    //endregion Fields

    //region Constructors

    /**
     * Creates a new layer renderer for armored wolf backpacks.
     *
     * @param renderer The parent renderer.
     */
    public LayerWolfBackpack(@Nonnull RenderLiving<?> renderer) {
        this.renderer = renderer;

        this.modelWolfBackpack = new ModelWolfBackpack(0.0F);
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Gets the wolf backpack model.
     * @return
     */
    public ModelBase getModelWolfBackpack() {
        return modelWolfBackpack;
    }

    /**
     * Renders the layer.
     *
     * @param entityWolf      The wolf to render.  If it is not am EntityWolfArmored, the layer will not render.
     * @param limbSwing       The entity's limb swing progress.
     * @param limbSwingAmount The entity's limb swing progress amount.
     * @param partialTicks    The current game tick.
     * @param ageInTicks      The entity's age.
     * @param netHeadYaw      The yaw of the entity's head.
     * @param headPitch       The pitch of the entity's head.
     * @param scale           The scale at which to render the layer.
     */
    @Override
    public void doRenderLayer(@Nonnull EntityWolf entityWolf,
                              float limbSwing,
                              float limbSwingAmount,
                              float partialTicks,
                              float ageInTicks,
                              float netHeadYaw,
                              float headPitch,
                              float scale) {

        if (!WolfArmorMod.getConfig().getChestRendered()) {
            return;
        }
        IArmoredWolf wolfArmor = (IArmoredWolf)entityWolf;
        if (wolfArmor.getHasChest()) {
            this.modelWolfBackpack.setModelAttributes(renderer.getMainModel());
            this.modelWolfBackpack.setLivingAnimations(entityWolf, limbSwing, limbSwingAmount, partialTicks);

            this.renderer.bindTexture(this.getPackTextureForItem(wolfArmor.getChestType()));

            GlStateManager.color(1, 1, 1, 1);

            if (!entityWolf.isInvisible()) {
                this.modelWolfBackpack.render(entityWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.color(1, 1, 1, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                this.modelWolfBackpack.render(entityWolf,
                        limbSwing,
                        limbSwingAmount,
                        ageInTicks,
                        netHeadYaw,
                        headPitch,
                        scale);
                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    /**
     * Gets a resource location for a specific pack texture to display for a given chest item.
     * Chest item may be null. In a case where no specific chest type is present, the default texture is returned.
     * @param chestItem The chest item.
     * @return The resource location.
     */
    protected @Nonnull ResourceLocation getPackTextureForItem(@Nullable Item chestItem) {
        @Nullable String chestType = null;
        if (chestItem instanceof ItemBlock) {
            // TODO: Trapped chest, maybe
            final @Nonnull ItemBlock block = (ItemBlock) chestItem;
            if (block.getBlock().equals(Blocks.ENDER_CHEST)) {
                chestType = "ender";
            }
        }

        final String path = String.format("%s:textures/entity/wolf/chest/%schest.png", Resources.MOD_ID, chestType == null ? "" : chestType + "_");

        @Nullable ResourceLocation resource = WOLF_PACK_TEXTURE_MAP.get(path);

        if (resource == null) {
            resource = new ResourceLocation(path);
            WOLF_PACK_TEXTURE_MAP.put(path, resource);
        }

        return resource;
    }

    //endregion Public / Protected Methods

    //region Accessors / Mutators

    /**
     * Whether or not textures should be combined.
     *
     * @return false.
     */
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    //endregion Accessors / Mutators
}
