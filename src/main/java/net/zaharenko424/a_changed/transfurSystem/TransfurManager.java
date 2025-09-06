package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.ability.GrabMode;
import net.zaharenko424.a_changed.attachment.GrabData;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class TransfurManager {

    public static final int LATEX_DAMAGE_BONUS = 1;//TODO move to attribute modifier?
    public static final float DEF_TRANSFUR_TOLERANCE = 20;
    @ApiStatus.Internal
    public static float TRANSFUR_TOLERANCE = DEF_TRANSFUR_TOLERANCE;
    public static final int MAX_ABILITIES = 6;

    public static boolean isTransfurred(@NotNull LivingEntity entity){
        if(entity instanceof LatexBeast) return true;
        TransfurHandler handler = TransfurHandler.of(entity);
        return handler != null && handler.isTransfurred();
    }

    public static boolean isBeingTransfurred(@NotNull LivingEntity entity){
        TransfurHandler handler = TransfurHandler.of(entity);
        return handler != null && handler.isBeingTransfurred();
    }

    public static float getTransfurProgress(@NotNull LivingEntity entity){
        return TransfurHandler.nonNullOf(entity).getTransfurProgress();
    }

    public static @Nullable TransfurType<?> getTransfurType(@NotNull LivingEntity entity){
        if(entity instanceof LatexBeast latex) return latex.transfurType();
        TransfurHandler handler = TransfurHandler.of(entity);
        return handler == null ? null : handler.getTransfurType();
    }

    public static @Nullable TransfurType<?> getTransfurType(@NotNull ResourceLocation transfurType){
        return TransfurRegistry.TRANSFUR_REGISTRY.get(transfurType);
    }

    public static @Nullable TransfurType<?> getTransfurType(int id){
        return TransfurRegistry.TRANSFUR_REGISTRY.byId(id);
    }

    public static int getTransfurId(@Nullable TransfurType<?> transfurType){
        return TransfurRegistry.TRANSFUR_REGISTRY.getId(transfurType);
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
}