package net.zaharenko424.testmod.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.TransfurDamageSource;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.entity.ai.LatexAttackGoal;
import net.zaharenko424.testmod.entity.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLatexBeast extends Monster {
    public final @NotNull ResourceLocation transfurType;

    protected AbstractLatexBeast(@NotNull EntityType<? extends Monster> p_33002_,@NotNull Level p_33003_,@NotNull AbstractTransfurType transfurType) {
        super(p_33002_, p_33003_);
        this.transfurType=transfurType.resourceLocation;
    }

    protected static AttributeSupplier.@NotNull Builder baseAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,20)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 2.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LatexAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, livingEntity -> !(livingEntity instanceof Player player&&TransfurManager.isTransfurred(player))));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity p_21372_) {
        boolean b=p_21372_ instanceof Transfurrable &&(!(p_21372_ instanceof TransfurHolder holder)||!holder.mod$isTransfurred());
        float f = b?0.1F:(float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (p_21372_ instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)p_21372_).getMobType());
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            p_21372_.setSecondsOnFire(i * 4);
        }

        boolean flag = p_21372_.hurt(TransfurDamageSource.transfur(p_21372_,this), f);
        if (flag) {
            this.doEnchantDamageEffects(this, p_21372_);
            this.setLastHurtMob(p_21372_);
        }
        if(!(p_21372_ instanceof LivingEntity)||level().isClientSide) return flag;
        if(b){
            TransfurManager.addTransfurProgress((Transfurrable)p_21372_,5,transfurType);
        }
        return flag;
    }
}