package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.AbilityHolder;
import net.zaharenko424.a_changed.ability.GrabAbility;
import net.zaharenko424.a_changed.ability.GrabMode;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

/**
 * Latex entity contract -> result of getAllowedAbilities() should stay the same throughout the runtime.
 */
@ParametersAreNonnullByDefault
public abstract class AbstractLatexBeast extends Monster implements AbilityHolder {

    public final @NotNull TransfurType transfurType;
    protected Ability selectedAbility;

    protected AbstractLatexBeast(EntityType<? extends Monster> entityType, Level level, TransfurType transfurType) {
        super(entityType, level);
        this.transfurType = transfurType;
        dimensions = transfurType.getPoseDimensions(Pose.STANDING);

        TransfurUtils.addModifiers(this, transfurType);
        transfurType.onTransfur(this);

        if(transfurType.abilities.contains(AbilityRegistry.GRAB_ABILITY.get())) {
            selectedAbility = AbilityRegistry.GRAB_ABILITY.get();
            ((GrabAbility) selectedAbility).getAbilityData(this).setMode(GrabMode.REPLICATE);//do replicate because ASSIMILATE would be too OP
        }
    }

    public static boolean isNonSurvivalOrTF(LivingEntity entity){
        return (entity instanceof Player pl && (pl.isCreative() || pl.isSpectator() || TransfurManager.isBeingTransfurred(pl)))
                || TransfurManager.isTransfurred(entity);
    }

    public static boolean isNonSurvivalOrNonTF(LivingEntity entity){
        return (entity instanceof Player pl && (pl.isCreative() || pl.isSpectator() || TransfurManager.isBeingTransfurred(pl)))
                || !TransfurManager.isTransfurred(entity);
    }

    @Override
    public Ability getSelectedAbility() {
        return selectedAbility;
    }

    @Override
    public List<? extends Ability> getAllowedAbilities() {
        return transfurType.abilities;
    }

    @Override
    public void selectAbility(Ability ability) {}

    protected static AttributeSupplier.@NotNull Builder baseAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,20)
                .add(Attributes.MOVEMENT_SPEED, .4)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 2.0);
    }

    public static boolean checkLatexBeastSpawnRules(EntityType<? extends AbstractLatexBeast> p_219014_, ServerLevelAccessor p_219015_, MobSpawnType p_219016_, BlockPos p_219017_, RandomSource p_219018_){
        return p_219015_.getDifficulty() != Difficulty.PEACEFUL
                && isDarkEnoughToSpawn(p_219015_, p_219017_, p_219018_)
                && checkMobSpawnRules(p_219014_, p_219015_, p_219016_, p_219017_, p_219018_);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pPose) {
        return transfurType.getPoseDimensions(pPose);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if(level().isClientSide || !DamageSources.checkTarget(target)) return super.doHurtTarget(target);

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) target.setSecondsOnFire(i * 4);

        if(!target.hurt(DamageSources.transfur(null,this), 0.1F)) return false;
        doEnchantDamageEffects(this, target);
        setLastHurtMob(target);
        TransfurHandler.nonNullOf((LivingEntity) target)
                .addTransfurProgress(5f, transfurType, TransfurContext.ADD_PROGRESS_DEF);
        return true;
    }

    public void copyEquipment(LivingEntity copyFrom){
        for(EquipmentSlot slot : EquipmentSlot.values()){
            if(!copyFrom.hasItemInSlot(slot)) continue;
            setItemSlot(slot, copyFrom.getItemBySlot(slot));
            copyFrom.setItemSlot(slot, ItemStack.EMPTY);
        }
        if(copyFrom instanceof Player){
            Arrays.fill(this.armorDropChances, 1);
            Arrays.fill(this.handDropChances, 1);
        } else {
            Arrays.fill(this.armorDropChances, 0.1f);
            Arrays.fill(this.handDropChances, 0.1f);
        }
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