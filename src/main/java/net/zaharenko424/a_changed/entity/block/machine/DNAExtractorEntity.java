package net.zaharenko424.a_changed.entity.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.item.BloodSyringe;
import net.zaharenko424.a_changed.menu.machine.DNAExtractorMenu;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.recipe.SingleInputRecipeWrapper;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DNAExtractorEntity extends ProcessingMachine<ItemStackHandler, ExtendedEnergyStorage> {

    protected final RangedWrapper in = new RangedWrapper(inventory, 0, 2);
    protected final RangedWrapper output = new RangedWrapper(inventory, 2, 4);
    protected int rotationDeg;
    protected int rotationDegO;

    protected RecipeHolder<DNAExtractorRecipe> currentRecipe;
    protected static final byte maxParallel = 4;
    protected int parallelRecipes;

    public DNAExtractorEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.DNA_EXTRACTOR_ENTITY.get(), pos, state);
    }

    @Override
    ItemStackHandler initInv() {
        return new ItemStackHandler(4){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot == 3 && stack.is(ItemRegistry.SYRINGE_ITEM) || super.isItemValid(slot, stack);
            }

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

    public int getRot(){
        return rotationDeg;
    }

    public int getRotO(){
        return rotationDegO;
    }

    public int getParallelRecipes(){
        return parallelRecipes;
    }

    @Override
    public boolean hasRecipe() {
        return currentRecipe != null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new DNAExtractorMenu(i, inventory, this);
    }

    @Override
    protected void machineTick() {
        if(hasRecipe() && getEnergy() < energyConsumption) {
            setActive(false);
            awaitEnergyChanges();
            return;
        }

        if(!hasRecipe()){
            Optional<RecipeHolder<DNAExtractorRecipe>> recipe = getRecipe();
            if(recipe.isEmpty() || getEnergy() < recipe.get().value().getEnergyConsumption() || !resultsFit(recipe.get().value())){
                setActive(false);
                awaitInventoryChanges();
                return;
            }

            currentRecipe = recipe.get();
            for(int i = 0; i < parallelRecipes; i++){
                currentRecipe.value().assemble(container, level.registryAccess());
            }
            if(currentRecipe.value().getIngredient().getItems()[0].getItem() instanceof BloodSyringe) output.insertItem(1, ItemRegistry.SYRINGE_ITEM.toStack(parallelRecipes), false);
            energyConsumption = currentRecipe.value().getEnergyConsumption();
            recipeProcessingTime = currentRecipe.value().getProcessingTime();
            setActive(true);
            changeCounter++;
            return;
        }

        energyStorage.consumeEnergy(energyConsumption);
        rotationDeg = (rotationDeg + 20) % 360;

        if(progress < recipeProcessingTime){
            progress++;
        } else {
            ItemStack result = currentRecipe.value().getResultItem(level.registryAccess());
            result.setCount(result.getCount() * parallelRecipes);
            inventory.insertItem(2, result, false);
            progress = 0;
            currentRecipe = null;
            parallelRecipes = 0;
        }

        setActive(true);
        changeCounter++;
    }

    @Override
    protected void setActive(boolean active) {
        if(!active && currentRecipe != null){
            ItemStack waste = output.insertItem(0, ItemRegistry.BIO_WASTE.toStack(parallelRecipes), false);
            if(!waste.isEmpty()) Block.popResource(level, getBlockPos(), waste);
            currentRecipe = null;
            parallelRecipes = 0;
            enabled = false;
        }
        super.setActive(active);
    }

    private final SingleInputRecipeWrapper container = new SingleInputRecipeWrapper(inventory, 0);

    private Optional<RecipeHolder<DNAExtractorRecipe>> getRecipe(){
        return level.getRecipeManager().getRecipeFor(RecipeRegistry.DNA_EXTRACTOR_RECIPE.get(), container, level);
    }

    private boolean resultsFit(DNAExtractorRecipe recipe){
        ItemStack[] outSlots = {output.getStackInSlot(0).copy(), output.getStackInSlot(1).copy()};

        parallelRecipes = Math.min(maxParallel, inventory.getStackInSlot(0).getCount());
        ItemStack result = recipe.getResultItem(level.registryAccess());
        result.setCount(result.getCount() * parallelRecipes);

        if(!outSlots[0].isEmpty()
                && (!ItemStack.isSameItemSameComponents(result, outSlots[0]) || result.getCount() + outSlots[0].getCount() > result.getMaxStackSize())){
            return false;
        }

        return !(in.getStackInSlot(0).getItem() instanceof BloodSyringe)
                || (outSlots[1].isEmpty() || parallelRecipes + outSlots[1].getCount() <= outSlots[1].getMaxStackSize());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.dna_extractor");
    }

    @Override
    protected <CT> CT getItemCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        if(side == null) return null;
        return (CT) switch(side){
            case DOWN -> output;
            default -> in;
        };
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        int rot = rotationDeg;
        rotationDeg = tag.getInt("rotation");
        if(level != null && level.isClientSide) rotationDegO = progress > 0 ? rot : rotationDeg;

        currentRecipe = tag.contains("recipe") ? level.getRecipeManager().byKeyTyped(RecipeRegistry.DNA_EXTRACTOR_RECIPE.get(), ResourceLocation.parse(tag.getString("recipe"))) : null;
        if(currentRecipe != null) parallelRecipes = tag.getInt("parallel");
    }

    @Override
    void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.save(tag, lookup);
        tag.putInt("rotation", rotationDeg);
        if(currentRecipe != null) {
            tag.putString("recipe", currentRecipe.id().toString());
            tag.putInt("parallel", parallelRecipes);
        }
    }
}