package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.block.ISeatBlock;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;

public class SeatEntity extends Entity {

    protected static final EntityDataAccessor<Boolean> RENDER_PLAYER = SynchedEntityData.defineId(SeatEntity.class, EntityDataSerializers.BOOLEAN);

    protected SeatEntity(EntityType<?> entityType, Level level){
        super(entityType, level);
        noPhysics = true;
    }

    protected SeatEntity(EntityType<?> entityType, @NotNull Level level, @NotNull BlockPos pos, boolean renderPlayer){
        this(entityType, level);
        setPos(pos.getCenter());
        entityData.set(RENDER_PLAYER, renderPlayer);
    }

    public SeatEntity(@NotNull Level level) {
        this(EntityRegistry.SEAT_ENTITY.get(), level);
    }

    public SeatEntity(@NotNull Level level, @NotNull BlockPos pos, boolean renderPlayer){
        this(EntityRegistry.SEAT_ENTITY.get(), level, pos, renderPlayer);
    }

    public boolean renderPlayer(){
        return entityData.get(RENDER_PLAYER);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return false;
    }

    @Override
    protected void addPassenger(@NotNull Entity passenger) {
        if(!entityData.get(RENDER_PLAYER)) passenger.setInvisible(true);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(@NotNull Entity passenger) {
        super.removePassenger(passenger);
        if(passenger instanceof LivingEntity entity && !entity.hasEffect(MobEffects.INVISIBILITY)) passenger.setInvisible(false);
    }

    private int tick = 0;
    @Override
    public void tick() {
        tick++;
        if(tick % 20 == 0){
            if(!(level().getBlockState(blockPosition()).getBlock() instanceof ISeatBlock)){
                if(isVehicle()) getFirstPassenger().stopRiding();
                discard();
                return;
            }
        }
        super.tick();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(RENDER_PLAYER, true);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        entityData.set(RENDER_PLAYER, NBTUtils.modTag(tag).getBoolean("renderPlayer"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        NBTUtils.modTag(tag).putBoolean("renderPlayer", entityData.get(RENDER_PLAYER));
    }
}