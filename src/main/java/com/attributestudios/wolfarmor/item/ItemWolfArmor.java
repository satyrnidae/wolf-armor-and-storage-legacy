package com.attributestudios.wolfarmor.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.attributestudios.wolfarmor.common.capabilities.CapabilityWolfArmor;
import com.attributestudios.wolfarmor.api.IWolfArmorCapability;

import java.util.List;

/**
 * A wolf armor item
 */
public class ItemWolfArmor extends Item {
    //region Fields

    private static final String NBT_TAG_DISPLAY = "display";
    private static final String NBT_TAG_COLOR = "color";
    private static final IBehaviorDispenseItem DISPENSE_ITEM = new BehaviorDefaultDispenseItem() {
        /**
         * Handles dispense of specified stack
         * @param source The source block
         * @param stack The stack
         * @return The stack to dispense
         */
        @Override
        @Nonnull
        protected ItemStack dispenseStack(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
            ItemStack itemStack = ItemWolfArmor.dispenseWolfArmor(source, stack);
            return !itemStack.isEmpty() ? itemStack : super.dispenseStack(source, stack);
        }
    };

    private final WolfArmorMaterial material;

    //endregion Fields

    //region Constructors

    /**
     * Creates a new wolf armor item
     *
     * @param material The armor material
     */
    @SuppressWarnings("WeakerAccess")
    public ItemWolfArmor(@Nonnull WolfArmorMaterial material) {
        super();

        this.material = material;
        this.maxStackSize = 1;
        this.canRepair = true;
        this.setMaxDamage(material.getDurability());

        this.setCreativeTab(CreativeTabs.MISC);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSE_ITEM);
    }

    //endregion Constructors

    //region Public / Protected Methods

    /**
     * Removes color data from the given stack
     *
     * @param stack The item stack
     */
    public void removeColor(@Nonnull ItemStack stack) {
        if(!stack.isEmpty() && this.material.getIsDyeable()) {
            if(this.getHasColor(stack)) {
                NBTTagCompound stackCompound = stack.getTagCompound();
                if (stackCompound != null) {
                    stackCompound.getCompoundTag(NBT_TAG_DISPLAY).removeTag(NBT_TAG_COLOR);
                }
            }
        }
    }

    //endregion Public / Protected Methods

    //region Private Methods

    /**
     * Helper method for dispensing item stacks
     *
     * @param source The block source
     * @param stack  The dispensing stack
     * @return An item stack to dispense, or null if default dispense should occur.
     */
    @Nonnull
    private static ItemStack dispenseWolfArmor(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
        BlockPos blockPos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
        List<EntityWolf> wolves = source.getWorld().getEntitiesWithinAABB(EntityWolf.class, new AxisAlignedBB(blockPos));
        if (!wolves.isEmpty()) {
            EntityWolf wolf = null;

            for (EntityWolf entity : wolves) {
                if (entity.isTamed() && !entity.isChild()) {
                    wolf = entity;
                    break;
                }
            }

            if (wolf != null) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(1);
                IWolfArmorCapability wolfArmor = wolf.getCapability(CapabilityWolfArmor.WOLF_ARMOR_CAPABILITY, null);
                if (wolfArmor == null || !wolfArmor.canEquipItem(copyStack)) {
                    return stack;
                }
                wolfArmor.equipArmor(copyStack);
                stack.shrink(1);
            }
        }

        return stack;
    }

    //endregion Private Methods

    //region Accessors / Mutators

    /**
     * Gets the item enchantability from the item's armor material
     *
     * @return The item enchantability
     */
    @Override
    public int getItemEnchantability() {
        return this.getMaterial().getEnchantability();
    }

    /**
     * Gets whether or not the item can be repaired by the given item in an anvil
     *
     * @param originalStack The original item stack
     * @param repairStack   The repair item stack
     * @return Whether or not the item can be repaired
     */
    @Override
    public boolean getIsRepairable(@Nonnull ItemStack originalStack, @Nonnull ItemStack repairStack) {
        return this.getMaterial().getRepairItem() == repairStack.getItem() || super.getIsRepairable(originalStack, repairStack);
    }

    /**
     * Gets whether or not the armor has an overlay
     *
     * @param stack The stack to check for an overlay
     * @return Whether or not the wolf armor has an overlay layer
     */
    public boolean getHasOverlay(@Nonnull ItemStack stack) {
        return this.getMaterial().getHasOverlay() || this.getColor(stack) != -1;
    }

    /**
     * whether or not the stack has a color applied
     *
     * @param stack The item stack
     * @return A boolean representing whether or not the stack has a color applied
     */
    public boolean getHasColor(@Nonnull ItemStack stack) {
        if(!stack.isEmpty() && this.getMaterial().getIsDyeable()) {
            if(stack.hasTagCompound()) {
                NBTTagCompound tagCompound = stack.getTagCompound();

                if (tagCompound != null && tagCompound.hasKey(NBT_TAG_DISPLAY)) {
                    NBTTagCompound display = tagCompound.getCompoundTag(NBT_TAG_DISPLAY);

                    return display.hasKey(NBT_TAG_COLOR);
                }
            }
        }

        return false;
    }

    /**
     * Gets the color of the item stack
     *
     * @param stack The item stack
     * @return The integer value of the color
     */
    public int getColor(@Nonnull ItemStack stack) {
        if (!this.getMaterial().getIsDyeable() || stack.isEmpty()) {
            return -1;
        }

        NBTTagCompound tagCompound = stack.getTagCompound();

        if (tagCompound != null) {
            NBTTagCompound display = tagCompound.getCompoundTag(NBT_TAG_DISPLAY);
            if (display.hasKey(NBT_TAG_COLOR)) {
                return display.getInteger(NBT_TAG_COLOR);
            }
        }

        return this.getMaterial().getDefaultColor();
    }

    /**
     * Sets the color of the item stack
     *
     * @param stack The item stack
     * @param color The integer value of the color
     */
    public void setColor(@Nonnull ItemStack stack, int color) {
        if (!this.material.getIsDyeable()) {
            throw new UnsupportedOperationException("Wolf armor material is not dyeable!");
        }
        else if(!stack.isEmpty()) {
            NBTTagCompound tagCompound = stack.getTagCompound();

            if (tagCompound == null) {
                tagCompound = new NBTTagCompound();
                stack.setTagCompound(tagCompound);
            }

            NBTTagCompound display = tagCompound.getCompoundTag(NBT_TAG_DISPLAY);

            if (!tagCompound.hasKey(NBT_TAG_DISPLAY)) {
                tagCompound.setTag(NBT_TAG_DISPLAY, display);
            }

            display.setInteger(NBT_TAG_COLOR, color);
        }
    }

    /**
     * Gets the armor material
     *
     * @return The armor material
     */
    public WolfArmorMaterial getMaterial() {
        return this.material;
    }

    /**
     * Gets the damage reduction amount.
     *
     * @return The damage reduction amount
     */
    @Deprecated
    public double getDamageReductionAmount() {
        return getMaterial().getDamageReductionAmount();
    }

    //endregion Accessors / Mutators

    //region Nested Classes

    /**
     * Wolf armor materials
     */
    public enum WolfArmorMaterial {
        //region Fields

        CLOTH("leather", 80, 6, 15, true, 0xA06540, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, true),
        CHAINMAIL("chain", 240, 12, 12, false, 0xFFFFFF, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, false),
        IRON("iron", 240, 15, 9, false, 0xFFFFFF, SoundEvents.ITEM_ARMOR_EQUIP_IRON, false),
        GOLD("gold", 112, 12, 25, false, 0xFFFFFF, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, false),
        DIAMOND("diamond", 528, 20, 10, false, 0xFFFFFF, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, false);

        private final String name;
        private final int defaultColor;
        private final int durability;
        private final double damageReductionAmount;
        private final int enchantability;
        private final boolean isDyeable;
        private final boolean hasOverlay;
        private final SoundEvent equipSound;
        private final AttributeModifier armorAttribute;

        //endregion Fields

        //region Constructors

        WolfArmorMaterial(@Nonnull String name,
                          int durability,
                          double damageReductionAmount,
                          int enchantability,
                          boolean isDyeable,
                          int defaultColor,
                          @Nonnull SoundEvent equipSound,
                          boolean hasOverlay) {
            this.name = name;
            this.durability = durability;
            this.damageReductionAmount = damageReductionAmount;
            this.enchantability = enchantability;
            this.isDyeable = isDyeable;
            this.defaultColor = defaultColor;
            this.equipSound = equipSound;
            this.hasOverlay = hasOverlay;
            this.armorAttribute = new AttributeModifier(name, damageReductionAmount, 0);
        }

        //endregion Constructors

        //region Accessors

        public int getDefaultColor() {
            return this.defaultColor;
        }

        @Nonnull
        public SoundEvent getEquipSound() {
            return equipSound;
        }

        /**
         * Gets the armor attribute modifier.
         * @return The armor attribute modifier.
         */
        public AttributeModifier getArmorAttribute() {
            return armorAttribute;
        }

        /**
         * Gets the armor durability
         *
         * @return The durability
         */
        public int getDurability() {
            return this.durability;
        }

        /**
         * Gets the armor damage reduction amount
         *
         * @return The damage reduction amount
         */
        @Deprecated
        public double getDamageReductionAmount() {
            return this.damageReductionAmount;
        }

        /**
         * Gets the material's enchantability
         *
         * @return how enchantable the material is
         */
        public int getEnchantability() {
            return this.enchantability;
        }

        /**
         * Whether or not this armor can be dyed
         *
         * @return True if the armor type is dyable, false if not.
         */
        public boolean getIsDyeable() {
            return this.isDyeable;
        }

        /**
         * Whether or not the armor type has an overlay
         *
         * @return True if the armor type has an overlay, false if not
         */
        public boolean getHasOverlay() {
            return this.hasOverlay;
        }

        /**
         * Gets the armor type name.
         * Client-side only.
         *
         * @return The armor type name
         */
        @SideOnly(Side.CLIENT)
        @Nonnull
        public String getName() {
            return this.name;
        }

        /**
         * Gets the maximum armor value for the armor material.
         *
         * @return The maximum armor value for the armor material.
         */
        public static double getMaxArmorValue() {
            double damageReduce = 0;

            for (WolfArmorMaterial wolfArmorMaterial : values()) {
                damageReduce = Math.max(damageReduce, wolfArmorMaterial.damageReductionAmount);
            }

            return damageReduce;
        }

        /**
         * Gets the armor repair item
         *
         * @return The armor repair item.
         */
        @Nullable
        public Item getRepairItem() {
            switch (this) {
                case CLOTH:
                    return Items.LEATHER;
                case CHAINMAIL:
                case IRON:
                    return Items.IRON_INGOT;
                case GOLD:
                    return Items.GOLD_INGOT;
                case DIAMOND:
                    return Items.DIAMOND;
            }

            return null;
        }

        //endregion Accessors
    }

    //endregion Nested Classes
}
