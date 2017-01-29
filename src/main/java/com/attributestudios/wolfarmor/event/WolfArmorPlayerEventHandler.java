package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

/**
 * Contains all forge subscribed events for players.
 */
public class WolfArmorPlayerEventHandler {
    //region Public / Protected Methods

    /**
     * Handles player interaction events.
     *
     * @param event The RightClickBlock event that should be handled.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPlayerInteract(@Nonnull RightClickBlock event) {
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

        if (block == Blocks.CAULDRON) {
            ItemStack stack = event.getItemStack();

            this.handleCauldronRightClick(event, stack);
        }
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Handles right click on cauldron block
     *
     * @param event The RightClickEvent that was handled.
     * @param stack The item stack
     */
    private void handleCauldronRightClick(@Nonnull RightClickBlock event, @Nullable ItemStack stack) {
        if (!event.getWorld().isRemote) {
            if (stack != null) {
                IBlockState blockCauldronState = event.getWorld().getBlockState(event.getPos());
                int fillLevel = blockCauldronState.getValue(BlockCauldron.LEVEL);

                if (fillLevel > 0 && stack.getItem() instanceof ItemWolfArmor) {
                    ItemWolfArmor itemWolfArmor = (ItemWolfArmor) stack.getItem();

                    if (itemWolfArmor.getMaterial().getIsDyeable()) {
                        itemWolfArmor.removeColor(stack);
                        Blocks.CAULDRON.setWaterLevel(event.getWorld(), event.getPos(), blockCauldronState, fillLevel - 1);
                        event.setResult(Event.Result.ALLOW);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    //endregion Private Methods
}
