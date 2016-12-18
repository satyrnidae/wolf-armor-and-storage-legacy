package com.attributestudios.wolfarmor.client.renderer.entity.layer;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.client.model.ModelWolfArmor;
import com.attributestudios.wolfarmor.client.renderer.entity.RenderWolfArmored;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

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
    private RenderWolfArmored renderer;

    private static final Map<String, ResourceLocation> WOLF_ARMOR_TEXTURE_MAP = Maps.newHashMap();
    private static final ResourceLocation TEXTURE_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private float colorRed = 1;
    private float colorGreen = 1;
    private float colorBlue = 1;
    private float alpha = 1;
    private boolean shouldSkipArmorGlint;

    //endregion Fields

    //region Constructors

    /**
     * Creates a new Wolf Armor layer renderer
     * @param renderer The parent renderer.
     */
    public LayerWolfArmor(@Nonnull RenderWolfArmored renderer)
    {
        this.renderer = renderer;
        this.initArmor();
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Renders this layer.
     * @param entityWolf The wolf to render.  If it is not am EntityWolfArmored, the layer will not render.
     * @param limbSwing The entity's limb swing progress
     * @param limbSwingAmount The entity's limb swing amount
     * @param partialTicks The current game tick
     * @param ageInTicks The entity's age
     * @param netHeadYaw The yaw of the entity's head
     * @param headPitch The pitch of the entity's head
     * @param scale The scale at which to render the layer.
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
        if (WolfArmorMod.getConfiguration().getIsWolfArmorRenderEnabled()) {
            if (entityWolf instanceof EntityWolfArmored) {
                EntityWolfArmored entityWolfArmored = (EntityWolfArmored) entityWolf;

                ItemStack itemStack = entityWolfArmored.getArmorItemStack();

                if (itemStack != null && itemStack.getItem() instanceof ItemWolfArmor) {
                    ItemWolfArmor armorItem = (ItemWolfArmor) itemStack.getItem();

                    for (int layer = 0; layer < modelWolfArmors.length; layer++) {
                        ModelWolfArmor model = modelWolfArmors[layer];
                        model = getArmorModelForLayer(entityWolfArmored, itemStack, layer, model);
                        this.renderer.setupModelAttributes(entityWolfArmored, partialTicks, model);
                        model.setLivingAnimations(entityWolfArmored, limbSwing, limbSwingAmount, partialTicks);

                        this.renderer.bindTexture(this.getArmorResource(entityWolfArmored, itemStack, layer, null));

                        int color = armorItem.getColor(itemStack);

                        if (color != 0xFFFFFFFF) {
                            float r = (color >> 16 & 0xFF) / 255F;
                            float g = (color >> 8 & 0xFF) / 255F;
                            float b = (color & 0xFF) / 255F;
                            GL11.glColor4f(getColorRed() * r, getColorGreen() * g, getColorBlue() * b, getAlpha());
                        } else {
                            GL11.glColor4f(getColorRed(), getColorGreen(), getColorBlue(), getAlpha());
                        }

                        model.render(entityWolfArmored, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                        GL11.glColor4f(getColorRed(), getColorGreen(), getColorBlue(), getAlpha());

                        if (armorItem.getHasOverlay(itemStack)) {
                            this.renderer.bindTexture(this.getArmorResource(entityWolfArmored, itemStack, layer, "overlay"));
                            model.render(entityWolfArmored, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        }

                        if (!getShouldSkipArmorGlint() && itemStack.hasEffect(layer)) {
                            this.renderEnchantedGlint(entityWolfArmored, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                        }
                    }
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

        this.modelWolfArmors = new ModelWolfArmor[] {
                modelWolfArmorLayer0,
                modelWolfArmorLayer1
        };
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Renders the armor layer glint when enchanted
     * @param entity The entity to render the glint on
     * @param model The model to render the glint with
     * @param limbSwing The limb swing
     * @param limbSwingAmount The limb swing progress
     * @param partialTicks The render partial ticks
     * @param ageInTicks The age of the entity in ticks
     * @param netHeadYaw The entity head yaw
     * @param headPitch The entity head pitch
     * @param scaleFactor The scale factor
     */
    private void renderEnchantedGlint(@Nonnull Entity entity,
                                      @Nonnull ModelBase model,
                                      float limbSwing,
                                      float limbSwingAmount,
                                      float partialTicks,
                                      float ageInTicks,
                                      float netHeadYaw,
                                      float headPitch,
                                      float scaleFactor) {
        float offset = entity.ticksExisted + partialTicks;
        this.renderer.bindTexture(TEXTURE_ITEM_GLINT);

        GL11.glEnable(GL11.GL_BLEND);
        {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDepthMask(false);
            {
                GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);

                GL11.glDisable(GL11.GL_LIGHTING);
                {
                    for (int pass = 0; pass < 2; pass++) {
                        GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                        GL11.glColor4f(0.38F, 0.19F, 0.608F, 1);
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        {
                            GL11.glLoadIdentity();
                            GL11.glScalef(1 / 3F, 1 / 3F, 1 / 3F);
                            GL11.glRotatef(30 - pass * 60, 0, 0, 1);
                            GL11.glTranslatef(0, offset * (0.001F + pass * 0.003F) * 20, 0);
                        }
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                    }
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    {
                        GL11.glLoadIdentity();
                    }
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                }
                GL11.glEnable(GL11.GL_LIGHTING);
            }
            GL11.glDepthMask(true);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    //endregion Private Methods

    //region Accessors / Mutators

    /**
     * Gets the armor model for the current layer.
     * @param entityWolfArmored The armored wolf entity.
     * @param itemStack The item stack in the armor slot.
     * @param layer The current layer.
     * @param model The default model.
     * @return The default model.
     */
    @SuppressWarnings({"WeakerAccess", "UnusedParameters"})
    @Nonnull
    protected ModelWolfArmor getArmorModelForLayer(@Nonnull EntityWolfArmored entityWolfArmored,
                                                   @Nonnull ItemStack itemStack,
                                                   int layer,
                                                   @Nonnull ModelWolfArmor model) {
        //TODO: API call to get model
        return model;
    }

    /**
     * Gets the resource location for the armor item.
     * @param entityWolfArmored The armored wolf entity.
     * @param itemStack The item stack in the armor slot.
     * @param layer The current layer.
     * @param type The texture variant. May be null or overlay.
     * @return A new or cached resource location corresponding to the generated / api path.
     */
    @SuppressWarnings({"WeakerAccess", "UnusedParameters"})
    protected ResourceLocation getArmorResource(@Nonnull EntityWolfArmored entityWolfArmored,
                                                @Nonnull ItemStack itemStack,
                                                int layer,
                                                @Nullable String type) {
        ItemWolfArmor armor = (ItemWolfArmor)itemStack.getItem();
        String texture = armor.getMaterial().getName();
        String domain = WolfArmorMod.MOD_ID;
        int i = texture.indexOf(':');
        if(i > -1) {
            domain = texture.substring(0, i);
            texture = texture.substring(i);
        }

        String path = String.format("%s:textures/models/armor/wolf_%s_%d%s.png", domain, texture, layer, type == null ? "" : String.format("_%s", type));

        //TODO: API call to get wolf armor layer path

        ResourceLocation resource = WOLF_ARMOR_TEXTURE_MAP.get(path);

        if(resource == null) {
            resource = new ResourceLocation(path);
            WOLF_ARMOR_TEXTURE_MAP.put(path, resource);
        }

        return resource;
    }

    /**
     * Gets the amount of red to tint the layer by.
     * @return 1.0F
     */
    @SuppressWarnings("WeakerAccess")
    protected float getColorRed() {
        return colorRed;
    }

    /**
     * Gets the amount of green to tint the layer by.
     * @return 1.0F
     */
    @SuppressWarnings("WeakerAccess")
    protected float getColorGreen() {
        return colorGreen;
    }

    /**
     * Gets the amount of blue to tint the layer by.
     * @return 1.0F
     */
    @SuppressWarnings("WeakerAccess")
    protected float getColorBlue() {
        return colorBlue;
    }

    /**
     * Gets the alpha value of the layer.
     * @return 1.0F
     */
    @SuppressWarnings("WeakerAccess")
    protected float getAlpha() {
        return alpha;
    }

    /**
     * Whether or not glint rendering should be skipped.
     * @return false
     */
    @SuppressWarnings("WeakerAccess")
    protected boolean getShouldSkipArmorGlint() {
        return shouldSkipArmorGlint;
    }

    /**
     * Sets up the red multiplier for this instance
     * @param colorRed The red color multiplier
     * @return This instance
     */
    @SuppressWarnings("unused")
    @Nonnull
    public LayerWolfArmor setColorRed(float colorRed) {
        this.colorRed = colorRed;
        return this;
    }

    /**
     * Sets up the green multiplier for this instance
     * @param colorGreen The green color multiplier
     * @return This instance
     */
    @SuppressWarnings("unused")
    @Nonnull
    public LayerWolfArmor setColorGreen(float colorGreen) {
        this.colorGreen = colorGreen;
        return this;
    }

    /**
     * Sets up the blue multiplier for this instance
     * @param colorBlue The blue color multiplier
     * @return This instance
     */
    @SuppressWarnings("unused")
    @Nonnull
    public LayerWolfArmor setColorBlue(float colorBlue) {
        this.colorBlue = colorBlue;
        return this;
    }

    /**
     * Sets up the alpha multiplier for this instance
     * @param alpha The alpha multiplier
     * @return This instance
     */
    @SuppressWarnings("unused")
    @Nonnull
    public LayerWolfArmor setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    /**
     * Sets whether or not this instance should skip glint rendering
     * @param shouldSkipArmorGlint Whether or not the enchanted item glint should be skipped.
     * @return This instance
     */
    @SuppressWarnings("unused")
    @Nonnull
    public LayerWolfArmor setShouldSkipArmorGlint(boolean shouldSkipArmorGlint) {
        this.shouldSkipArmorGlint = shouldSkipArmorGlint;
        return this;
    }

    //endregion Accessors / Mutators
}
