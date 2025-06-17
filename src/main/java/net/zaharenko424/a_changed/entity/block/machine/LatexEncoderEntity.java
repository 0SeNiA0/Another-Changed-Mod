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
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.item.DNASample;
import net.zaharenko424.a_changed.item.LatexSyringeItem;
import net.zaharenko424.a_changed.item.SyringeItem;
import net.zaharenko424.a_changed.menu.machine.LatexEncoderMenu;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipe;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipeWrapper;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LatexEncoderEntity extends ProcessingMachine<ItemStackHandler, ExtendedEnergyStorage> {

    private final RangedWrapper in = new RangedWrapper(inventory, 0, 7);
    private final RangedWrapper out = new RangedWrapper(inventory, 7, 8);
    private Gender gender = Gender.FEMALE;
    private RecipeHolder<LatexEncoderRecipe> currentRecipe;


    public LatexEncoderEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LATEX_ENCODER_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    ItemStackHandler initInv() {
        return new ItemStackHandler(8){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch(slot){
                    case 0 -> stack.getItem() instanceof SyringeItem;
                    case 2, 3, 4 -> stack.getItem() instanceof DNASample;
                    case 7 -> stack.getItem() instanceof LatexSyringeItem;
                    default -> true;
                };
            }

            @Override
            protected void onContentsChanged(int slot) {
                update();
            }
        };
    }

    @Override
    ExtendedEnergyStorage initEnergy() {
        return new ExtendedEnergyStorage(50000, 256, 0);
    }

    public Gender getSelectedGender(){
        return gender;
    }

    public boolean hasRecipe(){
        return currentRecipe != null;
    }

    public void setData(int index, int data){
        super.setData(index, data);
        if(index == 1) {
            gender = Gender.values()[data];
            update();
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new LatexEncoderMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void tick() {
        if(!enabled || (hasRecipe() && getEnergy() < energyConsumption)){
            setActive(false);
            return;
        }

        if(!hasRecipe()){
            Optional<RecipeHolder<LatexEncoderRecipe>> recipe = getRecipe();
            if(recipe.isEmpty() || !inventory.insertItem(7, recipe.get().value().getResultItem(), true).isEmpty()) {
                setActive(false);
                return;
            }

            currentRecipe = recipe.get();
            currentRecipe.value().assemble(container, level.registryAccess());
            energyConsumption = currentRecipe.value().getEnergyConsumption();
            recipeProcessingTime = currentRecipe.value().getProcessingTime();
            setActive(true);
            update();
            return;
        }

        energyStorage.addEnergy(-energyConsumption);

        if(progress < recipeProcessingTime){
            progress++;
        } else {
            inventory.insertItem(7, currentRecipe.value().getResultItem(), false);
            progress = 0;
            currentRecipe = null;
        }

        setActive(true);
        update();
    }

    private final LatexEncoderRecipeWrapper container = new LatexEncoderRecipeWrapper(in, this);

    private @NotNull Optional<RecipeHolder<LatexEncoderRecipe>> getRecipe(){
        return level.getRecipeManager().getAllRecipesFor(RecipeRegistry.LATEX_ENCODER_RECIPE.get()).stream()
                .filter(holder -> holder.value().matches(container, level)).findFirst();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.latex_encoder");
    }

    @Override
    protected <CT> CT getItemCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        return (CT) (side == Direction.DOWN ? out : in);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        if(tag.contains("selectedGender"))  gender = Gender.valueOf(tag.getString("selectedGender"));
        currentRecipe = tag.contains("recipe") ? level.getRecipeManager().byKeyTyped(RecipeRegistry.LATEX_ENCODER_RECIPE.get(), ResourceLocation.parse(tag.getString("recipe"))) : null;
    }

    @Override
    void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.save(tag, lookup);
        tag.putString("selectedGender", gender.toString());
        if(currentRecipe != null) tag.putString("recipe", currentRecipe.id().toString());
    }
}