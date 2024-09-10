package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.menu.machines.LatexPurifierMenu;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;
import net.zaharenko424.a_changed.recipe.SingleInputRecipeWrapper;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LatexPurifierEntity extends AbstractMachineEntity<ItemStackHandler, ExtendedEnergyStorage> {

    private final RangedWrapper in = new RangedWrapper(inventory, 0, 2);
    private final RangedWrapper out = new RangedWrapper(inventory, 2, 3);
    private int progress;
    private int energyConsumption;
    private int recipeProcessingTime;
    private RecipeHolder<LatexPurifierRecipe> currentRecipe;

    public LatexPurifierEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LATEX_PURIFIER_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    ItemStackHandler initInv() {
        return new ItemStackHandler(3){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch(slot){
                    case 0 -> checkItemEnergyCap(stack);
                    case 1 -> stack.is(ItemRegistry.DARK_LATEX_ITEM.get()) || stack.is(ItemRegistry.WHITE_LATEX_ITEM.get());
                    default -> false;
                };
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    ExtendedEnergyStorage initEnergy() {
        return new ExtendedEnergyStorage(25000, 256, 0);
    }

    public int getProgress(){
        return progress;
    }

    public int getRecipeProcessingTime() {
        return recipeProcessingTime;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new LatexPurifierMenu(pContainerId, playerInventory, this);
    }

    public void tick(){
        boolean changed = false;

        if(!inventory.getStackInSlot(0).isEmpty()){
            changed = energyStorage.receiveEnergyFrom(inventory.getStackInSlot(0).getCapability(Capabilities.EnergyStorage.ITEM),
                    energyStorage.getMaxReceive(), false) != 0;
        }

        if(getEnergy() < energyConsumption){
            setActive(false);
            if(changed) update();
            return;
        }

        if(currentRecipe != null && !currentRecipe.value().matches(container, level)){
            setActive(false);
            if(changed) update();
            return;
        }

        RegistryAccess access = level.registryAccess();

        if(currentRecipe == null) {
            Optional<RecipeHolder<LatexPurifierRecipe>> recipe = getRecipe();
            if (recipe.isEmpty() || !inventory.insertItem(2, recipe.get().value().getResultItem(access), true).isEmpty()) {
                setActive(false);
                return;
            }

            currentRecipe = recipe.get();
            energyConsumption = currentRecipe.value().getEnergyConsumption();
            recipeProcessingTime = currentRecipe.value().getProcessingTime();
            setActive(true);
        }

        energyStorage.addEnergy(-energyConsumption);

        if(progress < recipeProcessingTime) {
            progress++;
        } else {
            inventory.insertItem(2, currentRecipe.value().assemble(container, access), false);
            progress = 0;
        }

        update();
    }

    @Override
    protected void setActive(boolean active) {
        if(!active){
            progress = 0;
            currentRecipe = null;
        }
        super.setActive(active);
    }

    private final SingleInputRecipeWrapper container = new SingleInputRecipeWrapper(inventory, 1);

    private @NotNull Optional<RecipeHolder<LatexPurifierRecipe>> getRecipe(){
        return level.getRecipeManager().getRecipeFor(RecipeRegistry.LATEX_PURIFIER_RECIPE.get(), container, level);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.latex_purifier");
    }

    @Override
    protected <CT> CT getItemCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        return (CT) (side == Direction.DOWN ? out : in);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        progress = tag.getInt("progress");
        if(progress > 0){
            energyConsumption = tag.getInt("energyConsumption");
            recipeProcessingTime = tag.getInt("recipeProcessingTime");
        }
    }

    @Override
    void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.save(tag, lookup);
        if(progress > 0) {
            tag.putInt("progress", progress);
            tag.putInt("energyConsumption", energyConsumption);
            tag.putInt("recipeProcessingTime", recipeProcessingTime);
        }
    }
}