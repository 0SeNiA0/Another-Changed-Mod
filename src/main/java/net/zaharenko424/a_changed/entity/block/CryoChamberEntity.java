package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.zaharenko424.a_changed.block.blocks.CryoChamber;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CryoChamberEntity extends BlockEntity {

    private boolean open;
    private boolean active;
    private int fluidAmount;
    private Direction direction = getBlockState().getValue(CryoChamber.FACING);
    private AABB insideAABB;
    private int tick;

    public CryoChamberEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CRYO_CHAMBER_ENTITY.get(), pos, state);
        insideAABB = ((CryoChamber)state.getBlock()).inside(pos, state);
    }

    public boolean isOpen(){
        return open;
    }

    public void setOpen(boolean open){
        this.open = open;
        if(open) {
            if(!level.isClientSide){
                BlockState state = getBlockState();
                ((CryoChamber)state.getBlock()).leak(worldPosition, state, level, (int) (fluidAmount / 2.285));
            }
            fluidAmount = 0;
        }
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public int getFluidAmount(){
        return fluidAmount;
    }

    public Direction getDirection(){
        return direction;
    }

    public AABB getInsideAABB(){
        return insideAABB;
    }

    public void tick(){
        if(open || !active) return;
        tick++;
        if(tick % 20 != 0) return;
        tick = 0;

        boolean update = false;

        if(direction != getBlockState().getValue(CryoChamber.FACING)){
            direction = getBlockState().getValue(CryoChamber.FACING);
            insideAABB = ((CryoChamber)getBlockState().getBlock()).inside(worldPosition,getBlockState());
            update = true;
        }

        if(fluidAmount < 32) {
            fluidAmount++;
            update = true;
        }
        //TODO fill hunger while fluidAmount ~ 30 or smth ?
        if(fluidAmount > 24) level.getEntitiesOfClass(LivingEntity.class, insideAABB).forEach(entity -> {
            if(entity instanceof ServerPlayer player) player.getFoodData().eat(1,1);
        });

        if(update) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        CompoundTag modTag = NBTUtils.modTag(tag);
        save(modTag);
        modTag.putString("direction", direction.toString());
        NBTUtils.putAABB(modTag, insideAABB);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, @NotNull ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        load(tag);
        CompoundTag modTag = NBTUtils.modTag(tag);
        direction = Direction.byName(modTag.getString("direction"));
        insideAABB = NBTUtils.getAABB(modTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        CompoundTag modTag = NBTUtils.modTag(pTag);
        open = modTag.getBoolean("open");
        active = modTag.getBoolean("active");
        fluidAmount = open ? 0 : modTag.getInt("fluidAmount");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        save(NBTUtils.modTag(pTag));
    }

    void save(@NotNull CompoundTag tag){
        tag.putBoolean("open", open);
        tag.putBoolean("active", active);
        if(!open) tag.putInt("fluidAmount", fluidAmount);
    }
}