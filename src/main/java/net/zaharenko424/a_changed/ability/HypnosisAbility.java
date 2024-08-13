package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.zaharenko424.a_changed.attachments.HypnosisData;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.network.packets.ClientboundSmoothLookPacket;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class HypnosisAbility implements Ability {

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void drawIcon(@NotNull LivingEntity holder, @NotNull GuiGraphics graphics, int x, int y) {
        graphics.drawCenteredString(Minecraft.getInstance().font, "Hypnosis", x + 16, y + 6, Color.MAGENTA.getRGB());
        graphics.drawCenteredString(Minecraft.getInstance().font, "<img placeholder>", x + 16, y + 16, Color.MAGENTA.getRGB());
        //TODO add icon
    }

    @Override
    public Screen getScreen(@NotNull Player holder) {
        return null;
    }

    @Override
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull PlayPayloadContext context) {

    }

    @Override
    public void activate(@NotNull LivingEntity holder, boolean oneShot, @NotNull FriendlyByteBuf additionalData) {

    }

    @Override
    public void deactivate(@NotNull LivingEntity holder) {

    }

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {
        HypnosisData hypnosisData = localPlayer.getData(AttachmentRegistry.HYPNOSIS_DATA);

        if(Keybindings.GRAB_KEY.isDown()) {       //TODO placeholder keybinding
            if(!hypnosisData.isActivated()) {
                hypnosisData.setActivated(true);
                //send packet
            }
        } else {
            if(hypnosisData.isActivated()){
                hypnosisData.setActivated(false);
                //sendPacket
            }
        }
    }

    private static final float speed = .5f;
    private static final double playerHypnosisRange = 16;
    private static final double playerHypnosisRangeSqr = 16 * 16;

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        HypnosisData hypnosisData;
        LivingEntity hypnotisedBy;
        if(holder instanceof Targeting latex){
            LivingEntity target = latex.getTarget();
            if(!DamageSources.checkTarget(target)) return;

            hypnosisData = target.getData(AttachmentRegistry.HYPNOSIS_DATA);
            hypnotisedBy = hypnosisData.getHypnotisedBy();
            if(hypnotisedBy != null && hypnotisedBy != holder && hypnotisedBy.isAlive()) return;

            if(TransfurUtils.smoothLookAt(target, EntityAnchorArgument.Anchor.EYES, holder.getEyePosition(), true, speed)){
                hypnosisData.setHypnotisedBy(holder);
            } else if(target instanceof ServerPlayer serverPl) {
                if(hypnotisedBy != null)
                    PacketDistributor.PLAYER.with(serverPl).send(
                            new ClientboundSmoothLookPacket(serverPl.getXRot(), serverPl.getYRot(), speed, target.getType().updateInterval() + 1));
                hypnosisData.setHypnotisedBy(null);
            }
            return;
        }
        if(!(holder instanceof Player)) return; //player

        List<LivingEntity> targets = holder.level().getEntitiesOfClass(LivingEntity.class, holder.getBoundingBox().inflate(playerHypnosisRange), entity ->
                DamageSources.checkTarget(entity) && entity.distanceToSqr(holder) <= playerHypnosisRangeSqr);
        targets.remove(holder);//TODO add a limit for how many entities can be selected?

        for(LivingEntity target : targets){
            hypnosisData = target.getData(AttachmentRegistry.HYPNOSIS_DATA);
            hypnotisedBy = hypnosisData.getHypnotisedBy();
            if(hypnotisedBy != null && hypnotisedBy != holder && hypnotisedBy.isAlive()) continue;

            if(TransfurUtils.smoothLookAt(target, EntityAnchorArgument.Anchor.EYES, holder.getEyePosition(), true, speed)){
                hypnosisData.setHypnotisedBy(holder);
            } else if(target instanceof ServerPlayer serverPl) {
                if(hypnotisedBy != null)
                    PacketDistributor.PLAYER.with(serverPl).send(
                            new ClientboundSmoothLookPacket(serverPl.getXRot(), serverPl.getYRot(), speed, target.getType().updateInterval() + 1));
                hypnosisData.setHypnotisedBy(null);
            }
        }
    }
}