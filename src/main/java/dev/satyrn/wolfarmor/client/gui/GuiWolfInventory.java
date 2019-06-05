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

    //endregion Fields

    //region Constructors

    /**
     * Initializes a new wolf inventory GUI
     *
     * @param playerInventory The player's inventory. Used to display player's current items
     * @param wolfInventory   The wolf's inventory
     * @param theWolf         The wolf in question
     */
    @SuppressWarnings("ConstantConditions")
    public GuiWolfInventory(@Nonnull IInventory playerInventory,
                            @Nonnull IInventory wolfInventory,
                            @Nonnull EntityWolf theWolf,
                            @Nonnull EntityPlayer player) {
        super(new ContainerWolfInventory(playerInventory, wolfInventory, theWolf, player));
        this.wolfArmor = (IArmoredWolf)theWolf;
        this.wolfInventory = wolfInventory;
        this.playerInventory = playerInventory;
        this.theWolf = theWolf;
        this.allowUserInput = false;
    }

    //endregion Constructors

    //region Public / Protected Methods


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if(isChestButtonHovered(mouseX, mouseY)) {
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
                : I18n.format(this.playerInventory.getName()), 8, this.ySize - 94, 0x404040);

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
        this.screenPositionX = mouseX;
        this.screenPositionY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);

        if(this.wolfArmor.getHasChest() && this.isChestButtonHovered(mouseX, mouseY)) {
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

            this.drawTexturedModalRect(origin.getX(), origin.getY(), 0, 0, this.xSize, this.ySize);

            if (this.wolfArmor.getHasArmor()) {
                this.drawTexturedModalRect(origin.getX() + 7, origin.getY() + 17, this.xSize, 36, 18, 18);
            }

            if (this.wolfArmor.getHasChest()) {
                this.drawTexturedModalRect(origin.getX() + 97, origin.getY() + 17, this.xSize, 0, 54, 36);
            }

            GuiInventory.drawEntityOnScreen(origin.getX() + 51, origin.getY() + 60, 30,
                    (float) (origin.getX() + 51) - this.screenPositionX,
                    (float) (origin.getY() - 50) - this.screenPositionY, this.theWolf);

        }
        GlStateManager.popMatrix();
        if(this.wolfArmor.getHasChest()) {
            GlStateManager.pushMatrix();
            {
                // draw button at 7,35

                GlStateManager.color(1, 1, 1, 1);

                int textureY = 166;

                if (this.isChestButtonHovered(mouseX, mouseY)) {
                    textureY += 18;
                }

                this.mc.getTextureManager().bindTexture(Resources.TEXTURE_GUI_WOLF_INVENTORY);
                this.drawTexturedModalRect(origin.getX() + 7, origin.getY() + 35, 0, textureY, 18, 18);
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

                int yPosition = 56;

                if (!WolfArmorMod.getConfiguration().getIsWolfArmorDisplayEnabled() || !this.wolfArmor.getHasArmor()) {
                    yPosition += 5;
                }

                for (int columnOffset = 0; rowOffset > 0; columnOffset += 20) {
                    int rowIterate = Math.min(rowOffset, 10);
                    rowOffset -= rowIterate;

                    for (int row = rowIterate - 1; row >= 0; row--) {
                        int xPosition = (this.xSize / 2) + row * 8 - 4;

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
                int yPosition = 66;

                if (!WolfArmorMod.getConfiguration().getIsWolfHealthDisplayEnabled()) {
                    yPosition -= 5;
                }

                int armor = this.theWolf.getTotalArmorValue();
                double maxArmor = IWolfArmorMaterial.MAX_VANILLA_ARMOR_VALUE;
                int rowOffset = (int) Math.min((maxArmor + 0.5F) / 2, 30);

                for (int columnOffset = 0; rowOffset > 0; columnOffset += 20) {
                    int rowIterate = Math.min(rowOffset, 10);
                    rowOffset -= rowIterate;

                    for (int row = rowIterate - 1; row >= 0; row--) {
                        int xPosition = (this.xSize / 2) + row * 8 - 4;

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

        return mouseLoc.getX() >= 8 && mouseLoc.getY() >= 37 && mouseLoc.getX() < 8 + 16 && mouseLoc.getY() < 37 + 16;
    }

    //endregion Private Methods
}
