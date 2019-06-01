package com.attributestudios.wolfarmor.client.gui;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.api.IWolfArmorMaterial;
import dev.satyrn.wolfarmor.api.util.Capabilities;
import dev.satyrn.wolfarmor.api.util.Resources;
import dev.satyrn.wolfarmor.api.IWolfArmorCapability;
import com.attributestudios.wolfarmor.common.inventory.ContainerWolfInventory;
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

import javax.annotation.Nonnull;

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

    private final IWolfArmorCapability wolfArmor;

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
        this.wolfArmor = theWolf.getCapability(Capabilities.CAPABILITY_WOLF_ARMOR, null);
        this.wolfInventory = wolfInventory;
        this.playerInventory = playerInventory;
        this.theWolf = theWolf;
        this.allowUserInput = false;
    }

    //endregion Constructors

    //region Public / Protected Methods

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

    /**
     * Draws the background layer of the inventory.
     *
     * @param partialTicks The tick state of the client
     * @param mouseX       The X position of the mouse
     * @param mouseY       The Y position of the mouse
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        {
            GlStateManager.color(1, 1, 1, 1);

            this.mc.getTextureManager().bindTexture(Resources.TEXTURE_GUI_WOLF_INVENTORY);

            int positionX = (this.width - this.xSize) / 2;
            int positionY = (this.height - this.ySize) / 2;
            this.drawTexturedModalRect(positionX, positionY, 0, 0, this.xSize, this.ySize);

            if (this.wolfArmor.getHasArmor()) {
                this.drawTexturedModalRect(positionX + 7, positionY + 17, this.xSize, 36, 18, 18);
            }

            if (this.wolfArmor.getHasChest()) {
                this.drawTexturedModalRect(positionX + 97, positionY + 17, this.xSize, 0, 54, 36);
            }

            GuiInventory.drawEntityOnScreen(positionX + 51, positionY + 60, 30, (float) (positionX + 51) - this.screenPositionX,
                    (float) (positionY - 50) - this.screenPositionY, this.theWolf);

        }
        GlStateManager.popMatrix();
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

    //endregion Private Methods
}
