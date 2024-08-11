package net.zaharenko424.a_changed.attachments;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public class HypnosisData {

    public final LivingEntity holder;
    LivingEntity hypnotisedBy;
    boolean activated;

    public HypnosisData(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException();
        this.holder = entity;
    }

    public LivingEntity getHypnotisedBy() {
        return hypnotisedBy;
    }

    public void setHypnotisedBy(LivingEntity hypnotisedBy) {
        this.hypnotisedBy = hypnotisedBy;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}