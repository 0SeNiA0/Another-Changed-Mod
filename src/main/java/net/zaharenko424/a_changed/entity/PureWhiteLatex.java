package net.zaharenko424.a_changed.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
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
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.entity.ai.behaviour.attack.TryGrab;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.InvalidateWithCallback;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.Retaliate;
import net.zaharenko424.a_changed.entity.ai.behaviour.target.TargetTransfurrable;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.MemoryTypeRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class PureWhiteLatex extends LatexBeast {

    public PureWhiteLatex(@NotNull EntityType<? extends Monster> p_21368_, @NotNull Level p_21369_, @NotNull TransfurType transfurType) {
        super(p_21368_, p_21369_, transfurType);
    }

    @Override
    public List<? extends ExtendedSensor<? extends LatexBeast>> getSensors() {
        return List.of(
                new HurtBySensor<>(),
                new NearbyLivingEntitySensor<>(),
                new UnreachableTargetSensor<>(),
                new NearbyBlocksSensor<PureWhiteLatex>().setRadius(16).setPredicate((state, latex) -> state.is(BlockRegistry.WHITE_LATEX_PILLAR))
        );
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return List.of(AChanged.TRANSFUR_HOLD.get(), AChanged.TRANSFUR_ATTACK.get(), Activity.FIGHT, Activity.HIDE, Activity.IDLE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BrainActivityGroup<? extends LatexBeast> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                targetRetaliateLook(LOOK_RANGE_SQR),
                new FirstApplicableBehaviour<>(
                        new CustomBehaviour<PureWhiteLatex>(latex -> {
                            BrainUtils.setMemory(latex, MemoryTypeRegistry.INSIDE_PILLAR.get(), true);
                            latex.setPersistenceRequired();
                            latex.moveTo(latex.blockPosition(), latex.getYRot(), latex.getXRot());
                            double angle = Mth.TWO_PI * latex.getRandom().nextDouble();
                            latex.lookAt(EntityAnchorArgument.Anchor.EYES, latex.getEyePosition().add(Math.cos(angle), 0, Math.sin(angle)));
                            BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET);
                        }).startCondition(latex -> {
                            boolean inPillar = latex.getInBlockState().is(BlockRegistry.WHITE_LATEX_PILLAR)
                                    && latex.level().getEntities(latex, latex.getInBlockState().getShape(latex.level(), latex.blockPosition()).bounds().move(latex.blockPosition()), entity -> entity instanceof PureWhiteLatex).isEmpty();
                            if(!inPillar) BrainUtils.clearMemory(latex, MemoryTypeRegistry.INSIDE_PILLAR.get());
                            return inPillar;
                        }),
                        new SetWalkTargetToBlock<>().predicate((latex, pair) -> pair.getSecond().getValue(StateProperties.PART2) == 1
                                        && latex.level().getEntities((Entity) null, pair.getSecond().getShape(latex.level(), pair.getFirst()).bounds().move(pair.getFirst()), entity -> entity instanceof PureWhiteLatex).isEmpty())
                                .closeEnoughWhen((latex, pos) -> 0),
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
                AChanged.TRANSFUR_ATTACK.get(), new BrainActivityGroup<LatexBeast>(AChanged.TRANSFUR_ATTACK.get()).behaviours(
                                new InvalidateAttackTarget<LatexBeast>().invalidateIf((latex, entity) -> isNonSurvivalOrTF(entity)).whenStopping(latex -> BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET)),
                                new ReactToUnreachableTarget<>().reaction((latex, flag) -> latex.setDeltaMovement(latex.getDeltaMovement().add(0, .75, 0))),
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
                                            BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET);
                                        }).invalidateIf((latex, entity) -> isNonSurvivalOrTF(entity) || GrabData.dataOf(latex).getGrabbedEntity() == null),
                                new CustomHeldBehaviour<>(latex -> AbilityRegistry.GRAB_ABILITY.get().serverTick(latex))
                                        .stopIf(latex -> !BrainUtils.hasMemory(latex, MemoryTypeRegistry.TRANSFUR_HOLDING.get()))
                        ).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                        .requireAndWipeMemoriesOnUse(MemoryTypeRegistry.TRANSFUR_HOLDING.get()),

                Activity.HIDE, new BrainActivityGroup<LatexBeast>(Activity.HIDE).behaviours(
                        new FirstApplicableBehaviour<>(
                            new CustomBehaviour<>(latex -> {
                                BrainUtils.clearMemory(latex, MemoryTypeRegistry.INSIDE_PILLAR.get());
                                BrainUtils.clearMemory(latex, SBLMemoryTypes.NEARBY_BLOCKS.get());
                                BrainUtils.clearMemory(latex, MemoryModuleType.LOOK_TARGET);
                            }).startCondition(latex -> !latex.getInBlockState().is(BlockRegistry.WHITE_LATEX_PILLAR)),
                            new TargetTransfurrable<>(),
                            new Retaliate<>())
                ).requireAndWipeMemoriesOnUse(MemoryTypeRegistry.INSIDE_PILLAR.get())
        );
    }
}