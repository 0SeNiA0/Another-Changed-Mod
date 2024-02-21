package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.menu.PneumaticSyringeRifleMenu;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PneumaticSyringeRifle extends AbstractSyringeRifle {

    public PneumaticSyringeRifle() {
        super(new Properties());
    }

    @Override
    void consumeFuel(@NotNull IItemHandler handler) {
        handler.insertItem(0,
                CompressedAirCanister.consumeAir(handler.extractItem(0, 1, false)), false);
    }

    @Override
    void playSound(@NotNull Level level, @NotNull Player player) {
        level.playSound(null, player.blockPosition(), SoundRegistry.PNEUMATIC_RIFLE.get(), SoundSource.PLAYERS, .6f, .6f);
    }

    @Override
    void appendHoverText(@NotNull ItemStack fuel, @NotNull List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.a_changed.syringe_rifle_air",
                fuel.isEmpty() || !(fuel.getItem() instanceof CompressedAirCanister) ? 0
                        : fuel.getMaxDamage() - fuel.getDamageValue()).withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new PneumaticSyringeRifleMenu(pContainerId, pPlayerInventory, pPlayer.getItemInHand(InteractionHand.MAIN_HAND));
    }
}