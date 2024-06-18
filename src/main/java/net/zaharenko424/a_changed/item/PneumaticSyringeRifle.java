package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.menu.PneumaticSyringeRifleMenu;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PneumaticSyringeRifle extends AbstractSyringeRifle {

    public PneumaticSyringeRifle() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    @Override
    public boolean hasAmmo(@NotNull IItemHandler handler) {
        for(int i = 1; i < 9; i++){
            if(!handler.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    @Override
    TransfurType useFirst(@NotNull IItemHandler handler, boolean simulate) {
        for(int i = 1; i < 9; i++){
            if(!handler.getStackInSlot(i).isEmpty()) return TransfurManager.getTransfurType(
                    Objects.requireNonNull(LatexSyringeItem.decodeTransfur(handler.extractItem(i, 1, simulate))));
        }
        return null;
    }

    @Override
    boolean hasFuel(@NotNull ItemStack rifle, @NotNull IItemHandler inventory) {
        ItemStack canister = inventory.getStackInSlot(0);
        return !canister.isEmpty() && canister.is(ItemRegistry.COMPRESSED_AIR_CANISTER);
    }

    @Override
    void consumeFuel(ItemStack rifle, @NotNull IItemHandler handler) {
        handler.insertItem(0,
                CompressedAirCanister.consumeAir(handler.extractItem(0, 1, false)), false);
    }

    @Override
    int velocity() {
        return 3;
    }

    @Override
    float accuracy() {
        return 1.2f;
    }

    @Override
    void playSound(@NotNull Level level, @NotNull Player player) {
        level.playSound(null, player.blockPosition(), SoundRegistry.PNEUMATIC_RIFLE.get(), SoundSource.PLAYERS, .6f, .6f);
    }

    @Override
    int cooldown() {
        return 20;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel, @NotNull List<Component> tooltip, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
        IItemHandler inventory = stack.getCapability(Capabilities.ItemHandler.ITEM);
        ItemStack canister = inventory.getStackInSlot(0);

        tooltip.add(Component.translatable("tooltip.a_changed.syringe_rifle_air",
                canister.isEmpty() || !(canister.getItem() instanceof CompressedAirCanister) ? 0
                        : canister.getMaxDamage() - canister.getDamageValue()).withStyle(ChatFormatting.GRAY));

        int count = 0;
        for(int i = 1; i < 9; i++){
            if(!inventory.getStackInSlot(i).isEmpty()) count++;
        }
        tooltip.add(Component.translatable("tooltip.a_changed.syringe_rifle_shots", count).withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new PneumaticSyringeRifleMenu(pContainerId, pPlayerInventory, pPlayer.getMainHandItem());
    }
}