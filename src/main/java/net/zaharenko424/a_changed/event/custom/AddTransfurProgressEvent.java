package net.zaharenko424.a_changed.event.custom;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;

/**
 * Fired before calculating amount of transfur progress to add. Can be cancelled, which will result in no TF progress being added.
 */
public class AddTransfurProgressEvent extends Event implements ICancellableEvent {

    private final LivingEntity entity;
    private final TransfurType<?> transfurType;
    private final float rawProgressToAdd;
    private float progressToAdd;
    private final TransfurContext context;
    private TransfurContext newContext;

    public AddTransfurProgressEvent(LivingEntity entity, TransfurType<?> transfurType, float rawProgressToAdd, TransfurContext context){
        this.entity = entity;
        this.transfurType = transfurType;
        this.rawProgressToAdd = rawProgressToAdd;
        progressToAdd = rawProgressToAdd;
        this.context = context;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public TransfurType<?> getTransfurType() {
        return transfurType;
    }

    public float getRawProgressToAdd() {
        return rawProgressToAdd;
    }

    public float getProgressToAdd() {
        return progressToAdd;
    }

    /**
     * Sets new amount of TF progress to add.
     */
    public void setProgressToAdd(float progressToAdd){
        if(progressToAdd > 0) this.progressToAdd = progressToAdd;
    }

    public TransfurContext getOriginalContext(){
        return context;
    }

    public TransfurContext getContext(){
        return newContext == null ? context : newContext;
    }

    /**
     * Sets new TransfurContext.
     */
    public void setContext(TransfurContext context){
        this.newContext = context;
    }
}