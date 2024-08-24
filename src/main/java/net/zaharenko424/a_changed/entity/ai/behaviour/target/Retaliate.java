package net.zaharenko424.a_changed.entity.ai.behaviour.target;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import net.zaharenko424.a_changed.registry.MemoryTypeRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Retaliate<E extends Mob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.HURT_BY, MemoryStatus.VALUE_PRESENT));

    protected Predicate<LivingEntity> canAttackPredicate = entity -> entity.isAlive() && (!(entity instanceof Player player) || !player.isCreative());
    protected BiPredicate<E, Entity> alertAlliesPredicate = (owner, attacker) -> false;
    protected BiPredicate<E, LivingEntity> allyPredicate = (owner, ally) -> {
        if (!owner.getClass().isAssignableFrom(ally.getClass()) || BrainUtils.getTargetOfEntity(ally) != null)
            return false;

        if (owner instanceof OwnableEntity pet && pet.getOwner() != ((OwnableEntity)ally).getOwner())
            return false;

        Entity lastHurtBy = BrainUtils.getMemory(ally, MemoryModuleType.HURT_BY_ENTITY);

        return lastHurtBy == null || !ally.isAlliedTo(lastHurtBy);
    };

    protected LivingEntity toTarget = null;
    protected MemoryModuleType<? extends LivingEntity> priorityTargetMemory = MemoryModuleType.NEAREST_ATTACKABLE;

    /**
     * Set the predicate to determine whether a given entity should be targeted or not.
     * @param predicate The predicate
     * @return this
     */
    public Retaliate<E> attackablePredicate(Predicate<LivingEntity> predicate) {
        this.canAttackPredicate = predicate;

        return this;
    }

    /**
     * Set the memory type that is checked first to target an entity.
     * Useful for switching to player-only targeting
     * @return this
     */
    public Retaliate<E> useMemory(MemoryModuleType<? extends LivingEntity> memory) {
        this.priorityTargetMemory = memory;

        return this;
    }

    /**
     * Set the predicate to determine whether the brain owner should alert nearby allies of the same entity type when retaliating
     * @param predicate The predicate
     * @return this
     */
    public Retaliate<E> alertAlliesWhen(BiPredicate<E, Entity> predicate) {
        this.alertAlliesPredicate = predicate;

        return this;
    }

    /**
     * Set the predicate to determine whether a given entity should be alerted to the target as an ally of the brain owner.<br>
     * Overriding replaces the default predicate, so be sure to include any portions of the default predicate in your own if applicable
     * @param predicate The predicate
     * @return this
     */
    public Retaliate<E> isAllyIf(BiPredicate<E, LivingEntity> predicate) {
        this.allyPredicate = predicate;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, E owner) {
        Brain<?> brain = owner.getBrain();
        this.toTarget = BrainUtils.getMemory(brain, this.priorityTargetMemory);

        if(priorityTargetMemory != MemoryModuleType.HURT_BY_ENTITY && this.toTarget == null) {
            this.toTarget = BrainUtils.getMemory(brain, MemoryModuleType.HURT_BY_ENTITY);
        }

        if(this.toTarget == null) return false;

        if(this.alertAlliesPredicate.test(owner, this.toTarget))
            alertAllies(level, owner);

        return this.canAttackPredicate.test(this.toTarget);
    }

    @Override
    protected void start(E entity) {
        BrainUtils.setTargetOfEntity(entity, this.toTarget);
        if(DamageSources.checkTarget(toTarget)) BrainUtils.setMemory(entity, MemoryTypeRegistry.TRYING_TO_TRANSFUR.get(), true);
        BrainUtils.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

        this.toTarget = null;
    }

    protected void alertAllies(ServerLevel level, E owner) {
        double followRange = owner.getAttributeValue(Attributes.FOLLOW_RANGE);

        for (LivingEntity ally : EntityRetrievalUtil.<LivingEntity>getEntities(level, owner.getBoundingBox().inflate(followRange, 10, followRange),
                entity -> entity != owner && entity instanceof LivingEntity livingEntity && this.allyPredicate.test(owner, livingEntity))) {
            BrainUtils.setTargetOfEntity(ally, this.toTarget);
        }
    }
}