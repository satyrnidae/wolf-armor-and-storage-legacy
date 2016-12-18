package com.attributestudios.wolfarmor.event;

import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

/**
 * Contains all forge subscribed events for players.
 */
public class WolfArmorPlayerEventHandler {
    //region Public / Protected Methods

    /**
     * Handles player interaction events.
     * @param event The PlayerInteractEvent that should be handled.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPlayerInteract(@Nonnull PlayerInteractEvent event)
    {
        switch(event.action)
        {
            case RIGHT_CLICK_BLOCK:
                Block block = event.world.getBlock(event.x, event.y, event.z);

                if(block == Blocks.cauldron) {
                    this.handleCauldronRightClick(event);
                }
                break;
        }
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Handles right click on cauldron block
     * @param event The RightClickEvent that was handled.
     */
    private void handleCauldronRightClick(@Nonnull PlayerInteractEvent event)
    {
        if(!event.world.isRemote)
        {
            ItemStack stack = event.entityPlayer.inventory.getCurrentItem();

            if(stack != null)
            {
                int metadata = event.world.getBlockMetadata(event.x, event.y, event.z);
                int fillLevel = BlockCauldron.func_150027_b(metadata);

                if(fillLevel > 0 && stack.getItem() instanceof ItemWolfArmor)
                {
                    ItemWolfArmor itemWolfArmor = (ItemWolfArmor)stack.getItem();

                    if(itemWolfArmor.getMaterial().getIsDyeable())
                    {
                        itemWolfArmor.removeColor(stack);
                        event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, fillLevel - 1, 2);
                        event.world.func_147453_f(event.x, event.y, event.z, Blocks.cauldron);
                        event.setResult(Event.Result.ALLOW);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    //endregion Private Methods
}
