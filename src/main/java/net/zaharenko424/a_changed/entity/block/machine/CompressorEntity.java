package net.zaharenko424.a_changed.entity.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

public class CompressorEntity extends ProcessingMachine<ItemStackHandler, ExtendedEnergyStorage> {

    private final RangedWrapper in = new RangedWrapper(inventory, 0, 2);
    private final RangedWrapper out = new RangedWrapper(inventory, 2, 3);
    private RecipeHolder<CompressorRecipe> currentRecipe;

    public CompressorEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.COMPRESSOR_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    ItemStackHandler initInv() {
        return new ItemStackHandler(3){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    ExtendedEnergyStorage initEnergy() {
        return new ExtendedEnergyStorage(10000, 128, 0);
    }

    @Override
    public boolean hasRecipe() {
        return currentRecipe != null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new CompressorMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void tick() {
        consumeEnergyFrom(inventory.getStackInSlot(0));

        if(!enabled || (hasRecipe() && getEnergy() < energyConsumption)){
            setActive(false);
            updateIfChanged();
            return;
        }

        RegistryAccess access = level.registryAccess();

        if(!hasRecipe()) {
            Optional<RecipeHolder<CompressorRecipe>> recipe = getRecipe();
            if (recipe.isEmpty() || !inventory.insertItem(2, recipe.get().value().getResultItem(access), true).isEmpty()) {
                setActive(false);
                return;
            }

            currentRecipe = recipe.get();
            currentRecipe.value().assemble(container, access);//Consume item
            energyConsumption = currentRecipe.value().getEnergyConsumption();
            recipeProcessingTime = currentRecipe.value().getProcessingTime();
            setActive(true);
            update();
            return;
        }

        energyStorage.addEnergy(-energyConsumption);

        if(level.getGameTime() % 20 == 0)
            level.playSound(null, worldPosition, SoundRegistry.COMPRESSOR.get(), SoundSource.BLOCKS, .5f, 1);

        if(progress < recipeProcessingTime){
            progress++;
        } else {
            inventory.insertItem(2, currentRecipe.value().getResultItem(access), false);
            progress = 0;
            currentRecipe = null;
        }

        setActive(true);
        update();
    }

    private final SingleInputRecipeWrapper container = new SingleInputRecipeWrapper(inventory, 0);

    private @NotNull Optional<RecipeHolder<CompressorRecipe>> getRecipe(){
        return level.getRecipeManager().getRecipeFor(RecipeRegistry.COMPRESSOR_RECIPE.get(), container, level);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.compressor");
    }

    @Override
    protected <CT> CT getItemCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        return (CT) (side == Direction.DOWN ? out : in);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        currentRecipe = tag.contains("recipe") ? level.getRecipeManager().byKeyTyped(RecipeRegistry.COMPRESSOR_RECIPE.get(), ResourceLocation.parse(tag.getString("recipe"))) : null;
    }

    @Override
    void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.save(tag, lookup);
        if(currentRecipe != null) tag.putString("recipe", currentRecipe.id().toString());
    }
}