package net.zaharenko424.a_changed.attachments;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.AbilityData;
import net.zaharenko424.a_changed.network.packets.ability.ClientboundAbilitySyncPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;

/**
 * Whoever modifies the data is responsible for its synchronisation!
 */
public class HypnosisData implements AbilityData {

    public final LivingEntity holder;
    LivingEntity hypnotisedBy;
    long lastHypnotised;
    boolean activated;

    public HypnosisData(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException();
        this.holder = entity;
    }

    public static HypnosisData dataOf(LivingEntity holder){//TODO potentially return null for inappropriate holders instead of throwing in <init>
        return holder.getData(AttachmentRegistry.HYPNOSIS_DATA);
    }

    public LivingEntity getHypnotisedBy() {
        if(holder.level().getGameTime() - lastHypnotised > 20 && !holder.level().tickRateManager().isFrozen()){
            hypnotisedBy = null;
        }
        return hypnotisedBy;
    }

    public void setHypnotisedBy(LivingEntity hypnotisedBy) {
        this.hypnotisedBy = hypnotisedBy;
        lastHypnotised = holder.level().getGameTime();
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        if(this.activated == activated) return;
        this.activated = activated;
        syncClients();
    }

    @Override
    public void syncClients() {
        if(holder.level().isClientSide) return;
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, updatePacket());
    }

    @Override
    public void syncClient(ServerPlayer receiver) {
        PacketDistributor.sendToPlayer(receiver, updatePacket());
    }

    private ClientboundAbilitySyncPacket updatePacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(1));
        //buf.writeVarInt(hypnotisedBy != null ? hypnotisedBy.getId() : -1);
        buf.writeBoolean(activated);
        return new ClientboundAbilitySyncPacket(holder.getId(), AbilityRegistry.HYPNOSIS_ABILITY.getId(), buf);
    }

    @Override
    public void fromPacket(FriendlyByteBuf packet) {
        //int id = packet.readVarInt();
        //hypnotisedBy = id == -1 ? null : !(holder.level().getEntity(id) instanceof LivingEntity living) ? null : living;
        activated = packet.readBoolean();
    }
}