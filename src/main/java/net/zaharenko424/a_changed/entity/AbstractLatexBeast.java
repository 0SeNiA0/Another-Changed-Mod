package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.ai.LatexTargetPlayerGoal;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.worldgen.Biomes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.UUID;

@ParametersAreNonnullByDefault
public abstract class AbstractLatexBeast extends Monster {

    public final @NotNull AbstractTransfurType transfurType;
    static final UUID airDecreaseSpeed = UUID.fromString("3425eeff-ee2d-44c9-91f5-67044b84baa0");
    static final UUID healthModifier = UUID.fromString("ecc275cc-dc18-4792-bca2-0adf7f331bbc");
    static final UUID swimSpeed = UUID.fromString("577c604f-686a-4224-b9f6-e619c5f2ee06");

    protected AbstractLatexBeast(EntityType<? extends Monster> entityType, Level level, AbstractTransfurType transfurType) {
        super(entityType, level);
        this.transfurType = transfurType;
        dimensions = transfurType.getPoseDimensions(Pose.STANDING);//TODO remove?
        if(!hasBrain()) registerLatexGoals();
        AttributeMap map = getAttributes();
        if(!map.hasModifier(AChanged.AIR_DECREASE_SPEED, airDecreaseSpeed) && transfurType.airReductionModifier != 0) map.getInstance(AChanged.AIR_DECREASE_SPEED).addTransientModifier(new AttributeModifier(airDecreaseSpeed,"a", transfurType.airReductionModifier, AttributeModifier.Operation.ADDITION));
        if(!map.hasModifier(Attributes.MAX_HEALTH, healthModifier) && transfurType.maxHealthModifier != 0) map.getInstance(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(healthModifier,"a", transfurType.maxHealthModifier, AttributeModifier.Operation.ADDITION));
        if(!map.hasModifier(NeoForgeMod.SWIM_SPEED, swimSpeed) && transfurType.swimSpeedModifier != 0) map.getInstance(NeoForgeMod.SWIM_SPEED).addTransientModifier(new AttributeModifier(swimSpeed,"a", transfurType.swimSpeedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
        addModifiers(map);
        transfurType.onTransfur(this);
    }

    protected abstract boolean hasBrain();

    protected void addModifiers(AttributeMap map){}

    protected static AttributeSupplier.@NotNull Builder baseAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,20)
                .add(Attributes.MOVEMENT_SPEED, .4)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 2.0);
    }

    protected void registerLatexGoals(){
        if(!transfurType.isOrganic()){
            targetSelector.addGoal(1, new LatexTargetPlayerGoal(this, true, player -> !TransfurManager.isTransfurred((Player) player) && !TransfurManager.isBeingTransfurred((Player) player)));
            targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, true, mob -> mob.getType().is(AChanged.TRANSFURRABLE_TAG)));
        }
    }

    public static boolean checkLatexBeastSpawnRules(EntityType<? extends AbstractLatexBeast> p_219014_, ServerLevelAccessor p_219015_, MobSpawnType p_219016_, BlockPos p_219017_, RandomSource p_219018_){
        return p_219015_.getDifficulty() != Difficulty.PEACEFUL
                && (
                        isDarkEnoughToSpawn(p_219015_, p_219017_, p_219018_)
                        || ((p_219015_.getBiome(p_219017_).is(Biomes.DARK_LATEX_BIOME) && (p_219014_ == EntityRegistry.DARK_LATEX_WOLF_FEMALE.get() || p_219014_ == EntityRegistry.DARK_LATEX_WOLF_MALE.get()))
                                || (p_219015_.getBiome(p_219017_).is(Biomes.WHITE_LATEX_BIOME) && (p_219014_ == EntityRegistry.WHITE_LATEX_WOLF_FEMALE.get() || p_219014_ == EntityRegistry.WHITE_LATEX_WOLF_MALE.get())))
                )
                && checkMobSpawnRules(p_219014_, p_219015_, p_219016_, p_219017_, p_219018_);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pPose) {
        return transfurType.getPoseDimensions(pPose);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if(level().isClientSide || !DamageSources.checkTarget(p_21372_)) return super.doHurtTarget(p_21372_);

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) p_21372_.setSecondsOnFire(i * 4);

        if(!p_21372_.hurt(DamageSources.transfur(null,this), 0.1F)) return false;
        doEnchantDamageEffects(this, p_21372_);
        setLastHurtMob(p_21372_);
        TransfurEvent.ADD_TRANSFUR_DEF.accept((LivingEntity) p_21372_, transfurType, 5f);
        return true;
    }

    public void copyEquipment(LivingEntity copyFrom){
        if(copyFrom instanceof Player) return;
        for(EquipmentSlot slot : EquipmentSlot.values()){
            if(copyFrom.hasItemInSlot(slot)) setItemSlot(slot, copyFrom.getItemBySlot(slot));
        }
        Arrays.fill(this.armorDropChances, 0.1f);
        Arrays.fill(this.handDropChances, 0.1f);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        onFinalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void onFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag){
        setCanPickUpLoot(level.getRandom().nextFloat() < 0.55F * difficulty.getSpecialMultiplier());
    }
}