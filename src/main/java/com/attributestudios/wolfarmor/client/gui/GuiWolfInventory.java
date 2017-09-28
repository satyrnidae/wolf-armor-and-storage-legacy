package com.attributestudios.wolfarmor.client.gui;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.inventory.ContainerWolfInventory;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import com.attributestudios.wolfarmor.item.ItemWolfArmor.WolfArmorMaterial;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

/**
 * Graphical user interface for wolf inventories.
 */
@SideOnly(Side.CLIENT)
public class GuiWolfInventory extends GuiContainer {
    //region Fields

    private ResourceLocation TEXTURE_GUI_WOLF_INVENTORY = new ResourceLocation(WolfArmorMod.MOD_ID, "textures/gui/wolf.png");

    private IInventory wolfInventory;
    private IInventory playerInventory;

    private EntityWolfArmored theWolf;

    private float screenPositionX;
    private float screenPositionY;

    //endregion Fields

    //region Constructors

    /**
     * Initializes a new wolf inventory GUI
     * @param playerInventory The player's inventory. Used to display player's current items
     * @param wolfInventory The wolf's inventory
     * @param theWolf The wolf in question
     */
    public GuiWolfInventory(@Nonnull IInventory playerInventory,
                            @Nonnull IInventory wolfInventory,
                            @Nonnull EntityWolfArmored theWolf) {
        super(new ContainerWolfInventory(playerInventory, wolfInventory, theWolf));
        this.wolfInventory = wolfInventory;
        this.playerInventory = playerInventory;
        this.theWolf = theWolf;
        this.allowUserInput = false;
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Draws the foreground layer of the GUI
     * @param mouseX The X position of the mouse
     * @param mouseY The Y position of the mouse
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String wolfName = this.theWolf.hasCustomNameTag()
                ? this.theWolf.getCustomNameTag()
                : I18n.format("entity.wolfarmor.Wolf.name");

        this.fontRendererObj.drawString(this.wolfInventory.isCustomInventoryName()
                ? this.wolfInventory.getInventoryName()
                : I18n.format(this.wolfInventory.getInventoryName(), wolfName), 8, 6, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.isCustomInventoryName()
                ? this.playerInventory.getInventoryName()
                : I18n.format(this.playerInventory.getInventoryName()), 8, this.ySize - 94, 0x404040);

        this.drawWolfHealthAndArmor();
    }

    /**
     * Draws the screen and all components in it.
     * @param mouseX The X position of the mouse
     * @param mouseY The Y position of the mouse
     * @param partialTicks The tick state of the client.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.screenPositionX = mouseX;
        this.screenPositionY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Draws the background layer of the inventory.
     * @param partialTicks The tick state of the client
     * @param mouseX The X position of the mouse
     * @param mouseY The Y position of the mouse
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glPushMatrix(); {
            GL11.glColor4f(1, 1, 1, 1);

            this.mc.getTextureManager().bindTexture(TEXTURE_GUI_WOLF_INVENTORY);

            int positionX = (this.width - this.xSize) / 2;
            int positionY = (this.height - this.ySize) / 2;
            this.drawTexturedModalRect(positionX, positionY, 0, 0, this.xSize, this.ySize);

            if (this.theWolf.getHasArmor()) {
                this.drawTexturedModalRect(positionX + 7, positionY + 17, this.xSize, 36, 18, 18);
            }

            if (this.theWolf.getHasChest()) {
                this.drawTexturedModalRect(positionX + 97, positionY + 17, this.xSize, 0, 54, 36);
            }

            GuiInventory.drawEntityOnScreen(positionX + 51, positionY + 60, 30, (float) (positionX + 51) - this.screenPositionX,
                                       (float) (positionY - 50) - this.screenPositionY, this.theWolf);

        } GL11.glPopMatrix();
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Draws the wolf's current health and armor rating.
     */
    private void drawWolfHealthAndArmor() {
        GL11.glPushMatrix();
        {
            GL11.glColor4f(1, 1, 1, 1);

            this.mc.getTextureManager().bindTexture(Gui.icons);

            if (WolfArmorMod.getConfiguration().getIsWolfHealthDisplayEnabled()) {
                //TODO: Wolf potion effects should alter hearts like they do for the player GUI

                int health = (int) Math.ceil(this.theWolf.getHealth());
                float maxHealth = this.theWolf.getMaxHealth();
                int rowOffset = (int) Math.min((maxHealth + 0.5F) / 2, 30);

                int yPosition = 56;

                if (!WolfArmorMod.getConfiguration().getIsWolfArmorDisplayEnabled() || !this.theWolf.getHasArmor()) {
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

            if (WolfArmorMod.getConfiguration().getIsWolfArmorDisplayEnabled() && this.theWolf.getHasArmor()) {
                int yPosition = 66;

                if (!WolfArmorMod.getConfiguration().getIsWolfHealthDisplayEnabled()) {
                    yPosition -= 5;
                }

                int armor = this.theWolf.getTotalArmorValue();
                int maxArmor = WolfArmorMaterial.getMaxArmorValue();
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
        GL11.glPopMatrix();
    }

    //endregion Private Methods
}
