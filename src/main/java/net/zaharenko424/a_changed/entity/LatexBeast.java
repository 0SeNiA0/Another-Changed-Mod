package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
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
import net.zaharenko424.a_changed.worldgen.Biomes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class LatexBeast extends AbstractLatexBeast implements SmartBrainOwner<LatexBeast> {

    public LatexBeast(@NotNull EntityType<? extends Monster> p_21368_, @NotNull Level p_21369_, @NotNull TransfurType transfurType) {
        super(p_21368_, p_21369_, transfurType);
        ((GroundPathNavigation)navigation).setCanOpenDoors(true);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes(){
        return baseAttributes();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new SmoothGroundNavigation(this, level());
    }

    public static boolean checkDarkLatexSpawn(EntityType<? extends LatexBeast> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return level.getDifficulty() != Difficulty.PEACEFUL
                && (isDarkEnoughToSpawn(level, pos, random) || level.getBiome(pos).is(Biomes.DARK_LATEX_BIOME))
                && checkMobSpawnRules(type, level, spawnType, pos, random);
    }

    public static boolean checkWhiteLatexSpawn(EntityType<? extends LatexBeast> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return level.getDifficulty() != Difficulty.PEACEFUL
                && (isDarkEnoughToSpawn(level, pos, random) || level.getBiome(pos).is(Biomes.WHITE_LATEX_BIOME))
                && checkMobSpawnRules(type, level, spawnType, pos, random);
    }

    @Override
    protected Brain.@NotNull Provider<LatexBeast> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public @NotNull Brain<LatexBeast> getBrain() {
        return (Brain<LatexBeast>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        tickBrain(this);
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return List.of(AChanged.TRANSFUR_HOLD.get(), AChanged.TRANSFUR_ATTACK.get(), Activity.FIGHT, Activity.IDLE);
    }

    @Override
    public List<? extends ExtendedSensor<? extends LatexBeast>> getSensors() {
        return List.of(
                new HurtBySensor<>(),
                new NearbyLivingEntitySensor<>(),
                new UnreachableTargetSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends LatexBeast> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 300)),
                new MoveToWalkTarget<>());
    }

    public static final int LOOK_RANGE_SQR = 6 * 6;

    @Override
    @SuppressWarnings("unchecked")
    public BrainActivityGroup<? extends LatexBeast> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<>(
                        new TargetTransfurrable<>(),
                        new Retaliate<>(),
                        new OneRandomBehaviour<>(
                                new SetPlayerLookTarget<>()
                                        .predicate(player -> player.isAlive() && distanceToSqr(player) < LOOK_RANGE_SQR)
                                        .stopIf(entity -> !entity.isAlive() || distanceToSqr(entity) > LOOK_RANGE_SQR)
                                        .runFor(entity -> random.nextInt(60, 120)),
                                new SetRandomLookTarget<>())
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>().speedModifier(.8f).setRadius(16),// non swimming
                        new Idle<>().runFor(entity -> random.nextInt(60, 90)) // Don't walk anywhere
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends LatexBeast> getFightTasks() {// Retaliate redirects here
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((latex, entity) -> isNonSurvivalOrNonTF(entity)),
                new ReactToUnreachableTarget<>().reaction((latex, flag) -> latex.setDeltaMovement(latex.getDeltaMovement().add(0, .75, 0))),//TODO jump? 1 ~= 3 blocks
                new SetWalkTargetToAttackTarget<>(),
                new AnimatableMeleeAttack<>(0)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Activity, BrainActivityGroup<? extends LatexBeast>> getAdditionalTasks() {//  TargetTransfurrable goes here
        return Map.of(
                AChanged.TRANSFUR_ATTACK.get(), new BrainActivityGroup<LatexBeast>(AChanged.TRANSFUR_ATTACK.get()).behaviours(
                        new InvalidateAttackTarget<LatexBeast>().invalidateIf((latex, entity) -> isNonSurvivalOrTF(entity)),
                        new ReactToUnreachableTarget<>().reaction((latex, flag) -> latex.setDeltaMovement(latex.getDeltaMovement().add(0, .75, 0))),//TODO jump?
                        new SetWalkTargetToAttackTarget<>(),
                        new CustomHeldBehaviour<AbstractLatexBeast>(latex -> AbilityRegistry.HYPNOSIS_ABILITY.get().serverTick(latex))
                                .startCondition(latex -> latex.hasAbility(AbilityRegistry.HYPNOSIS_ABILITY))
                                .stopIf(latex -> !BrainUtils.hasMemory(latex, MemoryTypeRegistry.TRYING_TO_TRANSFUR.get())),
                        new FirstApplicableBehaviour<>(
                                new TryGrab<>(),
                                new AnimatableMeleeAttack<>(0)
                        )
                ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                .requireAndWipeMemoriesOnUse(MemoryTypeRegistry.TRYING_TO_TRANSFUR.get()),

                AChanged.TRANSFUR_HOLD.get(), new BrainActivityGroup<LatexBeast>(AChanged.TRANSFUR_HOLD.get()).behaviours(
                        new InvalidateWithCallback<>()
                                .onInvalidate((latex, entity) -> {
                                        BrainUtils.clearMemory(latex, MemoryTypeRegistry.TRANSFUR_HOLDING.get());
                                        AbilityRegistry.GRAB_ABILITY.get().deactivate(latex);
                                }).invalidateIf((latex, entity) -> isNonSurvivalOrTF(entity) || GrabData.dataOf(latex).getGrabbedEntity() == null),
                        new CustomHeldBehaviour<>(latex -> AbilityRegistry.GRAB_ABILITY.get().serverTick(latex))
                                .stopIf(latex -> !BrainUtils.hasMemory(latex, MemoryTypeRegistry.TRANSFUR_HOLDING.get()))
                ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                .requireAndWipeMemoriesOnUse(MemoryTypeRegistry.TRANSFUR_HOLDING.get())
        );
    }
}