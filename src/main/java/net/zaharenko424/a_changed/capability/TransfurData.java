package net.zaharenko424.a_changed.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.zaharenko424.a_changed.AChanged.ATTACHMENTS;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.*;

class TransfurData {

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<TransfurData>> TRANSFUR_DATA = ATTACHMENTS.register("transfur_data", ()->
            AttachmentType.builder(TransfurData::new).serialize(new Serializer()).build());

    float transfurProgress = 0;
    AbstractTransfurType transfurType = null;
    boolean isTransfurred = false;
    boolean isBeingTransfurred = false;

    static class Serializer implements IAttachmentSerializer<CompoundTag, TransfurData> {

        @Override
        public @NotNull TransfurData read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag) {
            if(!(holder instanceof LivingEntity entity) || !entity.getType().is(AChanged.TRANSFURRABLE_TAG)) throw new RuntimeException();
            TransfurCapability.nonNullOf(entity).load(tag);
            return new TransfurData();
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransfurData transfurData) {
            CompoundTag tag = new CompoundTag();
            tag.putFloat(TRANSFUR_PROGRESS_KEY, transfurData.transfurProgress);
            if(transfurData.transfurType != null) tag.putString(TRANSFUR_TYPE_KEY, transfurData.transfurType.id.toString());
            tag.putBoolean(BEING_TRANSFURRED_KEY, transfurData.isBeingTransfurred);
            tag.putBoolean(TRANSFURRED_KEY, transfurData.isTransfurred);
            return tag;
        }
    }
}