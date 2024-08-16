package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public interface Ability {

    /**
     * Only active abilities can be selected and activated.
     * @return whether the ability is active or passive.
     */
    boolean isActive();

    /**
     * Called to draw the icon of this ability. x & y - coordinates of top left corner.
     */
    void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay);

    boolean hasScreen();

    /**
     * @return screen that will be shown to player when opening menu of this ability.
     */
    Screen getScreen(@NotNull Player holder);

    /**
     * Handles all network activity regarding this ability (client & server) except activation & deactivation.
     */
    void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull PlayPayloadContext context);

    /**
     * Called when server receives ActivateAbilityPacket.
     * @param oneShot whether the click is one-and-done or hold.
     */
    void activate(@NotNull LivingEntity holder, boolean oneShot, @NotNull FriendlyByteBuf additionalData);

    /**
     * Called when server receives DeactivateAbilityPacket.
     */
    void deactivate(@NotNull LivingEntity holder);

    /**
     * Called serverside when the ability is selected.
     */
    default void select(@NotNull LivingEntity holder){
        getAbilityData(holder).syncClients();
    }

    /**
     * Called serverside when the ability is unselected.
     */
    default void unselect(@NotNull LivingEntity holder){
        deactivate(holder);
    }

    /**
     * Handles client input and activates/deactivates ability accordingly.
     */
    void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft);

    /**
     * Ticks ability serverside. Called only when the ability is selected.
     */
    void serverTick(@NotNull LivingEntity holder);

    /**
     * @return ability data or null if there is no data.
     */
    AbilityData getAbilityData(@NotNull LivingEntity holder);
}