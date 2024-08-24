package net.zaharenko424.a_changed.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class RoombaEntity extends PathfinderMob {

    public RoombaEntity(Level pLevel) {
        super(EntityRegistry.ROOMBA_ENTITY.get(), pLevel);
    }

    @Override
    protected void registerGoals() {
        WaterAvoidingRandomStrollGoal goal = new WaterAvoidingRandomStrollGoal(this, .5);
        goal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        goalSelector.addGoal(1, goal);
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, .5)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ARMOR, 2);
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity pTarget) {
        return false;
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity pLivingentity, @NotNull TargetingConditions pCondition) {
        return false;
    }

    @Override
    public boolean canAttackType(@NotNull EntityType<?> pType) {
        return false;
    }

    @Override
    protected void jumpFromGround() {}
}