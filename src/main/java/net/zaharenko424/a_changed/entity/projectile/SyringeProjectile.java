package net.zaharenko424.a_changed.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.EntityHitResult;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.item.LatexSyringeItem;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

public class SyringeProjectile extends AbstractArrow {

    private TransfurType transfurType = null;

    public SyringeProjectile(Level pLevel) {
        super(EntityRegistry.SYRINGE_PROJECTILE.get(), pLevel, ItemStack.EMPTY);
        setBaseDamage(.01);
    }

    public SyringeProjectile(Level level, LivingEntity shooter, TransfurType transfurType){
        super(EntityRegistry.SYRINGE_PROJECTILE.get(), shooter, level, ItemStack.EMPTY);
        this.transfurType = transfurType;
        setBaseDamage(.01);
    }

    public void setTransfurType(TransfurType transfurType){
        this.transfurType = transfurType;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        if(entity.isInvulnerable() || (entity instanceof Player player && player.isCreative())) return;
        Block.popResource(level(), blockPosition(), ItemRegistry.SYRINGE_ITEM.get().getDefaultInstance());
        if(transfurType == null || !DamageSources.checkTarget(entity)) return;
        entity.hurt(DamageSources.transfur(this, getOwner()), .5f);
        TransfurCapability.nonNullOf((LivingEntity) entity)
                .addTransfurProgress(10f, transfurType, TransfurContext.ADD_PROGRESS_DEF);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return transfurType != null ? LatexSyringeItem.encodeTransfur(transfurType) : ItemRegistry.SYRINGE_ITEM.get().getDefaultInstance();
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains(TransfurManager.TRANSFUR_TYPE_KEY))
            transfurType = TransfurManager.getTransfurType(new ResourceLocation(tag.getString(TransfurManager.TRANSFUR_TYPE_KEY)));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if(transfurType != null) tag.putString(TransfurManager.TRANSFUR_TYPE_KEY, transfurType.id.toString());
    }
}