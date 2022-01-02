package dev.satyrn.wolfarmor.api.util;

import dev.satyrn.wolfarmor.api.item.IItemWolfArmor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Resources.MOD_ID)
public abstract class Items
{
    /**
     * The leather wolf armor item.
     */
    @ObjectHolder("leather_wolf_armor")
    public static final Item LEATHER_WOLF_ARMOR = null;

    /**
     * The chainmail wolf armor item.
     */
    @ObjectHolder("chainmail_wolf_armor")
    public static final Item CHAINMAIL_WOLF_ARMOR = null;

    /**
     * The iron wolf armor item.
     */
    @ObjectHolder("iron_wolf_armor")
    public static final Item IRON_WOLF_ARMOR = null;

    /**
     * The gold wolf armor item.
     */
    @ObjectHolder("gold_wolf_armor")
    public static final Item GOLD_WOLF_ARMOR = null;

    /**
     * The diamond wolf armor item.
     */
    @ObjectHolder("diamond_wolf_armor")
    public static final Item DIAMOND_WOLF_ARMOR = null;

    /**
     * Checks if <tt>armorItemStack</tt> is a valid wolf armor item stack.
     * @param armorItemStack The armor item to check
     * @return <tt>true</tt> if it is a valid wolf armor item
     */
    //todo: API Implementation
    public static boolean isValidWolfArmor(@Nonnull ItemStack armorItemStack) {
        return armorItemStack.isEmpty() || isValidWolfArmor(armorItemStack.getItem());
    }

    /**
     * Checks if <tt>armorItem</tt> is a valid wolf armor item.
     * @param armorItem The armor item to check.
     * @return <tt>true</tt> if it is a valid wolf armor item.
     */
    public static boolean isValidWolfArmor(@Nullable Item armorItem) {
        return armorItem instanceof IItemWolfArmor;
    }
}
