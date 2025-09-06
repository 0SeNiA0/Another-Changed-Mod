package net.zaharenko424.a_changed.attachment;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.zaharenko424.a_changed.ability.api.AbilityData;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.util.TransfurUtilsClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class DLPupMeltData implements AbilityData {

    public static final Serializer SERIALIZER = new Serializer();
    public static final Sync SYNC = new Sync();

    private final LivingEntity holder;
    private boolean molten;

    public DLPupMeltData(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException();
        this.holder = entity;
    }

    public static DLPupMeltData dataOf(LivingEntity holder){
        return holder.getData(AttachmentRegistry.DL_PUP_MELT_DATA);
    }

    @Override
    public boolean isActivated() {
        return molten;
    }

    public void setMolten(boolean molten){
        if(this.molten == molten) return;
        this.molten = molten;
        sync();
    }

    @Override
    public void sync() {
        holder.syncData(AttachmentRegistry.DL_PUP_MELT_DATA);
    }

    @ParametersAreNonnullByDefault
    public static class Serializer implements IAttachmentSerializer<CompoundTag, DLPupMeltData> {

        private Serializer(){}

        @Override
        public @NotNull DLPupMeltData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
            DLPupMeltData data = new DLPupMeltData(holder);
            data.molten = tag.getBoolean("molten");
            return data;
        }

        @Override
        public @Nullable CompoundTag write(DLPupMeltData attachment, HolderLookup.Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("molten", attachment.molten);
            return tag;
        }
    }

    @ParametersAreNonnullByDefault
    public static class Sync implements AttachmentSyncHandler<DLPupMeltData> {

        private Sync(){}

        @Override
        public void write(RegistryFriendlyByteBuf buf, DLPupMeltData attachment, boolean initialSync) {
            buf.writeBoolean(attachment.molten);
        }

        @Override
        public @Nullable DLPupMeltData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable DLPupMeltData previousValue) {
            DLPupMeltData data = previousValue == null ? new DLPupMeltData(holder) : previousValue;
            data.molten = buf.readBoolean();
            data.holder.refreshDimensions();

            if(!(holder instanceof Player player)) return data;

            TransfurHandler handler = TransfurHandler.nonNullOf(player);
            handler.setLastTFModelId(TransfurUtilsClient.updateTFModel((AbstractClientPlayer) player, handler.getLastTFModelId(), handler.getTransfurType()));
            return data;
        }
    }
}
