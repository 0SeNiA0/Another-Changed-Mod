package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.capability.TransfurCapability.NO_CAPABILITY_EXC;

/**
 * Safe to use on any dist
 */
@ParametersAreNonnullByDefault
public class TransfurManager {

    public static final String TRANSFUR_TYPE_KEY = "transfur_type";
    public static final String TRANSFUR_PROGRESS_KEY = "transfur_progress";
    public static final String TRANSFURRED_KEY = "transfurred";
    public static final int LATEX_DAMAGE_BONUS = 1;
    @ApiStatus.Internal
    public static float TRANSFUR_TOLERANCE = 20;

    public static boolean hasCapability(LivingEntity entity){
        return entity.getCapability(TransfurCapability.CAPABILITY).isPresent();
    }

    public static boolean isTransfurred(Player player){
        return player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).isTransfurred();
    }

    public static boolean isBeingTransfurred(LivingEntity player){
        if(!(player instanceof Player)) return false;
        return player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).isBeingTransfurred();
    }

    public static float getTransfurProgress(Player player){
        return player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).getTransfurProgress();
    }

    public static @Nullable AbstractTransfurType getTransfurType(Player player){
        LazyOptional<ITransfurHandler> optional = player.getCapability(TransfurCapability.CAPABILITY);
        return optional.isPresent()?optional.orElseThrow(NO_CAPABILITY_EXC).getTransfurType():null;
    }

    public static boolean isOrganic(Player player){
        return getTransfurType(player).isOrganic();
    }

    public static @Nullable AbstractTransfurType getTransfurType(ResourceLocation transfurType){
        return TransfurRegistry.TRANSFUR_REGISTRY.get(transfurType);
    }

    public static @Nullable EntityType<AbstractLatexBeast> getTransfurEntity(ResourceLocation transfurType){
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