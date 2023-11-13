package net.zaharenko424.testmod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.zaharenko424.testmod.entity.AbstractLatexBeast;
import net.zaharenko424.testmod.entity.TransfurHolder;
import net.zaharenko424.testmod.entity.Transfurrable;
import net.zaharenko424.testmod.entity.transfurTypes.AbstractTransfurType;
import net.zaharenko424.testmod.network.PacketHandler;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurUpdatePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.zaharenko424.testmod.TestMod.LOGGER;
import static net.zaharenko424.testmod.TestMod.WHITE_LATEX_BEAST;

public class TransfurManager {

    public static final String TRANSFUR_TYPE_KEY ="transfur_type";
    public static final String TRANSFUR_PROGRESS_KEY="transfur_progress";
    public static final String TRANSFURRED_KEY="transfurred";
    public static final int TRANSFUR_TOLERANCE=20;//TODO add a way to change this magic number

    /**
     * Serverside use only!
     */
    public static void updatePlayer(@NotNull Player player){
        if(player.getCommandSenderWorld().isClientSide) return;

        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) player),
                new ClientboundPlayerTransfurUpdatePacket(getTransfurProgress(player),
                        getTransfurType(player),isTransfurred(player)));
        //TODO send update packet to everyone who can see @player
    }

    public static boolean isTransfurred(@NotNull Player player){
        return  ((TransfurHolder)player).mod$isTransfurred();
    }

    public static int getTransfurProgress(@NotNull Player player){
        return ((TransfurHolder)player).mod$getTransfurProgress();
    }

    /**
     * Serverside use only!
     */
    public static void addTransfurProgress(@NotNull Transfurrable transfurrable, int amount, @NotNull ResourceLocation transfurType){
        LOGGER.info("addTransfurProgress called");
        Level level=((LivingEntity)transfurrable).level();
        if(level.isClientSide) return;
        int progress=transfurrable.mod$getTransfurProgress()+amount;
        if(progress>=TRANSFUR_TOLERANCE) {
            transfur(transfurrable,transfurType);
            return;
        }
        transfurrable.mod$setTransfurProgress(progress,transfurType);
        LOGGER.info("transfur progress updated");
        //TODO update entity?
        if(transfurrable instanceof Player player) updatePlayer(player);
    }

    public static @Nullable AbstractTransfurType getTransfurType(@NotNull Player player){
        ResourceLocation type = ((TransfurHolder)player).mod$getTransfurType();
        return type!=null? getTransfurType(type):null;
    }

    /**
     * Serverside use only!
     */
    public static void transfur(@NotNull Transfurrable transfurrable, @NotNull ResourceLocation transfurType){
        LivingEntity entity=(LivingEntity) transfurrable;
        if(entity.level().isClientSide)return;
        if(!(transfurrable instanceof TransfurHolder holder)) {
            Objects.requireNonNullElseGet(getTransfurEntity(transfurType),WHITE_LATEX_BEAST).spawn((ServerLevel) entity.level(),entity.blockPosition(),MobSpawnType.CONVERSION);
            ((Entity)transfurrable).remove(Entity.RemovalReason.DISCARDED);
            return;
        }
        holder.mod$transfur(transfurType);
        updatePlayer((Player) holder);
    }

    /**
     * Serverside use only!
     */
    public static void unTransfur(@NotNull Player player){
        if(player.getCommandSenderWorld().isClientSide) return;
        ((TransfurHolder)player).mod$unTransfur();
        updatePlayer(player);
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
        return TestMod.REGISTRY.get().getValue(transfurType);
    }

    public static @Nullable EntityType<AbstractLatexBeast> getTransfurEntity(ResourceLocation transfurType){
        try {
            return (EntityType<AbstractLatexBeast>) ForgeRegistries.ENTITY_TYPES.getHolder(getTransfurType(transfurType).entityResourceLocation).get();

        } catch (Exception exception){
            TestMod.LOGGER.warn("Exception occurred while fetching entity for transfur type "+transfurType.toString(),exception);
            return null;
        }
    }
}