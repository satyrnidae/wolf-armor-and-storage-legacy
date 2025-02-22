package dev.satyrn.wolfarmor.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class WolfArmorItem extends Item implements Wearable {
    public static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("5490096a-4d96-49a6-aeb7-1b54a86d93a5");

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        @NotNull
        protected ItemStack execute(@NotNull BlockSource blockSource, @NotNull ItemStack itemStack) {
            return WolfArmorItem.dispenseArmor(blockSource, itemStack)
                    ? itemStack
                    : super.execute(blockSource, itemStack);
        }
    };

    private final ArmorMaterial material;
    private final int defense;
    private final float toughness;

    protected final Multimap<Attribute, AttributeModifier> armorModifiers;

    public static boolean dispenseArmor(@NotNull BlockSource blockSource, @NotNull ItemStack itemStack) {
        var blockPos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
        var entities = blockSource.getLevel()
                .getEntitiesOfClass(LivingEntity.class, new AABB(blockPos),
                        EntitySelector.NO_SPECTATORS.and(new EntitySelector.MobCanWearArmorEntitySelector(itemStack)));

        if (!entities.isEmpty() && entities.get(0) instanceof Wolf theWolf) {
            // Wolf armor is always equipped in the chest slot
            var splitStack = itemStack.split(1);
            // TODO: Need equip armor method
            return true;
        }

        return false;
    }

    /**
     * Creates a new wolf armor item with the properties of the specified armor material.
     *
     * @param material   The armor material. This determines armor enchantability, knockback/damage resistance, toughness,
     *                   etc.
     * @param properties The item properties.
     */
    public WolfArmorItem(ArmorMaterial material, Properties properties) {
        super(properties);
        this.material = material;
        this.defense = material.getDefenseForSlot(EquipmentSlot.HEAD) +
                material.getDefenseForSlot(EquipmentSlot.CHEST) +
                material.getDefenseForSlot(EquipmentSlot.LEGS) +
                material.getDefenseForSlot(EquipmentSlot.FEET);
        this.toughness = material.getToughness() * 4;
        float knockbackResistance = material.getKnockbackResistance() * 4;

        // Armor dispense
        // TODO: Need to override or mix canTakeItem(ItemStack) on WolfMixin
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);

        // TODO: Maybe implement this in the wolf so we can apply head / chest / leg / boot enchants on all armor?
        // Build out armor attributes
        var builder = ImmutableMultimap.<Attribute, AttributeModifier>builder();
        builder.put(Attributes.ARMOR, new AttributeModifier(ARMOR_MODIFIER_UUID, "Armor modifier", this.defense,
                AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(ARMOR_MODIFIER_UUID, "Armor toughness", this.toughness,
                        AttributeModifier.Operation.ADDITION));
        if (knockbackResistance > 0.0F) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(ARMOR_MODIFIER_UUID, "Armor knockback resistance", knockbackResistance,
                            AttributeModifier.Operation.ADDITION));
        }

        this.armorModifiers = builder.build();
    }

    /**
     * Gets the enchantability of the wolf armor from its material.
     *
     * @return The enchantability of the wolf armor.
     */
    @Override
    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    /**
     * Getter for the wolf armor's material.
     *
     * @return The material of the wolf armor.
     */
    public ArmorMaterial getMaterial() {
        return this.material;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stackToRepair, @NotNull ItemStack repairIngredient) {
        return this.material.getRepairIngredient().test(repairIngredient) ||
                super.isValidRepairItem(stackToRepair, repairIngredient);
    }

    public final @NotNull ImmutableMultimap<Attribute, AttributeModifier> getArmorModifiers() {
        return ImmutableMultimap.copyOf(this.armorModifiers);
    }

    @Nullable
    @Override
    public SoundEvent getEquipSound() {
        return this.getMaterial().getEquipSound();
    }

    @Override
    public void appendHoverText(ItemStack stack,
                                @Nullable Level level,
                                List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        // TODO: Additional tooltip data :)
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        if (!this.armorModifiers.isEmpty()) {
            tooltipComponents.add(CommonComponents.EMPTY);
            tooltipComponents.add(Component.translatable("item.modifiers.wolfarmor").withStyle(ChatFormatting.GRAY));

            for (final @NotNull var entry : this.getArmorModifiers().entries()) {
                final @NotNull var modifier = entry.getValue();
                double amount = modifier.getAmount();
                if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                    amount *= 10.0;
                }

                if (amount > 0) {
                    tooltipComponents.add(Component.literal(" ")
                            .append(Component.translatable(
                                    "attribute.modifier.plus." + modifier.getOperation().toValue(),
                                    ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(amount),
                                    Component.translatable(entry.getKey().getDescriptionId())))
                            .withStyle(ChatFormatting.BLUE));
                } else if (amount < 0) {
                    amount *= -1;
                    tooltipComponents.add(Component.literal(" ")
                            .append(Component.translatable(
                                    "attribute.modifier.plus." + modifier.getOperation().toValue(),
                                    ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(amount),
                                    Component.translatable(entry.getKey().getDescriptionId())))
                            .withStyle(ChatFormatting.RED));
                }
            }
        }
    }
}
