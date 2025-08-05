package net.zaharenko424.a_changed.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.LatexBeast;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractLatexPup extends TamableAnimal implements LatexBeast {

    protected static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(AbstractLatexPup.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(AbstractLatexPup.class, EntityDataSerializers.INT);

    protected float interestedAngle;
    protected float interestedAngleO;
    protected boolean isWet;
    protected boolean isShaking;
    protected float shakeAnim;
    protected float shakeAnimO;

    protected static final int loveCooldown = 6000;
    protected int inLoveCooldown = loveCooldown;

    protected AbstractLatexPup(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        setTame(false, false);
        setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
        setPathfindingMalus(PathType.DANGER_POWDER_SNOW, -1.0F);

        TransfurType<?> transfurType = transfurType();
        EntityDimensions dimensions = transfurType.getPoseDimensions(this, Pose.STANDING);
        if(dimensions != null) {
            this.dimensions = dimensions;
            refreshDimensions();
        }

        TransfurUtils.addModifiers(this, transfurType);
        transfurType.onTransfur(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 0);
    }

    @Override
    public void selectAbility(@NotNull Ability ability) {}

    public boolean isInterested() {
        return entityData.get(DATA_INTERESTED_ID);
    }

    protected void setIsInterested(boolean isInterested) {
        this.entityData.set(DATA_INTERESTED_ID, isInterested);
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(entityData.get(DATA_COLLAR_COLOR));
    }

    protected void setCollarColor(DyeColor collarColor) {
        entityData.set(DATA_COLLAR_COLOR, collarColor.getId());
    }

    @Override
    public void copyEquipment(@NotNull LivingEntity copyFrom) {
        ItemStack stack = copyFrom.getItemBySlot(EquipmentSlot.BODY);
        if(stack.isEmpty()) return;

        setBodyArmorItem(stack);
        copyFrom.setItemSlot(EquipmentSlot.BODY, ItemStack.EMPTY);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        EntityDimensions dimensions = transfurType().getPoseDimensions(this, pose);
        return dimensions != null ? dimensions : super.getDefaultDimensions(pose);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
        playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (isAngry()) {
            return SoundEvents.WOLF_GROWL;
        } else if (random.nextInt(3) == 0) {
            return isTame() && getHealth() < 20.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        } else {
            return SoundEvents.WOLF_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return canArmorAbsorb(damageSource) ? SoundEvents.WOLF_ARMOR_DAMAGE : SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void customServerAiStep() {
        int inLove = getInLoveTime();
        super.customServerAiStep();
        if(inLoveCooldown == 0) setInLoveTime(inLove);
    }

    @Override
    public void aiStep() {
        int inLove = getInLoveTime();
        super.aiStep();
        if(inLoveCooldown == 0) setInLoveTime(inLove);
        if (!level().isClientSide && isWet && !isShaking && !isPathFinding() && onGround()) {
            isShaking = true;
            shakeAnim = 0.0F;
            shakeAnimO = 0.0F;
            level().broadcastEntityEvent(this, (byte)8);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(!isAlive()) return;

        if(inLoveCooldown > 0) inLoveCooldown--;

        interestedAngleO = interestedAngle;
        if (isInterested()) {
            interestedAngle = interestedAngle + (1.0F - interestedAngle) * 0.4F;
        } else {
            interestedAngle = interestedAngle + (0.0F - interestedAngle) * 0.4F;
        }

        if (isInWaterRainOrBubble()) {
            isWet = true;
            if (isShaking && !level().isClientSide) {
                level().broadcastEntityEvent(this, (byte)56);
                cancelShake();
            }
        } else if ((isWet || isShaking) && isShaking) {
            if (shakeAnim == 0.0F) {
                playSound(SoundEvents.WOLF_SHAKE, getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                gameEvent(GameEvent.ENTITY_ACTION);
            }

            shakeAnimO = shakeAnim;
            shakeAnim += 0.05F;
            if (shakeAnimO >= 2.0F) {
                isWet = false;
                cancelShake();
            }

            if (shakeAnim > 0.4F) {
                float f = (float)getY();
                int i = (int)(Mth.sin((shakeAnim - 0.4F) * (float) Math.PI) * 7.0F);
                Vec3 vec3 = getDeltaMovement();

                for (int j = 0; j < i; j++) {
                    float f1 = (random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
                    float f2 = (random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
                    level().addParticle(ParticleTypes.SPLASH, getX() + (double)f1, f + 0.8F, getZ() + (double)f2, vec3.x, vec3.y, vec3.z);
                }
            }
        }
        if(!level().isClientSide) {
            sitIfOrdered();
            AbilityRegistry.DL_PUP_MELT.get().serverTick(this);
            AbilityRegistry.DL_PUP_AGE.get().serverTick(this);
        }
    }

    protected void sitIfOrdered(){
        if(!isTame()) return;
        if(isInWaterOrBubble() || !onGround()){
            setInSittingPose(false);
            return;
        }

        if(!isOrderedToSit()) return;

        LivingEntity owner = getOwner();
        if(owner == null) return;
        setInSittingPose((!(distanceToSqr(owner) < 144) || owner.getLastHurtByMob() == null) && isOrderedToSit());
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        isWet = false;
        cancelShake();
        super.die(cause);
    }

    protected boolean isAngry(){
        return getTarget() != null;
    }

    protected void cancelShake() {
        isShaking = false;
        shakeAnim = 0.0F;
        shakeAnimO = 0.0F;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if(isInvulnerableTo(source)) return false;

        if (!level().isClientSide) setOrderedToSit(false);
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource damageSource, float damageAmount) {
        if(!canArmorAbsorb(damageSource)){
            super.actuallyHurt(damageSource, damageAmount);
            return;
        }

        ItemStack itemstack = getBodyArmorItem();
        int i = itemstack.getDamageValue();
        int j = itemstack.getMaxDamage();
        itemstack.hurtAndBreak(Mth.ceil(damageAmount), this, EquipmentSlot.BODY);
        if(Crackiness.WOLF_ARMOR.byDamage(i, j) == Crackiness.WOLF_ARMOR.byDamage(getBodyArmorItem())) return;

        playSound(SoundEvents.WOLF_ARMOR_CRACK);
        if(!(level() instanceof ServerLevel level)) return;
        level.sendParticles(
                new ItemParticleOption(ParticleTypes.ITEM, Items.ARMADILLO_SCUTE.getDefaultInstance()),
                getX(), getY() + 1.0, getZ(),
                20,
                0.2, 0.1, 0.2,
                0.1);
    }

    protected boolean canArmorAbsorb(DamageSource damageSource) {
        return hasArmor() && !damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR);
    }

    protected boolean hasArmor() {
        return getBodyArmorItem().is(Items.WOLF_ARMOR);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if (!level().isClientSide || isBaby() && isFood(itemstack)) {
            if (isTame()) {
                if (isFood(itemstack) && getHealth() < getMaxHealth()) {
                    FoodProperties foodproperties = itemstack.getFoodProperties(this);
                    float f = foodproperties != null ? (float)foodproperties.nutrition() : 1.0F;
                    heal(2.0F * f);
                    itemstack.consume(1, player);
                    gameEvent(GameEvent.EAT); // Neo: add EAT game event
                    return InteractionResult.sidedSuccess(level().isClientSide());
                } else {
                    if (item instanceof DyeItem dyeitem && isOwnedBy(player)) {
                        DyeColor dyecolor = dyeitem.getDyeColor();
                        if(dyecolor == getCollarColor()) return superMobInteractOverride(player, hand);

                        setCollarColor(dyecolor);
                        itemstack.consume(1, player);
                        return InteractionResult.SUCCESS;
                    }

                    if (itemstack.is(Items.WOLF_ARMOR) && isOwnedBy(player) && getBodyArmorItem().isEmpty()) {
                        setBodyArmorItem(itemstack.copyWithCount(1));
                        itemstack.consume(1, player);
                        return InteractionResult.SUCCESS;
                    } else if (itemstack.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SHEARS_REMOVE_ARMOR)
                            && isOwnedBy(player)
                            && hasArmor()
                            && (!EnchantmentHelper.has(getBodyArmorItem(), EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) || player.isCreative())) {
                        itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
                        playSound(SoundEvents.ARMOR_UNEQUIP_WOLF);
                        ItemStack itemstack1 = getBodyArmorItem();
                        setBodyArmorItem(ItemStack.EMPTY);
                        spawnAtLocation(itemstack1);
                        return InteractionResult.SUCCESS;
                    } else if (ArmorMaterials.ARMADILLO.value().repairIngredient().get().test(itemstack)
                            && isInSittingPose()
                            && hasArmor()
                            && isOwnedBy(player)
                            && getBodyArmorItem().isDamaged()) {
                        itemstack.shrink(1);
                        playSound(SoundEvents.WOLF_ARMOR_REPAIR);
                        ItemStack itemstack2 = getBodyArmorItem();
                        int i = (int)((float)itemstack2.getMaxDamage() * 0.125F);
                        itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
                        return InteractionResult.SUCCESS;
                    } else {
                        InteractionResult interactionresult = superMobInteractOverride(player, hand);
                        if(interactionresult.consumesAction() || !isOwnedBy(player)) return interactionresult;

                        setOrderedToSit(!isOrderedToSit());
                        jumping = false;
                        navigation.stop();
                        setTarget(null);
                        return InteractionResult.SUCCESS_NO_ITEM_USED;
                    }
                }
            } else if (isFood(itemstack) && !isAngry()) {
                itemstack.consume(1, player);
                tryToTame(player);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        } else {
            boolean flag = isOwnedBy(player) || isTame() || isFood(itemstack) && !isTame() && !isAngry();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
    }

    /**
     * Replace super.mobInteract() since vanilla uses age as cooldown for love but im adding a separate variable.
     */
    protected InteractionResult superMobInteractOverride(@NotNull Player player, InteractionHand hand){
        ItemStack itemstack = player.getItemInHand(hand);
        if(!isFood(itemstack)) return InteractionResult.PASS;

        if (!level().isClientSide && !isBaby() && canFallInLove()) {
            usePlayerItem(player, hand, itemstack);
            setInLove(player);
            return InteractionResult.SUCCESS;
        }

        if (isBaby()) {
            usePlayerItem(player, hand, itemstack);
            speedUpAging();
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return level().isClientSide ? InteractionResult.CONSUME : InteractionResult.PASS;
    }

    protected abstract void speedUpAging();

    protected void tryToTame(Player player) {
        if (random.nextInt(3) == 0  && !net.neoforged.neoforge.event.EventHooks.onAnimalTame(this, player)) {
            tame(player);
            navigation.stop();
            setTarget(null);
            setOrderedToSit(true);
            level().broadcastEntityEvent(this, (byte)7);
        } else level().broadcastEntityEvent(this, (byte)6);
    }

    @Override
    public boolean canUseSlot(@NotNull EquipmentSlot slot) {
        return true;
    }

    @Override
    protected void applyTamingSideEffects() {
        if (isTame()) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);//TODO less health?
            setHealth(20);
        } else getAttribute(Attributes.MAX_HEALTH).setBaseValue(8);
    }

    @Override
    protected void hurtArmor(@NotNull DamageSource damageSource, float damageAmount) {
        doHurtEquipment(damageSource, damageAmount, EquipmentSlot.BODY);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 8) {
            isShaking = true;
            shakeAnim = 0.0F;
            shakeAnimO = 0.0F;
        } else if (id == 56) {
            cancelShake();
        } else super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_INTERESTED_ID, false);
        builder.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("CollarColor", (byte)getCollarColor().getId());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("CollarColor", 99)) {
            setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
    }

    public float getBodyRollAngle(float partialTicks, float offset) {
        float f = (Mth.lerp(partialTicks, shakeAnimO, shakeAnim) + offset) / 1.8F;
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }

        return Mth.sin(f * (float) Math.PI) * Mth.sin(f * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
    }

    public float getHeadRollAngle(float partialTicks) {
        return Mth.lerp(partialTicks, interestedAngleO, interestedAngle) * 0.15F * (float) Math.PI;
    }

    @Override
    public int getMaxHeadXRot() {
        return isInSittingPose() ? 20 : super.getMaxHeadXRot();
    }

    public float getTailAngle() {
        if (isAngry()) return 1.5393804F;
        if (isTame()) {
            float f = getMaxHealth();
            float f1 = (f - getHealth()) / f;
            return (0.55F - f1 * 0.4F) * (float) Math.PI;
        }

        return (float) (Math.PI / 5);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ItemRegistry.ORANGE_ITEM);
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @Nullable LivingEntity owner) {
        if(target == this || target == owner) return false;
        if (target instanceof Creeper || target instanceof Ghast || target instanceof ArmorStand) {
            return false;
        } else {
            return switch (target) {
                case Wolf wolf -> !wolf.isTame() || wolf.getOwner() != owner;
                case AbstractLatexPup pup -> !pup.isTame() || pup.getOwner() != owner;
                case Player player when owner instanceof Player player1 && !player1.canHarmPlayer(player) -> false;
                case AbstractHorse horse when horse.isTamed() -> false;
                case TamableAnimal animal when animal.isTame() -> false;
                default -> true;
            };
        }
    }
}
