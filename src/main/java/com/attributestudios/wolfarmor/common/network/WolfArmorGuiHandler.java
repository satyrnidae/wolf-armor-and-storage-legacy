package com.attributestudios.wolfarmor.common.network;

import com.attributestudios.wolfarmor.client.gui.GuiWolfInventory;
import com.attributestudios.wolfarmor.common.inventory.ContainerWolfInventory;
import com.attributestudios.wolfarmor.entity.passive.EntityWolfArmored;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Handles GUI requests over the network for this mod.
 */
public class WolfArmorGuiHandler implements IGuiHandler {
    //region Accessors / Mutators

    /**
     * Returns a Server side Container to be displayed to the user.
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Override
    @Nullable
    public Object getServerGuiElement(int ID,
                                      @Nonnull EntityPlayer player,
                                      @Nonnull World world,
                                      int x,
                                      int y,
                                      int z) {
        Entity entity = world.getEntityByID(ID);

        if(entity != null && entity instanceof EntityWolfArmored) {
            EntityWolfArmored wolfArmored = (EntityWolfArmored)entity;
            return new ContainerWolfInventory(player.inventory, wolfArmored.inventory, wolfArmored);
        }

        return null;
    }

    /**
     * Returns a Container to be displayed to the user. On the client side, this
     * needs to return a instance of GuiScreen On the server side, this needs to
     * return a instance of Container
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Override
    @Nullable
    public Object getClientGuiElement(int ID,
                                      @Nonnull EntityPlayer player,
                                      @Nonnull World world,
                                      int x,
                                      int y,
                                      int z) {
        Entity entity = world.getEntityByID(ID);

        if(entity != null && entity instanceof EntityWolfArmored) {
            EntityWolfArmored wolfArmored = (EntityWolfArmored)entity;
            return new GuiWolfInventory(player.inventory, wolfArmored.inventory, wolfArmored);
        }

        return null;
    }

    //endregion Accessors / Mutators
}
