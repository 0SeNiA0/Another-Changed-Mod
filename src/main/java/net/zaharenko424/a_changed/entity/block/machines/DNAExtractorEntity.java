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
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.zaharenko424.a_changed.capability.energy.EnergyConsumer;
import net.zaharenko424.a_changed.menu.ItemHandlerContainer;
import net.zaharenko424.a_changed.menu.machines.DNAExtractorMenu;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DNAExtractorEntity extends AbstractMachineEntity<ItemStackHandler, EnergyConsumer> {

    private final RangedWrapper output = new RangedWrapper(inventory, 4, 8);

    private static final int maxProgress = 600;
    private int[] progress = new int[4];
    private int rotationDeg;
    private int rotationDegO;

    public DNAExtractorEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DNA_EXTRACTOR_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    ItemStackHandler initInv() {
        return new ItemStackHandler(8){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot < 4;
            }

            @Override
            protected void onContentsChanged(int slot) {
                update();
            }
        };
    }

    @Override
    EnergyConsumer initEnergy() {
        return new EnergyConsumer(25000, 256, 0);
    }

    public int getRot(){
        return rotationDeg;
    }

    public int getRotO(){
        return rotationDegO;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new DNAExtractorMenu(i, inventory, this);
    }

//Eats 64/t for now
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
            ItemStack result = r.get().value().assemble(container, slot);
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
        List<ItemStack> results = recipes.values().stream().filter(Optional::isPresent)
                .map(optional -> optional.get().value().getResultItem()).toList();

        List<ItemStack> outSlots = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            outSlots.add(output.getStackInSlot(i).copy());
        }

        for(ItemStack result : results){
            for(ItemStack out : outSlots){
                if(!Utils.canStacksStack(result, out)) continue;
                int toAdd = Math.min(result.getCount(), out.getMaxStackSize() - out.getCount());
                out.grow(toAdd);
                result.shrink(toAdd);
                if(result.isEmpty()) break;
            }
            if(!result.isEmpty()) return false;
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
        if(anyProgress) update();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.dna_extractor");
    }

    @Override
    protected <CT> CT getItemCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        if(side == null) return null;
        return (CT) switch(side){
            case UP -> new RangedWrapper(inventory, 0, 4);
            case NORTH -> new RangedWrapper(inventory, 0, 1);
            case EAST -> new RangedWrapper(inventory, 1, 2);
            case SOUTH -> new RangedWrapper(inventory, 2, 3);
            case WEST -> new RangedWrapper(inventory, 3, 4);
            case DOWN -> output;
        };
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        int[] ar = tag.getIntArray("progress");
        if(ar.length == 4) progress = ar;
        int rot = rotationDeg;
        rotationDeg = tag.getInt("rotation");
        if(level != null && level.isClientSide)
            if(hasAnyProgress()) rotationDegO = rot; else rotationDegO = rotationDeg;
    }

    @Override
    void save(@NotNull CompoundTag tag) {
        super.save(tag);
        tag.putIntArray("progress", progress);
        tag.putInt("rotation", rotationDeg);
    }
}