package net.zaharenko424.a_changed.event.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Fired after entity is transfurred. If transfurred entity died in the process, it will be removed from the world but data is still accessible. <p>Do not transfur/untransfur player here or delay it by 1 tick.</p>
 */
public class TransfurredEvent extends Event {

    private final LivingEntity entity;
    private final AbstractLatexBeast latex;
    private final TransfurType transfurType;
    private final DamageSource source;

    @ApiStatus.Internal
    public TransfurredEvent(LivingEntity entity, AbstractLatexBeast latex, TransfurType transfurType){
        this.entity = entity;
        this.latex = latex;
        this.transfurType = transfurType;
        this.source = entity.getLastDamageSource();

        if(entity instanceof ServerPlayer player) {
            AChanged.PLAYER_TRANSFURRED.get().trigger(player, source, transfurType);
        } else {
            if(source != null && source.getEntity() instanceof ServerPlayer player){
                AChanged.PLAYER_TRANSFURRED_ENTITY.get().trigger(player, source, transfurType);
            }
        }

    }

    public LivingEntity getEntity(){
        return entity;
    }

    public @Nullable AbstractLatexBeast getLatex() {
        return latex;
    }

    public TransfurType getTransfurType(){
        return transfurType;
    }

    public @Nullable DamageSource getSource(){
        return source;
    }
}