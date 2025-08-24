package net.zaharenko424.a_changed.entity.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.menu.machine.LatexPurifierMenu;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;
import net.zaharenko424.a_changed.recipe.SingleInputRecipeWrapper;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LatexPurifierEntity extends SimpleRecipeProcessingMachine<ItemStackHandler, ExtendedEnergyStorage, LatexPurifierRecipe> {

    private final RangedWrapper in = new RangedWrapper(inventory, 0, 2);
    private final RangedWrapper out = new RangedWrapper(inventory, 2, 3);

    public LatexPurifierEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LATEX_PURIFIER_ENTITY.get(), pos, state, 1, RecipeRegistry.LATEX_PURIFIER_RECIPE);
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
        return new ExtendedEnergyStorage(25000, 256, 0){
            @Override
            public void onEnergyChanged() {
                energyLevelChanged();
            }
        };
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new LatexPurifierMenu(pContainerId, playerInventory, this);
    }

    @Override
    protected Optional<RecipeHolder<LatexPurifierRecipe>> getRecipe() {
        return level.getRecipeManager().getRecipeFor(RecipeRegistry.LATEX_PURIFIER_RECIPE.get(), container, level);
    }

    @Override
    protected void consumeInput(RegistryAccess access) {
        currentRecipe.value().assemble(container, access);
    }

    @Override
    protected boolean outputResult(LatexPurifierRecipe recipe, RegistryAccess access, boolean simulate) {
        return inventory.insertItem(2, recipe.getResultItem(access), simulate).isEmpty();
    }

    private final SingleInputRecipeWrapper container = new SingleInputRecipeWrapper(inventory, 0);

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.latex_purifier");
    }

    @Override
    protected <CT> CT getItemCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        return (CT) (side == Direction.DOWN ? out : in);
    }
}