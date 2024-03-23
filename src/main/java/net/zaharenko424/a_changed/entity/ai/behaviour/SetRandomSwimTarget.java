package net.zaharenko424.a_changed.entity.ai.behaviour;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SetRandomSwimTarget <E extends PathfinderMob> extends SetRandomWalkTarget<E> {

    protected Predicate<E> avoidLandPredicate = (entity) -> true;

    public SetRandomSwimTarget<E> dontAvoidLand() {
        return this.avoidLandWhen((entity) -> false);
    }

    public SetRandomSwimTarget<E> avoidLandWhen(Predicate<E> predicate) {
        this.avoidWaterPredicate = predicate;
        return this;
    }

    protected void start(E penguin) {
        Vec3 targetPos = this.getTargetPos(penguin);
        if (!this.positionPredicate.test(penguin, targetPos)) {
            targetPos = null;
        }

        if (targetPos == null) {
            BrainUtils.clearMemory(penguin, MemoryModuleType.WALK_TARGET);
        } else {
            BrainUtils.setMemory(penguin, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier.apply(penguin, targetPos), 2));
        }

    }

    protected @Nullable Vec3 getTargetPos(E penguin) {
        return this.avoidLandPredicate.test(penguin) ? BehaviorUtils.getRandomSwimmablePos(penguin, (int)this.radius.xzRadius(), (int)this.radius.yRadius()) : DefaultRandomPos.getPos(penguin, (int)this.radius.xzRadius(), (int)this.radius.yRadius());
    }
}