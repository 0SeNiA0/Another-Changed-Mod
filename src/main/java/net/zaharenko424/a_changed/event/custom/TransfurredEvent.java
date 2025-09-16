package net.zaharenko424.a_changed.event.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.zaharenko424.a_changed.registry.CriterionTriggerRegistry;
import net.zaharenko424.a_changed.transfurSystem.LatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Fired after entity is transfurred. If transfurred entity died in the process, it will be removed from the world but data is still accessible. <p>Do not transfur/untransfur player here or delay it by 1 tick.</p>
 */
public class TransfurredEvent extends Event {

    private final LivingEntity entity;
    private final LatexBeast latex;
    private final TransfurType<?> previous;
    private final TransfurType<?> transfurType;
    private final TransfurContext context;
    private final DamageSource source;

    @ApiStatus.Internal
    public TransfurredEvent(LivingEntity entity, LatexBeast latex, TransfurType<?> transfurType, TransfurContext context){
        this(entity, latex, null, transfurType, context, entity.getLastDamageSource());
    }

    @ApiStatus.Internal
    public TransfurredEvent(LivingEntity entity, LatexBeast latex, TransfurType<?> previous, TransfurType<?> transfurType, TransfurContext context, DamageSource source){
        this.entity = entity;
        this.latex = latex;
        this.previous = previous;
        this.transfurType = transfurType;
        this.context = context;
        this.source = source;

        if(entity instanceof ServerPlayer player && entity.isAlive()) {
            CriterionTriggerRegistry.PLAYER_TRANSFURRED_NO_DEATH.get().trigger(player, source, transfurType);
        }

        if(source != null && source.getEntity() instanceof ServerPlayer player){
            CriterionTriggerRegistry.PLAYER_TRANSFURRED_ENTITY.get().trigger(player, source, transfurType);
        }
    }

    public LivingEntity getEntity(){
        return entity;
    }

    public @Nullable LatexBeast getLatex() {
        return latex;
    }

    public @Nullable TransfurType<?> getPrevious(){
        return previous;
    }

    public TransfurType<?> getTransfurType(){
        return transfurType;
    }

    public TransfurContext getContext() {
        return context;
    }

    public @Nullable DamageSource getSource(){
        return source;
    }
}