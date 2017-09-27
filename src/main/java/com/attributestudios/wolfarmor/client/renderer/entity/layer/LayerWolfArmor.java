package com.attributestudios.wolfarmor.client.renderer.entity.layer;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.util.Definitions;
import com.attributestudios.wolfarmor.api.util.annotation.Future;
import com.attributestudios.wolfarmor.client.model.ModelWolfArmor;
import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * A layer renderer for wolf armor.
 */
@SideOnly(Side.CLIENT)
public class LayerWolfArmor implements LayerRenderer<EntityWolf> {
    //region Fields

    private ModelWolfArmor[] modelWolfArmors;
    @SuppressWarnings("rawtypes")
    private final RenderLiving renderer;

    private static final Map<String, ResourceLocation> WOLF_ARMOR_TEXTURE_MAP = Maps.newHashMap();

    private float colorRed = 1;
    private float colorGreen = 1;
    private float colorBlue = 1;
    private float alpha = 1;
    private boolean shouldSkipArmorGlint;

    //endregion Fields

    //region Constructors

    /**
     * Creates a new Wolf Armor layer renderer
     *
     * @param renderer The parent renderer.
     */
    @SuppressWarnings("rawtypes")
    public LayerWolfArmor(@Nonnull RenderLiving renderer) {
        this.renderer = renderer;
        this.initArmor();
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Renders this layer.
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
        if (!WolfArmorMod.getConfiguration().getIsWolfArmorRenderEnabled()) {
            return;
        }

        IWolfArmorCapability wolfArmor = entityWolf.getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
        if(wolfArmor == null) {
            return;
        }

        ItemStack itemStack = wolfArmor.getArmorItemStack();

        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemWolfArmor) {
            ItemWolfArmor armorItem = (ItemWolfArmor) itemStack.getItem();

            for (int layer = 0; layer < modelWolfArmors.length; layer++) {
                ModelWolfArmor model = modelWolfArmors[layer];
                model = getArmorModelForLayer(entityWolf, itemStack, layer, model);

                model.setModelAttributes(this.renderer.getMainModel());
                model.setLivingAnimations(entityWolf, limbSwing, limbSwingAmount, partialTicks);

                this.renderer.bindTexture(this.getArmorResource(entityWolf, itemStack, layer, null));

                int color = armorItem.getColor(itemStack);

                if (color != 0xFFFFFFFF) {
                    float r = (color >> 16 & 0xFF) / 255F;
                    float g = (color >> 8 & 0xFF) / 255F;
                    float b = (color & 0xFF) / 255F;
                    GlStateManager.color(getColorRed() * r, getColorGreen() * g, getColorBlue() * b, getAlpha());
                } else {
                    GlStateManager.color(getColorRed(), getColorGreen(), getColorBlue(), getAlpha());
                }

                model.render(entityWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                GlStateManager.color(getColorRed(), getColorGreen(), getColorBlue(), getAlpha());

                if (armorItem.getHasOverlay(itemStack)) {
                    this.renderer.bindTexture(this.getArmorResource(entityWolf, itemStack, layer, "overlay"));
                    model.render(entityWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                }

                if (!getShouldSkipArmorGlint() && itemStack.hasEffect()) {
                    LayerArmorBase.renderEnchantedGlint(renderer, entityWolf, model, limbSwing, limbSwingAmount, partialTicks,
                            ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
        }
    }

    /**
     * Initializes the armor layers.
     */
    @SuppressWarnings("WeakerAccess")
    protected void initArmor() {
        ModelWolfArmor modelWolfArmorLayer0 = new ModelWolfArmor(0.2F);
        ModelWolfArmor modelWolfArmorLayer1 = new ModelWolfArmor(0.1F);

        this.modelWolfArmors = new ModelWolfArmor[]{
                modelWolfArmorLayer0,
                modelWolfArmorLayer1
        };
    }

    //endregion Public / Protected Methods

    //region Accessors / Mutators

    /**
     * Gets the armor model for the current layer.
     *
     * @param entityWolfArmored The armored wolf entity.
     * @param itemStack         The item stack in the armor slot.
     * @param layer             The current layer.
     * @param model             The default model.
     * @return The default model.
     */
    @Nonnull
    private ModelWolfArmor getArmorModelForLayer(@Future @Nonnull EntityWolf entityWolfArmored,
                                                   @Future @Nonnull ItemStack itemStack,
                                                   @Future int layer,
                                                   @Nonnull ModelWolfArmor model) {
        //TODO: API call to get model
        return model;
    }

    /**
     * Gets the resource location for the armor item.
     *
     * @param entityWolf The armored wolf entity.
     * @param itemStack  The item stack in the armor slot.
     * @param layer      The current layer.
     * @param type       The texture variant. May be null or overlay.
     * @return A new or cached resource location corresponding to the generated / api path.
     */
    @Nonnull
    private ResourceLocation getArmorResource(@Future @Nonnull EntityWolf entityWolf,
                                                @Nonnull ItemStack itemStack,
                                                int layer,
                                                @Nullable String type) {
        ItemWolfArmor armor = (ItemWolfArmor) itemStack.getItem();
        String texture = armor.getMaterial().getName();
        String domain = Definitions.MOD_ID;
        int i = texture.indexOf(':');
        if (i > -1) {
            domain = texture.substring(0, i);
            texture = texture.substring(i);
        }

        String path = String.format("%s:textures/models/armor/wolf_%s_%d%s.png", domain, texture, layer, type == null ? "" : String.format("_%s", type));

        //TODO: API / JSON call to get wolf armor layer path

        ResourceLocation resource = WOLF_ARMOR_TEXTURE_MAP.get(path);

        if (resource == null) {
            resource = new ResourceLocation(path);
            WOLF_ARMOR_TEXTURE_MAP.put(path, resource);
        }

        return resource;
    }

    /**
     * Gets the amount of red to tint the layer by.
     *
     * @return 1.0F
     */
    private float getColorRed() {
        return colorRed;
    }

    /**
     * Gets the amount of green to tint the layer by.
     *
     * @return 1.0F
     */
    private float getColorGreen() {
        return colorGreen;
    }

    /**
     * Gets the amount of blue to tint the layer by.
     *
     * @return 1.0F
     */
    private float getColorBlue() {
        return colorBlue;
    }

    /**
     * Gets the alpha value of the layer.
     *
     * @return 1.0F
     */
    private float getAlpha() {
        return alpha;
    }

    /**
     * Whether or not textures should combine.
     *
     * @return false
     */
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    /**
     * Whether or not glint rendering should be skipped.
     *
     * @return false
     */
    @SuppressWarnings("WeakerAccess")
    protected boolean getShouldSkipArmorGlint() {
        return shouldSkipArmorGlint;
    }

    /**
     * Sets up the red multiplier for this instance
     *
     * @param colorRed The red color multiplier
     * @return This instance
     */
    @Future
    @Nonnull
    public LayerWolfArmor setColorRed(float colorRed) {
        this.colorRed = colorRed;
        return this;
    }

    /**
     * Sets up the green multiplier for this instance
     *
     * @param colorGreen The green color multiplier
     * @return This instance
     */
    @Future
    @Nonnull
    public LayerWolfArmor setColorGreen(float colorGreen) {
        this.colorGreen = colorGreen;
        return this;
    }

    /**
     * Sets up the blue multiplier for this instance
     *
     * @param colorBlue The blue color multiplier
     * @return This instance
     */
    @Future
    @Nonnull
    public LayerWolfArmor setColorBlue(float colorBlue) {
        this.colorBlue = colorBlue;
        return this;
    }

    /**
     * Sets up the alpha multiplier for this instance
     *
     * @param alpha The alpha multiplier
     * @return This instance
     */
    @Future
    @Nonnull
    public LayerWolfArmor setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    /**
     * Sets whether or not this instance should skip glint rendering
     *
     * @param shouldSkipArmorGlint Whether or not the enchanted item glint should be skipped.
     * @return This instance
     */
    @Future
    @Nonnull
    public LayerWolfArmor setShouldSkipArmorGlint(boolean shouldSkipArmorGlint) {
        this.shouldSkipArmorGlint = shouldSkipArmorGlint;
        return this;
    }

    //endregion Accessors / Mutators
}
