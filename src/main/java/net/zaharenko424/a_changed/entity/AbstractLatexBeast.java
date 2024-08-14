package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.entity.ai.LatexTargetPlayerGoal;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.worldgen.Biomes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public abstract class AbstractLatexBeast extends Monster {

    public final @NotNull TransfurType transfurType;

    protected AbstractLatexBeast(EntityType<? extends Monster> entityType, Level level, TransfurType transfurType) {
        super(entityType, level);
        this.transfurType = transfurType;
        dimensions = transfurType.getPoseDimensions(Pose.STANDING);
        if(!hasBrain()) registerLatexGoals();
        AttributeMap map = getAttributes();

        AttributeInstance[] instance = new AttributeInstance[1];
        transfurType.modifiers.asMap().forEach((attribute, modifiers) -> {
            if(!map.hasAttribute(attribute)){
                AChanged.LOGGER.error("Attempted to add transfur modifier to not existing attribute {} {}", attribute, transfurType);
                return;
            }

            instance[0] = map.getInstance(attribute);
            for(AttributeModifier modifier : modifiers){
                instance[0].addTransientModifier(modifier);
            }
        });

        transfurType.onTransfur(this);
    }

    protected abstract boolean hasBrain();

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
        TransfurHandler.nonNullOf((LivingEntity) p_21372_)
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