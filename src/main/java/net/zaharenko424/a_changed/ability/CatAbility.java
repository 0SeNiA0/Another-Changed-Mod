package net.zaharenko424.a_changed.ability;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.zaharenko424.a_changed.client.screen.ability.CatAbilityScreen;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class CatAbility implements PassiveAbility {

    private static final List<Pair<String, SoundEvent>> sounds = List.of(Pair.of("Meow", SoundEvents.CAT_AMBIENT),
            Pair.of("Purr", SoundEvents.CAT_PURR), Pair.of("Purreow", SoundEvents.CAT_PURREOW),
            Pair.of("Hiss", SoundEvents.CAT_HISS), Pair.of("Hurt", SoundEvents.CAT_HURT));

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        graphics.drawCenteredString(Minecraft.getInstance().font, "Cat Ability",x + 16, y + 12, Color.YELLOW.getRGB());
        //TODO add icon?
    }

    @Override
    public boolean hasScreen() {
        return true;
    }

    @Override
    public Screen getScreen(@NotNull Player holder) {
        return new CatAbilityScreen(sounds);
    }

    @Override
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull PlayPayloadContext context) {
        if(holder.level().isClientSide) return;
        holder.level().playSound(null, holder, sounds.get(buf.readByte()).right(), SoundSource.PLAYERS, 1, 1);
    }

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {}

    @Override
    public void serverTick(@NotNull LivingEntity holder) {}

    @Override
    public AbilityData getAbilityData(@NotNull LivingEntity holder) {
        return null;
    }
}