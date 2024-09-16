package net.zaharenko424.a_changed.util;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.network.packets.ClientboundSmoothLookPacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

import static net.zaharenko424.a_changed.registry.EntityRegistry.WHITE_LATEX_WOLF_MALE;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TOLERANCE;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.getTransfurEntity;

public class TransfurUtils {

    public static final Consumer<LivingEntity> RECALCULATE_PROGRESS = target -> {
        if(target.level().isClientSide) throw new IllegalStateException("Cannot run serverside only methods on client!");
        TransfurHandler handler = TransfurHandler.of(target);
        if(handler == null) return;

        if(handler.isTransfurred() || handler.isBeingTransfurred()) return;
        if(handler.getTransfurProgress() >= TRANSFUR_TOLERANCE && handler.getTransfurType() != null)
            handler.transfur(handler.getTransfurType(), TransfurContext.TRANSFUR_DEF);
    };

    public static void addModifiers(LivingEntity holder, TransfurType transfurType){
        AttributeMap map = holder.getAttributes();
        AttributeInstance[] instance = new AttributeInstance[1];
        transfurType.modifiers.asMap().forEach((attribute, modifiers) -> {
            if(!map.hasAttribute(attribute)){
                AChanged.LOGGER.error("Attempted to add transfur modifier to not existing attribute {} {}", attribute, transfurType);
                return;
            }

            if(holder instanceof Player && attribute == Attributes.MAX_HEALTH){
                float maxHealthO = holder.getMaxHealth();
                instance[0] = map.getInstance(attribute);
                for(AttributeModifier modifier : modifiers){
                    instance[0].addTransientModifier(modifier);
                }
                float diff = holder.getMaxHealth() - maxHealthO;
                if(diff > 0) holder.setHealth(holder.getHealth() + diff);
                return;
            }

            instance[0] = map.getInstance(attribute);
            for(AttributeModifier modifier : modifiers){
                instance[0].addTransientModifier(modifier);
            }
        });
    }

    public static void removeModifiers(LivingEntity holder, TransfurType transfurType){
        AttributeMap map = holder.getAttributes();
        AttributeInstance[] instance = new AttributeInstance[1];
        transfurType.modifiers.asMap().forEach((attribute, modifiers) -> {
            if(!map.hasAttribute(attribute)) return;

            if(holder instanceof Player && attribute == Attributes.MAX_HEALTH){
                float maxHealthO = holder.getMaxHealth();
                instance[0] = map.getInstance(attribute);
                for(AttributeModifier modifier : modifiers){
                    instance[0].removeModifier(modifier);
                }
                if(holder.getMaxHealth() - maxHealthO < 0) holder.setHealth(holder.getMaxHealth());
                return;
            }

            instance[0] = map.getInstance(attribute);
            for(AttributeModifier modifier : modifiers){
                instance[0].removeModifier(modifier);
            }
        });
    }

    public static AbstractLatexBeast spawnLatex(@NotNull TransfurType transfurType, @NotNull ServerLevel level, @NotNull BlockPos pos){
        return Objects.requireNonNullElseGet(getTransfurEntity(transfurType.id), WHITE_LATEX_WOLF_MALE).spawn(level, pos, MobSpawnType.CONVERSION);
    }

    public static Vec2 targetLookAngles(Vec3 looker, Vec3 target){
        double dx = target.x() - looker.x();
        double dy = target.y() - looker.y();
        double dz = target.z() - looker.z();
        double dist = Math.sqrt(dx * dx + dz * dz);

        return new Vec2(Mth.wrapDegrees((float)(-(Mth.atan2(dy, dist) * 180.0F / (float)Math.PI))),
                        Mth.wrapDegrees((float)(Mth.atan2(dz, dx) * 180.0F / (float)Math.PI) - 90.0F));
    }

    /**
     * Makes looker look at the target position.
     * @param looker entity that needs to be pointed to target.
     * @param anchor anchor
     * @param target target position.
     * @param mustSee whether the looker has to see the target.
     * @param speed applies only to players. Dictates the speed with which the player will look at target.
     * @return whether the looker was pointed to target
     */
    public static boolean smoothLookAt(LivingEntity looker, EntityAnchorArgument.Anchor anchor, Vec3 target, boolean mustSee, float speed){
        if(looker.level().isClientSide) return false;

        Vec3 lookerPos = anchor.apply(looker);

        if(mustSee && looker.level().clip(new ClipContext(lookerPos, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, looker)).getType() != HitResult.Type.MISS) return false;

        if(!(looker instanceof ServerPlayer) && !mustSee){
            looker.lookAt(anchor, target);
            return true;
        }

        Vec2 rot = targetLookAngles(anchor.apply(looker), target);

        if(mustSee){
            float diffX = Mth.abs(looker.getXRot() - rot.x);
            float diffY = Mth.abs(Mth.wrapDegrees(looker.getYRot() - rot.y));
            if(diffX > 45 || diffY > 45) return false;//    Check if target is outside FOV
        }

        if(!(looker instanceof ServerPlayer player)){
            looker.lookAt(anchor, target);
            return true;
        }

        player.setXRot(Mth.lerp(.6f, player.getXRot(), rot.x));
        player.setYRot(Mth.rotLerp(.6f, player.getYRot(), rot.y));

        PacketDistributor.sendToPlayer(player,
                new ClientboundSmoothLookPacket(rot.x, rot.y, speed, looker.getType().updateInterval() + 1));
        return true;
    }
}