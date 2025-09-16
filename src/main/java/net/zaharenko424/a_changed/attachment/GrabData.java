package net.zaharenko424.a_changed.attachment;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.zaharenko424.a_changed.ability.GrabMode;
import net.zaharenko424.a_changed.ability.api.AbilityData;
import net.zaharenko424.a_changed.ability.api.AttachmentSelfSyncHandler;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class GrabData implements AbilityData {

    public static final Serializer SERIALIZER = new Serializer();
    public static final Sync SYNC = new Sync();

    private final LivingEntity holder;

    private LivingEntity grabbedEntity;
    private LivingEntity grabbedBy;//move to GrabbedData and add GrabEscapeAbility for non tf players?
    private GrabMode mode = GrabMode.ASSIMILATE;
    private int grabCooldown;
    private boolean activated;

    private boolean wantsToBeGrabbed;

    public GrabData(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException();
        this.holder = entity;
        if(!(holder instanceof Player)) wantsToBeGrabbed = true;
    }

    public static GrabData dataOf(LivingEntity holder){//TODO potentially return null for inappropriate holders instead of throwing in <init>
        return holder.getData(AttachmentRegistry.GRAB_DATA);
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    public LivingEntity getGrabbedEntity() {
        return grabbedEntity;
    }

    public boolean wantsToBeGrabbed() {
        return wantsToBeGrabbed;
    }

    public void setWantsToBeGrabbed(boolean wantsToBeGrabbed) {
        if(holder.level().isClientSide || wantsToBeGrabbed == this.wantsToBeGrabbed) return;

        if(!wantsToBeGrabbed) escape(false);

        this.wantsToBeGrabbed = wantsToBeGrabbed;
        sync();
    }

    public void escape(boolean force){
        if(holder.level().isClientSide || grabbedBy == null) return;

        GrabData data = dataOf(grabbedBy);
        GrabMode mode = data.getMode();
        if(force || !mode.isOffensive()) {
            if(mode.isOffensive()) data.holder.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
            data.drop();
        }
    }

    public LivingEntity getGrabbedBy() {
        return grabbedBy;
    }

    public void setGrabbedBy(LivingEntity grabbedBy) {
        if(holder.level().isClientSide) return;

        this.grabbedBy = grabbedBy;
        if(grabbedEntity != null) drop();
        else sync();
    }

    public GrabMode getMode() {
        return mode;
    }

    public void setMode(@NotNull GrabMode mode) {
        if(holder.level().isClientSide || this.mode == mode) return;

        if(grabbedEntity == null){
            this.mode = mode;
            sync();
            return;
        }

        if((!this.mode.isOffensive() && mode.isOffensive()) || !mode.checkTarget(grabbedEntity)) {
            drop();
            this.mode = mode;
            sync();
            return;
        }

        if(grabbedEntity instanceof ServerPlayer player){
            if(this.mode == GrabMode.FRIENDLY){
                player.setCamera(null);
                player.setGameMode(GameType.SURVIVAL);
                player.removeEffect(MobEffectRegistry.FRIENDLY_GRAB);
            }

            if(mode == GrabMode.FRIENDLY){
                player.setGameMode(GameType.SPECTATOR);
                player.addEffect(new MobEffectInstance(MobEffectRegistry.FRIENDLY_GRAB, -1, 0, false, false));
            }
        }

        if(mode.isOffensive()) grabbedEntity.addEffect(new MobEffectInstance(MobEffectRegistry.GRABBED_DEBUFF, grabDuration, 0, false, false));
        if(this.mode.isOffensive() && !mode.isOffensive()) grabbedEntity.removeEffect(MobEffectRegistry.GRABBED_DEBUFF);

        if(this.mode.givesDebuffToSelf){
            if(!mode.givesDebuffToSelf) holder.removeEffect(MobEffectRegistry.HOLDING_DEBUFF);
        } else if(mode.givesDebuffToSelf) holder.addEffect(new MobEffectInstance(MobEffectRegistry.HOLDING_DEBUFF, -1, 0, false, false));

        this.mode = mode;
        sync();
    }

    public int getGrabCooldown(){
        return grabCooldown;
    }

    public void tickCooldown(){
        if(grabCooldown <= 0) return;
        grabCooldown--;
        sync();
    }

    public static final int grabCooldown_ = 160;
    public static final int grabDuration = 160;

    public boolean canGrab(LivingEntity potentialTarget) {
        return potentialTarget != null && getGrabbedBy() == null && getGrabbedEntity() == null
                && getGrabCooldown() <= 0
                && (mode.isOffensive() || dataOf(potentialTarget).wantsToBeGrabbed())
                && mode.checkTarget(potentialTarget);
    }

    public static boolean canGrab(LivingEntity holder, LivingEntity potentialTarget){
        GrabData data = dataOf(holder);
        return data.canGrab(potentialTarget);
    }

    public void grab(@NotNull LivingEntity target){
        if(holder.level().isClientSide || !canGrab(target)) return;

        grabbedEntity = target;
        grabbedEntity.setNoGravity(true);
        dataOf(grabbedEntity).setGrabbedBy(holder);
        if(target instanceof ServerPlayer player1 && mode == GrabMode.FRIENDLY) {
            player1.setGameMode(GameType.SPECTATOR);
            player1.addEffect(new MobEffectInstance(MobEffectRegistry.FRIENDLY_GRAB, -1, 0, false, false));
        }

        if(mode.isOffensive()) grabbedEntity.addEffect(new MobEffectInstance(MobEffectRegistry.GRABBED_DEBUFF, grabDuration, 0, false, false));
        if(mode.givesDebuffToSelf) holder.addEffect(new MobEffectInstance(MobEffectRegistry.HOLDING_DEBUFF, -1, 0, false, false));
        activated = true;
        sync();
    }

    public void drop() {
        if(holder.level().isClientSide || grabbedEntity == null) return;

        grabbedEntity.setNoGravity(false);
        if(grabbedEntity.isAlive()){
            if(grabbedEntity instanceof ServerPlayer player && mode == GrabMode.FRIENDLY) {
                player.setCamera(null);
                player.setGameMode(GameType.SURVIVAL);
                player.removeEffect(MobEffectRegistry.FRIENDLY_GRAB);
            }
            if(mode.isOffensive()) grabbedEntity.removeEffect(MobEffectRegistry.GRABBED_DEBUFF);
            dataOf(grabbedEntity).setGrabbedBy(null);
        }

        grabbedEntity = null;
        if(mode.givesDebuffToSelf) holder.removeEffect(MobEffectRegistry.HOLDING_DEBUFF);
        grabCooldown = grabCooldown_;
        activated = false;
        sync();
    }

    public void sync(){
        holder.syncData(AttachmentRegistry.GRAB_DATA);
    }

    @ParametersAreNonnullByDefault
    public static class Serializer implements IAttachmentSerializer<CompoundTag, GrabData> {

        private Serializer() {}

        @Override
        public @NotNull GrabData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider lookup) {
            GrabData data = new GrabData(holder);
            if(!(holder instanceof Player)) return data;

            data.mode = GrabMode.valueOf(tag.getString("mode"));
            data.grabCooldown = tag.getInt("grabCooldown");
            data.wantsToBeGrabbed = tag.getBoolean("wantToBeGrabbed");
            return data;
        }

        @Override
        public @Nullable CompoundTag write(GrabData data, HolderLookup.Provider lookup) {
            if(!(data.holder instanceof Player)) return null;

            CompoundTag tag = new CompoundTag();
            tag.putString("mode", data.mode.toString());
            if(data.grabCooldown > 0) tag.putInt("grabCooldown", data.grabCooldown);
            tag.putBoolean("wantToBeGrabbed", data.wantsToBeGrabbed);
            return tag;
        }
    }

    @ParametersAreNonnullByDefault
    public static class Sync implements AttachmentSelfSyncHandler<GrabData> {

        private Sync(){}

        @Override
        public void writeToSelf(RegistryFriendlyByteBuf buf, GrabData attachment, boolean initialSync) {
            write(buf, attachment, initialSync);
            buf.writeVarInt(attachment.grabCooldown);
            buf.writeBoolean(attachment.activated);
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf, GrabData attachment, boolean initialSync) {
            buf.writeVarInt(attachment.grabbedEntity != null ? attachment.grabbedEntity.getId() : -1);
            buf.writeVarInt(attachment.grabbedBy != null ? attachment.grabbedBy.getId() : -1);
            buf.writeEnum(attachment.mode);
            buf.writeBoolean(attachment.wantsToBeGrabbed);
        }

        @Override
        public @Nullable GrabData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable GrabData previousValue) {
            GrabData data = previousValue == null ? new GrabData(holder) : previousValue;

            Level level = data.holder.level();
            int id = buf.readVarInt();
            data.grabbedEntity = id == -1 ? null : level.getEntity(id) instanceof LivingEntity entity ? entity : null;
            id = buf.readVarInt();
            data.grabbedBy = id == -1 ? null : level.getEntity(id) instanceof LivingEntity entity ? entity : null;
            data.mode = buf.readEnum(GrabMode.class);
            data.wantsToBeGrabbed = buf.readBoolean();

            if(holder == Minecraft.getInstance().player) {
                data.grabCooldown = buf.readVarInt();
                data.activated = buf.readBoolean();
            }

            return data;
        }
    }
}