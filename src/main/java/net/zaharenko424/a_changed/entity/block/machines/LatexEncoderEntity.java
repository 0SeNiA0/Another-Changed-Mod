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
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.zaharenko424.a_changed.capability.energy.EnergyConsumer;
import net.zaharenko424.a_changed.item.DNASample;
import net.zaharenko424.a_changed.item.LatexSyringeItem;
import net.zaharenko424.a_changed.item.SyringeItem;
import net.zaharenko424.a_changed.menu.ItemHandlerContainer;
import net.zaharenko424.a_changed.menu.machines.LatexEncoderMenu;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipe;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LatexEncoderEntity extends AbstractMachineEntity<ItemStackHandler, EnergyConsumer> {

    public static final int MAX_PROGRESS = 300;
    private LazyOptional<RangedWrapper> in = LazyOptional.of(()-> new RangedWrapper(inventory, 0, 7));
    private LazyOptional<RangedWrapper> out = LazyOptional.of(()-> new RangedWrapper(inventory, 7, 8));
    private Gender gender = Gender.FEMALE;
    private boolean enabled;
    private int progress;


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
                setChanged();
            }
        };
    }

    @Override
    EnergyConsumer initEnergy() {
        return new EnergyConsumer(50000, 256, 0);
    }

    public int getProgress(){
        return progress;
    }

    public Gender getSelectedGender(){
        return gender;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setData(int index, int data){
        if(index == 0) gender = Gender.values()[data];
        if(index == 1) enabled = data == 1;
        if(index == 0 || index == 1) update();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new LatexEncoderMenu(pContainerId, pPlayerInventory, this);
    }
    //Eats 96/t
    @Override
    public void tick() {
        if(!enabled || energyStorage.getEnergyStored() < 96){
            setActive(false);
            return;
        }

        Optional<RecipeHolder<LatexEncoderRecipe>> recipe = getRecipe();
        if(recipe.isEmpty() || !Utils.canStacksStack(recipe.get().value().getResultItem(), inventory.getStackInSlot(7))) {
            if(progress != 0) progress = 0;
            setActive(false);
            return;
        }

        energyStorage.consumeEnergy(96);
        setActive(true);

        if(progress < MAX_PROGRESS){
            progress++;
        } else {
            inventory.insertItem(7, recipe.get().value().assemble(container), false);
            progress = 0;
        }

        update();
    }

    private final ItemHandlerContainer container = new ItemHandlerContainer(inventory);

    private @NotNull Optional<RecipeHolder<LatexEncoderRecipe>> getRecipe(){
        return level.getRecipeManager().getAllRecipesFor(LatexEncoderRecipe.Type.INSTANCE).stream()
                .filter(holder -> holder.value().matches(container, gender)).findFirst();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.latex_encoder");
    }

    @Override
    protected <CT> LazyOptional<CT> getItemCap(@NotNull Capability<CT> cap, @Nullable Direction side) {
        return side == Direction.DOWN ? out.cast() : in.cast();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("progress");
        if(tag.contains("selectedGender"))  gender = Gender.valueOf(tag.getString("selectedGender"));
        enabled = tag.getBoolean("enabled");
    }

    @Override
    void save(@NotNull CompoundTag tag) {
        super.save(tag);
        if(progress > 0) tag.putInt("progress", progress);
        tag.putString("selectedGender", gender.toString());
        tag.putBoolean("enabled", enabled);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        in.invalidate();
        out.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        in = LazyOptional.of(()-> new RangedWrapper(inventory, 0, 7));
        out = LazyOptional.of(()-> new RangedWrapper(inventory, 7, 8));
    }
}