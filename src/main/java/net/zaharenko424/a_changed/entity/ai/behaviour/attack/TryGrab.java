package net.zaharenko424.a_changed.entity.ai.behaviour.attack;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.zaharenko424.a_changed.ability.AbilityHolder;
import net.zaharenko424.a_changed.ability.GrabAbility;
import net.zaharenko424.a_changed.attachments.GrabChanceData;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.MemoryTypeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class TryGrab<E extends Mob> extends DelayedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryTypeRegistry.GRAB_ATTEMPT_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT));

    protected Function<E, Integer> attackIntervalSupplier = entity -> 20;

    @Nullable
    protected LivingEntity target = null;

    public TryGrab(){
        super(0);
    }

    public TryGrab(int delayTicks) {
        super(delayTicks);
    }

    /**
     * Set the time between attacks.
     * @param supplier The tick value provider
     * @return this
     */
    public TryGrab<E> attackInterval(Function<E, Integer> supplier) {
        this.attackIntervalSupplier = supplier;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if(!(entity instanceof AbilityHolder holder) || !holder.hasAbility(AbilityRegistry.GRAB_ABILITY)) return false;

        this.target = BrainUtils.getTargetOfEntity(entity);

        if(!GrabData.canGrab(entity, target)) return false;

        if(entity.getSensing().hasLineOfSight(this.target) && isCloseEnoughForGrab(entity, this.target)
                && entity.getRandom().nextFloat() <= GrabChanceData.of(level).getGrabChance()) return true;

        BrainUtils.setForgettableMemory(entity, MemoryTypeRegistry.GRAB_ATTEMPT_COOLDOWN.get(), true, attackIntervalSupplier.apply(entity));
        return false;
    }

    @Override
    protected void start(E entity) {
        entity.swing(InteractionHand.MAIN_HAND);
        BehaviorUtils.lookAtEntity(entity, this.target);
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }

    @Override
    protected void doDelayedAction(E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryTypeRegistry.GRAB_ATTEMPT_COOLDOWN.get(), true, attackIntervalSupplier.apply(entity));

        if(this.target == null) return;

        if(!entity.getSensing().hasLineOfSight(this.target) || !isCloseEnoughForGrab(entity, this.target)) return;

        BrainUtils.setMemory(entity, MemoryTypeRegistry.TRANSFUR_HOLDING.get(), true);
        AbilityRegistry.GRAB_ABILITY.get().activate(entity, target);
    }

    protected boolean isCloseEnoughForGrab(LivingEntity entity, LivingEntity target){
        return entity.distanceToSqr(target) <= GrabAbility.CLOSE_ENOUGH;
    }
}