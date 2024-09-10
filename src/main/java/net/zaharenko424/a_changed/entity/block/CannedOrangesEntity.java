package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CannedOrangesEntity extends BlockEntity {

    private static final int MAX_FOOD = 4;
    private int left;

    public CannedOrangesEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CANNED_ORANGES_ENTITY.get(), pPos, pBlockState);
    }

    public void setFood(int foodLevel){
        left = foodLevel;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public boolean hasFoodLeft(){
        return left > 0;
    }

    public int getFoodLeft(){
        return left;
    }

    public void consumeFood(){
        left--;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public ItemStack getCan(){
        ItemStack stack = ItemRegistry.CANNED_ORANGES_ITEM.get().getDefaultInstance();
        stack.setDamageValue(MAX_FOOD - left);
        return stack;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider lookup) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, lookup);
        return tag;
    }

    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookup) {
        loadAdditional(pkt.getTag(), lookup);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider lookup) {
        left = pTag.getInt("left");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider lookup) {
        pTag.putInt("left", left);
    }
}