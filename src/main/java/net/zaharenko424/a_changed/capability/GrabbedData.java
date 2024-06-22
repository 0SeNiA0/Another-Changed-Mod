package net.zaharenko424.a_changed.capability;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.zaharenko424.a_changed.AChanged;

public class GrabbedData {

    final LivingEntity holder;
    LivingEntity grabbedBy;

    public GrabbedData(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity living) || !living.getType().is(AChanged.TRANSFURRABLE_TAG))
            throw new IllegalStateException("Tried to create GrabbedData for unsupported holder: " + holder);
        this.holder = living;
    }

    public LivingEntity getGrabbedBy(){
        return grabbedBy;
    }
}