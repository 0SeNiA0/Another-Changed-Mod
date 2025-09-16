package net.zaharenko424.a_changed.entity.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.menu.machine.CompressorMenu;
import net.zaharenko424.a_changed.recipe.CompressorRecipe;
import net.zaharenko424.a_changed.recipe.SingleInputRecipeWrapper;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CompressorEntity extends SimpleRecipeProcessingMachine<ItemStackHandler, ExtendedEnergyStorage, CompressorRecipe> {

    private final RangedWrapper in = new RangedWrapper(inventory, 0, 2);
    private final RangedWrapper out = new RangedWrapper(inventory, 2, 3);

    public CompressorEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.COMPRESSOR_ENTITY.get(), pos, state, 1, RecipeRegistry.COMPRESSOR_RECIPE);
    }

    @Override
    ItemStackHandler initInv() {
        return new ItemStackHandler(3){
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    @Override
    ExtendedEnergyStorage initEnergy() {
        return new ExtendedEnergyStorage(10000, 128, 0){
            @Override
            public void onEnergyChanged() {
                energyLevelChanged();
            }
        };
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new CompressorMenu(pContainerId, pPlayerInventory, this);
    }

    private final SingleInputRecipeWrapper container = new SingleInputRecipeWrapper(inventory, 0);

    protected @NotNull Optional<RecipeHolder<CompressorRecipe>> getRecipe(){
        return level.getRecipeManager().getRecipeFor(RecipeRegistry.COMPRESSOR_RECIPE.get(), container, level);
    }

    @Override
    protected void consumeInput(RegistryAccess access) {
        currentRecipe.value().assemble(container, access);
    }

    @Override
    protected boolean outputResult(CompressorRecipe recipe, RegistryAccess access, boolean simulate) {
        return inventory.insertItem(2, recipe.getResultItem(access), simulate).isEmpty();
    }

    @Override
    protected void onProcessingTick() {
        if(level.getGameTime() % 20 == 0) level.playSound(null, worldPosition, SoundRegistry.COMPRESSOR.get(), SoundSource.BLOCKS, .5f, 1);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.compressor");
    }

    @Override
    protected <CT> CT getItemCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        return (CT) (side == Direction.DOWN ? out : in);
    }
}