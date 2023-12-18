package net.zaharenko424.a_changed;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.network.packets.ClientboundPlayerTransfurUpdatePacket;
import net.zaharenko424.a_changed.network.packets.ClientboundRemotePlayerTransfurUpdatePacket;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.capability.TransfurCapability.NO_CAPABILITY_EXC;
import static net.zaharenko424.a_changed.registry.EntityRegistry.WHITE_LATEX_WOLF_MALE;
@ParametersAreNonnullByDefault
public class TransfurManager {

    public static final String TRANSFUR_TYPE_KEY ="transfur_type";
    public static final String TRANSFUR_PROGRESS_KEY="transfur_progress";
    public static final String TRANSFURRED_KEY="transfurred";
    public static final int LATEX_DAMAGE_BONUS=1;
    @ApiStatus.Internal
    public static float TRANSFUR_TOLERANCE=20;


    public static boolean hasCapability(LivingEntity entity){
        return entity.getCapability(TransfurCapability.CAPABILITY).isPresent();
    }

    public static void updatePlayer(ServerPlayer player){
        updatePlayer(player,player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(NO_CAPABILITY_EXC));
    }

    public static void updatePlayer(ServerPlayer player, ITransfurHandler handler){
        player.refreshDimensions();
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->player),new ClientboundPlayerTransfurUpdatePacket(handler));
        PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(()->player),new ClientboundRemotePlayerTransfurUpdatePacket(handler,player.getUUID()));
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

    /**
     * Serverside use only!
     */
    public static void addTransfurProgress(LivingEntity entity, float amount, AbstractTransfurType transfurType){
        addTransfurProgress(entity,amount,transfurType,TransfurSource.GENERIC,true);
    }

    /**
     * Serverside use only!
     */
    public static void addTransfurProgress(LivingEntity entity, float amount, AbstractTransfurType transfurType, TransfurSource source){
        addTransfurProgress(entity,amount,transfurType,source,true);
    }

    /**
     * Serverside use only!
     */
    public static void addTransfurProgress(LivingEntity entity,float amount, AbstractTransfurType transfurType,TransfurSource source, boolean checkResistance){
        if(entity.level().isClientSide||!entity.isAlive()) return;
        entity.getCapability(TransfurCapability.CAPABILITY).ifPresent((handler)->{
            if(handler.isBeingTransfurred()) return;
            float finalAmount=amount;
            if(checkResistance) {
                float resistance = (float) entity.getAttributeValue(LATEX_RESISTANCE);
                finalAmount*=(1-resistance);
            }
            float progress=handler.getTransfurProgress()+finalAmount;
            if(progress>=TRANSFUR_TOLERANCE){
                transfur(entity,handler,transfurType,source);
                return;
            }
            handler.setTransfurProgress(progress,transfurType);
            if(entity instanceof ServerPlayer player) updatePlayer(player,handler);
        });
    }

    public static @Nullable AbstractTransfurType getTransfurType(Player player){
        LazyOptional<ITransfurHandler> optional = player.getCapability(TransfurCapability.CAPABILITY);
        return optional.isPresent()?optional.orElseThrow(NO_CAPABILITY_EXC).getTransfurType():null;
    }

    /**
     * Serverside use only!
     */
    public static void transfur(LivingEntity entity, AbstractTransfurType transfurType){
        if(entity.level().isClientSide) return;
        entity.getCapability(TransfurCapability.CAPABILITY).ifPresent((handler-> transfur(entity,handler,transfurType,TransfurSource.GENERIC)));
    }

    private static void transfur(LivingEntity entity,ITransfurHandler handler,AbstractTransfurType transfurType, TransfurSource source){
        SoundEvent sound=source==TransfurSource.GENERIC?SoundRegistry.TRANSFUR.get():SoundRegistry.TRANSFUR_1.get();
        Level level=entity.level();
        if(entity instanceof ServerPlayer player){
            transfurPlayer(player,handler,transfurType,sound);
            return;
        }
        spawnLatex(transfurType,level,entity.blockPosition());
        entity.playSound(sound);
        entity.discard();
    }

    private static void transfurPlayer(ServerPlayer player,ITransfurHandler handler, AbstractTransfurType transfurType, SoundEvent sound){
        Level level=player.level();
        level.playSound(null,player,sound, SoundSource.PLAYERS,1,1);
        if(!player.isCreative()){
            if(level.getGameRules().getBoolean(TRANSFUR_IS_DEATH)){
                handler.unTransfur();
                updatePlayer(player,handler);
                player.hurt(TransfurDamageSource.transfur(player,player.getLastHurtByMob()),Float.MAX_VALUE);
                spawnLatex(transfurType,level,player.blockPosition());
                return;
            }
            if(level.getGameRules().getBoolean(CHOOSE_TF_OR_DIE)){
                handler.setBeingTransfurred(true);
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->player),new ClientboundOpenTransfurScreenPacket());
                return;
            }
        }
        handler.transfur(transfurType);
        updatePlayer(player,handler);
    }

    public static void spawnLatex(AbstractTransfurType transfurType, Level level, BlockPos pos){
        Objects.requireNonNullElseGet(getTransfurEntity(transfurType.id), WHITE_LATEX_WOLF_MALE).spawn((ServerLevel) level,pos,MobSpawnType.CONVERSION);
    }

    public static void unTransfur(ServerPlayer player){
        player.getCapability(TransfurCapability.CAPABILITY).ifPresent((handler->{
            handler.unTransfur();
            updatePlayer(player,handler);
        }));
    }

    public static boolean isOrganic(Player player){
        return getTransfurType(player).isOrganic();
    }

    /**
     * Serverside only!
     */
    //Might be a cause of server lag if there are too many entities loaded. Can be replaced with clientside check if progress/tolerance > 1 -> alpha = 1
    public static void recalculateTransfurProgress(LivingEntity entity){
        if(entity.level().isClientSide) return;
        entity.getCapability(TransfurCapability.CAPABILITY).ifPresent(handler ->{
            if(handler.isTransfurred()||handler.isBeingTransfurred()) return;
            if(handler.getTransfurProgress()<TRANSFUR_TOLERANCE||handler.getTransfurType()==null) return;
            transfur(entity,handler, handler.getTransfurType(), TransfurSource.GENERIC);
        });
    }

    private static final String KEY="changed";

    public static @NotNull CompoundTag modTag(CompoundTag tag){
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
            EntityType<?> entity=BuiltInRegistries.ENTITY_TYPE.get(getTransfurType(transfurType).id);
            if(entity==EntityType.PIG) return null;
            return (EntityType<AbstractLatexBeast>) entity;

        } catch (Exception exception){
            AChanged.LOGGER.warn("Exception occurred while fetching entity for transfur type "+ transfurType,exception);
            return null;
        }
    }
}