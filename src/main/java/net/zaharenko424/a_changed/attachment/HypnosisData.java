package net.zaharenko424.a_changed.attachment;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.zaharenko424.a_changed.ability.api.AbilityData;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class HypnosisData implements AbilityData {

    public static final Sync SYNC = new Sync();

    public final LivingEntity holder;
    private boolean activated;

    public HypnosisData(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException();
        this.holder = entity;
    }

    public static HypnosisData dataOf(LivingEntity holder){//TODO potentially return null for inappropriate holders instead of throwing in <init>
        return holder.getData(AttachmentRegistry.HYPNOSIS_DATA);
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        if(this.activated == activated) return;
        this.activated = activated;
        sync();
    }

    @Override
    public void sync() {
        holder.syncData(AttachmentRegistry.HYPNOSIS_DATA);
    }

    @ParametersAreNonnullByDefault
    public static class Sync implements AttachmentSyncHandler<HypnosisData> {

        private Sync(){}

        @Override
        public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
            return holder == to;//Only sync to self, others don't need to know when this is activated
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf, HypnosisData attachment, boolean initialSync) {
            buf.writeBoolean(attachment.activated);
        }

        @Override
        public @Nullable HypnosisData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable HypnosisData previousValue) {
            HypnosisData data = previousValue == null ? new HypnosisData(holder) : previousValue;
            data.activated = buf.readBoolean();
            return data;
        }
    }
}