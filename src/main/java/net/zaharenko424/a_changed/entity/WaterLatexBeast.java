package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomHeldBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.ReactToUnreachableTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SeekRandomNearbyPosition;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomSwimTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.InWaterSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.entity.ai.behaviour.attack.TryGrab;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.InvalidateWithCallback;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.Retaliate;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.TargetTransfurrable;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.MemoryTypeRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class WaterLatexBeast extends AbstractLatexBeast implements SmartBrainOwner<WaterLatexBeast> {

    public WaterLatexBeast(EntityType<? extends Monster> entityType, Level level, TransfurType transfurType) {
        super(entityType, level, transfurType);
        ((Navigation)navigation).setCanOpenDoors(true);
        moveControl = new Control(this);
        lookControl = new SmoothSwimmingLookControl(this, 25);
        setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes(){
        return AbstractLatexBeast.baseAttributes()
                .add(Attributes.STEP_HEIGHT, 1);
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
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new Navigation(this, pLevel);
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return !isSwimming();
    }

    @Override
    public @NotNull Vec3 getFluidFallingAdjustedMovement(double pGravity, boolean pIsFalling, @NotNull Vec3 pDeltaMovement) {
        if(pIsFalling) pDeltaMovement.multiply(1, 2, 1);
        return pDeltaMovement;
    }

    @Override
    public boolean checkSpawnObstruction(@NotNull LevelReader pLevel) {
        return pLevel.isUnobstructed(this);
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
    public List<Activity> getActivityPriorities() {
        return List.of(AChanged.TRANSFUR_HOLD.get(), AChanged.TRANSFUR_ATTACK.get(), Activity.FIGHT, Activity.SWIM, Activity.IDLE);
    }

    @Override
    public List<? extends ExtendedSensor<? extends WaterLatexBeast>> getSensors() {
        return List.of(
                new HurtBySensor<>(),
                new NearbyLivingEntitySensor<>(),
                new UnreachableTargetSensor<>(),
                new InWaterSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends WaterLatexBeast> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 300)),
                new MoveToWalkTarget<>());
    }

    public static final int LOOK_RANGE_SQR = 6 * 6;

    @Override
    @SuppressWarnings("unchecked")
    public BrainActivityGroup<? extends WaterLatexBeast> getIdleTasks() {// not swimming
        return BrainActivityGroup.idleTasks(
                targetRetaliateLook(LOOK_RANGE_SQR),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>().dontAvoidWater(),
                        new SeekRandomNearbyPosition<>().validPositions((latex, state) -> state.is(Blocks.WATER)),//try find water
                        new Idle<>().runFor(entity -> random.nextInt(60, 90)) // Don't walk anywhere
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends WaterLatexBeast> getFightTasks() {// Retaliate redirects here
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((latex, entity) -> isNonSurvivalOrNonTF(entity)),
                new ReactToUnreachableTarget<>().reaction((latex, flag) -> latex.setDeltaMovement(latex.getDeltaMovement().add(0, .75, 0))),//TODO jump?
                new SetWalkTargetToAttackTarget<>().speedMod((latex, target) -> 1.8f),
                new AnimatableMeleeAttack<>(0)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Activity, BrainActivityGroup<? extends WaterLatexBeast>> getAdditionalTasks() {//  TargetTransfurrable goes here
        return Map.of(
                AChanged.TRANSFUR_ATTACK.get(), new BrainActivityGroup<WaterLatexBeast>(AChanged.TRANSFUR_ATTACK.get()).behaviours(
                        new InvalidateAttackTarget<WaterLatexBeast>().invalidateIf((latex, entity) -> isNonSurvivalOrTF(entity)),
                        new ReactToUnreachableTarget<>().reaction((latex, flag) -> latex.setDeltaMovement(latex.getDeltaMovement().add(0, .75, 0))),//TODO jump?
                        new SetWalkTargetToAttackTarget<>().speedMod((latex, target) -> 1.8f),
                        new CustomHeldBehaviour<AbstractLatexBeast>(latex -> AbilityRegistry.HYPNOSIS_ABILITY.get().serverTick(latex))
                                .startCondition(latex -> latex.hasAbility(AbilityRegistry.HYPNOSIS_ABILITY))
                                .stopIf(latex -> !BrainUtils.hasMemory(latex, MemoryTypeRegistry.TRYING_TO_TRANSFUR.get())),
                        new FirstApplicableBehaviour<>(
                                new TryGrab<>(),
                                new AnimatableMeleeAttack<>(0)
                        )
                ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                .requireAndWipeMemoriesOnUse(MemoryTypeRegistry.TRYING_TO_TRANSFUR.get()),

                AChanged.TRANSFUR_HOLD.get(), new BrainActivityGroup<WaterLatexBeast>(AChanged.TRANSFUR_HOLD.get()).behaviours(
                        new InvalidateWithCallback<>()
                                .onInvalidate((latex, entity) -> {
                                    BrainUtils.clearMemory(latex, MemoryTypeRegistry.TRANSFUR_HOLDING.get());
                                    AbilityRegistry.GRAB_ABILITY.get().deactivate(latex);
                                }).invalidateIf((latex, entity) -> isNonSurvivalOrTF(entity) || GrabData.dataOf(latex).getGrabbedEntity() == null),
                        new CustomHeldBehaviour<>(latex -> AbilityRegistry.GRAB_ABILITY.get().serverTick(latex))
                                .stopIf(latex -> !BrainUtils.hasMemory(latex, MemoryTypeRegistry.TRANSFUR_HOLDING.get()))
                ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                .requireAndWipeMemoriesOnUse(MemoryTypeRegistry.TRANSFUR_HOLDING.get()),

                Activity.SWIM, new BrainActivityGroup<WaterLatexBeast>(Activity.SWIM).behaviours(
                        new FirstApplicableBehaviour<>(
                                new TargetTransfurrable<>(),
                                new Retaliate<>(),
                                new OneRandomBehaviour<>(
                                        new SetPlayerLookTarget<>()
                                                .predicate(player -> player.isAlive() && distanceToSqr(player) < LOOK_RANGE_SQR)
                                                .runFor(entity -> random.nextInt(60, 120)),
                                        new SetRandomLookTarget<>())
                        ),
                        new OneRandomBehaviour<>(
                                new SetRandomSwimTarget<>().speedModifier(1.5f).setRadius(16),// swimming
                                new Idle<>().runFor(entity -> random.nextInt(60, 90)) // Don't walk anywhere
                        )
                ).onlyStartWithMemoryStatus(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT)
        );
    }

    static class Navigation extends AmphibiousPathNavigation {

        public Navigation(Mob p_217788_, Level p_217789_) {
            super(p_217788_, p_217789_);
        }

        public void setCanOpenDoors(boolean pCanOpenDoors) {
            this.nodeEvaluator.setCanOpenDoors(pCanOpenDoors);
        }
    }

    static class Control extends SmoothSwimmingMoveControl {

        public Control(Mob pMob) {
            super(pMob, 80, 40, 1, .5f, false);
        }
    }
}