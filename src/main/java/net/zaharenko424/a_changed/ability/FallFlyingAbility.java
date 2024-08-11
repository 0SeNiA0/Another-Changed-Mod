package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class FallFlyingAbility implements PassiveAbility {

    @Override
    public void drawIcon(@NotNull LivingEntity holder, @NotNull GuiGraphics graphics, int x, int y) {
        //TODO
    }

    @Override
    public Screen getScreen(@NotNull Player holder) {
        return null;
    }

    @Override
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull PlayPayloadContext context) {}

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {}

    @Override
    public void serverTick(@NotNull LivingEntity holder) {}
}