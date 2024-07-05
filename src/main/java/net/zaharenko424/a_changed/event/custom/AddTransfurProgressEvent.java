package net.zaharenko424.a_changed.event.custom;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;

/**
 * Fired before calculating amount of transfur progress to add. Can be cancelled, which will result in no TF progress being added.
 */
public class AddTransfurProgressEvent extends Event implements ICancellableEvent {

    private final LivingEntity entity;
    private final TransfurType transfurType;
    private final float rawProgressToAdd;
    private float progressToAdd;

    public AddTransfurProgressEvent(LivingEntity entity, TransfurType transfurType, float rawProgressToAdd){
        this.entity = entity;
        this.transfurType = transfurType;
        this.rawProgressToAdd = rawProgressToAdd;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public TransfurType getTransfurType() {
        return transfurType;
    }

    public float getRawProgressToAdd() {
        return rawProgressToAdd;
    }

    public float getProgressToAdd() {
        return progressToAdd;
    }

    /**
     * Sets amount of TF progress to add bypassing resistance calculations.
     */
    public void setProgressToAdd(float progressToAdd){
        if(progressToAdd > 0) this.progressToAdd = progressToAdd;
    }
}