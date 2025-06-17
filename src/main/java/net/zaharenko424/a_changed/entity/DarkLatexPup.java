package net.zaharenko424.a_changed.entity;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.Difficulty;
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
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BreedWithPartner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomBehaviour;
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
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.DLPupMeltAbility;
import net.zaharenko424.a_changed.attachment.DLPupMeltData;
import net.zaharenko424.a_changed.attachment.LatexPupAgingData;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.SetAttackTarget;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.SetPlayerLookTarget;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.worldgen.Biomes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DarkLatexPup extends AbstractLatexPup implements SmartBrainOwner<DarkLatexPup> {

    protected DarkLatexPup(EntityType<? extends DarkLatexPup> entityType, Level level) {
        super(entityType, level);
    }

    public DarkLatexPup(Level level) {
        super(EntityRegistry.DARK_LATEX_PUP.get(), level);
    }

    public static boolean checkSpawnRules(EntityType<? extends AbstractLatexPup> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return level.getDifficulty() != Difficulty.PEACEFUL
                && level.getBiome(pos).is(Biomes.DARK_LATEX_BIOME)
                && checkMobSpawnRules(type, level, spawnType, pos, random);
    }

    @Override
    public @NotNull TransfurType transfurType() {
        return TransfurRegistry.DARK_LATEX_PUP_TF.get();
    }

    @Override
    public Ability getSelectedAbility() {
        return AbilityRegistry.DL_PUP_MELT.get();
    }

    public boolean isMolten(){
        return AbilityRegistry.DL_PUP_MELT.get().getAbilityData(this).isMolten();
    }

    private static FriendlyByteBuf dummy;

    public void setMolten(boolean molten){
        if(level().isClientSide) return;
        DLPupMeltAbility ability = AbilityRegistry.DL_PUP_MELT.get();
        DLPupMeltData data = ability.getAbilityData(this);
        if(molten == data.isMolten()) return;
        if(molten) {
            if(dummy == null) dummy = new FriendlyByteBuf(Unpooled.wrappedBuffer(new byte[0]));
            ability.activate(this, false, dummy);
        } else ability.deactivate(this);
    }

    @Override
    public boolean isBaby() {
        return AbilityRegistry.DL_PUP_AGE.get().getAbilityData(this).isBaby();
    }

    @Override
    public void setBaby(boolean baby) {
        LatexPupAgingData data = AbilityRegistry.DL_PUP_AGE.get().getAbilityData(this);
        data.setBaby(baby);
    }

    @Override
    public int getAge() {
        return AbilityRegistry.DL_PUP_AGE.get().getAbilityData(this).getAge();
    }

    @Override
    protected Brain.@NotNull Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public @NotNull Brain<DarkLatexPup> getBrain() {
        return (Brain<DarkLatexPup>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        super.customServerAiStep();
    }

    @Override
    public List<? extends ExtendedSensor<? extends DarkLatexPup>> getSensors() {
        return List.of(
                new ItemTemptingSensor<DarkLatexPup>().temptedWith(TamableAnimal::isFood),
                new NearbyLivingEntitySensor<>(),
                new NearbyPlayersSensor<>()
        );
    }

    protected static final int cooldown = 40;
    protected int unMeltCooldown;

    @Override
    public BrainActivityGroup<? extends DarkLatexPup> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new CustomBehaviour<DarkLatexPup>(pup -> {
                        LivingEntity target = BrainUtils.getTargetOfEntity(pup);
                            if(target == null){
                                switch (pup.unMeltCooldown){//Wait a bit before unMelting
                                    case -1 -> pup.unMeltCooldown = cooldown + pup.random.nextInt(20);
                                    case 0 -> {
                                        pup.setMolten(false);
                                        pup.unMeltCooldown = -1;
                                    }
                                    default -> pup.unMeltCooldown--;
                                }
                                return;
                            }
                            float distance = (float) pup.distanceToSqr(target);
                            boolean isMolten = pup.isMolten();

                            if(isMolten) {
                                if(distance <= 1.5f) return;

                                switch (pup.unMeltCooldown){//Wait a bit before unMelting
                                    case -1 -> pup.unMeltCooldown = cooldown + pup.random.nextInt(20);
                                    case 0 -> {
                                        pup.setMolten(false);
                                        pup.unMeltCooldown = -1;
                                    }
                                    default -> pup.unMeltCooldown--;
                                }
                            } else {
                                if(distance > 1) return;
                                pup.setMolten(true);//Push pup into the target
                                pup.setDeltaMovement(target.position().subtract(pup.position()).multiply(.5f, 0, .5f));
                            }
                        }).startCondition(pup -> pup.isMolten() || BrainUtils.getTargetOfEntity(pup) != null),
                new AllApplicableBehaviours<>(
                        new FloatToSurfaceOfFluid<>(),
                        new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(60, 100)),
                        new MoveToWalkTarget<DarkLatexPup>()
                                .startCondition(pup -> !pup.isInSittingPose())
                                .stopIf(TamableAnimal::isInSittingPose)
                ).startCondition(pup -> !pup.isMolten())
        );
    }

    @Override @SuppressWarnings("unchecked")
    public BrainActivityGroup<? extends DarkLatexPup> getIdleTasks() {
        return BrainActivityGroup.<DarkLatexPup>idleTasks(
                new FirstApplicableBehaviour<>(
                        new BreedWithPartner<DarkLatexPup>(),
                        new FollowTemptation<DarkLatexPup>()
                                .whenStarting(pup -> pup.setIsInterested(true))
                                .whenStopping(pup -> pup.setIsInterested(false)),
                        new SetAttackTarget<DarkLatexPup>(false)
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
                ).startCondition(pup -> !pup.isMolten()),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<DarkLatexPup>()
                                .setRadius(16, 8)
                                .startCondition(pup -> !pup.isOrderedToSit()),
                        new SetRandomLookTarget<>()
                                .lookChance(ConstantFloat.of(.1f)),
                        new SetPlayerLookTarget<DarkLatexPup>()
                                .predicate(player -> player.isAlive() && distanceToSqr(player) <= 64)
                                .lookChance(ConstantFloat.of(.2f))
                                .lookTime(pup -> pup.random.nextInt(60, 90)),
                        new Idle<>().runFor(entity -> random.nextInt(60, 90))
                ).startCondition(pup -> !pup.isMolten())
        ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT);
    }

    @Override
    public BrainActivityGroup<? extends DarkLatexPup> getFightTasks() {
        return BrainActivityGroup.<DarkLatexPup>fightTasks(
                new InvalidateAttackTarget<DarkLatexPup>()
                        .whenStopping(latex -> BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET)),
                new ReactToUnreachableTarget<DarkLatexPup>()
                        .reaction((latex, flag) -> latex.setDeltaMovement(latex.getDeltaMovement().add(0, .4, 0)))
                        .startCondition(pup -> !pup.isMolten()),
                new SetWalkTargetToAttackTarget<>()
        ).onlyStartWithMemoryStatus(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT)
                .onlyStartWithMemoryStatus(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT);
    }

    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide || !isAlive()) return;
        AbilityRegistry.DL_PUP_MELT.get().serverTick(this);
        AbilityRegistry.DL_PUP_AGE.get().serverTick(this);
    }

    protected void sitIfOrdered(){
        if(!isTame()) return;
        if(isInWaterOrBubble() || !onGround()){
            setInSittingPose(false);
            return;
        }
        if(isMolten()){
            if(!isOrderedToSit()) return;
            setInSittingPose(false);
            setOrderedToSit(false);
        }
        LivingEntity owner = getOwner();
        if(owner == null) return;
        setInSittingPose((!(distanceToSqr(owner) < 144) || owner.getLastHurtByMob() == null) && isOrderedToSit());
    }

    @Override
    protected void speedUpAging() {
        AbilityRegistry.DL_PUP_AGE.get().getAbilityData(this).speedUpAging();
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return getTargetFromBrain();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        DarkLatexPup dlPup = EntityRegistry.DARK_LATEX_PUP.get().create(level);
        if(dlPup == null || !(otherParent instanceof DarkLatexPup)) return null;

        if(isTame()){
            dlPup.setOwnerUUID(getOwnerUUID());
            dlPup.setTame(true, true);
        }

        return dlPup;
    }
}