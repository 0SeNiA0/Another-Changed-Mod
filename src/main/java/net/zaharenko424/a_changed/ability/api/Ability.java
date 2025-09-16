package net.zaharenko424.a_changed.ability.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public interface Ability {

    default boolean isPassive(){
        return ActivationType.isPassive(activationType());
    }

    /**
     * Called to draw the icon of this ability. x & y - coordinates of top left corner.
     */
    void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay);

    default boolean hasScreen(){
        return false;
    }

    /**
     * @return screen that will be shown to player when opening menu of this ability.
     */
    default Screen getScreen(@NotNull Player holder){
        return null;
    }

    boolean canUse(@NotNull LivingEntity holder);

    default ActivationType activationType(){
        return ActivationType.INSTANT;
    }

    default boolean isActivated(LivingEntity holder){
        return false;
    }

    default void activate(LivingEntity holder){}

    default void deactivate(@NotNull LivingEntity holder){}

    /**
     * Called serverside when the ability is selected.
     */
    default void select(@NotNull LivingEntity holder){
        AbilityData data = getAbilityData(holder);
        if(data != null) data.sync();
    }

    /**
     * Called serverside when the ability is unselected.
     */
    default void unselect(@NotNull LivingEntity holder){
        deactivate(holder);
    }

    /**
     * Handles all network activity regarding this ability (client & server) except activation & deactivation.
     */
    default void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull IPayloadContext context){}

    /**
     * Ticks ability serverside. Called only when the ability is selected.
     */
    default void serverTick(@NotNull LivingEntity holder){}

    /**
     * Ticks ability serverside. Called only when the ability is unselected.
     */
    default void serverTickUnselected(@NotNull LivingEntity holder){}

    /**
     * @return ability data or null if there is no data.
     */
    default AbilityData getAbilityData(@NotNull LivingEntity holder){
        return null;
    }

    /**
     * Called serverside when this ability is added to the specified holder.
     */
    default void add(@NotNull LivingEntity holder){}

    /**
     * Called serverside when this ability is removed from specified holder.
     */
    default void remove(@NotNull LivingEntity holder){
        deactivate(holder);
    }
}