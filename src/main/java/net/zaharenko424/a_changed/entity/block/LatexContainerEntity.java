package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.item.LatexItem;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.Latex;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LatexContainerEntity extends BlockEntity {

    private final ItemStackHandler handler = new ItemStackHandler(){
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof LatexItem;
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 16;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public LatexContainerEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.LATEX_CONTAINER_ENTITY.get(), p_155229_, p_155230_);
    }

    public boolean hasSpace(Item item){
        return handler.getStackInSlot(0).isEmpty() || (handler.getStackInSlot(0).getCount() < 16 && isSameItem(item));
    }

    public boolean isEmpty(){
        return handler.getStackInSlot(0).isEmpty();
    }

    public boolean isSameItem(Item item){
        return handler.getStackInSlot(0).getItem() == item;
    }

    public int getLatexAmount(){
        return handler.getStackInSlot(0).getCount();
    }

    public Latex getLatexType(){
        if(isEmpty()) return null;
        return ((LatexItem)handler.getStackInSlot(0).getItem()).getLatexType();
    }

    public void addLatex(ItemStack item, boolean shrink){
        handler.insertItem(0, item.copyWithCount(1),false);
        if(shrink) item.shrink(1);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public ItemStack removeLatex() {
        ItemStack item = handler.extractItem(0,1,false);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        return item;
    }

    public void onRemove(){
        if(getLatexAmount() >= 8){
            BlockState state = isSameItem(ItemRegistry.DARK_LATEX_ITEM.asItem())
                    ? BlockRegistry.DARK_LATEX_FLUID_BLOCK.get().defaultBlockState() : BlockRegistry.WHITE_LATEX_FLUID_BLOCK.get().defaultBlockState();
            level.setBlockAndUpdate(getBlockPos(), state);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider lookup) {
        if(!handler.getStackInSlot(0).isEmpty()) return (CompoundTag) handler.getStackInSlot(0).save(lookup, new CompoundTag());
        return new CompoundTag();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookup) {
        handleUpdateTag(pkt.getTag(), lookup);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        if(!tag.isEmpty()) {
            handler.setStackInSlot(0, ItemStack.parseOptional(lookup, tag));
        } else handler.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public void loadAdditional(CompoundTag p_155245_, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(p_155245_, lookup);
        CompoundTag modTag = NBTUtils.modTag(p_155245_);
        if(!modTag.contains("latex")) return;
        handler.setStackInSlot(0, ItemStack.parseOptional(lookup, modTag.getCompound("latex")));
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_, HolderLookup.@NotNull Provider lookup) {
        super.saveAdditional(p_187471_, lookup);
        if(isEmpty()) return;
        CompoundTag item = new CompoundTag();
        handler.getStackInSlot(0).save(lookup, item);
        NBTUtils.modTag(p_187471_).put("latex", item);
    }
}