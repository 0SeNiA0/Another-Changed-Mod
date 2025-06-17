package net.zaharenko424.a_changed.attachment;

import io.netty.buffer.Unpooled;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.AbilityData;
import net.zaharenko424.a_changed.network.packets.ability.ClientboundAbilitySyncPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DLPupMeltData implements AbilityData {

    public static final Serializer SERIALIZER = new Serializer();

    private final LivingEntity holder;
    private boolean molten;

    public DLPupMeltData(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException();
        this.holder = entity;
    }

    public static DLPupMeltData dataOf(LivingEntity holder){
        return holder.getData(AttachmentRegistry.DL_PUP_MELT_DATA);
    }

    public boolean isMolten(){
        return molten;
    }

    public void setMolten(boolean molten){
        if(this.molten == molten) return;
        this.molten = molten;
        syncClients();
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
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(1));
        buf.writeBoolean(molten);
        return new ClientboundAbilitySyncPacket(holder.getId(), AbilityRegistry.DL_PUP_MELT.getId(), buf);
    }

    @Override
    public void fromPacket(@NotNull FriendlyByteBuf packet) {
        molten = packet.readBoolean();
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, DLPupMeltData> {

        @Override
        public @NotNull DLPupMeltData read(@NotNull IAttachmentHolder holder, CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            DLPupMeltData data = new DLPupMeltData(holder);
            data.molten = tag.getBoolean("molten");
            return data;
        }

        @Override
        public @Nullable CompoundTag write(DLPupMeltData attachment, HolderLookup.@NotNull Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("molten", attachment.molten);
            return tag;
        }
    }
}
