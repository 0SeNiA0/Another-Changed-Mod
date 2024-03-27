package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.block.blocks.LaserEmitter;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.util.NBTUtils;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LaserEmitterEntity extends BlockEntity {

    private boolean active;
    private int lengthCache = 0;
    private Direction directionCache = getBlockState().getValue(LaserEmitter.FACING);
    private AABB aabbCache;
    private int tick;

    public LaserEmitterEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.LASER_EMITTER_ENTITY.get(), p_155229_, p_155230_);
        active = p_155230_.getValue(StateProperties.ACTIVE);
    }

    public boolean isActive(){
        return active;
    }

    public int getLaserLength(){
        return lengthCache;
    }

    public Direction getDirection(){
        return directionCache;
    }

    public AABB getLaserAABB(){
        return aabbCache != null ? aabbCache : new AABB(worldPosition);
    }

    public void switchActive(){
        active = !active;
    }

    public void tick(){
        if(!active) return;
        tick++;
        if(tick < 10) return;
        tick = 0;

        directionCache = getBlockState().getValue(LaserEmitter.FACING);
        BlockPos.MutableBlockPos pos = worldPosition.mutable();
        BlockState state;
        int length = 0;

        while (length <= 20){
            pos.move(directionCache);
            state = level.getBlockState(pos);
            if(!state.is(AChanged.LASER_TRANSPARENT)) break;
            length++;
        }

        if(lengthCache == length){
            transfurEntities();
            return;
        }
        lengthCache = length;

        float halfLength = (float) length / 2;
        Vector3f step = directionCache.step();
        aabbCache = new AABB(worldPosition.relative(directionCache))
                .deflate(step.x == 0 ? .25f : 0, step.y == 0 ? .25f : 0, step.z == 0 ? .25f : 0)
                .inflate(Math.abs(step.x) * halfLength, Math.abs(step.y) * halfLength, Math.abs(step.z) * halfLength)
                .move(new Vec3(step.mul(halfLength)));

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);

        transfurEntities();
    }

    protected void transfurEntities(){
        level.getEntitiesOfClass(LivingEntity.class, aabbCache, DamageSources::checkTarget).forEach(entity -> {
            if(!entity.getItemBySlot(EquipmentSlot.LEGS).is(ItemRegistry.BLACK_LATEX_SHORTS.get())) return;
            TransfurEvent.TRANSFUR_DEF.accept(entity, TransfurRegistry.BENIGN_TF.get());
        });
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("active", active);
        tag.putInt("length", lengthCache);
        tag.putString("direction", directionCache.toString());
        NBTUtils.putAABB(tag, aabbCache != null ? aabbCache : new AABB(worldPosition));
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        active = tag.getBoolean("active");
        lengthCache = tag.getInt("length");
        directionCache = Direction.byName(tag.getString("direction"));
        aabbCache = NBTUtils.getAABB(tag);
    }
}