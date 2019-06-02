package dev.satyrn.wolfarmor.common.network;

import dev.satyrn.wolfarmor.api.IArmoredWolf;
import dev.satyrn.wolfarmor.client.gui.GuiWolfInventory;
import dev.satyrn.wolfarmor.common.inventory.ContainerWolfInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

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

        if (entity != null && entity instanceof EntityWolf) {
            return new ContainerWolfInventory(player.inventory, ((IArmoredWolf) entity).getInventory(), (EntityWolf) entity, player);
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

        if (entity != null && entity instanceof EntityWolf) {
            return new GuiWolfInventory(player.inventory, ((IArmoredWolf)entity).getInventory(), (EntityWolf) entity, player);
        }
        return null;
    }

    //endregion Accessors / Mutators
}
