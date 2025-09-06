package net.zaharenko424.a_changed.ability.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.zaharenko424.a_changed.ability.api.AbilityHolder;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Fired when player joins the server to set up the abilities.
 */
@ParametersAreNonnullByDefault
public class InitializePlayerAbilitiesEvent extends PlayerEvent {

    private final AbilityHolder holder;

    @ApiStatus.Internal
    public InitializePlayerAbilitiesEvent(Player player, AbilityHolder holder) {
        super(player);
        this.holder = holder;
    }

    public AbilityHolder getAbilityHolder(){
        return holder;
    }
}
