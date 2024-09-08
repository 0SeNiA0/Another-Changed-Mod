package net.zaharenko424.a_changed.entity.ai.behaviour.target;

import net.minecraft.world.entity.LivingEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.BiConsumer;

public class InvalidateWithCallback<E extends LivingEntity> extends InvalidateAttackTarget<E> {

    protected BiConsumer<E, LivingEntity> onInvalidate;

    public InvalidateWithCallback<E> onInvalidate(BiConsumer<E, LivingEntity> onInvalidate){
        this.onInvalidate = onInvalidate;

        return this;
    }

    @Override
    protected void start(E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        if (target == null)
            return;

        if (isTargetInvalid(entity, target) || !canAttack(entity, target) ||
                isTiredOfPathing(entity) || this.customPredicate.applyAsBoolean(entity, target)) {
            BrainUtils.setTargetOfEntity(entity, null);
            if(onInvalidate != null) onInvalidate.accept(entity, target);
        }
    }
}