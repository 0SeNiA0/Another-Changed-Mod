package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BreedWithPartner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.ReactToUnreachableTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowOwner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.ItemTemptingSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.attachment.LatexPupAgingData;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.SetAttackTarget;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.SetPlayerLookTarget;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.worldgen.Biomes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WhiteLatexPup extends AbstractLatexPup implements SmartBrainOwner<WhiteLatexPup> {

    protected WhiteLatexPup(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public WhiteLatexPup(Level level){
        super(EntityRegistry.WHITE_LATEX_PUP.get(), level);
    }

    public static boolean checkSpawnRules(EntityType<? extends AbstractLatexPup> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return level.getDifficulty() != Difficulty.PEACEFUL
                && level.getBiome(pos).is(Biomes.WHITE_LATEX_BIOME)
                && checkMobSpawnRules(type, level, spawnType, pos, random);
    }

    @Override
    public @NotNull TransfurType<WhiteLatexPup> transfurType() {
        return TransfurRegistry.WHITE_LATEX_PUP_TF.get();
    }

    @Override
    public Ability getSelectedAbility() {
        return null;
    }

    @Override
    public boolean isBaby() {
        return AbilityRegistry.WL_PUP_AGE.get().getAbilityData(this).isBaby();
    }

    @Override
    public void setBaby(boolean baby) {
        LatexPupAgingData data = AbilityRegistry.WL_PUP_AGE.get().getAbilityData(this);
        data.setBaby(baby);
    }

    @Override
    public int getAge() {
        return AbilityRegistry.WL_PUP_AGE.get().getAbilityData(this).getAge();
    }

    @Override
    protected Brain.@NotNull Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public @NotNull Brain<WhiteLatexPup> getBrain() {
        return (Brain<WhiteLatexPup>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        super.customServerAiStep();
    }

    @Override
    public List<? extends ExtendedSensor<? extends WhiteLatexPup>> getSensors() {
        return List.of(
                new ItemTemptingSensor<WhiteLatexPup>().temptedWith(TamableAnimal::isFood),
                new NearbyLivingEntitySensor<>(),
                new NearbyPlayersSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends WhiteLatexPup> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(60, 100)),
                new MoveToWalkTarget<WhiteLatexPup>()
                        .startCondition(pup -> !pup.isInSittingPose())
                        .stopIf(TamableAnimal::isInSittingPose)
        );
    }

    @Override
    public BrainActivityGroup<? extends WhiteLatexPup> getIdleTasks() {
        return BrainActivityGroup.<WhiteLatexPup>idleTasks(
                new FirstApplicableBehaviour<>(
                        new BreedWithPartner<WhiteLatexPup>(),
                        new FollowTemptation<WhiteLatexPup>()
                                .whenStarting(pup -> pup.setIsInterested(true))
                                .whenStopping(pup -> pup.setIsInterested(false)),
                        new SetAttackTarget<WhiteLatexPup>(false)
                                .targetFinder(pup -> {
                                    LivingEntity target;

                                    LivingEntity owner = pup.getOwner();
                                    if(owner != null){
                                        target = owner.getLastHurtMob();
                                        if(target == null) target = owner.getLastHurtByMob();
                                        if(target != null && pup.wantsToAttack(target, owner)){
                                            pup.setOrderedToSit(false);
                                            pup.setInSittingPose(false);
                                            return target;
                                        }
                                    }
                                    if(pup.isOrderedToSit()) return null;

                                    NearestVisibleLivingEntities entities = BrainUtils.getMemory(pup, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
                                    if(entities == null) return null;
                                    target = entities.findClosest(target1 ->
                                                    target1.isAlive() && (!(target1 instanceof Player player) || (!player.isCreative() && pup.getOwner() != player)) && DamageSources.checkTFTarget(target1))
                                            .orElse(null);
                                    return target == null ? null : (pup.wantsToAttack(target, owner) ? target : null);
                                }),
                        new FollowOwner<>()
                                .startCondition(pup -> !pup.isOrderedToSit())
                                .stopIf(TamableAnimal::isInSittingPose)
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<WhiteLatexPup>()
                                .setRadius(16, 8)
                                .startCondition(pup -> !pup.isOrderedToSit()),
                        new SetRandomLookTarget<>()
                                .lookChance(ConstantFloat.of(.1f)),
                        new SetPlayerLookTarget<WhiteLatexPup>()
                                .predicate(player -> player.isAlive() && distanceToSqr(player) <= 64)
                                .lookChance(ConstantFloat.of(.2f))
                                .lookTime(pup -> pup.random.nextInt(60, 90)),
                        new Idle<>().runFor(entity -> random.nextInt(60, 90))
                )
        ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT);
    }

    @Override
    public BrainActivityGroup<? extends WhiteLatexPup> getFightTasks() {
        return BrainActivityGroup.<WhiteLatexPup>fightTasks(
                        new InvalidateAttackTarget<WhiteLatexPup>()
                                .whenStopping(latex -> BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET)),
                        new ReactToUnreachableTarget<WhiteLatexPup>()
                                .reaction((latex, flag) -> latex.setDeltaMovement(latex.getDeltaMovement().add(0, .4, 0))),
                        new SetWalkTargetToAttackTarget<>(),
                        new AnimatableMeleeAttack<>(0)
                ).onlyStartWithMemoryStatus(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT)
                .onlyStartWithMemoryStatus(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if(level().isClientSide || !DamageSources.checkTFTarget(target)) return super.doHurtTarget(target);

        if(!isTame()) {
            target.hurt(DamageSources.transfur(this), 0.1f);
            TransfurHandler.of((LivingEntity) target).addTransfurProgress(TransfurManager.TRANSFUR_TOLERANCE / 2,
                    TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF.get(), TransfurContext.DEF);
            ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
            discard();
            return true;
        }

        if(!target.hurt(DamageSources.transfur(this), 0.1f)) return false;

        setLastHurtMob(target);
        TransfurHandler.nonNullOf((LivingEntity) target)
                .addTransfurProgress(5, TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF.get(), TransfurContext.DEF);
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide || !isAlive()) return;
        AbilityRegistry.WL_PUP_AGE.get().serverTick(this);
    }

    @Override
    protected void speedUpAging() {
        AbilityRegistry.WL_PUP_AGE.get().getAbilityData(this).speedUpAging();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        WhiteLatexPup wlPup = EntityRegistry.WHITE_LATEX_PUP.get().create(level);
        if(wlPup == null || !(otherParent instanceof WhiteLatexPup)) return null;

        if(isTame()){
            wlPup.setOwnerUUID(getOwnerUUID());
            wlPup.setTame(true, true);
        }

        return wlPup;
    }
}
