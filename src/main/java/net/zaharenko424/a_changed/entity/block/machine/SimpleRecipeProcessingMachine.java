package net.zaharenko424.a_changed.entity.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.recipe.MachineRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class SimpleRecipeProcessingMachine<IT extends ItemStackHandler, ET extends ExtendedEnergyStorage, R extends MachineRecipe<?>> extends ProcessingMachine<IT, ET> {

    protected final DeferredHolder<RecipeType<?>, RecipeType<R>> recipeType;
    protected RecipeHolder<R> currentRecipe;

    public SimpleRecipeProcessingMachine(BlockEntityType<?> type, BlockPos pos, BlockState state, DeferredHolder<RecipeType<?>, RecipeType<R>> recipeType) {
        super(type, pos, state);
        this.recipeType = recipeType;
    }

    public SimpleRecipeProcessingMachine(BlockEntityType<?> type, BlockPos pos, BlockState state, int pullEnergyFromSlot, DeferredHolder<RecipeType<?>, RecipeType<R>> recipeType) {
        super(type, pos, state, pullEnergyFromSlot);
        this.recipeType = recipeType;
    }

    @Override
    public boolean hasRecipe() {
        return currentRecipe != null;
    }

    @Override
    protected void machineTick() {
        if(hasRecipe() && getEnergy() < energyConsumption){
            setActive(false);
            awaitEnergyChanges();
            return;
        }

        RegistryAccess access = level.registryAccess();
        if(!hasRecipe()){
            Optional<RecipeHolder<R>> recipe = getRecipe();
            if (recipe.isEmpty() || !outputResult(recipe.get().value(), access, true)) {
                setActive(false);
                awaitInventoryChanges();
                return;
            }

            currentRecipe = recipe.get();
            consumeInput(access);//Consume item
            energyConsumption = currentRecipe.value().getEnergyConsumption();
            recipeProcessingTime = currentRecipe.value().getProcessingTime();
            setActive(true);
            changeCounter++;
            return;
        }

        energyStorage.consumeEnergy(energyConsumption);

        onProcessingTick();

        if(progress < recipeProcessingTime){
            progress++;
        } else {
            outputResult(currentRecipe.value(), access, false);
            progress = 0;
            currentRecipe = null;
        }

        setActive(true);
        changeCounter++;
    }

    protected void onProcessingTick(){}

    protected abstract Optional<RecipeHolder<R>> getRecipe();

    protected abstract void consumeInput(RegistryAccess access);

    protected abstract boolean outputResult(R recipe, RegistryAccess access, boolean simulate);

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        currentRecipe = tag.contains("recipe") ? level.getRecipeManager().byKeyTyped(recipeType.get(), ResourceLocation.parse(tag.getString("recipe"))) : null;
    }

    @Override
    void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.save(tag, lookup);
        if(currentRecipe != null) tag.putString("recipe", currentRecipe.id().toString());
    }
}
