package net.zaharenko424.a_changed.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.item.AbstractSyringe;
import net.zaharenko424.a_changed.registry.ArmorMaterialRegistry;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class SyringeProjectile extends Projectile {

    private static final EntityDataAccessor<ItemStack> SYRINGE_STACK = SynchedEntityData.defineId(SyringeProjectile.class, EntityDataSerializers.ITEM_STACK);

    public AbstractArrow.Pickup pickup = AbstractArrow.Pickup.DISALLOWED;
    public int shakeTime;
    protected BlockState lastState;
    protected boolean inGround;
    protected int inGroundTime;
    protected int life;
    protected boolean placed;
    protected ItemStack rifle = null;

    public SyringeProjectile(Level level) {
        super(EntityRegistry.SYRINGE_PROJECTILE.get(), level);
    }

    public SyringeProjectile(Level level, LivingEntity shooter, ItemStack syringe, ItemStack rifle){
        this(EntityRegistry.SYRINGE_PROJECTILE.get(), level, shooter, syringe, rifle);
    }

    protected SyringeProjectile(EntityType<? extends SyringeProjectile> type, Level level){
        super(type, level);
    }

    protected SyringeProjectile(EntityType<? extends SyringeProjectile> type, Level level, LivingEntity shooter, ItemStack syringe, ItemStack rifle){
        super(type, level);
        if(!(syringe.getItem() instanceof AbstractSyringe) || syringe.isEmpty()) throw new IllegalArgumentException("Syringe must be an instance of AbstractSyringe!");
        setOwner(shooter);
        if(!level.isClientSide) {
            syringe.setCount(1);
            setPickupItemStack(syringe);
        }

        setCustomName(syringe.get(DataComponents.CUSTOM_NAME));
        Unit unit = syringe.remove(DataComponents.INTANGIBLE_PROJECTILE);
        if (unit != null) pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

        setPos(shooter.getX(), shooter.getEyeY() - .1, shooter.getZ());
        if (rifle != null && level instanceof ServerLevel) {
            if (rifle.isEmpty()) throw new IllegalArgumentException("Invalid weapon firing an arrow");

            this.rifle = rifle.copy();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(SYRINGE_STACK, getDefaultPickupItem());
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);

        pickup = switch (entity) {
            case Player player when pickup == AbstractArrow.Pickup.DISALLOWED -> player.isCreative() ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;
            case null, default -> pickup;
        };
    }

    public void place(@NotNull Vec3 pos){
        place((float) pos.x, (float) pos.y, (float) pos.z);
    }

    public void place(float x, float y, float z){
        setPos(x, y - .1f, z);
        placed = true;
        inGround = true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = getBoundingBox().getSize() * 10.0;
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }

        d0 *= 64.0 * getViewScale();
        return distance < d0 * d0;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        life = 0;
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        if(x != 0 || y != 0 || z != 0) super.lerpMotion(x, y, z);
        life = 0;
    }

    @Override
    public void move(@NotNull MoverType type, @NotNull Vec3 pos) {
        super.move(type, pos);
        if (type != MoverType.SELF && shouldFall()) {
            startFalling();
        }
    }

    @Override
    public void tick() {
        super.tick();
        boolean noPhysics = false;
        Vec3 vec3 = getDeltaMovement();
        if (xRotO == 0.0F && yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();

            setYRot((float)(Mth.atan2(vec3.x, vec3.z) * Mth.RAD_TO_DEG));
            setXRot((float)(Mth.atan2(vec3.y, d0) * Mth.RAD_TO_DEG));
            yRotO = getYRot();
            xRotO = getXRot();
        }

        BlockPos blockpos = blockPosition();
        BlockState blockstate = level().getBlockState(blockpos);
        if (!blockstate.isAir() && !noPhysics) {
            VoxelShape voxelshape = blockstate.getCollisionShape(level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = position();

                for (AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        inGround = true;
                        break;
                    }
                }
            }
        }

        if (shakeTime > 0) {
            shakeTime--;
        }

        if (isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || isInFluidType((fluidType, height) -> canFluidExtinguish(fluidType))) {
            clearFire();
        }

        if (inGround) {
            if (lastState != blockstate && shouldFall()) {
                startFalling();
            } else if (!level().isClientSide) {
                tickDespawn();
            }
            inGroundTime++;

            if(level().isClientSide || !placed) return;

            List<LivingEntity> list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox());
            LivingEntity entity = list.isEmpty() ? null : list.getFirst();
            if(entity == null) return;

            ItemStack syringe = getPickupItemStackOrigin();
            Block.popResource(level(), blockPosition().above(), ((AbstractSyringe)syringe.getItem()).applyEffectsAsProjectile(syringe, level(), entity, this, null));
            discard();
            return;
        }

        inGroundTime = 0;
        Vec3 vec32 = position();
        Vec3 vec33 = vec32.add(vec3);
        HitResult hitresult = level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec33 = hitresult.getLocation();
        }

        while (!isRemoved()) {
            EntityHitResult entityhitresult = findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult)hitresult).getEntity();
                Entity entity1 = getOwner();
                if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                    hitresult = null;
                    entityhitresult = null;
                }
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !noPhysics) {
                if (net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult))
                    break;
                ProjectileDeflection projectiledeflection = hitTargetOrDeflectSelf(hitresult);
                hasImpulse = true;
                if (projectiledeflection != ProjectileDeflection.NONE) {
                    break;
                }
            }

            if(entityhitresult == null) break;

            hitresult = null;
        }

        vec3 = getDeltaMovement();

        double d7 = getX() + vec3.x;
        double d2 = getY() + vec3.y;
        double d3 = getZ() + vec3.z;
        double d4 = vec3.horizontalDistance();
        if (noPhysics) {
            setYRot((float)(Mth.atan2(-vec3.x, -vec3.z) * Mth.RAD_TO_DEG));
        } else {
            setYRot((float)(Mth.atan2(vec3.x, vec3.z) * Mth.RAD_TO_DEG));
        }

        setXRot((float)(Mth.atan2(vec3.y, d4) * Mth.RAD_TO_DEG));
        setXRot(lerpRotation(xRotO, getXRot()));
        setYRot(lerpRotation(yRotO, getYRot()));
        float f = 0.99F;
        if (isInWater()) {
            for (int j = 0; j < 4; j++) {
                level().addParticle(ParticleTypes.BUBBLE, d7 - vec3.x * 0.25, d2 - vec3.y * 0.25, d3 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }

            f = getWaterInertia();
        }

        setDeltaMovement(vec3.scale(f));
        if (!noPhysics) {
            applyGravity();
        }

        setPos(d7, d2, d3);
        checkInsideBlocks();
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(level(), this, startVec, endVec, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0), this::canHitEntity);
    }

    protected boolean shouldFall() {
        return inGround && level().noCollision(new AABB(position(), position()).inflate(0.06));
    }

    protected void startFalling() {
        placed = false;
        inGround = false;
        Vec3 vec3 = getDeltaMovement();
        setDeltaMovement(vec3.multiply(random.nextFloat() * 0.2F, random.nextFloat() * 0.2F, random.nextFloat() * 0.2F));
        life = 0;
    }

    protected float getWaterInertia() {
        return 0.6F;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    protected void onHit(HitResult result) {
        HitResult.Type hitresult$type = result.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            EntityHitResult entityhitresult = (EntityHitResult)result;
            Entity entity = entityhitresult.getEntity();

            if(entity.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entity instanceof Projectile projectile) {
                projectile.deflect(ProjectileDeflection.AIM_DEFLECT, getOwner(), getOwner(), true);
            }

            if(entity.isInvulnerable() || (entity instanceof Player player && player.isCreative())){
                deflect(entity);
                return;
            }

            onHitEntity(entityhitresult);
            level().gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, null));
        } else if (hitresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)result;
            onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, level().getBlockState(blockpos)));
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        Entity entity = result.getEntity();

        if(getPickupItemStackOrigin().isEmpty()){
            discard();
            return;
        }

        Entity owner = getOwner();
        if(!(entity instanceof LivingEntity living) || entity.getType() == EntityType.ENDERMAN){
            entity.hurt(DamageSources.syringe(level(), this, owner), .5f);
            deflect(entity);
            return;
        }

        if(bounce(living)){
            deflect(entity);
            return;
        }

        if(owner instanceof LivingEntity ownerL) ownerL.setLastHurtMob(entity);

        if(!isSilent() && owner != living && living instanceof ServerPlayer ownerPl){
            ownerPl.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0));
        }
        playSound(getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
        doKnockback(living);
        ItemStack syringe = getPickupItemStackOrigin();
        Block.popResource(level(), blockPosition(),
                ((AbstractSyringe)syringe.getItem()).applyEffectsAsProjectile(syringe, level(), living, this, owner));
        discard();
    }

    protected boolean bounce(@NotNull LivingEntity entity){
        Iterable<ItemStack> iterable = entity.getArmorSlots();
        int armorSlots = 0, armorPieces = 0, projProt = 0;
        float toughness = 0;

        Holder<ArmorMaterial> material;
        Holder<Enchantment> projProtHolder = level().holderOrThrow(Enchantments.PROJECTILE_PROTECTION);
        for (ItemStack stack : iterable) {
            armorSlots++;

            if(!(stack.getItem() instanceof ArmorItem armor)) continue;

            material = armor.getMaterial();
            if(material == ArmorMaterials.LEATHER || material == ArmorMaterials.CHAIN || material == ArmorMaterialRegistry.LATEX.getDelegate()) continue;

            armorPieces++;
            projProt += stack.getEnchantmentLevel(projProtHolder);
            toughness += armor.getToughness();
        }

        float covered = armorSlots > 0 && armorPieces > 0 ? (armorSlots == armorPieces ? 1 : (float)armorPieces / (float)armorSlots) : 0;
        if(covered == 0) return false;

        float armorChance = Math.min(toughness, 12) / 12 * .3f;//Balanced for vanilla (netherite full set 12 toughness)
        float projChance = Math.min(projProt, 16) / 16f * .5f;
        float bounceChance = (armorChance + projChance) * covered;//Max .8
        if(getWeaponItem() != null && getWeaponItem().is(ItemRegistry.PNEUMATIC_SYRINGE_RIFLE)) bounceChance += .1f;//Add 10% if pneumatic is used

        if(entity.getRandom().nextFloat() > bounceChance) return false;

        deflect(entity);
        return true;
    }

    protected void deflect(Entity entity){
        deflect(ProjectileDeflection.REVERSE, entity, getOwner(), false);
        setDeltaMovement(getDeltaMovement().scale(0.2));
        if (!level().isClientSide && getDeltaMovement().lengthSqr() < 1.0E-7) {
            if (pickup == AbstractArrow.Pickup.ALLOWED) {
                spawnAtLocation(getPickupItem(), 0.1F);
            }

            discard();
        }
    }

    protected void doKnockback(LivingEntity entity) {
        double d1 = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        Vec3 vec3 = getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(.5f * .6f * d1);
        if (vec3.lengthSqr() > 0.0) {
            entity.push(vec3.x, 0.1, vec3.z);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        lastState = level().getBlockState(result.getBlockPos());
        super.onHitBlock(result);
        Vec3 vec3 = result.getLocation().subtract(getX(), getY(), getZ());
        setDeltaMovement(vec3);

        Vec3 vec31 = vec3.normalize().scale(0.05F);
        setPosRaw(getX() - vec31.x, getY() - vec31.y, getZ() - vec31.z);
        playSound(getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
        inGround = true;
        shakeTime = 7;
    }

    protected void tickDespawn() {
        if(pickup == AbstractArrow.Pickup.ALLOWED || placed) return;

        life++;
        if (life >= 1200) {
            discard();
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return .1f;
    }

    protected boolean tryPickup(Player player) {
        return switch (pickup) {
            case DISALLOWED -> false;
            case ALLOWED -> player.getInventory().add(getPickupItem());
            case CREATIVE_ONLY -> {
                boolean b = player.hasInfiniteMaterials();
                if(b) player.getInventory().add(getPickupItem());
                yield b;
            }
        };
    }

    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if(level().isClientSide || hand != InteractionHand.MAIN_HAND || !player.getMainHandItem().isEmpty()) return InteractionResult.PASS;

        if(!player.isCrouching()){
            ItemStack syringe = getPickupItemStackOrigin();
            setPickupItemStack(((AbstractSyringe)syringe.getItem()).applyEffectsAsProjectile(syringe, level(), player, this, null));
        }

        if(tryPickup(player)){
            level().playSound(null, player, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f, (random.nextFloat() - random.nextFloat()) * 1.4F + 2.0F);
            discard();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void playerTouch(@NotNull Player entity) {
        if(level().isClientSide || placed || !inGround || shakeTime > 0) return;

        if(tryPickup(entity)) {
            level().playSound(null, entity, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f, (random.nextFloat() - random.nextFloat()) * 1.4F + 2.0F);
            discard();
        }
    }

    public @NotNull ItemStack getPickupItemStackOrigin() {
        return entityData.get(SYRINGE_STACK);
    }

    protected @NotNull ItemStack getPickupItem() {
        return getPickupItemStackOrigin().copy();
    }

    protected void setPickupItemStack(@NotNull ItemStack pickupItemStack) {
        entityData.set(SYRINGE_STACK, pickupItemStack);
    }

    protected @NotNull ItemStack getDefaultPickupItem() {
        return ItemRegistry.SYRINGE_ITEM.toStack();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putShort("life", (short)life);
        if (lastState != null) {
            compound.put("inBlockState", NbtUtils.writeBlockState(lastState));
        }
        compound.putByte("shake", (byte)shakeTime);
        compound.putBoolean("inGround", inGround);
        compound.putByte("pickup", (byte)pickup.ordinal());
        compound.put("item", getPickupItemStackOrigin().save(registryAccess()));

        if (rifle != null) {
            compound.put("weapon", rifle.save(registryAccess(), new CompoundTag()));
        }

        if(placed) compound.putBoolean("placed", true);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        life = compound.getShort("life");
        if (compound.contains("inBlockState", 10)) {
            lastState = NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK), compound.getCompound("inBlockState"));
        }
        shakeTime = compound.getByte("shake") & 255;
        inGround = compound.getBoolean("inGround");
        pickup = AbstractArrow.Pickup.byOrdinal(compound.getByte("pickup"));

        if (compound.contains("item", 10)) {
            setPickupItemStack(ItemStack.parse(registryAccess(), compound.getCompound("item")).orElse(getDefaultPickupItem()));
        } else {
            setPickupItemStack(getDefaultPickupItem());
        }

        if (compound.contains("weapon", 10)) {
            rifle = ItemStack.parse(registryAccess(), compound.getCompound("weapon")).orElse(null);
        } else {
            rifle = null;
        }

        placed = compound.getBoolean("placed");
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity p_entity) {
        Entity entity = getOwner();
        int i = entity == null ? 0 : entity.getId();
        Vec3 vec3 = p_entity.getPositionBase();
        return new ClientboundAddEntityPacket(
                getId(),
                getUUID(),
                vec3.x(),
                vec3.y(),
                vec3.z(),
                getXRot(),
                getYRot(),
                getType(),
                i,
                p_entity.getLastSentMovement(),
                0.0
        );
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }
}