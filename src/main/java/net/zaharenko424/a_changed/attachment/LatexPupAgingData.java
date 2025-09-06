package net.zaharenko424.a_changed.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.zaharenko424.a_changed.ability.api.AbilityData;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class LatexPupAgingData implements AbilityData {

    public static final Serializer SERIALIZER = new Serializer();
    public static final Sync SYNC = new Sync();

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
        sync();
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
        sync();
    }

    public boolean isAgingFrozen(){
        return freezeAging;
    }

    public void freezeAging(boolean freeze){
        if(holder.level().isClientSide || freezeAging == freeze) return;
        freezeAging = freeze;
        sync();
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
    public void sync() {
        holder.syncData(AttachmentRegistry.LATEX_PUP_AGING_DATA);
    }

    @ParametersAreNonnullByDefault
    public static class Serializer implements IAttachmentSerializer<CompoundTag, LatexPupAgingData> {

        private Serializer(){}

        @Override
        public @NotNull LatexPupAgingData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
            LatexPupAgingData data = new LatexPupAgingData(holder);
            data.age = tag.getInt("age");
            data.freezeAging = tag.getBoolean("freeze");
            return data;
        }

        @Override
        public @Nullable CompoundTag write(LatexPupAgingData attachment, HolderLookup.Provider provider) {
            if(attachment.age == 0 || attachment.age >= LatexPupAgingData.turnAfter) return null;
            CompoundTag tag = new CompoundTag();
            tag.putInt("age", attachment.age);
            tag.putBoolean("freeze", attachment.freezeAging);
            return tag;
        }
    }

    @ParametersAreNonnullByDefault
    public static class Sync implements AttachmentSyncHandler<LatexPupAgingData> {

        private Sync(){}

        @Override
        public void write(RegistryFriendlyByteBuf buf, LatexPupAgingData attachment, boolean initialSync) {
            buf.writeVarInt(attachment.age);
            buf.writeBoolean(attachment.freezeAging);
        }

        @Override
        public @Nullable LatexPupAgingData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable LatexPupAgingData previousValue) {
            LatexPupAgingData data = previousValue == null ? new LatexPupAgingData(holder) : previousValue;
            data.age = buf.readVarInt();
            data.freezeAging = buf.readBoolean();
            return data;
        }
    }
}
