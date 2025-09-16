package net.zaharenko424.a_changed.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.block.VentDuct;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

    @Shadow @Final
    Inventory inventory;

    public MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    /**
     * Apply transfur progress to the thing, that is being attacked by the player.
     */
    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/Entity.hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),method = "attack",allow = 1)
    public boolean hurtProxy(@NotNull Entity target, DamageSource damageSource, float damage){
        if(target.level().isClientSide) return target.hurt(damageSource, damage);

        TransfurHandler handler = TransfurHandler.nonNullOf(this);
        if(!getMainHandItem().isEmpty() || !handler.isTransfurred() || handler.getTransfurType().isOrganic()) return target.hurt(damageSource, damage);
        damage += TransfurManager.LATEX_DAMAGE_BONUS;

        if(!DamageSources.checkTFTarget(target)) return target.hurt(damageSource, damage);
        if(!target.hurt(DamageSources.transfur(this), damage)) return false;

        float tfProgress = 5f;
        if(hasEffect(MobEffectRegistry.ASSIMILATION_BUFF)) tfProgress += 5;
        TransfurHandler.nonNullOf((LivingEntity) target)
                .addTransfurProgress(tfProgress, Objects.requireNonNull(handler.getTransfurType()), TransfurContext.DEF);
        return true;
    }


    /**
     * Allow using body armor when DL Pup.
     */
    @ModifyReturnValue(at = @At("RETURN"), method = "canUseSlot")
    private boolean onCanUseSlot(boolean original, @Local(argsOnly = true) EquipmentSlot slot){
        if(!AbilityUtils.hasLatexPupAbilities(this)) return original;

        return switch(slot){
            case CHEST, BODY -> true;
            default -> false;
        };
    }

    /**
     * Allow to lookup body armor when DL Pup.
     */
    @ModifyReturnValue(at = @At("TAIL"), method = "getItemBySlot")
    private ItemStack onGetItemBySlot(ItemStack original, @Local(argsOnly = true) EquipmentSlot slot){
        if(slot == EquipmentSlot.BODY && AbilityUtils.hasLatexPupAbilities(this)) return inventory.armor.getFirst();

        return original;
    }

    @ModifyReturnValue(at = @At("TAIL"), method = "doesEmitEquipEvent")
    private boolean onDoesEmitEquipEvent(boolean original, @Local(argsOnly = true) EquipmentSlot slot){
        return original || (slot == EquipmentSlot.BODY && AbilityUtils.hasLatexPupAbilities(this));
    }

    /**
     * Allow putting on body armor.
     * Disallow putting items in offhand.
     */
    @Inject(at = @At("TAIL"), method = "setItemSlot")
    private void onSetItemSlot(EquipmentSlot slot, ItemStack stack, CallbackInfo ci){
        if(slot == EquipmentSlot.OFFHAND && AbilityUtils.hasLatexPupAbilities(this)){
            ItemHandlerHelper.giveItemToPlayer((Player) self(), stack);
            return;
        }
        if(!(stack.getItem() instanceof AnimalArmorItem armor) || armor.getBodyType() != AnimalArmorItem.BodyType.CANINE
                || !AbilityUtils.hasLatexPupAbilities(this)) return;
        onEquipItem(slot, inventory.armor.set(0, stack), stack);
    }


    /**
     * @return  dimensions of specified pose when transfurred.
     */
    @ModifyReturnValue(at = @At("RETURN"), method = "getDefaultDimensions")
    private EntityDimensions onGetDimensions(EntityDimensions original, Pose pose) {
        if(self() instanceof ServerPlayer sPlayer && sPlayer.connection == null) return original;
        TransfurHandler handler = TransfurHandler.of(this);
        EntityDimensions dimensions = null;
        if(handler != null && handler.isTransfurred()) dimensions = handler.getTransfurType().getPoseDimensions(this, pose);
        return dimensions != null ? dimensions : original;
    }

    /**
     * Make player "swim" in vents.
     */
    @ModifyReturnValue(at = @At("TAIL"), method = "canPlayerFitWithinBlocksAndEntitiesWhen")
    private boolean onCanFitWithinBlocksAndEntitiesWhen(boolean original, Pose pose){
        if(pose != Pose.SWIMMING && getInBlockState().getBlock() instanceof VentDuct duct
                && duct.getShape(getInBlockState(), level(), blockPosition(), CollisionContext.empty()).bounds().move(blockPosition()).intersects(getBoundingBox())) {
            return false;
        }
        return original;
    }

    /**
     * Allows player to fly when transfurred as flying latex.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"),
            method = "tryToStartFallFlying")
    private boolean onTryStartFallFlyingCheck(boolean original){
        if(AbilityUtils.hasFallFlyingAbility(this)) return true;
        return original;
    }
}