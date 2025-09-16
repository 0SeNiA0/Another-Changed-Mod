package net.zaharenko424.a_changed.entity;

import com.mojang.datafixers.util.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomHeldBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.ReactToUnreachableTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToBlock;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.zaharenko424.a_changed.attachment.GrabData;
import net.zaharenko424.a_changed.entity.ai.behaviour.attack.TryGrab;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.InvalidateWithCallback;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.RetaliateOrTransfur;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.TargetTransfurrable;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.ActivityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.MemoryTypeRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class PureWhiteLatex extends LatexBeast {

    public PureWhiteLatex(@NotNull EntityType<? extends Monster> type, @NotNull Level level, @NotNull TransfurType<?> transfurType) {
        super(type, level, transfurType);
    }

    @Override
    public List<? extends ExtendedSensor<? extends LatexBeast>> getSensors() {
        return List.of(
                new HurtBySensor<>(),
                new NearbyLivingEntitySensor<>(),
                new UnreachableTargetSensor<>(),
                new NearbyBlocksSensor<PureWhiteLatex>().setRadius(16).setPredicate((state, latex) ->
                        state.is(BlockRegistry.WHITE_LATEX_PILLAR) && state.getValue(StateProperties.PART2) == 1)
        );
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return List.of(ActivityRegistry.TRANSFUR_HOLD.get(), ActivityRegistry.TRANSFUR_GRAB_ESCAPE_STUN.get(), ActivityRegistry.TRANSFUR_ATTACK.get(), Activity.FIGHT, Activity.HIDE, Activity.IDLE);
    }

    @Override
    public BrainActivityGroup<? extends LatexBeast> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                targetRetaliateLook(LOOK_RANGE_SQR),
                new FirstApplicableBehaviour<>(
                        new CustomBehaviour<PureWhiteLatex>(latex -> {
                            BrainUtils.setMemory(latex, MemoryTypeRegistry.INSIDE_PILLAR.get(), true);
                            latex.setPersistenceRequired();
                            latex.moveTo(latex.blockPosition(), latex.getYRot(), latex.getXRot());
                            BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET);
                            BrainUtils.clearMemory(latex, MemoryModuleType.WALK_TARGET);
                        }).startCondition(latex -> {
                            boolean inPillar = latex.getInBlockState().is(BlockRegistry.WHITE_LATEX_PILLAR)
                                    && latex.level().getEntities(latex, latex.getInBlockState().getShape(latex.level(), latex.blockPosition()).bounds().move(latex.blockPosition()), entity -> entity instanceof PureWhiteLatex).isEmpty();
                            if(!inPillar && BrainUtils.hasMemory(latex, MemoryTypeRegistry.INSIDE_PILLAR.get())) {
                                BrainUtils.clearMemory(latex, MemoryTypeRegistry.INSIDE_PILLAR.get());
                                BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET);
                            }
                            return inPillar;
                        }),
                        new SetWalkTargetToBlock<>().predicate((latex, pair) -> {
                            Level level = latex.level();
                            BlockPos pos = pair.getFirst();

                            if(!level.getBlockState(pos).is(BlockRegistry.WHITE_LATEX_PILLAR)) {
                                WalkTarget target = BrainUtils.getMemory(latex, MemoryModuleType.WALK_TARGET);
                                if(target != null && target.getTarget().currentBlockPosition().equals(pos)){
                                    BrainUtils.clearMemory(latex, MemoryModuleType.WALK_TARGET);
                                    BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET);
                                }

                                return false;
                            }

                            List<LivingEntity> entities = BrainUtils.getMemory(latex, MemoryModuleType.NEAREST_LIVING_ENTITIES);
                            if(entities != null){
                                WalkTarget targetOfOther;
                                for(LivingEntity entity : entities){
                                    if(!(entity instanceof PureWhiteLatex other)) continue;

                                    targetOfOther = BrainUtils.getMemory(other, MemoryModuleType.WALK_TARGET);
                                    if(targetOfOther == null) continue;

                                    if(targetOfOther.getTarget().currentBlockPosition().equals(pos)) return false;
                                }
                            }

                            return level.getEntities(latex, pair.getSecond().getShape(level, pos).bounds().move(pos), entity -> entity instanceof PureWhiteLatex).isEmpty();
                        }).closeEnoughWhen((latex, pos) -> 0),
                        new OneRandomBehaviour<>(
                            new SetRandomWalkTarget<>().speedModifier(.8f).setRadius(16),// non swimming
                            new Idle<>().runFor(entity -> random.nextInt(60, 90)) // Don't walk anywhere
                ))
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Activity, BrainActivityGroup<? extends LatexBeast>> getAdditionalTasks() {//  TargetTransfurrable goes here
        return Map.of(
                ActivityRegistry.TRANSFUR_ATTACK.get(), new BrainActivityGroup<LatexBeast>(ActivityRegistry.TRANSFUR_ATTACK.get()).behaviours(
                        new InvalidateAttackTarget<LatexBeast>().invalidateIf((latex, entity) -> isNonSurvivalOrTF(entity)).whenStopping(latex -> BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET)),
                        new ReactToUnreachableTarget<>().reaction((latex, flag) -> latex.setDeltaMovement(latex.getDeltaMovement().add(0, .75, 0))),
                        new SetWalkTargetToAttackTarget<>(),
                        new CustomHeldBehaviour<AbstractLatexBeast>(latex -> AbilityRegistry.HYPNOSIS_ABILITY.get().serverTick(latex))
                                .startCondition(latex -> latex.hasAbility(AbilityRegistry.HYPNOSIS_ABILITY))
                                .stopIf(latex -> !BrainUtils.hasMemory(latex, MemoryTypeRegistry.TRYING_TO_TRANSFUR.get())),
                        new FirstApplicableBehaviour<>(
                                new TryGrab<>(),
                                new AnimatableMeleeAttack<>(0)
                                        .startCondition(latex -> latex.getTarget() != null && !TransfurManager.isGrabbed(latex.getTarget()))
                        )
                ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                .requireAndWipeMemoriesOnUse(MemoryTypeRegistry.TRYING_TO_TRANSFUR.get()),

                ActivityRegistry.TRANSFUR_HOLD.get(), new BrainActivityGroup<LatexBeast>(ActivityRegistry.TRANSFUR_HOLD.get()).behaviours(
                        new InvalidateWithCallback<>()
                                .onInvalidate((latex, entity) -> {
                                    BrainUtils.clearMemory(latex, MemoryTypeRegistry.TRANSFUR_HOLDING.get());
                                    AbilityRegistry.GRAB_ABILITY.get().deactivate(latex);
                                    BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET);
                                }).invalidateIf((latex, entity) -> {
                                    if(isNonSurvivalOrTF(entity)) return true;

                                    GrabData data = GrabData.dataOf(latex);
                                    if(data.getGrabbedEntity() != null) return false;

                                    BrainUtils.setForgettableMemory(latex, MemoryTypeRegistry.TRANSFUR_GRAB_ESCAPE_STUN.get(), Unit.INSTANCE, 100);
                                    return true;
                                }),
                        new CustomHeldBehaviour<>(latex -> AbilityRegistry.GRAB_ABILITY.get().serverTick(latex))
                                .stopIf(latex -> !BrainUtils.hasMemory(latex, MemoryTypeRegistry.TRANSFUR_HOLDING.get()))
                ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                .requireAndWipeMemoriesOnUse(MemoryTypeRegistry.TRANSFUR_HOLDING.get()),

                Activity.HIDE, new BrainActivityGroup<LatexBeast>(Activity.HIDE).behaviours(
                        new FirstApplicableBehaviour<>(
                            new CustomBehaviour<>(latex ->
                                    BrainUtils.clearMemory(latex, MemoryTypeRegistry.INSIDE_PILLAR.get()))
                            .startCondition(latex -> !latex.getInBlockState().is(BlockRegistry.WHITE_LATEX_PILLAR)),
                            new TargetTransfurrable<>(),
                            new RetaliateOrTransfur<>())
                ).requireAndWipeMemoriesOnUse(MemoryTypeRegistry.INSIDE_PILLAR.get()),

                ActivityRegistry.TRANSFUR_GRAB_ESCAPE_STUN.get(), new BrainActivityGroup<LatexBeast>(ActivityRegistry.TRANSFUR_GRAB_ESCAPE_STUN.get()).behaviours(
                        new Idle<>()
                ).requireAndWipeMemoriesOnUse(MemoryTypeRegistry.TRANSFUR_GRAB_ESCAPE_STUN.get())
        );
    }
}