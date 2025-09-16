package net.zaharenko424.a_changed.entity.ai.behaviour.target;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class SetPlayerLookTarget<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = ObjectArrayList.of(Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.NEAREST_PLAYERS, MemoryStatus.VALUE_PRESENT));

    protected FloatProvider runChance = ConstantFloat.of(0.02f);
    protected Function<E, Integer> lookTime = entity -> entity.getRandom().nextInt(20) + 20;
    protected Predicate<Player> predicate = pl -> true;

    protected Player target = null;

    /**
     * Set the predicate for the player to look at.
     * @param predicate The predicate
     * @return this
     */
    public SetPlayerLookTarget<E> predicate(Predicate<Player> predicate) {
        this.predicate = predicate;

        return this;
    }

    /**
     * Set the value provider for the chance of the look target being set.
     * @param chance The float provider
     * @return this
     */
    public SetPlayerLookTarget<E> lookChance(FloatProvider chance) {
        this.runChance = chance;

        return this;
    }

    /**
     * Set the value provider for how long the entity's look target should be set for
     * @param function The tick providing function
     * @return this
     */
    public SetPlayerLookTarget<E> lookTime(Function<E, Integer> function) {
        this.lookTime = function;

        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if(entity.getRandom().nextFloat() >= runChance.sample(entity.getRandom())) return false;

        for (Player player : BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_PLAYERS)) {
            if (predicate.test(player)) {
                target = player;

                break;
            }
        }

        return target != null;
    }

    @Override
    protected void start(E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true), lookTime.apply(entity));
    }

    @Override
    protected void stop(E entity) {
        target = null;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }
}
