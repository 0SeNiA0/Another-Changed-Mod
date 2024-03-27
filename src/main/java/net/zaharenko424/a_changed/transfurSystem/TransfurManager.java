package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.GrabMode;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
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

    public static boolean isTransfurred(@NotNull Player player){
        return TransfurCapability.nonNullOf(player).isTransfurred();
    }

    public static boolean isBeingTransfurred(@NotNull Player player){
        return TransfurCapability.nonNullOf(player).isBeingTransfurred();
    }

    public static float getTransfurProgress(@NotNull LivingEntity entity){
        return TransfurCapability.nonNullOf(entity).getTransfurProgress();
    }

    public static @Nullable AbstractTransfurType getTransfurType(@NotNull Player player){
        ITransfurHandler handler = TransfurCapability.of(player);
        return handler == null ? null : handler.getTransfurType();
    }

    public static @Nullable AbstractTransfurType getTransfurType(@NotNull ResourceLocation transfurType){
        return TransfurRegistry.TRANSFUR_REGISTRY.get(transfurType);
    }

    public static boolean isOrganic(@NotNull Player player){
        return getTransfurType(player).isOrganic();
    }

    public static boolean isHoldingEntity(@NotNull Player player){
        return GrabCapability.nonNullOf(player).getTarget() != null;
    }

    public static boolean isGrabbed(@NotNull Player player){
        return GrabCapability.nonNullOf(player).getGrabbedBy() != null;
    }

    public static GrabMode getGrabMode(@NotNull Player player){
        return GrabCapability.nonNullOf(player).grabMode();
    }

    public static boolean wantsToBeGrabbed(@NotNull Player player){
        return GrabCapability.nonNullOf(player).wantsToBeGrabbed();
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