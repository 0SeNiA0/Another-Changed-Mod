package net.zaharenko424.a_changed.attachment;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.ref.WeakReference;

@ParametersAreNonnullByDefault
public class HypnotisedData {

    private WeakReference<LivingEntity> hypnotisedBy;
    private int lastHypnotised;

    public HypnotisedData(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity)) throw new IllegalArgumentException();
    }

    public static void hypnotise(LivingEntity target, @Nullable LivingEntity hypnotisedBy){
        if(hypnotisedBy == null) {
            target.removeData(AttachmentRegistry.HYPNOTISED_DATA);
            return;
        }

        HypnotisedData data = target.getData(AttachmentRegistry.HYPNOTISED_DATA);
        data.hypnotisedBy = new WeakReference<>(hypnotisedBy);
        data.lastHypnotised = target.tickCount;
    }

    public static LivingEntity getHypnotisedBy(LivingEntity target){
        HypnotisedData data = target.getExistingDataOrNull(AttachmentRegistry.HYPNOTISED_DATA);
        if(data == null) return null;

        if(data.hypnotisedBy == null || data.hypnotisedBy.get() == null || target.tickCount - data.lastHypnotised > 20){
            target.removeData(AttachmentRegistry.HYPNOTISED_DATA);
            return null;
        }

        return data.hypnotisedBy.get();
    }
}
