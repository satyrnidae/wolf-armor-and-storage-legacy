package dev.satyrn.wolfarmor.client.gui;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.IArmoredWolf;
import dev.satyrn.wolfarmor.api.IWolfArmorMaterial;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.common.inventory.ContainerWolfInventory;
import dev.satyrn.wolfarmor.common.network.PacketHandler;
import dev.satyrn.wolfarmor.common.network.packets.WolfDropChestMessage;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Graphical user interface for wolf inventories.
 */
@SideOnly(Side.CLIENT)
public class GuiWolfInventory extends GuiContainer {
    //region Fields

    private IInventory wolfInventory;
    private IInventory playerInventory;

    private EntityWolf theWolf;

    private float screenPositionX;
    private float screenPositionY;

    private final IArmoredWolf wolfArmor;

    // Empty Slot Graphic
    private final Rectangle EMPTY_SLOT = new Rectangle(7, 83, 18, 18);

    // Button Effective Area
    private final Rectangle BUTTON_AREA = new Rectangle(7, 35, 18, 18);

    // Button Graphic
    private final Rectangle BUTTON_TEXTURE = new Rectangle(0, 173, 18, 18);

    private final Rectangle EMPTY_CHEST_BG = new Rectangle(176, 0, 18, 18);

    //endregion Fields

    //region Constructors

    /**
     * Initializes a new wolf inventory GUI
     *
     * @param playerInventory The player's inventory. Used to display player's current items
     * @param wolfInventory   The wolf's inventory
     * @param theWolf         The wolf in question
     */
    public GuiWolfInventory(@Nonnull IInventory playerInventory,
                            @Nonnull IInventory wolfInventory,
                            @Nonnull EntityWolf theWolf,
                            @Nonnull EntityPlayer player) {
        super(new ContainerWolfInventory(playerInventory, wolfInventory, theWolf, player));
        this.wolfArmor = (IArmoredWolf) theWolf;
        this.wolfInventory = wolfInventory;
        this.playerInventory = playerInventory;
        this.theWolf = theWolf;
        this.allowUserInput = false;
        this.ySize = 172;
    }

    //endregion Constructors

    //region Public / Protected Methods


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (isChestButtonHovered(mouseX, mouseY)) {
            PacketHandler.getChannel().sendToServer(new WolfDropChestMessage(theWolf.getEntityId()));
            this.mc.player.closeScreen();
        }
    }

    /**
     * Draws the foreground layer of the GUI
     *
     * @param mouseX The X position of the mouse
     * @param mouseY The Y position of the mouse
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String wolfName = this.theWolf.hasCustomName()
                ? this.theWolf.getCustomNameTag()
                : I18n.format("entity.Wolf.name");

        this.fontRenderer.drawString(this.wolfInventory.hasCustomName()
                ? this.wolfInventory.getName()
                : I18n.format(this.wolfInventory.getName(), wolfName), 8, 6, 0x404040);
        this.fontRenderer.drawString(this.playerInventory.hasCustomName()
                ? this.playerInventory.getName()
                : I18n.format(this.playerInventory.getName()), 8, this.ySize - 98, 0x404040);

        this.drawWolfHealthAndArmor();

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    /**
     * Draws the screen and all components in it.
     *
     * @param mouseX       The X position of the mouse
     * @param mouseY       The Y position of the mouse
     * @param partialTicks The tick state of the client.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.screenPositionX = mouseX;
        this.screenPositionY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);

        if (this.wolfArmor.getHasChest() && this.isChestButtonHovered(mouseX, mouseY)) {
            List<String> displayText = new ArrayList<>();
            displayText.add(I18n.format("gui.wolfarmor.inventory.remove_chest"));
            this.drawHoveringText(displayText, mouseX, mouseY, fontRenderer);
        }
    }

    /**
     * Draws the background layer of the inventory.
     *
     * @param partialTicks The tick state of the client
     * @param mouseX       The X position of the mouse
     * @param mouseY       The Y position of the mouse
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Point origin = this.getOrigin();

        GlStateManager.pushMatrix();
        {
            GlStateManager.color(1, 1, 1, 1);

            this.mc.getTextureManager().bindTexture(Resources.TEXTURE_GUI_WOLF_INVENTORY);

            // Draw screen background
            this.drawTexturedModalRect(origin.getX(), origin.getY(), 0, 0, this.xSize, this.ySize);

            if (this.wolfArmor.getHasArmor()) {
                // Draw unlined wolf armor slot
                this.drawTexturedModalRect(
                        origin.getX() + 7,
                        origin.getY() + 17,
                        EMPTY_SLOT.getX(),
                        EMPTY_SLOT.getY(),
                        EMPTY_SLOT.getWidth(),
                        EMPTY_SLOT.getHeight());
            }


            int wolfInventorySizeX = WolfArmorMod.getConfiguration().getWolfChestSizeHorizontal();
            int wolfInventorySizeY = WolfArmorMod.getConfiguration().getWolfChestSizeVertical();

            Point chestOrigin = new Point((5 - wolfInventorySizeX) * 9 + 79, (3 - wolfInventorySizeY) * 9 + 17);

            if (this.wolfArmor.getHasChest()) {
                //draw chest slots
                for (int xOffset = 0; xOffset < wolfInventorySizeX; xOffset++) {
                    for (int yOffset = 0; yOffset < wolfInventorySizeY; yOffset++) {
                        this.drawTexturedModalRect(
                                origin.getX() + chestOrigin.getX() + (EMPTY_SLOT.getWidth() * xOffset),
                                origin.getY() + chestOrigin.getY() + (EMPTY_SLOT.getHeight() * yOffset),
                                EMPTY_SLOT.getX(),
                                EMPTY_SLOT.getY(),
                                EMPTY_SLOT.getWidth(),
                                EMPTY_SLOT.getHeight());
                    }
                }
            } else {
                for (int xSlot = 0; xSlot < wolfInventorySizeX; xSlot++) {
                    for (int ySlot = 0; ySlot < wolfInventorySizeY; ySlot++) {
                        int textureXOffset = (wolfInventorySizeX > 1 ? 1 : 0) + (xSlot > 0 ? (xSlot == wolfInventorySizeX - 1 ? 2 : 1) : 0);
                        int textureYOffset = (wolfInventorySizeY > 1 ? 1 : 0) + (ySlot > 0 ? (ySlot == wolfInventorySizeY - 1 ? 2 : 1) : 0);

                        drawTexturedModalRect(
                                origin.getX() + chestOrigin.getX() + (EMPTY_CHEST_BG.getWidth() * xSlot),
                                origin.getY() + chestOrigin.getY() + (EMPTY_CHEST_BG.getHeight() * ySlot),
                                textureXOffset * EMPTY_CHEST_BG.getWidth() + EMPTY_CHEST_BG.getX(),
                                textureYOffset * EMPTY_CHEST_BG.getHeight() + EMPTY_CHEST_BG.getY(),
                                EMPTY_CHEST_BG.getWidth(),
                                EMPTY_CHEST_BG.getHeight());
                    }
                }
            }

            GuiInventory.drawEntityOnScreen(origin.getX() + 51, origin.getY() + 60, 30,
                    (float) (origin.getX() + 51) - this.screenPositionX,
                    (float) (origin.getY() - 50) - this.screenPositionY, this.theWolf);
        }
        GlStateManager.popMatrix();
        if (this.wolfArmor.getHasChest()) {
            GlStateManager.pushMatrix();
            {
                // draw button at 7,35

                GlStateManager.color(1, 1, 1, 1);

                this.mc.getTextureManager().bindTexture(Resources.TEXTURE_GUI_WOLF_INVENTORY);
                this.drawTexturedModalRect(
                        origin.getX() + BUTTON_AREA.getX(),
                        origin.getY() + BUTTON_AREA.getY(),
                        BUTTON_TEXTURE.getX() + (this.isChestButtonHovered(mouseX, mouseY) ? BUTTON_TEXTURE.getWidth() : 0),
                        BUTTON_TEXTURE.getY(),
                        BUTTON_TEXTURE.getWidth(),
                        BUTTON_TEXTURE.getHeight());
            }
            GlStateManager.popMatrix();
        }
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Draws the wolf's current health and armor rating.
     */
    private void drawWolfHealthAndArmor() {
        GlStateManager.pushMatrix();
        {
            GlStateManager.color(1, 1, 1, 1);

            this.mc.getTextureManager().bindTexture(Gui.ICONS);

            if (WolfArmorMod.getConfiguration().getIsWolfHealthDisplayEnabled()) {
                //TODO: Wolf potion effects should alter hearts like they do for the player GUI

                int health = (int) Math.ceil(this.theWolf.getHealth());
                float maxHealth = this.theWolf.getMaxHealth();
                int rowOffset = (int) Math.min((maxHealth + 0.5F) / 2, 30);

                int yPosition = 160;

                for (int columnOffset = 0; rowOffset > 0; columnOffset += 20) {
                    int rowIterate = Math.min(rowOffset, 10);
                    rowOffset -= rowIterate;

                    for (int row = rowIterate - 1; row >= 0; row--) {
                        int xPosition = row * 8 + 5;

                        this.drawTexturedModalRect(xPosition, yPosition, 16, 0, 9, 9);

                        if (row * 2 + 1 + columnOffset < health) {
                            this.drawTexturedModalRect(xPosition, yPosition, 52, 0, 9, 9);
                        }

                        if (row * 2 + 1 + columnOffset == health) {
                            this.drawTexturedModalRect(xPosition, yPosition, 61, 0, 9, 9);
                        }
                    }
                }
            }

            if (WolfArmorMod.getConfiguration().getIsWolfArmorDisplayEnabled() && this.wolfArmor.getHasArmor()) {
                int yPosition = 160;

                int armor = this.theWolf.getTotalArmorValue();
                double maxArmor = IWolfArmorMaterial.MAX_VANILLA_ARMOR_VALUE;
                int rowOffset = (int) Math.min((maxArmor + 0.5F) / 2, 30);

                for (int columnOffset = 0; rowOffset > 0; columnOffset += 20) {
                    int rowIterate = Math.min(rowOffset, 10);
                    rowOffset -= rowIterate;

                    for (int row = rowIterate - 1; row >= 0; row--) {
                        int xPosition = xSize - row * 8 - 14;

                        this.drawTexturedModalRect(xPosition, yPosition, 16, 9, 9, 9);

                        if (row * 2 + 1 + columnOffset < armor) {
                            this.drawTexturedModalRect(xPosition, yPosition, 34, 9, 9, 9);
                        }

                        if (row * 2 + 1 + columnOffset == armor) {
                            this.drawTexturedModalRect(xPosition, yPosition, 25, 9, 9, 9);
                        }
                    }
                }
            }
        }
        GlStateManager.popMatrix();
    }

    private Point getOrigin() {
        return new Point((this.width - this.xSize) / 2, (this.height - this.ySize) / 2);
    }

    private Point getLocalFromScreen(int x, int y) {
        Point origin = getOrigin();
        int mouseLocX = x - origin.getX();
        int mouseLocY = y - origin.getY();

        return new Point(mouseLocX, mouseLocY);
    }


    private boolean isChestButtonHovered(int mouseX, int mouseY) {
        Point mouseLoc = getLocalFromScreen(mouseX, mouseY);

        return mouseLoc.getX() >= BUTTON_AREA.getX() &&
                mouseLoc.getY() >= BUTTON_AREA.getY() &&
                mouseLoc.getX() < BUTTON_AREA.getX() + BUTTON_AREA.getWidth() &&
                mouseLoc.getY() < BUTTON_AREA.getY() + BUTTON_AREA.getHeight();
    }

    //endregion Private Methods
}
