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
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.client.screen.ability.WolfAbilityScreen;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class WolfAbility implements PassiveAbility {

    private static final List<Pair<String, SoundEvent>> sounds = List.of(Pair.of("Ambient", SoundEvents.WOLF_AMBIENT),
            Pair.of("Howl", SoundEvents.WOLF_HOWL), Pair.of("Growl", SoundEvents.WOLF_GROWL),
            Pair.of("Hurt", SoundEvents.WOLF_HURT), Pair.of("Whine", SoundEvents.WOLF_WHINE));

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        graphics.drawCenteredString(Minecraft.getInstance().font, "Wolf Ability",x + 16, y + 12, Color.YELLOW.getRGB());
        //TODO add icon?
    }

    @Override
    public boolean hasScreen() {
        return true;
    }

    @Override
    public Screen getScreen(@NotNull Player holder) {
        return FMLLoader.getDist().isClient() ? Utils.get(()-> new WolfAbilityScreen(sounds)) : null;
    }

    @Override
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull IPayloadContext context) {
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