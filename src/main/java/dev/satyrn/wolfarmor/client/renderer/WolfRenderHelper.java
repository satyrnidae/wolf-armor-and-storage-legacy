package dev.satyrn.wolfarmor.client.renderer;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.entity.IFoodStatsCreature;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.api.util.CreatureFoodStats;
import dev.satyrn.wolfarmor.util.WolfFoodStatsLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

import javax.annotation.Nonnull;

/**
 * Assists with wolf rendering
 * @author Isabel Maskrey
 * @since 3.6.0
 */
@SideOnly(Side.CLIENT)
public class WolfRenderHelper {
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
        WolfRenderHelper.renderStats(wolf, 5, renderNameplate ? 22 : 0);

        // Reenable lighting, rest the depth function, and pop the view matrix from the stack
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private static final int ICON_SIZE = 9;

    /**
     * Draws an icon
     * @param x The X location of the rectangle's upper-left point
     * @param y The Y location of the rectangle's upper-left point
     * @param texX The texture coordinate's X position
     * @param texY The texture coordinate's Y position
     * @since 3.6.0
     */
    private static void drawIcon(int x, int y, int texX, int texY) {
        final float pixelScale = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y + ICON_SIZE, 0).tex(texX * pixelScale, (texY + ICON_SIZE) * pixelScale).endVertex();
        builder.pos(x + ICON_SIZE, y + ICON_SIZE, 0).tex((texX + ICON_SIZE) * pixelScale, (texY + ICON_SIZE) * pixelScale).endVertex();
        builder.pos(x + ICON_SIZE, y, 0).tex((texX + ICON_SIZE) * pixelScale, texY * pixelScale).endVertex();
        builder.pos(x, y, 0).tex(texX * pixelScale, texY * pixelScale).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a texture-reversed icon
     * @param x The X location of the rectangle's upper-left point
     * @param y The Y location of the rectangle's upper-left point
     * @param texX The texture coordinate's X position
     * @param texY The texture coordinate's Y position
     * @since 3.6.0
     */
    private static void drawReversedIcon(int x, int y, int texX, int texY) {
        float pixelScale = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y + ICON_SIZE, 0).tex((texX + ICON_SIZE) * pixelScale, (texY + ICON_SIZE) * pixelScale).endVertex();
        builder.pos(x + ICON_SIZE, y + ICON_SIZE, 0).tex(texX * pixelScale, (texY + ICON_SIZE) * pixelScale).endVertex();
        builder.pos(x + ICON_SIZE, y, 0).tex(texX * pixelScale, texY * pixelScale).endVertex();
        builder.pos(x, y, 0).tex((texX + ICON_SIZE) * pixelScale, texY * pixelScale).endVertex();
        tessellator.draw();
    }

    /**
     * Draws an icon
     * @param x The X location of the rectangle's upper-left point
     * @param y The Y location of the rectangle's upper-left point
     * @param texX The texture coordinate's X position
     * @param texY The texture coordinate's Y position
     * @param reverse Whether or not the texture of the icon should be flipped horizontally
     * @since 3.6.0
     */
    public static void drawIcon(int x, int y, int texX, int texY, boolean reverse) {
        if (reverse) {
            drawReversedIcon(x, y, texX, texY);
        } else {
            drawIcon(x, y, texX, texY);
        }
    }

    /**
     * Renders the wolf's stats in the nameplate render area
     * @param wolf The wolf
     * @param x The X offset of the stats
     * @param y The Y offset of the stats
     * @since 3.6.0
     */
    private static void renderStats(EntityWolf wolf, int x, int y) {
        int armorValue = wolf.getTotalArmorValue();

        int iconWidth = 9;
        int iconCount = 10;
        Point renderPos;
        int xOffset = x + (-iconWidth * iconCount / 2);
        int yOffset = y;


        // Armor
        if (((IArmoredWolf)wolf).getHasArmor() && armorValue > 0) {
            renderPos = renderArmor(wolf, new Point(xOffset, yOffset), false);
            yOffset = renderPos.getY();
        }

        // Health
        renderPos = renderHealth(wolf, new Point(xOffset, yOffset), false);
        yOffset = renderPos.getY();

        // food
        if (WolfArmorMod.getConfig().getFoodStatsLevel() != WolfFoodStatsLevel.DISABLED) {
            renderHunger(wolf, new Point(xOffset, yOffset), false);
        }
    }

    private static final Point ARMOR_FULL = new Point(34, 9);
    private static final Point ARMOR_HALF = new Point(25, 9);
    private static final Point ARMOR_EMPTY = new Point(16, 9);

    /**
     * Renders the wolf's armor value as icons
     * @param wolf The wolf
     * @param coord The coordinate at which to render the icons
     * @param reverse If <c>true</c>, the stat will be rendered right to left instead of left to right
     * @return The location at the end of the render.
     */
    public static Point renderArmor(EntityWolf wolf, Point coord, boolean reverse) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
        final int armorValue = wolf.getTotalArmorValue();
        final int xInitial = reverse ? coord.getX() + 81 : coord.getX();

        for (int index = 0; index < 10; ++index) {
            int xOffset = reverse ? xInitial - index * 8 : xInitial + index * 8;

            if (index * 2 + 1 < armorValue) {
                drawIcon(xOffset, coord.getY(), ARMOR_FULL.getX(), ARMOR_FULL.getY(), reverse);
            } else if (index * 2 + 1 == armorValue) {
                drawIcon(xOffset, coord.getY(), ARMOR_HALF.getX(), ARMOR_HALF.getY(), reverse);
            } else if (index * 2 + 1 > armorValue) {
                drawIcon(xOffset, coord.getY(), ARMOR_EMPTY.getX(), ARMOR_EMPTY.getY(), reverse);
            }
        }

        return new Point(81 + coord.getX(), 10 + coord.getY());
    }

    private static final int HEART_POISON_OFFSET_X = 36;
    private static final int HEART_WITHER_OFFSET_X = 72;
    private static final int HEART_ABSORPTION_FULL_OFFSET_X = 144;
    private static final int HEART_ABSORPTION_HALF_OFFSET_X = 153;
    private static final int HEART_FULL_OFFSET = 36;
    private static final int HEART_HALF_OFFSET = 45;
    private static final Point HEART_EMPTY = new Point(16, 0);

    /**
     * Renders the wolf's current health as icons
     * @param wolf The wolf
     * @param coord The coordinate at which to render the icons
     * @param reverse If <c>true</c>, the stat will be rendered right to left instead of the usual left to right
     * @return The location at the end of the render.
     */
    public static Point renderHealth(EntityWolf wolf, Point coord, boolean reverse) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
        final float health = wolf.getHealth();
        final double maxHealth = wolf.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
        final int absorption = MathHelper.ceil(wolf.getAbsorptionAmount());
        final int heartRows = MathHelper.ceil((maxHealth + absorption) / 2F / 10F);
        final int heartHeight = Math.max(10 - (heartRows - 2), 3);

        final int xInitial = reverse ? coord.getX() + 81 : coord.getX();

        int currentAbsorption = absorption;
        int regenHeartIndex = -1;

        if (wolf.isPotionActive(MobEffects.REGENERATION)) {
            regenHeartIndex = wolf.ticksExisted % MathHelper.ceil(maxHealth + 5F);
        }

        for (int index = MathHelper.ceil((maxHealth + absorption) / 2F) - 1; index >= 0; --index) {
            int texX = HEART_EMPTY.getX();
            if (wolf.isPotionActive(MobEffects.POISON)) {
                texX += HEART_POISON_OFFSET_X;
            } else if (wolf.isPotionActive(MobEffects.WITHER)) {
                texX += HEART_WITHER_OFFSET_X;
            }
            final int heartRow = MathHelper.ceil((index + 1F) / 10F) - 1;
            final int xOffset = reverse ? xInitial - index % 10 * 8 : xInitial + index % 10 * 8;
            int yOffset = coord.getY() + heartRow * heartHeight;

            // shake hearts at low health values
            if (health <= 2) {
                yOffset += wolf.getRNG().nextInt(2);
            }

            // Tick through hearts during regen
            if (currentAbsorption <= 0 && index == regenHeartIndex) {
                yOffset -= 2;
            }

            drawIcon(xOffset, yOffset, HEART_EMPTY.getX(), HEART_EMPTY.getY(), reverse);
            if (currentAbsorption > 0) {
                if (currentAbsorption == absorption && absorption % 2 == 1) {
                    drawIcon(xOffset, yOffset, texX + HEART_ABSORPTION_HALF_OFFSET_X, HEART_EMPTY.getY(), reverse);
                    --currentAbsorption;
                } else {
                    drawIcon(xOffset, yOffset, texX + HEART_ABSORPTION_FULL_OFFSET_X, HEART_EMPTY.getY(), reverse);
                    currentAbsorption -= 2;
                }
            } else {
                if (index * 2 + 1 < health) {
                    drawIcon(xOffset, yOffset, texX + HEART_FULL_OFFSET, HEART_EMPTY.getY(), reverse);
                } else if (index * 2 + 1 == health) {
                    drawIcon(xOffset, yOffset, texX + HEART_HALF_OFFSET, HEART_EMPTY.getY(), reverse);
                }
            }
        }

        return new Point(81 + coord.getX(), heartHeight - 1 + coord.getY());
    }

    private static final int FOOD_HUNGER_OFFSET_X = 36;
    private static final int FOOD_HUNGER_BG_MULT_X = 13;
    private static final int FOOD_FULL_OFFSET_X = 36;
    private static final int FOOD_HALF_OFFSET_X = 45;
    private static final Point FOOD_EMPTY = new Point (16, 27);

    /**
     * Renders the wolf's current hunger level as icons
     * @param wolf The wolf
     * @param coord The coordinate at which to render the icons
     * @param reverse If <c>true</c>, the stat will be rendered right to left instead of the usual left to right
     * @return The location at the end of the render.
     */
    public static Point renderHunger(EntityWolf wolf, Point coord, boolean reverse) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
        final CreatureFoodStats foodStats = ((IFoodStatsCreature)wolf).getFoodStats();
        final int foodLevel = foodStats.getFoodLevel();
        final int xInitial = reverse ? coord.getX() + 81 : coord.getX();

        for (int index = 0; index < 10; ++index) {
            final int xOffset = reverse ? xInitial - index * 8 : xInitial + index * 8;
            int yOffset = coord.getY();

            int texX = FOOD_EMPTY.getX();
            int hungerOffset = 0;

            if (wolf.isPotionActive(MobEffects.HUNGER)) {
                texX += FOOD_HUNGER_OFFSET_X;
                hungerOffset = FOOD_HUNGER_BG_MULT_X;
            }
            if (foodStats.getSaturationLevel() <= 0F && wolf.ticksExisted % (foodLevel * 3 + 1) == 0) {
                yOffset += wolf.getRNG().nextInt(3) - 1;
            }

            // For the draw routine we use !reverse, as food is reversed normally
            drawIcon(xOffset, yOffset, FOOD_EMPTY.getX() + hungerOffset * ICON_SIZE, FOOD_EMPTY.getY(), !reverse);
            if (index * 2 + 1 < foodLevel) {
                drawIcon(xOffset, yOffset, texX + FOOD_FULL_OFFSET_X, FOOD_EMPTY.getY(), !reverse);
            } else if (index * 2 + 1 == foodLevel) {
                drawIcon(xOffset, yOffset, texX + FOOD_HALF_OFFSET_X, FOOD_EMPTY.getY(), !reverse);
            }
        }

        return new Point(81 + coord.getX(), 10 + coord.getY());
    }
}
