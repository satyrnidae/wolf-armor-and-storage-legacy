package dev.satyrn.wolfarmor.mixins.entity;

import dev.satyrn.wolfarmor.api.entity.ExtendedWolf;
import dev.satyrn.wolfarmor.item.WolfArmorItem;
import dev.satyrn.wolfarmor.tags.WolfArmorTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Awooooooo!
 * Mixes in all new wolf functionality to the base entity class.
 *
 * @author isabel
 * @since 4.0.0
 */
@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements NeutralMob, ExtendedWolf {
    // Wolf equipment slots:
    // - Main hand: Jaws / mouth slot
    // - Offhand: Collar charm for misc items, back for shields / swords / tools / etc.
    // - Head: Armor slot
    // - Chest: Armor slot
    // - Legs: Armor slot
    // - Feet: Armor slot
    // Armor modifier should be applied in the Wolf Entity itself, like w/ horse armor, because otherwise too much armor
    //  would be applied to the wolf.

    // Holy shit, this works?
    @Unique
    private static final EntityDataAccessor<Boolean> WOLFARMOR$DATA_CHEST_ID = SynchedEntityData.defineId(WolfMixin.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<ItemStack> WOLFARMOR$DATA_CHEST_ITEM_ID = SynchedEntityData.defineId(WolfMixin.class, EntityDataSerializers.ITEM_STACK);
    @Unique
    private static final int WOLFARMOR$SLOT_ID_ARMOR = 0;
    @Unique
    private static final int WOLFARMOR$SLOT_ID_MAIN_HAND = 1;
    @Unique
    private static final int WOLFARMOR$SLOT_ID_OFF_HAND = 2;
    @Unique
    private static final int WOLFARMOR$EQUIPMENT_INVENTORY_SIZE = 3;
    @Unique
    private static final int WOLFARMOR$EQUIPMENT_SLOT_OFFSET = 400;
    @Unique
    private static final int WOLFARMOR$CHEST_SLOT_OFFSET = 499;
    @Unique
    private static final int WOLFARMOR$INVENTORY_SLOT_OFFSET = 500;

    @Unique
    private SimpleContainer wolfarmor$inventory;

    /** Ignore */
    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void wolfarmor$init(final @NotNull EntityType<? extends Wolf> entityType, final @NotNull Level level, CallbackInfo ci) {
        this.wolfarmor$createInventory();
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void wolfarmor$registerGoals(CallbackInfo ci) {
        // TODO: Wolf should sneak when owner is sneaking
    }

    // TODO: Implement wolf inventory based on standard Horse inventory.
    //    Check if there is a base interface you can implement.

    /**
     * Inject extra synced entity data into the synced data packet.
     * @param ci The method callback info.
     */
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void wolfarmor$defineSynchedData(CallbackInfo ci) {
        this.getEntityData().define(WOLFARMOR$DATA_CHEST_ID, false);
        this.getEntityData().define(WOLFARMOR$DATA_CHEST_ITEM_ID, ItemStack.EMPTY);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void wolfarmor$addAdditionalSaveData(final @NotNull CompoundTag compound, final @NotNull CallbackInfo ci) {
        compound.putBoolean("HasChest", this.wolfarmor$getHasChest());
        if (this.wolfarmor$getHasChest()) {
            final @NotNull var items = new ListTag();

            for (int slotId = WOLFARMOR$EQUIPMENT_INVENTORY_SIZE; slotId < this.wolfarmor$inventory.getContainerSize(); ++slotId) {
                final @NotNull var itemInSlot = this.wolfarmor$inventory.getItem(slotId);
                if (!itemInSlot.isEmpty()) {
                    final @NotNull var itemSlot = new CompoundTag();
                    itemSlot.putByte("Slot", (byte) slotId);
                    itemInSlot.save(itemSlot);
                    items.add(itemSlot);
                }
            }

            compound.put("Items", items);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void wolfarmor$readAdditionalSaveData(final @NotNull CompoundTag compound, final @NotNull CallbackInfo ci) {
        this.wolfarmor$setHasChest(compound.getBoolean("HasChest"));
        this.wolfarmor$createInventory();
        if (this.wolfarmor$getHasChest()) {
            final @NotNull var items = compound.getList("Items", 10);

            for (int listIndex = 0; listIndex < items.size(); ++listIndex) {
                final @NotNull var itemSlot = items.getCompound(listIndex);
                int slotId = itemSlot.getByte("Slot") & 255;
                if (slotId >= WOLFARMOR$EQUIPMENT_INVENTORY_SIZE && slotId < this.wolfarmor$inventory.getContainerSize()) {
                    this.wolfarmor$inventory.setItem(slotId, ItemStack.of(itemSlot));
                }
            }
        }

        this.wolfarmor$updateContainerEquipment();
    }

    @Inject(method = "getAmbientSound", at = @At("HEAD"))
    private void wolfarmor$getAmbientSound(final @NotNull CallbackInfoReturnable<SoundEvent> cir) {
        // TODO: Squeaky noise
        // TODO: Moon howl
        // TODO: No noise while sneaky
        // TODO: Wolves whine when hungry
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void wolfarmor$mobInteract(final @NotNull Player player, final @NotNull InteractionHand hand, CallbackInfoReturnable<@NotNull InteractionResult> cir) {
        // TODO: Wolf chest interaction
        // TODO: Play chest equip sound on chest equipped
        // TODO: Wolf armor interaction
        // TODO: Play armor equip sound on armor equipped
        // TODO: Give wolf item interaction
    }

    // TODO: Implement wolf hunger
    // TODO: Implement wolf automatic eating when "hand" is free
    // TODO: When a wolf is eating, reduce their walk speed. If they are leashed and get too far away, stop eating.
    @Override
    protected void onLeashDistance(float distance) {
        super.onLeashDistance(distance);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.wolfarmor$inventory != null) {
            for (int slotId = 0; slotId < this.wolfarmor$inventory.getContainerSize(); ++slotId) {
                final @NotNull ItemStack itemInSlot = this.wolfarmor$inventory.getItem(slotId);
                if (!itemInSlot.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemInSlot)) {
                    this.spawnAtLocation(itemInSlot);
                }
            }
        }
        if (this.wolfarmor$getHasChest()) {
            if (!this.level.isClientSide) {
                final @NotNull var chestItem = this.wolfarmor$getChestItem();
                if (!chestItem.isEmpty()) {
                    this.spawnAtLocation(chestItem);
                } else {
                    this.spawnAtLocation(Blocks.CHEST);
                }
            }
            this.wolfarmor$setHasChest(false);
        }
    }

    @Override
    public void containerChanged(Container container) {
        this.wolfarmor$updateContainerEquipment();
        // TODO: Play armored sound
    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        // TODO: Implement wolf UI on interaction
        // TODO: Open wolf inventory
    }

    @Override
    public boolean canTakeItem(ItemStack stack) {
        return this.wolfarmor$isArmor(stack);
    }

    @Override
    public @NotNull SlotAccess getSlot(final int slot) {
        if (slot == WOLFARMOR$CHEST_SLOT_OFFSET) {
            return new SlotAccess() {
                @Override
                public @NotNull ItemStack get() {
                    return WolfMixin.this.wolfarmor$getHasChest()
                            ? WolfMixin.this.wolfarmor$getChestItem()
                            : ItemStack.EMPTY;
                }

                @Override
                public boolean set(final @NotNull ItemStack carried) {
                    if (carried.isEmpty()) {
                        if (WolfMixin.this.wolfarmor$getHasChest()) {
                            WolfMixin.this.wolfarmor$setHasChest(false);
                            WolfMixin.this.wolfarmor$setChestItem(ItemStack.EMPTY);
                            WolfMixin.this.wolfarmor$createInventory();
                        }
                        return true;
                    }

                    if (carried.is(WolfArmorTags.WOLF_CHESTS)) {
                        if (!WolfMixin.this.wolfarmor$getHasChest()) {
                            WolfMixin.this.wolfarmor$setHasChest(true);
                            WolfMixin.this.wolfarmor$setChestItem(carried);
                            WolfMixin.this.wolfarmor$createInventory();
                        }
                        return true;
                    }

                    return false;
                }
            };
        }

        int equipmentSlot = slot - WOLFARMOR$EQUIPMENT_SLOT_OFFSET; // TODO: How does slot indexing work in this method?
        if (equipmentSlot >= 0 &&
                equipmentSlot < WOLFARMOR$EQUIPMENT_INVENTORY_SIZE &&
                equipmentSlot < this.wolfarmor$inventory.getContainerSize()) {
            if (equipmentSlot == WOLFARMOR$SLOT_ID_ARMOR) { //Wolf armor slot
                return new SlotAccess() {
                    @Override
                    public @NotNull ItemStack get() {
                        return WolfMixin.this.wolfarmor$inventory.getItem(slot);
                    }

                    @Override
                    public boolean set(final @NotNull ItemStack carried) {
                        if (carried.isEmpty() || WolfMixin.this.wolfarmor$isArmor(carried)) {
                            return false;
                        }
                        WolfMixin.this.wolfarmor$inventory.setItem(slot, carried);
                        WolfMixin.this.wolfarmor$updateContainerEquipment();
                        return true;
                    }
                };
            }

            return SlotAccess.forContainer(this.wolfarmor$inventory, equipmentSlot);
        }

        int chestSlot = slot - WOLFARMOR$INVENTORY_SLOT_OFFSET + WOLFARMOR$EQUIPMENT_INVENTORY_SIZE;
        return chestSlot >= WOLFARMOR$EQUIPMENT_INVENTORY_SIZE &&
                chestSlot < this.wolfarmor$inventory.getContainerSize() ? SlotAccess.forContainer(
                this.wolfarmor$inventory, chestSlot) : super.getSlot(slot);
    }



    // TODO: Accept any #forge:chests/wooden items as chests (see https://forge.gemwire.uk/wiki/Tags)

    // TODO: Wolf ender chest, must accept #forge:chests/ender

    // TODO: Drop inventory, chest, and armor

    // TODO: Armor damage

    // TODO: Wolf potion effect fix for hunger

    // TODO: Wolf held items (replaces fire aspect / etc. on wolf armor)

    // TODO: Implement mending for wolf armor

    // TODO: Alter NBT format to include wolf inventory

    // TODO: Ensure wolf interact is HIGH PRIORITY, and will be processed *before* other mixins

    // TODO: We probably don't need legacy data fix anymore

    // TODO: Consider backporting wolf variants? Maybe, maybe not.

    @Unique
    public int wolfarmor$getInventorySize() {
        return WOLFARMOR$EQUIPMENT_INVENTORY_SIZE + (this.wolfarmor$getHasChest() ? 15 : 0); // TODO: Make wolf chest size configurable
    }

    @Unique
    private void wolfarmor$createInventory() {
        final @Nullable var previousContainer = this.wolfarmor$inventory;
        this.wolfarmor$inventory = new SimpleContainer(this.wolfarmor$getInventorySize());
        if (previousContainer != null) {
            previousContainer.removeListener(this);
            int minimumSize = Math.min(previousContainer.getContainerSize(), this.wolfarmor$inventory.getContainerSize());

            for (int slotId = 0; slotId < minimumSize; ++slotId) {
                final @NotNull ItemStack itemInSlot = previousContainer.getItem(slotId);
                if (!itemInSlot.isEmpty()) {
                    this.wolfarmor$inventory.setItem(slotId, itemInSlot.copy());
                }
            }
        }

        this.wolfarmor$inventory.addListener(this);
        this.wolfarmor$updateContainerEquipment();
    }

    @Unique
    private void wolfarmor$updateContainerEquipment() {
        // TODO: Add armor, hand items to equipment slots

    }

    @Unique
    @Override
    public boolean wolfarmor$isArmor(final @NotNull ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof WolfArmorItem;
    }

    @Unique
    @Override
    public boolean wolfarmor$hasInventoryChanged(final @NotNull Container container) {
        return this.wolfarmor$inventory != container;
    }

    @Unique
    @Override
    public boolean wolfarmor$getHasChest() {
        return this.entityData.get(WOLFARMOR$DATA_CHEST_ID);
    }

    @Unique
    @Override
    public void wolfarmor$setHasChest(boolean value) {
        this.entityData.set(WOLFARMOR$DATA_CHEST_ID, value);
    }

    @Unique
    @Override
    public @NotNull ItemStack wolfarmor$getChestItem() {
        return this.entityData.get(WOLFARMOR$DATA_CHEST_ITEM_ID);
    }

    @Unique
    @Override
    public void wolfarmor$setChestItem(final @NotNull ItemStack itemStack) {
        this.entityData.set(WOLFARMOR$DATA_CHEST_ITEM_ID, itemStack);
    }

    @Unique
    private void wolfarmor$playChestEquipsSound() {
        // TODO: Custom sound event for proper subtitles
        // TODO: Should be called from wolf interaction
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    @Unique
    @Override
    public @NotNull ItemStack wolfarmor$getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }

    @Unique
    @Override
    public void wolfarmor$setArmor(final @NotNull ItemStack itemStack) {
        this.setItemSlot(EquipmentSlot.HEAD, itemStack);
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setItemSlot(EquipmentSlot.CHEST, itemStack);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setItemSlot(EquipmentSlot.LEGS, itemStack);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setItemSlot(EquipmentSlot.FEET, itemStack);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
    }
}
