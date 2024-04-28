package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RotatingChairEntity extends SeatEntity {

    public RotatingChairEntity(@NotNull Level level) {
        super(EntityRegistry.CHAIR_ENTITY.get(), level);
    }

    public RotatingChairEntity(@NotNull Level level, @NotNull BlockPos pos, boolean renderPlayer) {
        super(EntityRegistry.CHAIR_ENTITY.get(), level, pos, renderPlayer);
    }

    @Override
    public boolean shouldRender(double p_20296_, double p_20297_, double p_20298_) {
        return true;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity instanceof LivingEntity livingentity ? livingentity : super.getControllingPassenger();
    }

    @Override
    protected void addPassenger(@NotNull Entity passenger) {
        if(!level().isClientSide)
            passenger.setYRot(getYRot());
        super.addPassenger(passenger);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity passenger = getControllingPassenger();
        if(passenger != null) {
            if(level().isClientSide) yRotO = getYRot();
            setYRot(passenger.yBodyRot);
        }
    }

    @Override
    public void setYRot(float pYRot) {
        super.setYRot(Mth.wrapDegrees(pYRot));
    }
}