package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStackEntity extends BlockEntity {

    protected final NonNullList<ItemStack> items = NonNullList.create();
    protected final NonNullList<Entry> entries = NonNullList.create();

    public AbstractStackEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public abstract boolean hasSpace();

    public boolean isEmpty(){
        return entries.isEmpty();
    }

    public int size(){
        return entries.size();
    }

    public NonNullList<Entry> entries(){
        return entries;
    }

    protected abstract boolean isItemValid(ItemStack stack);

    protected abstract Entry makeEntry(@NotNull ItemStack stack, float headRotDeg);

    public void addItem(@NotNull ItemStack stack, float headRotDeg, boolean shrink){
        if(level.isClientSide || stack.isEmpty() || !isItemValid(stack)) return;

        items.add(stack.copyWithCount(1));
        entries.add(makeEntry(stack, headRotDeg));
        if(shrink) stack.shrink(1);

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    public ItemStack removeItem(){
        if(level.isClientSide || isEmpty()) return ItemStack.EMPTY;

        ItemStack item = items.removeLast();
        entries.removeLast();

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
        return item;
    }

    public void dropItems(){
        BlockPos pos = getBlockPos();
        items.forEach(stack -> Block.popResource(level, pos, stack));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider lookup) {
        if(isEmpty()) return new CompoundTag();

        CompoundTag tag = new CompoundTag();
        tag.putInt("size", entries.size());
        Entry entry;
        for(int i = 0; i < entries.size(); i++){
            entry = entries.get(i);
            tag.putFloat("rotation" + i, entry.rotation);
            tag.putInt("modelId" + i, entry.modelId);
        }
        return tag;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        items.clear();
        entries.clear();
        int size = tag.getInt("size");

        for(int i = 0; i < size; i++){
            items.add(ItemStack.parseOptional(lookup, tag.getCompound("item" + i)));
            entries.add(new Entry(tag.getFloat("rotation" + i), tag.getInt("modelId" + i)));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.saveAdditional(tag, lookup);
        tag.putInt("size", entries.size());
        Entry entry;

        for(int i = 0; i < entries.size(); i++){
            entry = entries.get(i);
            if(level == null || !level.isClientSide) tag.put("item" + i, items.get(i).save(lookup));
            tag.putFloat("rotation" + i, entry.rotation);
            tag.putInt("modelId" + i, entry.modelId);
        }
    }

    public record Entry(float rotation, int modelId){}
}
