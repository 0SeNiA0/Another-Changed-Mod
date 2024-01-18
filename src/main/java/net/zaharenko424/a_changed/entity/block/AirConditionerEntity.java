package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.zaharenko424.a_changed.block.blocks.GasTank;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;

public class AirConditionerEntity extends BlockEntity {

    private static final AABB aabb = Shapes.block().bounds().inflate(2,2,2);
    private final AABB ab = aabb.move(worldPosition.above().relative(getBlockState().getValue(GasTank.FACING),2));
    private int tick = 0;

    public AirConditionerEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.AIR_CONDITIONER_ENTITY.get(), p_155229_, p_155230_);
    }

    public void tick(){
        tick++;
        if(tick < 20) return;
        tick = 0;
        level.getEntitiesOfClass(LivingEntity.class,ab).forEach((entity -> entity.addEffect(new MobEffectInstance(MobEffectRegistry.FRESH_AIR.get(),20,0,false,false))));
    }
}