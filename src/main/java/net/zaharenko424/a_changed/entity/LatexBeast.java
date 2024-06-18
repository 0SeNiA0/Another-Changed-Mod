package net.zaharenko424.a_changed.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

public class LatexBeast extends AbstractLatexBeast {

    public LatexBeast(@NotNull EntityType<? extends Monster> p_21368_, @NotNull Level p_21369_, @NotNull TransfurType transfurType) {
        super(p_21368_, p_21369_,transfurType);
        ((GroundPathNavigation)navigation).setCanOpenDoors(true);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes(){
        return baseAttributes();
    }

    @Override
    protected boolean hasBrain() {
        return false;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new OpenDoorGoal(this,false));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, .8));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }
}