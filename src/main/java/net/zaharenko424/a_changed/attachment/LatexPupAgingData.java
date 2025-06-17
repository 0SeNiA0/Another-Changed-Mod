package net.zaharenko424.a_changed.attachment;

import io.netty.buffer.Unpooled;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.AbilityData;
import net.zaharenko424.a_changed.network.packets.ability.ClientboundAbilitySyncPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexPupAgingData implements AbilityData {

    public static final Serializer SERIALIZER = new Serializer();
    private static final int babyUntil = 24000;
    public static final int turnAfter = 48000;

    private final LivingEntity holder;
    private int age;
    private boolean freezeAging;

    public LatexPupAgingData(@NotNull IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException();
        this.holder = entity;
    }

    public boolean isBaby(){
        return age < babyUntil;
    }

    public void setBaby(boolean baby){
        if(holder.level().isClientSide) return;
        age = baby ? 0 : babyUntil;
        syncClients();
    }

    public boolean isAboutToTurn(){
        return age >= turnAfter;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if(holder.level().isClientSide) return;
        if(age < 0) age = 0;
        this.age = age;
        syncClients();
    }

    public boolean isAgingFrozen(){
        return freezeAging;
    }

    public void freezeAging(boolean freeze){
        if(holder.level().isClientSide || freezeAging == freeze) return;
        freezeAging = freeze;
        syncClients();
    }

    public void tickAge(){
        if(holder.level().isClientSide || freezeAging) return;
        boolean wasBaby = isBaby();
        age++;
        if(wasBaby != isBaby()) holder.refreshDimensions();
    }

    public void speedUpAging(){
        if(holder.level().isClientSide || freezeAging) return;
        setAge(age + AgeableMob.getSpeedUpSecondsWhenFeeding(babyUntil - age));
    }

    @Override
    public void syncClients() {
        if(holder.level().isClientSide) return;
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, updatePacket());
    }

    @Override
    public void syncClient(@NotNull ServerPlayer receiver) {
        PacketDistributor.sendToPlayer(receiver, updatePacket());
    }

    private ClientboundAbilitySyncPacket updatePacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(6));
        buf.writeVarInt(age);
        buf.writeBoolean(freezeAging);
        return new ClientboundAbilitySyncPacket(holder.getId(), AbilityRegistry.DL_PUP_AGE.getId(), buf);
    }

    @Override
    public void fromPacket(@NotNull FriendlyByteBuf packet) {
        boolean wasBaby = isBaby();
        age = packet.readVarInt();
        if(wasBaby != isBaby()) holder.refreshDimensions();
        freezeAging = packet.readBoolean();
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, LatexPupAgingData> {

        @Override
        public @NotNull LatexPupAgingData read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            LatexPupAgingData data = new LatexPupAgingData(holder);
            data.age = tag.getInt("age");
            data.freezeAging = tag.getBoolean("freeze");
            return data;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull LatexPupAgingData attachment, HolderLookup.@NotNull Provider provider) {
            if(attachment.age == 0 || attachment.age >= LatexPupAgingData.turnAfter) return null;
            CompoundTag tag = new CompoundTag();
            tag.putInt("age", attachment.age);
            tag.putBoolean("freeze", attachment.freezeAging);
            return tag;
        }
    }
}
