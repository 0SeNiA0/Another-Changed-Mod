package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class MilkPuddingEntity extends Monster {

    public MilkPuddingEntity(Level pLevel) {
        super(EntityRegistry.MILK_PUDDING.get(), pLevel);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, entity -> DamageSources.checkTarget(entity) && AbstractLatexBeast.isNonSurvivalOrNonTF(entity)));
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, .2)
                .add(Attributes.FOLLOW_RANGE, 16);
    }

    public static boolean checkSpawnRules(EntityType<? extends MilkPuddingEntity> p_219014_, ServerLevelAccessor p_219015_, MobSpawnType p_219016_, BlockPos p_219017_, RandomSource p_219018_){
        return p_219015_.getDifficulty() != Difficulty.PEACEFUL
                && isDarkEnoughToSpawn(p_219015_, p_219017_, p_219018_)
                && checkMobSpawnRules(p_219014_, p_219015_, p_219016_, p_219017_, p_219018_);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pEntity) {
        if(!DamageSources.checkTarget(pEntity) || !AbstractLatexBeast.isNonSurvivalOrNonTF((LivingEntity) pEntity)) {
            setTarget(null);
            return false;
        }

        TransfurHandler.of((LivingEntity) pEntity).addTransfurProgress(TransfurManager.TRANSFUR_TOLERANCE / 2,
                random.nextFloat() > .5 ? TransfurRegistry.WHITE_LATEX_WOLF_F_TF.get() : TransfurRegistry.WHITE_LATEX_WOLF_M_TF.get(),
                TransfurContext.ADD_PROGRESS_DEF);
        ((LivingEntity) pEntity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
        discard();
        return true;
    }
}