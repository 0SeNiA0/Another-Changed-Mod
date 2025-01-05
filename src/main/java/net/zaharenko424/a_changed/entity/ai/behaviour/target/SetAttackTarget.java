package net.zaharenko424.a_changed.entity.ai.behaviour.target;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class SetAttackTarget <E extends LivingEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.NEAREST_ATTACKABLE, MemoryStatus.VALUE_PRESENT));
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> CUSTOM_TARGETING_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT));

    protected final boolean usingNearestAttackable;
    protected LivingEntity toTarget;
    protected Predicate<LivingEntity> canAttackPredicate = entity -> true;
    protected BiPredicate<E, Entity> alertAlliesPredicate = (owner, attacker) -> false;
    protected BiPredicate<E, LivingEntity> allyPredicate = (owner, ally) -> owner.getClass().isAssignableFrom(ally.getClass()) && BrainUtils.getTargetOfEntity(ally) == null && (!(owner instanceof TamableAnimal pet) || pet.getOwner() == ((TamableAnimal)ally).getOwner()) && !ally.isAlliedTo(toTarget);
    protected Function<E, ? extends LivingEntity> targetFinder = entity -> BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_ATTACKABLE);

    public SetAttackTarget() {
        this(true);
    }

    public SetAttackTarget(boolean usingNearestAttackable) {
        this.usingNearestAttackable = usingNearestAttackable;
    }

    /**
     * Set the predicate to determine whether the entity is ready to attack or not.
     * @param predicate The predicate
     * @return this
     */
    public SetAttackTarget<E> attackPredicate(Predicate<LivingEntity> predicate) {
        canAttackPredicate = predicate;

        return this;
    }

    /**
     * Set the target finding function. If replacing the {@link MemoryModuleType#NEAREST_ATTACKABLE} memory retrieval, set false in the constructor of the behaviour.
     * @param targetFindingFunction The function
     * @return this
     */
    public SetAttackTarget<E> targetFinder(Function<E, ? extends LivingEntity> targetFindingFunction) {
        targetFinder = targetFindingFunction;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return usingNearestAttackable ? MEMORY_REQUIREMENTS : CUSTOM_TARGETING_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        toTarget = usingNearestAttackable ? BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_ATTACKABLE) : targetFinder.apply(entity);

        if(toTarget == null || !toTarget.isAlive() || toTarget.level() != level || !canAttackPredicate.test(toTarget)) return false;

        if (alertAlliesPredicate.test(entity, toTarget))
            alertAllies(level, entity);

        return true;
    }

    @Override
    protected void start(E entity) {
        if (toTarget == null) {
            BrainUtils.clearMemory(entity, MemoryModuleType.ATTACK_TARGET);
        } else {
            BrainUtils.setMemory(entity, MemoryModuleType.ATTACK_TARGET, toTarget);
            BrainUtils.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        }

        toTarget = null;
    }

    protected void alertAllies(ServerLevel level, E owner) {
        double followRange = owner.getAttributeValue(Attributes.FOLLOW_RANGE);

        for (LivingEntity ally : EntityRetrievalUtil.<LivingEntity>getEntities(level, owner.getBoundingBox().inflate(followRange, 10, followRange),
                entity -> entity != owner && entity instanceof LivingEntity livingEntity && allyPredicate.test(owner, livingEntity))) {
            BrainUtils.setTargetOfEntity(ally, toTarget);
        }
    }
}
