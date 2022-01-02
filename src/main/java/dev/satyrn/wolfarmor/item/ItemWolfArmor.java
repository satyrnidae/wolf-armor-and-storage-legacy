package dev.satyrn.wolfarmor.item;

import com.google.common.collect.Multimap;
import dev.satyrn.wolfarmor.api.entity.passive.IArmoredWolf;
import dev.satyrn.wolfarmor.api.item.IItemWolfArmor;
import dev.satyrn.wolfarmor.api.item.IWolfArmorMaterial;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemWolfArmor extends Item implements IItemWolfArmor {

    private static final String NBT_TAG_DISPLAY = "Display";
    private static final String NBT_TAG_COLOR = "Color";
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
            ItemStack itemStack = dispenseWolfArmor(source, stack);
            return !itemStack.isEmpty() ? itemStack : super.dispenseStack(source, stack);
        }
    };
    private final IWolfArmorMaterial material;

    public ItemWolfArmor(IWolfArmorMaterial material) {
        super();

        this.material = material;
        this.maxStackSize = 1;
        this.canRepair = true;

        this.setMaxDamage(material.getDurability());
        this.setCreativeTab(CreativeTabs.MISC);

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSE_ITEM);
    }

    /**
     * Removes color data from the given stack
     *
     * @param stack The item stack
     */
    public void removeColor(@Nonnull ItemStack stack) {
        if(!stack.isEmpty() && this.getMaterial().getCanBeDyed()) {
            if(this.getHasColor(stack)) {
                NBTTagCompound stackCompound = stack.getTagCompound();
                if (stackCompound != null) {
                    stackCompound.getCompoundTag(NBT_TAG_DISPLAY).removeTag(NBT_TAG_COLOR);
                }
            }
        }
    }

    /**
     * Gets the armor material
     *
     * @return The armor material
     */
    public IWolfArmorMaterial getMaterial() {
        return this.material;
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
        if(!stack.isEmpty() && this.getMaterial().getCanBeDyed()) {
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
        if (!this.getMaterial().getCanBeDyed() || stack.isEmpty()) {
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
        if (!this.getMaterial().getCanBeDyed()) {
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
     * Gets the damage reduction amount.
     *
     * @return The damage reduction amount
     */
    @Deprecated
    public double getDamageReductionAmount() {
        return getMaterial().getDamageReductionAmount();
    }

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
                IArmoredWolf wolfArmor = (IArmoredWolf)wolf;
                if (!wolfArmor.canEquipItem(copyStack)) {
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

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack, Enchantment enchantment) {
        if(enchantment == Enchantments.SWEEPING) return false;

        return enchantment.type == EnumEnchantmentType.BREAKABLE || enchantment.type == EnumEnchantmentType.WEAPON ||
                enchantment.type == EnumEnchantmentType.ARMOR || enchantment.type == EnumEnchantmentType.ARMOR_FEET;
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

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot, @Nonnull ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

        if(equipmentSlot == EntityEquipmentSlot.CHEST && !stack.isEmpty() && stack.getItem() instanceof ItemWolfArmor) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_UUID, "Armor modifier", this.getMaterial().getDamageReductionAmount(), 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_UUID, "Armor toughness", this.getMaterial().getToughness(), 0));
        }

        return multimap;
    }


}
