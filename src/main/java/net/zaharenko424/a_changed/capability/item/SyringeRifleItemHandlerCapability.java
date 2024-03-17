package net.zaharenko424.a_changed.capability.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.item.CompressedAirCanister;
import net.zaharenko424.a_changed.item.LatexSyringeItem;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SyringeRifleItemHandlerCapability {

    public static class PneumaticSyringeRifleItemHandler extends ItemStackHandler {

        public PneumaticSyringeRifleItemHandler(){
            super(9);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            Item item = stack.getItem();
            return slot == 0 ? item instanceof CompressedAirCanister || stack.is(ItemRegistry.EMPTY_CANISTER.get())
                    : item instanceof LatexSyringeItem;
        }
    }

    public static class SyringeCoilGunItemHandler extends ItemStackHandler {

        public SyringeCoilGunItemHandler(){
            super(4);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof LatexSyringeItem;
        }
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, PneumaticSyringeRifleItemHandler> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {}

        @Override
        public @NotNull PneumaticSyringeRifleItemHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag) {
            PneumaticSyringeRifleItemHandler handler = new PneumaticSyringeRifleItemHandler();
            handler.deserializeNBT(tag);
            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull PneumaticSyringeRifleItemHandler pneumaticSyringeRifleItemHandler) {
            return pneumaticSyringeRifleItemHandler.serializeNBT();
        }
    }

    public static class Serializer1 implements IAttachmentSerializer<CompoundTag, SyringeCoilGunItemHandler> {

        public static final Serializer1 INSTANCE = new Serializer1();

        private Serializer1() {}

        @Override
        public @NotNull SyringeCoilGunItemHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag) {
            SyringeCoilGunItemHandler handler = new SyringeCoilGunItemHandler();
            handler.deserializeNBT(tag);
            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull SyringeCoilGunItemHandler syringeCoilGunItemHandler) {
            return syringeCoilGunItemHandler.serializeNBT();
        }
    }
}