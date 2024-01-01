package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.ai.LatexAttackGoal;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurDamageSource;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.worldgen.biome.DarkLatexBiome;
import net.zaharenko424.a_changed.worldgen.biome.WhiteLatexBiome;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public abstract class AbstractLatexBeast extends Monster {
    public final @NotNull AbstractTransfurType transfurType;
    static final UUID airDecreaseSpeed = UUID.fromString("3425eeff-ee2d-44c9-91f5-67044b84baa0");
    static final UUID healthModifier = UUID.fromString("ecc275cc-dc18-4792-bca2-0adf7f331bbc");
    static final UUID swimSpeed = UUID.fromString("577c604f-686a-4224-b9f6-e619c5f2ee06");

    protected AbstractLatexBeast(EntityType<? extends Monster> entityType, Level level, AbstractTransfurType transfurType) {
        super(entityType, level);
        this.transfurType=transfurType;
        ((GroundPathNavigation)navigation).setCanOpenDoors(true);
        AttributeMap map = getAttributes();
        if(!map.hasModifier(AChanged.AIR_DECREASE_SPEED,airDecreaseSpeed)&&transfurType.airReductionModifier!=0) map.getInstance(AChanged.AIR_DECREASE_SPEED).addTransientModifier(new AttributeModifier(airDecreaseSpeed,"a",transfurType.airReductionModifier, AttributeModifier.Operation.ADDITION));
        if(!map.hasModifier(Attributes.MAX_HEALTH,healthModifier)&&transfurType.maxHealthModifier!=0) map.getInstance(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(healthModifier,"a",transfurType.maxHealthModifier, AttributeModifier.Operation.ADDITION));
        if(!map.hasModifier(NeoForgeMod.SWIM_SPEED,swimSpeed)&&transfurType.swimSpeedModifier!=0) map.getInstance(NeoForgeMod.SWIM_SPEED).addTransientModifier(new AttributeModifier(swimSpeed,"a",transfurType.swimSpeedModifier, AttributeModifier.Operation.ADDITION));
        addModifiers(map);
    }

    protected void addModifiers(AttributeMap map){}

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
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, .8));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !TransfurManager.isTransfurred((Player) player)&&!TransfurManager.isBeingTransfurred(player)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, true, mob -> mob.getType().is(AChanged.TRANSFURRABLE_TAG)));

    }

    public static boolean checkLatexBeastSpawnRules(EntityType<? extends AbstractLatexBeast> p_219014_, ServerLevelAccessor p_219015_, MobSpawnType p_219016_, BlockPos p_219017_, RandomSource p_219018_){
        return p_219015_.getDifficulty() != Difficulty.PEACEFUL
                && (
                        isDarkEnoughToSpawn(p_219015_, p_219017_, p_219018_)
                        || ((p_219015_.getBiome(p_219017_).is(DarkLatexBiome.KEY)&&(p_219014_==EntityRegistry.DARK_LATEX_WOLF_FEMALE.get()||p_219014_==EntityRegistry.DARK_LATEX_WOLF_MALE.get()))
                                ||(p_219015_.getBiome(p_219017_).is(WhiteLatexBiome.KEY)&&(p_219014_==EntityRegistry.WHITE_LATEX_WOLF_FEMALE.get()||p_219014_==EntityRegistry.WHITE_LATEX_WOLF_MALE.get())))
                )
                && checkMobSpawnRules(p_219014_, p_219015_, p_219016_, p_219017_, p_219018_);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if(level().isClientSide||!(getMainHandItem().isEmpty()&&TransfurDamageSource.checkTarget(p_21372_))) return super.doHurtTarget(p_21372_);

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) p_21372_.setSecondsOnFire(i * 4);

        if(!p_21372_.hurt(TransfurDamageSource.transfur(p_21372_,this), 0.1F)) return false;
        doEnchantDamageEffects(this, p_21372_);
        setLastHurtMob(p_21372_);
        TransfurManager.addTransfurProgress((LivingEntity) p_21372_,5,transfurType);
        return true;
    }
}