package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.zaharenko424.a_changed.entity.SeatEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
//TODO remove this?
public interface ISeatBlock <S extends SeatEntity> {

    default boolean sit(@NotNull Level level, @NotNull BlockPos pos, @NotNull AABB ab, @NotNull Player player, boolean renderPlayer){
        List<S> list = level.getEntitiesOfClass(seatClass(), ab);
        if(list.isEmpty()){
            S seat = getSeat(level, pos, renderPlayer);
            level.addFreshEntity(seat);
            list.add(seat);
        }
        if(!list.get(0).isVehicle()){
            player.startRiding(list.get(0));
            return true;
        }
        return false;
    }

    default void removeSeat(@NotNull Level level, BlockPos pos){
        List<S> list = level.getEntitiesOfClass(seatClass(), new AABB(pos));
        if(list.isEmpty()) return;
        S seat = list.get(0);
        if(!seat.getPassengers().isEmpty()) seat.unRide();
        seat.discard();
    }

    default Class<S> seatClass(){
        return (Class<S>) SeatEntity.class;
    }

    default S getSeat(Level level, BlockPos pos, boolean renderPlayer){
        return (S) new SeatEntity(level, pos, renderPlayer);
    }
}