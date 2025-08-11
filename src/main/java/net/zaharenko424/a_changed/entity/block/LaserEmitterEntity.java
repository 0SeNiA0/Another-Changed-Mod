package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.zaharenko424.a_changed.AChangedTags;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.block.LaserEmitter;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.util.DynamicClipContext;
import net.zaharenko424.a_changed.util.NBTUtils;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LaserEmitterEntity extends BlockEntity {

    private static final int MAX_LENGTH = 32;

    private float lengthCache = 0;
    private Direction directionCache;
    private AABB aabbCache;
    private int tick;

    public LaserEmitterEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LASER_EMITTER_ENTITY.get(), pos, state);
    }


    public boolean isActive(){
        return getBlockState().getValue(StateProperties.ACTIVE);
    }

    public float getLaserLength(){
        return lengthCache;
    }

    public Direction getDirection(){
        return getBlockState().getValue(LaserEmitter.FACING);
    }

    public AABB getLaserAABB(){
        if(aabbCache == null) aabbCache = new AABB(worldPosition);
        return aabbCache;
    }


    public void tick(){
        BlockState state = getBlockState();
        if(!state.getValue(StateProperties.ACTIVE)) return;
        tick++;
        if(tick < 10) return;
        tick = 0;

        Direction direction = state.getValue(LaserEmitter.FACING);

        Vec3 center = worldPosition.getCenter();
        Vec3 start = center.relative(direction, .5);
        BlockHitResult result = level.clip(new DynamicClipContext(start, center.relative(direction,  MAX_LENGTH + 1.5),
                (state1, level, pos, context) ->
                        state1.is(AChangedTags.Block.LASER_TRANSPARENT)
                                ? Shapes.empty()
                                : ClipContext.Block.COLLIDER.get(state1, level, pos, context),
                ClipContext.Fluid.NONE::canPick, CollisionContext.empty()));

        float length = (float) result.getLocation().distanceTo(start);

        if(lengthCache == length && direction == directionCache){
            transfurEntities();
            return;
        }

        lengthCache = length;
        directionCache = direction;

        Vector3f step = directionCache.step();
        aabbCache = new AABB(start, result.getLocation())
                .inflate(step.x == 0 ? .25 : 0, step.y == 0 ? .25f : 0, step.z == 0 ? .25f : 0);

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);

        transfurEntities();
    }

    protected void transfurEntities(){
        level.getEntitiesOfClass(LivingEntity.class, aabbCache, DamageSources::checkTFTarget).forEach(entity -> {
            if(!entity.getItemBySlot(EquipmentSlot.LEGS).is(ItemRegistry.BLACK_LATEX_SHORTS.get())) return;

            TransfurHandler handler = TransfurHandler.nonNullOf(entity);
            handler.transfur(TransfurRegistry.BENIGN_TF.get(), TransfurContext.DEF);
        });
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider lookup) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("length", lengthCache);
        NBTUtils.putAABB(tag, aabbCache != null ? aabbCache : new AABB(worldPosition));
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookup) {
        handleUpdateTag(pkt.getTag(), lookup);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        lengthCache = tag.getFloat("length");
        aabbCache = NBTUtils.getAABB(tag);
    }
}