package net.zaharenko424.a_changed.entity.block.machines;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.zaharenko424.a_changed.capability.EnergyConsumer;
import net.zaharenko424.a_changed.menu.DNAExtractorMenu;
import net.zaharenko424.a_changed.menu.ItemHandlerContainer;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DNAExtractorEntity extends AbstractMachineEntity<ItemStackHandler, EnergyConsumer> {

    private final RangedWrapper ingredients = new RangedWrapper(inventory, 0, 4);
    private LazyOptional<RangedWrapper> inOptional = LazyOptional.of(()-> ingredients);
    private LazyOptional<RangedWrapper> in0 = LazyOptional.of(()-> new RangedWrapper(inventory, 0, 1));
    private LazyOptional<RangedWrapper> in1 = LazyOptional.of(()-> new RangedWrapper(inventory, 1, 2));
    private LazyOptional<RangedWrapper> in2 = LazyOptional.of(()-> new RangedWrapper(inventory, 2, 3));
    private LazyOptional<RangedWrapper> in3 = LazyOptional.of(()-> new RangedWrapper(inventory, 3, 4));
    private final RangedWrapper output = new RangedWrapper(inventory, 4, 8);
    private LazyOptional<RangedWrapper> outOptional = LazyOptional.of(()-> output);

    private static final int maxProgress = 600;
    private int[] progress = new int[4];
    private int rotationDeg;

    public DNAExtractorEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DNA_EXTRACTOR_ENTITY.get(), pPos, pBlockState, new ItemStackHandler(8){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot < 4;
            }
        }, new EnergyConsumer(25000, 256, 0));
    }

    public int getRot(){
        return rotationDeg;
    }

    @Override
    public void tick() {
        if(energyStorage.getEnergyStored() < 64) {
            resetProgress();
            setActive(false);
            return;
        }

        Int2ObjectArrayMap<Optional<RecipeHolder<DNAExtractorRecipe>>> map = new Int2ObjectArrayMap<>();
        boolean hasAnyRecipes = false;
        Optional<RecipeHolder<DNAExtractorRecipe>> recipe;
        for(int i = 0; i < 4; i++){
            recipe = getRecipe(i);
            if(recipe.isEmpty()) {
                if(progress[i] > 0) progress[i] = 0;
                continue;
            }
            map.put(i, recipe);
            hasAnyRecipes = true;
        }

        if(!hasAnyRecipes || !hasEnoughOutputSpace(map)){
            resetProgress();
            setActive(false);
            return;
        }

        setActive(true);
        energyStorage.consumeEnergy(64);
        rotationDeg = Mth.wrapDegrees(rotationDeg + 20);

        map.forEach((slot, r) -> {
            if(progress[slot] < maxProgress){
                progress[slot]++;
                return;
            }
            progress[slot] = 0;
            inventory.extractItem(slot, 1, false);
            ItemStack result = r.get().value().getResultItem();
            ItemStack out;
            for(int i0 = 0; i0 < 4; i0++){
                out = output.getStackInSlot(i0);
                if(out.isEmpty() || ItemHandlerHelper.canItemStacksStack(result, out)){
                    int toAdd = Math.min(output.getSlotLimit(i0) - out.getCount(), result.getCount());
                    output.setStackInSlot(i0, result.copyWithCount(out.getCount() + toAdd));
                    result.shrink(toAdd);
                }
                if(result.isEmpty()) break;
            }
        });

        update();
    }

    private boolean hasEnoughOutputSpace(@NotNull Int2ObjectArrayMap<Optional<RecipeHolder<DNAExtractorRecipe>>> recipes){
        List<ItemStack> results = recipes.values().stream().map(optional -> optional.get().value().getResultItem()).toList();

        List<ItemStack> outSlots = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            outSlots.add(output.getStackInSlot(i).copy());
        }

        for(ItemStack result : results){
            for(ItemStack out : outSlots){
                if(!ItemHandlerHelper.canItemStacksStack(result, out)) continue;
                int toAdd = Math.min(result.getCount(), out.getMaxStackSize() - out.getCount());
                out.grow(toAdd);
                result.shrink(toAdd);
                if(result.isEmpty()) break;
            }
            if(result.isEmpty()) return false;
        }

        return true;
    }

    private final ItemHandlerContainer container = new ItemHandlerContainer(inventory);

    private @NotNull Optional<RecipeHolder<DNAExtractorRecipe>> getRecipe(int slot){
        return level.getRecipeManager().getAllRecipesFor(DNAExtractorRecipe.Type.INSTANCE).stream()
                .filter(holder -> holder.value().matches(container, slot, level)).findFirst();
    }

    public boolean hasAnyProgress(){
        for(int i = 0; i < 4;i++){
            if(progress[i] > 0) return true;
        }
        return false;
    }

    private void resetProgress(){
        boolean anyProgress = false;
        for(int i = 0; i < 4;i++){
            if(progress[i] != 0) anyProgress = true;
            progress[i] = 0;
        }
        if(anyProgress) {
            rotationDeg = 0;
            update();
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.dna_extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new DNAExtractorMenu(i, inventory, this);
    }

    @Override
    protected <CT> LazyOptional<CT> getItemCap(@NotNull Capability<CT> cap, @Nullable Direction side) {
        if(side == null) return LazyOptional.empty();
        return switch(side){
            case UP -> inOptional.cast();
            case NORTH -> in0.cast();
            case EAST -> in1.cast();
            case SOUTH -> in2.cast();
            case WEST -> in3.cast();
            case DOWN -> outOptional.cast();
        };
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        int[] ar = tag.getIntArray("progress");
        if(ar.length == 4) progress = ar;
        rotationDeg = tag.getInt("rotation");
    }

    @Override
    void save(@NotNull CompoundTag tag) {
        super.save(tag);
        tag.putIntArray("progress", progress);
        tag.putInt("rotation", rotationDeg);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inOptional.invalidate();
        in0.invalidate();
        in1.invalidate();
        in2.invalidate();
        in3.invalidate();
        outOptional.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        inOptional = LazyOptional.of(()-> ingredients);
        in0 = LazyOptional.of(()-> new RangedWrapper(inventory, 0, 1));
        in1 = LazyOptional.of(()-> new RangedWrapper(inventory, 1, 2));
        in2 = LazyOptional.of(()-> new RangedWrapper(inventory, 2, 3));
        in3 = LazyOptional.of(()-> new RangedWrapper(inventory, 3, 4));
        outOptional = LazyOptional.of(()-> output);
    }
}