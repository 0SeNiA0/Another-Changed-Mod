package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.zaharenko424.a_changed.menu.ItemHandlerContainer;
import net.zaharenko424.a_changed.menu.machines.LatexPurifierMenu;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LatexPurifierEntity extends AbstractMachineEntity<ItemStackHandler, ExtendedEnergyStorage> {

    public static final int MAX_PROGRESS = 160;
    private final RangedWrapper in = new RangedWrapper(inventory, 0, 2);
    private final RangedWrapper out = new RangedWrapper(inventory, 2, 3);
    private int progress;

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

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new LatexPurifierMenu(pContainerId, playerInventory, this);
    }
//Eats 48/t
    public void tick(){
        boolean changed = false;

        if(!inventory.getStackInSlot(0).isEmpty()){
            changed = energyStorage.receiveEnergyFrom(inventory.getStackInSlot(0).getCapability(Capabilities.EnergyStorage.ITEM),
                    energyStorage.getMaxReceive(), false) != 0;
        }

        if(getEnergy() < 48){
            setActive(false);
            if(changed) update();
            return;
        }

        Optional<RecipeHolder<LatexPurifierRecipe>> recipe = getRecipe();
        if(recipe.isEmpty() || !Utils.canStacksStack(recipe.get().value().getResultItem(), inventory.getStackInSlot(2))) {
            if(progress != 0) progress = 0;
            setActive(false);
            return;
        }

        setActive(true);
        energyStorage.addEnergy(-48);

        if(progress < MAX_PROGRESS) {
            progress++;
        } else {
            ItemStack item = recipe.get().value().assemble(container);
            inventory.setStackInSlot(2,  item.copyWithCount(item.getCount() + inventory.getStackInSlot(2).getCount()));
            progress = 0;
        }

        update();
    }

    private final ItemHandlerContainer container = new ItemHandlerContainer(inventory);

    private @NotNull Optional<RecipeHolder<LatexPurifierRecipe>> getRecipe(){
        return level.getRecipeManager().getRecipeFor(LatexPurifierRecipe.Type.INSTANCE, container, level);
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
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("progress");
    }

    @Override
    void save(@NotNull CompoundTag tag) {
        super.save(tag);
        if(progress > 0) tag.putInt("progress", progress);
    }
}