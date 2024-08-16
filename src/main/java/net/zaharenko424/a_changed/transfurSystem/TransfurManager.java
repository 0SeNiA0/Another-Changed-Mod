package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.GrabMode;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class TransfurManager {

    public static final String TRANSFUR_TYPE_KEY = "transfur_type";
    public static final String TRANSFUR_PROGRESS_KEY = "transfur_progress";
    public static final String BEING_TRANSFURRED_KEY = "isBeingTransfurred";
    public static final String TRANSFURRED_KEY = "transfurred";
    public static final int LATEX_DAMAGE_BONUS = 1;
    public static final float DEF_TRANSFUR_TOLERANCE = 20;
    @ApiStatus.Internal
    public static float TRANSFUR_TOLERANCE = DEF_TRANSFUR_TOLERANCE;
    public static final int MAX_ABILITIES = 6;

    public static boolean isTransfurred(@NotNull LivingEntity entity){
        return entity instanceof LatexBeast || TransfurHandler.nonNullOf(entity).isTransfurred();
    }

    public static boolean isBeingTransfurred(@NotNull Player player){
        return TransfurHandler.nonNullOf(player).isBeingTransfurred();
    }

    public static float getTransfurProgress(@NotNull LivingEntity entity){
        return TransfurHandler.nonNullOf(entity).getTransfurProgress();
    }

    public static @Nullable TransfurType getTransfurType(@NotNull LivingEntity player){
        TransfurHandler handler = TransfurHandler.of(player);
        return handler == null ? null : handler.getTransfurType();
    }

    public static @Nullable TransfurType getTransfurType(@NotNull ResourceLocation transfurType){
        return TransfurRegistry.TRANSFUR_REGISTRY.get(transfurType);
    }

    public static boolean isOrganic(@NotNull Player player){
        return getTransfurType(player).isOrganic();
    }

    public static boolean isHoldingEntity(@NotNull LivingEntity holder){
        GrabData data = GrabData.dataOf(holder);
        return data != null ? data.getGrabbedEntity() != null : false;
    }

    public static boolean isGrabbed(@NotNull LivingEntity entity){
        GrabData data = GrabData.dataOf(entity);
        return data != null ? data.getGrabbedBy() != null : false;
    }

    public static GrabMode getGrabMode(@NotNull LivingEntity entity){
        GrabData data = GrabData.dataOf(entity);
        return data != null ? data.getMode() : null;
    }

    public static boolean wantsToBeGrabbed(@NotNull Player player){
        return GrabData.dataOf(player).wantsToBeGrabbed();
    }

    public static boolean hasAbility(DeferredHolder<Ability, ? extends Ability> ability, LivingEntity holder){
        return hasAbility(ability.get(), holder);
    }

    public static boolean hasAbility(Ability ability, LivingEntity holder){
        if(holder instanceof AbstractLatexBeast latex) return latex.transfurType.abilities.contains(ability);

        TransfurHandler handler = TransfurHandler.of(holder);
        return handler != null && handler.hasAbility(ability);
    }

    public static boolean hasCatAbility(LivingEntity entity){
        return hasAbility(AbilityRegistry.CAT_PASSIVE, entity);
    }

    public static boolean hasFallFlyingAbility(LivingEntity entity){
        return hasAbility(AbilityRegistry.FALL_FLYING_PASSIVE, entity);
    }

    public static boolean hasFishAbility(LivingEntity entity){
        return hasAbility(AbilityRegistry.FISH_PASSIVE, entity);
    }

    public static boolean hasWolfAbility(LivingEntity entity){
        return hasAbility(AbilityRegistry.WOLF_PASSIVE, entity);
    }

    public static @Nullable EntityType<AbstractLatexBeast> getTransfurEntity(@NotNull ResourceLocation transfurType){
        try {
            EntityType<?> entity = BuiltInRegistries.ENTITY_TYPE.get(getTransfurType(transfurType).id);
            if(entity == EntityType.PIG) return null;
            return (EntityType<AbstractLatexBeast>) entity;
        } catch (Exception exception){
            AChanged.LOGGER.error("Exception occurred while fetching entity for transfur type "+ transfurType, exception);
            return null;
        }
    }
}