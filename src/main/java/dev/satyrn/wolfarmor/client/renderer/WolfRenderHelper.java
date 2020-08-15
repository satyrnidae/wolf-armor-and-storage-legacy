package dev.satyrn.wolfarmor.client.renderer;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.api.util.CreatureFoodStats;
import dev.satyrn.wolfarmor.util.WolfFoodStatsLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

/**
 * Assists with wolf rendering
 * @author Isabel Maskrey
 * @since 3.6.0
 */
@SideOnly(Side.CLIENT)
public class WolfRenderHelper {
    protected static final ResourceLocation ICONS_TEX_PATH = new ResourceLocation("minecraft:textures/gui/icons.png");

    /**
     * Renders the wolf stats below the nameplate.
     * @param wolf The wolf
     * @param renderNameplate Whether or not the nameplate is present
     * @param x The X translation
     * @param y The Y translation
     * @param z The Z translation
     * @param playerViewX The player view pitch amount
     * @param playerViewY The player view yaw amount
     * @param isThirdPerson Whether or not the player is in the third person camera
     * @param isSneaking Whether or not the wolf is currently sneaking
     * @since 3.6.0
     */
    public static void drawStats(@Nonnull EntityWolf wolf, boolean renderNameplate, double x, double y, double z, float playerViewX, float playerViewY, boolean isThirdPerson, boolean isSneaking) {
        if (!wolf.isTamed()) return;
        Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS_TEX_PATH);

        // Set up a view matrix matching the nameplate matrix and scale it down
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0F, 1F, 0F);
        GlStateManager.rotate(-playerViewY, 0F, 1F, 0F);
        GlStateManager.rotate((isThirdPerson ? -1F : 1F) * playerViewX, 1F, 0F, 0F);
        GlStateManager.scale(-0.01F, -0.01F, 0.01F);

        // Reset the rendering color to prevent oddly-colored interface elements
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Disable lighting and set the depth function to always render regardless of depth
        GlStateManager.disableLighting();
        GlStateManager.depthFunc(GL11.GL_ALWAYS);

        // Render the wolf armor, health, and food levels above the wolf's head
        WolfRenderHelper.renderStats(wolf, 5, renderNameplate ? 20 : 0);

        // Reenable lighting, rest the depth function, and pop the view matrix from the stack
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    /**
     * Draws a textured rectangle
     * @param x The X location of the rectangle's upper-left point
     * @param y The Y location of the rectangle's upper-left point
     * @param texX The texture coordinate's X position
     * @param texY The texture coordinate's Y position
     * @param width The width of the
     * @param height
     */
    private static void drawTexturedModalRect(int x, int y, int texX, int texY, int width, int height) {
        final float pixelScale = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y + height, 0).tex(texX * pixelScale, (texY + height) * pixelScale).endVertex();
        builder.pos(x + width, y + height, 0).tex((texX + width) * pixelScale, (texY + height) * pixelScale).endVertex();
        builder.pos(x + width, y, 0).tex((texX + width) * pixelScale, texY * pixelScale).endVertex();
        builder.pos(x, y, 0).tex(texX * pixelScale, texY * pixelScale).endVertex();
        tessellator.draw();
    }

    private static void drawReverseTexturedModalRect(int x, int y, int texX, int texY, int width, int height) {
        float pixelX = 0.00390625F;
        float pixelY = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y + height, 0).tex((texX + width) * pixelX, (texY + height) * pixelY).endVertex();
        builder.pos(x + width, y + height, 0).tex(texX * pixelX, (texY + height) * pixelY).endVertex();
        builder.pos(x + width, y, 0).tex(texX * pixelX, texY * pixelY).endVertex();
        builder.pos(x, y, 0).tex((texX + width) * pixelX, texY * pixelY).endVertex();
        tessellator.draw();
    }

    private static void renderStats(EntityWolf wolf, int x, int y) {
        int armorValue = wolf.getTotalArmorValue();

        int iconWidth = 9;
        int iconCount = 10;
        int xOffsetInitial = x + (-iconWidth * iconCount / 2);
        int yOffsetInitial = y;

        // Armor
        if (((IArmoredWolf)wolf).getHasArmor() && armorValue > 0) {
            for (int index = 0; index < iconCount; ++index) {
                int xOffset = index * 8 + xOffsetInitial;

                if (index * 2 + 1 < armorValue) {
                    // Full armor icon
                    drawTexturedModalRect(xOffset, yOffsetInitial, 34, 9, 9, 9);
                }
                if (index * 2 + 1 == armorValue) {
                    // Half armor icon
                    drawTexturedModalRect(xOffset, yOffsetInitial, 25, 9, 9, 9);
                }
                if (index * 2 + 1 > armorValue) {
                    // Empty armor icon
                    drawTexturedModalRect(xOffset, yOffsetInitial, 16, 9, 9, 9);
                }
            }
            yOffsetInitial += 10;
        }

        // Health
        double maxHealth = wolf.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
        int absorption = MathHelper.ceil(wolf.getAbsorptionAmount());
        int absorptionIndex = absorption;
        int absorptionHearts = MathHelper.ceil((maxHealth + absorption) / 2F / 10F);
        int healthLevels = Math.max(10 - (absorptionHearts - 2), 3);
        int regenHeartIndex = -1;
        if (wolf.isPotionActive(MobEffects.REGENERATION)) {
            regenHeartIndex = wolf.ticksExisted % MathHelper.ceil(maxHealth + 5F);
        }
        float health = MathHelper.ceil(wolf.getHealth());
        xOffsetInitial = -MathHelper.floor((Math.min(10, maxHealth / 2f) * 9f) / 2f) + 5;

        for (int index = MathHelper.ceil((maxHealth + absorption) / 2F) - 1; index >= 0; --index) {
            int texXOffset = 16;
            if (wolf.isPotionActive(MobEffects.POISON)) {
                texXOffset += 36;
            } else if (wolf.isPotionActive(MobEffects.WITHER)) {
                texXOffset += 72;
            }

            int healthLayer = MathHelper.ceil((float)(index + 1) / 10F) - 1;
            int xOffset = index % 10 * 8 + xOffsetInitial;
            int yOffset = yOffsetInitial + healthLayer * healthLevels;
            if (health <= 4) {
                yOffset += wolf.getRNG().nextInt(2);
            }

            if (absorptionIndex <= 0 && index == regenHeartIndex) {
                yOffset -= 2;
            }

            drawTexturedModalRect(xOffset, yOffset, 16, 0, 9, 9);
            if (absorptionIndex > 0) {
                if (absorptionIndex == absorption && absorption % 2 == 1) {
                    drawTexturedModalRect(xOffset, yOffset, texXOffset + 153, 0, 9, 9);
                    --absorptionIndex;
                } else {
                    drawTexturedModalRect(xOffset, yOffset, texXOffset + 144, 0, 9, 9);
                    absorptionIndex -= 2;
                }
            } else {
                if (index * 2 + 1 < health) {
                    drawTexturedModalRect(xOffset, yOffset, texXOffset + 36, 0, 9, 9);
                }
                if (index * 2 + 1 == health) {
                    drawTexturedModalRect(xOffset, yOffset, texXOffset + 45, 0, 9, 9);
                }
            }
        }
        yOffsetInitial += healthLevels - 1;

        // food
        if (WolfArmorMod.getConfig().getFoodStatsLevel() != WolfFoodStatsLevel.DISABLED) {
            CreatureFoodStats foodStats = ((IArmoredWolf) wolf).getFoodStats();
            if (foodStats != null) {
                xOffsetInitial = -iconWidth * iconCount / 2 + 5;
                int foodLevel = foodStats.getFoodLevel();
                for (int index = 0; index < iconCount; ++index) {
                    int yOffset = yOffsetInitial;
                    int texXOffset = 16;
                    int hungerBackground = 0;
                    if (wolf.isPotionActive(MobEffects.HUNGER)) {
                        texXOffset += 36;
                        hungerBackground = 13;
                    }

                    if (foodStats.getSaturationLevel() <= 0.0F && wolf.ticksExisted % (foodLevel * 3 + 1) == 0) {
                        yOffset += wolf.getRNG().nextInt(3) - 1;
                    }

                    int xOffset = xOffsetInitial + index * 8;
                    drawReverseTexturedModalRect(xOffset, yOffset, 16 + hungerBackground * 9, 27, 9, 9);
                    if (index * 2 + 1 < foodLevel) {
                        drawReverseTexturedModalRect(xOffset, yOffset, texXOffset + 36, 27, 9, 9);
                    }
                    if (index * 2 + 1 == foodLevel) {
                        drawReverseTexturedModalRect(xOffset, yOffset, texXOffset + 45, 27, 9, 9);
                    }
                }
            }
        }
    }
}
