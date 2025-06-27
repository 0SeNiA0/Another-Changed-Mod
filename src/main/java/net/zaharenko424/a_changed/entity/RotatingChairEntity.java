package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class RotatingChairEntity extends SeatEntity {

    public RotatingChairEntity(@NotNull Level level) {
        super(EntityRegistry.CHAIR_ENTITY.get(), level);
    }

    public RotatingChairEntity(@NotNull Level level, @NotNull BlockPos pos, boolean renderPlayer) {
        super(EntityRegistry.CHAIR_ENTITY.get(), level, pos, renderPlayer);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
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
    public void onAddedToLevel() {
        super.onAddedToLevel();

        List<RotatingChairEntity> entities = level().getEntitiesOfClass(RotatingChairEntity.class, Shapes.block().bounds().move(getOnPos()));
        for (RotatingChairEntity entity : entities){
            if(entity != this) entity.discard();
        }
    }

    @Override
    public void setYRot(float yRot) {
        super.setYRot(Mth.wrapDegrees(yRot));
    }
}