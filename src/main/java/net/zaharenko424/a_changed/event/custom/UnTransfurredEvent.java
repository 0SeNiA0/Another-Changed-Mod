package net.zaharenko424.a_changed.event.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;

/**
 * Fired after player is untransfurred. <p>Do not transfur/untransfur player here or delay it by 1 tick.</p>
 */
public class UnTransfurredEvent extends Event {

    private final Player player;
    private final TransfurType prevTransfurType;
    private final TransfurContext context;

    public UnTransfurredEvent(Player player, TransfurType prevTransfurType, TransfurContext context){
        this.player = player;
        this.prevTransfurType = prevTransfurType;
        this.context = context;
    }

    public Player getPlayer() {
        return player;
    }

    public TransfurType getPrevTransfurType() {
        return prevTransfurType;
    }

    public TransfurContext getContext() {
        return context;
    }
}