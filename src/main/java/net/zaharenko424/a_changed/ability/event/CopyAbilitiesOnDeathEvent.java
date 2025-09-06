package net.zaharenko424.a_changed.ability.event;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.zaharenko424.a_changed.ability.api.Ability;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Set;

/**
 * Fired when copying abilities on player death.
 */
@ParametersAreNonnullByDefault
public class CopyAbilitiesOnDeathEvent extends PlayerEvent {

    private final Player dead;
    private final Set<ResourceLocation> providersToCopy;
    private final Map<Ability, Set<ResourceLocation>> toCopy;

    @ApiStatus.Internal
    public CopyAbilitiesOnDeathEvent(Player player, Player dead, Set<ResourceLocation> providersToCopy, Map<Ability, Set<ResourceLocation>> toCopy) {
        super(player);
        this.dead = dead;
        this.providersToCopy = providersToCopy;
        this.toCopy = toCopy;
    }

    public Player getDead() {
        return dead;
    }

    public void copyProvider(ResourceLocation key){
        providersToCopy.add(key);
    }

    public void copy(Ability ability, ResourceLocation key){
        toCopy.computeIfAbsent(ability, ab -> new ObjectArraySet<>()).add(key);
    }
}
