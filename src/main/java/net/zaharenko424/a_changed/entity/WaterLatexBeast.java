package net.zaharenko424.a_changed.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToBlock;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.GenericAttackTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.InWaterSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.ai.behaviour.SetRandomSwimTarget;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WaterLatexBeast extends AbstractLatexBeast implements SmartBrainOwner<WaterLatexBeast> {

    public boolean wasTargetTransfurred = false;

    public WaterLatexBeast(EntityType<? extends Monster> entityType, Level level, AbstractTransfurType transfurType) {
        super(entityType, level, transfurType);
        //moveControl = new WaterLatexMoveControl(this);
        moveControl = new Control(this);
        lookControl = new SmoothSwimmingLookControl(this, 25);
        setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes(){
        return AbstractLatexBeast.baseAttributes()
                .add(NeoForgeMod.STEP_HEIGHT.value(), 1);
    }

    @Override
    protected boolean hasBrain() {
        return true;
    }

    public static boolean checkSpawnRules(EntityType<? extends AbstractLatexBeast> p_219014_, @NotNull ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        if(level.getDifficulty() == Difficulty.PEACEFUL) return false;
        if(!level.getFluidState(pos.below()).is(FluidTags.WATER) && !MobSpawnType.isSpawner(spawnType)) return false;

        return MobSpawnType.isSpawner(spawnType) || (random.nextInt(20) == 0 && isDeepEnoughToSpawn(level, pos));
    }

    private static boolean isDeepEnoughToSpawn(@NotNull LevelAccessor pLevel, @NotNull BlockPos pPos) {
        return pPos.getY() < pLevel.getSeaLevel() - 5;
    }

    @Override
    public boolean checkSpawnObstruction(@NotNull LevelReader pLevel) {
        return pLevel.isUnobstructed(this);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    protected Brain.@NotNull Provider<WaterLatexBeast> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public @NotNull Brain<WaterLatexBeast> getBrain() {
        return (Brain<WaterLatexBeast>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new Navigation(this, pLevel);
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return !isSwimming();
    }

    public void travel(@NotNull Vec3 vec3) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(vec3);
        }
    }

    public static final int DETECTION_DISTANCE = 24 * 24;

    @Override
    public List<? extends ExtendedSensor<? extends WaterLatexBeast>> getSensors() {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<WaterLatexBeast>().setRadius(24).setPredicate((target, latex) -> {
                    if(target instanceof Player player) return !player.isCreative() && !player.isSpectator();
                    return target.getType().is(AChanged.TRANSFURRABLE_TAG);
                }),

                new HurtBySensor<WaterLatexBeast>().setPredicate((source, latex) ->
                        !(source.getEntity() instanceof Player player) || (!player.isSpectator() && !player.isCreative())),

                new GenericAttackTargetSensor<WaterLatexBeast>().setPredicate((target, latex) -> {
                    Optional<LivingEntity> hurtBy = latex.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY);
                    if(target instanceof Player player){
                        if(player.isCreative() || player.isSpectator()) return false;
                        if(!TransfurManager.isTransfurred(player)){
                            latex.wasTargetTransfurred = false;
                            return true;
                        }
                        latex.wasTargetTransfurred = true;
                        return hurtBy.orElse(null) == target;
                    }
                    if(!target.getType().is(AChanged.TRANSFURRABLE_TAG)) return hurtBy.orElse(null) == target;
                    return true;
                }),

                new InWaterSensor<>(),
                new NearbyBlocksSensor<WaterLatexBeast>().setRadius(16).setPredicate((block, latex) ->
                        block.getFluidState().is(Fluids.WATER))
        );
    }

    @Override
    public BrainActivityGroup<? extends WaterLatexBeast> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends WaterLatexBeast> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<WaterLatexBeast>().invalidateIf((latex, target) -> {
                    if(target instanceof Player player){
                        if(player.isSpectator() || player.isCreative()) return true;
                        return TransfurManager.isTransfurred(player) && !wasTargetTransfurred;
                    }
                    return distanceToSqr(target) > DETECTION_DISTANCE;
                }),
                new SetWalkTargetToAttackTarget<>(),
                new AnimatableMeleeAttack<>(10)
        );
    }

    @Override
    public BrainActivityGroup<? extends WaterLatexBeast> getIdleTasks() {
        return BrainActivityGroup.<WaterLatexBeast>idleTasks(
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<>().attackablePredicate(target -> {
                            Optional<LivingEntity> hurtBy = getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY);
                            if(target instanceof Player player){
                                if(player.isCreative() || player.isSpectator()) return false;
                                if(!TransfurManager.isTransfurred(player)){
                                    wasTargetTransfurred = false;
                                    return true;
                                }
                                wasTargetTransfurred = true;
                                return hurtBy.orElse(null) == target;
                            }
                            if(!target.getType().is(AChanged.TRANSFURRABLE_TAG)) return hurtBy.orElse(null) == target;
                            return true;
                        }),
                        new SetPlayerLookTarget<>().predicate(player -> distanceToSqr(player) <= DETECTION_DISTANCE),
                        new SetRandomLookTarget<>().lookChance(ConstantFloat.of(.4f))),
                new OneRandomBehaviour<>(
                        new SetWalkTargetToBlock<>().speedMod((mob, pair) -> 1f),
                        new SetRandomWalkTarget<>().dontAvoidWater().setRadius(10),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        ).onlyStartWithMemoryStatus(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT);
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends WaterLatexBeast>> getAdditionalTasks() {
        return Map.of(
                Activity.SWIM, new BrainActivityGroup<WaterLatexBeast>(Activity.SWIM).priority(10).behaviours(
                     new FirstApplicableBehaviour<>(
                             new TargetOrRetaliate<>().attackablePredicate(target -> {
                                 Optional<LivingEntity> hurtBy = getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY);
                                 if(target instanceof Player player){
                                     if(player.isCreative() || player.isSpectator()) return false;
                                     if(!TransfurManager.isTransfurred(player)){
                                         wasTargetTransfurred = false;
                                         return true;
                                     }
                                     wasTargetTransfurred = true;
                                     return hurtBy.orElse(null) == target;
                                 }
                                 if(!target.getType().is(AChanged.TRANSFURRABLE_TAG)) return hurtBy.orElse(null) == target;
                                 return true;
                             }),
                             new SetPlayerLookTarget<>().predicate(player -> distanceToSqr(player) <= DETECTION_DISTANCE && player.getRandom().nextFloat() > .5),
                             new SetRandomLookTarget<>().lookChance(ConstantFloat.of(.4f))),
                     new OneRandomBehaviour<>(
                             new SetRandomSwimTarget<>().setRadius(10),
                             new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                     )
                ).onlyStartWithMemoryStatus(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT)
        );
    }

    @Override
    public void updateSwimming() {
        if(isInWaterOrBubble() || isUnderWater()){
            setSwimming(true);
            setPose(Pose.SWIMMING);
        } else {
            setSwimming(false);
            setPose(Pose.STANDING);
        }
    }

    static class Navigation extends AmphibiousPathNavigation {

        public Navigation(Mob p_217788_, Level p_217789_) {
            super(p_217788_, p_217789_);
        }

        public boolean canCutCorner(@NotNull BlockPathTypes pathTypes) {
            return pathTypes != BlockPathTypes.WATER_BORDER && super.canCutCorner(pathTypes);
        }
    }

    static class Control extends SmoothSwimmingMoveControl {

        public Control(Mob pMob) {
            super(pMob, 80, 40, 1, .5f, false);
        }
    }

    static class WaterLatexMoveControl extends MoveControl {
        WaterLatexBeast waterLatex;

        public WaterLatexMoveControl(WaterLatexBeast pMob) {
            super(pMob);
            waterLatex = pMob;
        }

        @Override
        public void tick() {
            if(!mob.getNavigation().isDone() && !mob.getNavigation().isStuck()){
                Brain<WaterLatexBeast> brain = waterLatex.getBrain();
                if(operation == Operation.MOVE_TO){
                    Optional<WalkTarget> target = brain.getMemory(MemoryModuleType.WALK_TARGET);
                    if(target.isEmpty()){
                        waterLatex.setSpeed(0);
                        return;
                    }

                    Vec3 targetVec = target.get().getTarget().currentPosition();
                    double targetDist = waterLatex.distanceToSqr(targetVec);
                    if(targetDist <= 1) {
                        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                        waterLatex.setSpeed(0);
                        return;
                    }

                    Vec3 wantedPos = new Vec3(wantedX, wantedY, wantedZ);

                    Vec3 step = targetDist < waterLatex.distanceToSqr(wantedPos) ? targetVec : wantedPos;

                    if(brain.getMemory(MemoryModuleType.NEAREST_ATTACKABLE).isPresent()){
                        waterLatex.lookAt(brain.getMemory(MemoryModuleType.NEAREST_ATTACKABLE).get(), 45, 45);
                    } else waterLatex.lookAt(EntityAnchorArgument.Anchor.FEET, !waterLatex.isInWaterOrBubble() ? targetVec : step);

                    if(waterLatex.isInWaterOrBubble()) {
                        double moveY = step.y - waterLatex.getY();
                        targetDist = waterLatex.distanceToSqr(step);

                        if (moveY != 0)
                            mob.setDeltaMovement(mob.getDeltaMovement().add(0.0, (double) mob.getSpeed() * (moveY / targetDist) * 0.2, 0.0));

                        waterLatex.setSpeed((float) (speedModifier * mob.getAttributeValue(NeoForgeMod.SWIM_SPEED)));
                    } else waterLatex.setSpeed((float) (speedModifier * waterLatex.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if(waterLatex.onGround() && !waterLatex.isInWaterOrBubble()) {
                        Vec3 diff = step.subtract(waterLatex.position());
                        BlockPos blockpos = mob.blockPosition();
                        BlockState blockstate = mob.level().getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate.getCollisionShape(mob.level(), blockpos);
                        AChanged.LOGGER.warn("diff y " + diff.y);
                        if(diff.y > 0 && diff.y <= 1){
                            waterLatex.jumpFromGround();
                            operation = Operation.JUMPING;
                            return;
                        }

                        AChanged.LOGGER.warn("test 1 " + (diff.y > (double) mob.getStepHeight()) + " " + (diff.x * diff.x + diff.z * diff.z < (double) Math.max(1.0F, mob.getBbWidth())));
                        AChanged.LOGGER.warn("test 2 " + !voxelshape.isEmpty() + " " + (mob.getY() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.getY()));
                        if (diff.y > (double) mob.getStepHeight() && diff.x * diff.x + diff.z * diff.z < (double) Math.max(1.0F, mob.getBbWidth())
                                || !voxelshape.isEmpty()
                                    && mob.getY() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.getY()
                                    && !blockstate.is(BlockTags.DOORS)
                                    && !blockstate.is(BlockTags.FENCES)) {
                            AChanged.LOGGER.warn("jumping!");
                            mob.getJumpControl().jump();
                            operation = MoveControl.Operation.JUMPING;
                        }
                    }
                } else if(operation == Operation.JUMPING){
                    if(mob.isInWaterOrBubble()){
                        operation = Operation.MOVE_TO;
                        return;
                    }
                    mob.setSpeed((float)(speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (mob.onGround()) operation = Operation.MOVE_TO;
                }
            } else mob.setSpeed(0);
        }
    }
}