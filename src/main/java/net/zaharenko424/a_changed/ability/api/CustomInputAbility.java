package net.zaharenko424.a_changed.ability.api;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public interface CustomInputAbility extends Ability {

    /**
     * Handles client input in case ability key input isn't enough.
     */
    void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft);

    /**
     * Handles client input while the ability is unselected.
     */
    void inputTickUnselected(@NotNull Player localPlayer, @NotNull Minecraft minecraft);
}
