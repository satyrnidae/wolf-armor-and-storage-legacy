package com.attributestudios.wolfarmor.common.inventory;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.common.capabilities.IWolfArmor;

/**
 * Models a container for a wolf's inventory.
 */
public class ContainerWolfInventory extends Container {
    //region Fields

    private IInventory wolfInventory;
    private EntityWolf theWolf;

    //endregion Fields

    //region Constructors

    /**
     * Creates a new wolf inventory container.
     *
     * @param playerInventory The player's  inventory
     * @param wolfInventory   The wolf's inventory
     * @param theWolf         The wolf
     */
    @SuppressWarnings("ConstantConditions")
    public ContainerWolfInventory(@Nonnull IInventory playerInventory,
                                  @Nonnull final IInventory wolfInventory,
                                  @Nonnull final EntityWolf theWolf,
                                  @Nonnull EntityPlayer player) {
        this.wolfInventory = wolfInventory;
        this.theWolf = theWolf;

        this.wolfInventory.openInventory(player);

        this.addSlotToContainer(new Slot(wolfInventory, 0, 8, 18) {

            /**
             * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
             * in the case of armor slots)
             */
            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            /**
             * Returns whether or not an item is valid
             * @param stack The item stack
             * @return True if the item is valid in a slot and if the item is a wolf armor item
             */
            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return stack == null || (super.isItemValid(stack) && CapabilityWolfArmor.getIsValidWolfArmorItem(stack.getItem()));
            }
        });

        IWolfArmor wolfArmor = theWolf.getCapability(CapabilityWolfArmor.WOLF_ARMOR, null);

        int x;
        int y;
        if (wolfArmor.getHasChest()) {
            for (y = 0; y < 2; y++) {
                for (x = 0; x < 3; x++) {
                    this.addSlotToContainer(new Slot(wolfInventory, 1 + x + y * 3, 98 + x * 18, 18 + y * 18));
                }
            }
        }

        for (y = 0; y < 3; y++) {
            for (x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Transfers an item stack.
     *
     * @param player The player entity.
     * @param slot   The slot the player is interacting with.
     * @return The item stack
     */
    @Override
    @Nullable
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot inventorySlot = this.inventorySlots.get(slot);

        if (inventorySlot != null && inventorySlot.getHasStack()) {
            ItemStack stackInSlot = inventorySlot.getStack();

            if (stackInSlot != null) {
                stack = stackInSlot.copy();

                if (slot < this.wolfInventory.getSizeInventory()) {
                    if (!this.mergeItemStack(stackInSlot, this.wolfInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                        return null;
                    }
                } else if (this.getSlot(0).isItemValid(stackInSlot) && !this.getSlot(0).getHasStack()) {
                    if (!this.mergeItemStack(stackInSlot, 0, 2, false)) {
                        return null;
                    }
                } else if (this.wolfInventory.getSizeInventory() <= 1 || !this.mergeItemStack(stackInSlot, 1, this.wolfInventory.getSizeInventory(), false)) {
                    return null;
                }

                if (stackInSlot.stackSize == 0) {
                    inventorySlot.putStack(null);
                } else {
                    inventorySlot.onSlotChanged();
                }
            }
        }

        return stack;
    }

    /**
     * Gets whether or not the container can be interacted with/
     *
     * @param player The player attempting to interact with this container
     * @return <tt>true</tt> if the interaction can be initiated, <tt>false</tt> if not
     */
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return this.wolfInventory.isUseableByPlayer(player) &&
                !this.theWolf.isDead &&
                this.theWolf.getDistanceToEntity(player) < 8;
    }

    //endregion Public / Protected Methods
}
