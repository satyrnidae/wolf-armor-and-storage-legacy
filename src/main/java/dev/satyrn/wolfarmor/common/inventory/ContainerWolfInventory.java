package dev.satyrn.wolfarmor.common.inventory;

import dev.satyrn.wolfarmor.WolfArmorMod;
import dev.satyrn.wolfarmor.advancements.WolfArmorTrigger;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.item.ItemWolfArmor;
import dev.satyrn.wolfarmor.api.util.Criteria;
import dev.satyrn.wolfarmor.api.util.Items;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import net.minecraft.util.SoundEvent;

/**
 * Models a container for a wolf's inventory.
 */
public class ContainerWolfInventory extends Container {
    //region Fields

    public static final int INVENTORY_SLOT_ARMOR = 0;
    public static final int INVENTORY_SLOT_CHEST_START = 1;

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
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.isEmpty() || (super.isItemValid(stack) && Items.isValidWolfArmor(stack.getItem()));
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();

                ItemStack stack = this.getStack();
                if(player instanceof EntityPlayerMP && !stack.isEmpty() && Items.isValidWolfArmor(stack.getItem())) {
                    ((WolfArmorTrigger)Criteria.EQUIP_WOLF_ARMOR).trigger((EntityPlayerMP)player, theWolf);
                }
                if (stack.getItem() instanceof ItemWolfArmor && !theWolf.getEntityWorld().isRemote) {
                    ItemWolfArmor wolfArmor = (ItemWolfArmor) stack.getItem();
                    SoundEvent sound = wolfArmor.getMaterial().getEquipSound();
                    theWolf.playSound(sound, 1, (theWolf.getRNG().nextFloat() - theWolf.getRNG().nextFloat()) * 0.2F + 1);
                }
            }


        });

        IArmoredWolf wolfArmor = (IArmoredWolf)theWolf;

        int wolfInventorySizeX = WolfArmorMod.getConfiguration().getWolfChestSizeHorizontal();
        int wolfInventorySizeY = WolfArmorMod.getConfiguration().getWolfChestSizeVertical();
        int xItemOffset = (5 - wolfInventorySizeX) * 9;
        int yItemOffset = (3 - wolfInventorySizeY) * 9;

        int x;
        int y;
        if (wolfArmor.getHasChest()) {
            for (y = 0; y < wolfInventorySizeY; y++) {
                for (x = 0; x < wolfInventorySizeX; x++) {
                    this.addSlotToContainer(new Slot(wolfInventory, 1 + x + y * wolfInventorySizeX, 80 + xItemOffset + x * 18, 18 + yItemOffset + y * 18));
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
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer player, int slot) {
        ItemStack stack = ItemStack.EMPTY;

        Slot inventorySlot = this.inventorySlots.get(slot);

        if (inventorySlot != null && inventorySlot.getHasStack()) {
            ItemStack stackInSlot = inventorySlot.getStack();

            if (!stackInSlot.isEmpty()) {
                stack = stackInSlot.copy();

                if(slot < this.wolfInventory.getSizeInventory()) {
                    if(!this.mergeItemStack(stackInSlot, this.wolfInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if(this.getSlot(0).isItemValid(stackInSlot) && !this.getSlot(0).getHasStack()) {
                    if(!this.mergeItemStack(stackInSlot, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if(this.wolfInventory.getSizeInventory() <= 1 || !this.mergeItemStack(stackInSlot, 1, this.wolfInventory.getSizeInventory(), false)) {
                    return ItemStack.EMPTY;
                }

                if(stackInSlot.getCount() == 0) {
                    inventorySlot.putStack(ItemStack.EMPTY);
                }
                else {
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
        return this.wolfInventory.isUsableByPlayer(player) &&
                !this.theWolf.isDead &&
                this.theWolf.getDistance(player) < 8;
    }

    //endregion Public / Protected Methods
}
