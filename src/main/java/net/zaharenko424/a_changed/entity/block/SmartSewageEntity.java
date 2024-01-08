package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.SoundRegistry;

import java.util.List;

public class SmartSewageEntity extends BlockEntity {

    LivingEntity entity;
    int tick=0;

    public SmartSewageEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.SMART_SEWAGE_ENTITY.get(), p_155229_, p_155230_);
    }

    public void tick() {
        tick++;
        if(tick<20) return;
        tick=0;
        List<LivingEntity> list=level.getEntitiesOfClass(LivingEntity.class, Shapes.block().bounds().move(worldPosition.above()),
                (entity)->entity.getType().is(AChanged.SEWAGE_SYSTEM_CONSUMABLE));
        if(list.isEmpty()){
            entity=null;
            return;
        }
        if(entity!=null&&list.contains(entity)){
            entity.discard();
            level.playSound(null,worldPosition, SoundRegistry.SMART_SEWAGE_CONSUME.get(), SoundSource.BLOCKS);
            entity=null;
            return;
        }
        entity=list.get(0);
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,2));
    }
}