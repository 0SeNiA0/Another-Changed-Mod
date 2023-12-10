package net.zaharenko424.testmod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.testmod.capability.ITransfurHandler;
import net.zaharenko424.testmod.capability.TransfurCapability;
import net.zaharenko424.testmod.entity.AbstractLatexBeast;
import net.zaharenko424.testmod.network.PacketHandler;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurUpdatePacket;
import net.zaharenko424.testmod.network.packets.ClientboundRemotePlayerTransfurUpdatePacket;
import net.zaharenko424.testmod.registry.TransfurRegistry;
import net.zaharenko424.testmod.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.zaharenko424.testmod.TestMod.LATEX_RESISTANCE;
import static net.zaharenko424.testmod.TestMod.LOGGER;
import static net.zaharenko424.testmod.capability.TransfurCapability.NO_CAPABILITY_EXC;
import static net.zaharenko424.testmod.registry.EntityRegistry.WHITE_LATEX_WOLF_MALE;

public class TransfurManager {

    public static final String TRANSFUR_TYPE_KEY ="transfur_type";
    public static final String TRANSFUR_PROGRESS_KEY="transfur_progress";
    public static final String TRANSFURRED_KEY="transfurred";
    public static final int LATEX_DAMAGE_BONUS=1;
    @ApiStatus.Internal
    public static int TRANSFUR_TOLERANCE=20;


    public static boolean hasCapability(@NotNull LivingEntity entity){
        return entity.getCapability(TransfurCapability.CAPABILITY).isPresent();
    }

    public static void updatePlayer(@NotNull ServerPlayer player){
        updatePlayer(player,player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(NO_CAPABILITY_EXC));
    }

    private static void updatePlayer(@NotNull ServerPlayer player,@NotNull ITransfurHandler handler){
        player.refreshDimensions();
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->player),new ClientboundPlayerTransfurUpdatePacket(handler));
        PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(()->player),new ClientboundRemotePlayerTransfurUpdatePacket(handler,player.getUUID()));
    }

    public static boolean isTransfurred(@NotNull Player player){
        return player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).isTransfurred();
    }

    public static float getTransfurProgress(@NotNull Player player){
        return player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).getTransfurProgress();
    }

    public static void addTransfurProgress(@NotNull LivingEntity entity, float amount, @NotNull AbstractTransfurType transfurType){
        addTransfurProgress(entity,amount,transfurType,true);
    }

    /**
     * Serverside use only!
     */
    public static void addTransfurProgress(@NotNull LivingEntity entity,float amount, @NotNull AbstractTransfurType transfurType, boolean checkResistance){
        if(entity.level().isClientSide) return;
        entity.getCapability(TransfurCapability.CAPABILITY).ifPresent((handler)->{
            float finalAmount=amount;
            if(checkResistance) {
                float resistance = (float) entity.getAttributeValue(LATEX_RESISTANCE);
                finalAmount*=(1-resistance);
            }
            float progress=handler.getTransfurProgress()+finalAmount;
            if(progress>=TRANSFUR_TOLERANCE){
                transfur(entity,handler,transfurType);
                return;
            }
            handler.setTransfurProgress(progress,transfurType);
            if(entity instanceof ServerPlayer player) updatePlayer(player,handler);
        });
    }

    public static @Nullable AbstractTransfurType getTransfurType(@NotNull Player player){
        LazyOptional<ITransfurHandler> optional = player.getCapability(TransfurCapability.CAPABILITY);
        return optional.isPresent()?optional.orElseThrow(NO_CAPABILITY_EXC).getTransfurType():null;
    }

    /**
     * Serverside use only!
     */
    public static void transfur(@NotNull LivingEntity entity, @NotNull AbstractTransfurType transfurType){
        if(entity.level().isClientSide) return;
        LOGGER.info("transfur");
        entity.getCapability(TransfurCapability.CAPABILITY).ifPresent((handler-> transfur(entity,handler,transfurType)));
    }

    private static void transfur(@NotNull LivingEntity entity,@NotNull ITransfurHandler handler,@NotNull AbstractTransfurType transfurType){
        if(entity instanceof ServerPlayer player){
            handler.transfur(transfurType);
            updatePlayer(player,handler);
            return;
        }
        Objects.requireNonNullElseGet(getTransfurEntity(transfurType.id), WHITE_LATEX_WOLF_MALE).spawn((ServerLevel) entity.level(),entity.blockPosition(),MobSpawnType.CONVERSION);
        entity.remove(Entity.RemovalReason.DISCARDED);
    }

    public static void unTransfur(@NotNull ServerPlayer player){
        player.getCapability(TransfurCapability.CAPABILITY).ifPresent((handler->{
            handler.unTransfur();
            updatePlayer(player,handler);
        }));
    }

    private static final String KEY="changed";

    public static @NotNull CompoundTag modTag(@NotNull CompoundTag tag){
        if(tag.contains(KEY)) return tag.getCompound(KEY);
        tag.put(KEY,new CompoundTag());
        return tag.getCompound(KEY);
    }

    public static boolean hasModTag(@Nullable CompoundTag tag){
        return tag != null && tag.contains(KEY);
    }

    public static @Nullable AbstractTransfurType getTransfurType(ResourceLocation transfurType){
        return TransfurRegistry.TRANSFUR_REGISTRY.get(transfurType);
    }

    public static @Nullable EntityType<AbstractLatexBeast> getTransfurEntity(ResourceLocation transfurType){
        try {
            return (EntityType<AbstractLatexBeast>) BuiltInRegistries.ENTITY_TYPE.get(getTransfurType(transfurType).id);

        } catch (Exception exception){
            TestMod.LOGGER.warn("Exception occurred while fetching entity for transfur type "+transfurType.toString(),exception);
            return null;
        }
    }
}