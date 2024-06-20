package net.zaharenko424.a_changed.event.custom;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;

/**
 * Fired after entity is transfurred. If transfurred entity died in the process, entity will be the latex beast that spawned on its place. <p>Do not transfur/untransfur player here or delay it by 1 tick.</p>
 */
public class TransfurredEvent extends Event {

    private final LivingEntity entity;
    private final TransfurType transfurType;

    public TransfurredEvent(LivingEntity entity, TransfurType transfurType){
        this.entity = entity;
        this.transfurType = transfurType;
    }

    public LivingEntity getEntity(){
        return entity;
    }

    public TransfurType getTransfurType(){
        return transfurType;
    }
}