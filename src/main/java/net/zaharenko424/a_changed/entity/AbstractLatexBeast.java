package net.zaharenko424.a_changed.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.TransfurDamageSource;
import net.zaharenko424.a_changed.TransfurManager;
import net.zaharenko424.a_changed.entity.ai.LatexAttackGoal;
import net.zaharenko424.a_changed.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLatexBeast extends Monster {
    public final @NotNull AbstractTransfurType transfurType;

    protected AbstractLatexBeast(@NotNull EntityType<? extends Monster> p_33002_,@NotNull Level p_33003_,@NotNull AbstractTransfurType transfurType) {
        super(p_33002_, p_33003_);
        this.transfurType=transfurType;
        ((GroundPathNavigation)navigation).setCanOpenDoors(true);
    }

    protected static AttributeSupplier.@NotNull Builder baseAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,24)
                .add(Attributes.MOVEMENT_SPEED, .4)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 2.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this,false));
        this.goalSelector.addGoal(2, new LatexAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, .8));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !TransfurManager.isTransfurred((Player) player)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, false, mob -> mob.getType().is(AChanged.TRANSFURRABLE_TAG)));

    }

    @Override
    public boolean doHurtTarget(@NotNull Entity p_21372_) {
        if(level().isClientSide||!(getMainHandItem().isEmpty()&&TransfurDamageSource.checkTarget(p_21372_))) return super.doHurtTarget(p_21372_);

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            p_21372_.setSecondsOnFire(i * 4);
        }

        if(!p_21372_.hurt(TransfurDamageSource.transfur(p_21372_,this), 0.1F)) return false;
        this.doEnchantDamageEffects(this, p_21372_);
        this.setLastHurtMob(p_21372_);
        TransfurManager.addTransfurProgress((LivingEntity) p_21372_,5,transfurType);
        return true;
    }
}