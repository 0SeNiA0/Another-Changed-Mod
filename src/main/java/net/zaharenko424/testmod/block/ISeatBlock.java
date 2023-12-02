package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.entity.SeatEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ISeatBlock {

    default boolean sit(@NotNull Level level,@NotNull BlockPos pos,@NotNull AABB ab,@NotNull Player player, boolean renderPlayer){
        List<SeatEntity> list=level.getEntitiesOfClass(SeatEntity.class,ab);
        if(list.isEmpty()){
            TestMod.LOGGER.warn("No seat entity found! Creating new");
            SeatEntity seat=new SeatEntity(level,pos,renderPlayer);
            level.addFreshEntity(seat);
            list.add(seat);
        }
        if(!list.get(0).isVehicle()){
            player.startRiding(list.get(0));
            return true;
        }
        return false;
    }
}