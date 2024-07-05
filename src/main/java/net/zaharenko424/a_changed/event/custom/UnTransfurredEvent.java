package net.zaharenko424.a_changed.event.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;

/**
 * Fired after player is untransfurred. <p>Do not transfur/untransfur player here or delay it by 1 tick.</p>
 */
public class UnTransfurredEvent extends Event {

    private final Player player;
    private final TransfurType prevTransfurType;

    public UnTransfurredEvent(Player player, TransfurType prevTransfurType){
        this.player = player;
        this.prevTransfurType = prevTransfurType;
    }

    public Player getPlayer() {
        return player;
    }

    public TransfurType getPrevTransfurType() {
        return prevTransfurType;
    }
}