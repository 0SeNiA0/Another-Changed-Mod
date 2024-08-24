package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.HypnosisData;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.network.packets.ClientboundSmoothLookPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundActivateAbilityPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundDeactivateAbilityPacket;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class HypnosisAbility implements Ability {

    public static final ResourceLocation activated = AChanged.textureLoc("gui/ability_activated");

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        graphics.drawCenteredString(Minecraft.getInstance().font, "Hypnosis", x + 16, y + 6, Color.MAGENTA.getRGB());
        graphics.drawCenteredString(Minecraft.getInstance().font, "<img placeholder>", x + 16, y + 16, Color.MAGENTA.getRGB());
        if(overlay && getAbilityData(player).isActivated()) graphics.blit(activated,x - 8, y - 8, 0, 0, 48, 48, 48, 48);
        //TODO add icon
    }

    @Override
    public boolean hasScreen() {
        return false;
    }

    @Override
    public Screen getScreen(@NotNull Player holder) {
        return null;
    }

    @Override
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull PlayPayloadContext context) {}

    @Override
    public void activate(@NotNull LivingEntity holder, boolean oneShot, @Nullable FriendlyByteBuf additionalData) {
        getAbilityData(holder).setActivated(true);
    }

    @Override
    public void deactivate(@NotNull LivingEntity holder) {
        getAbilityData(holder).setActivated(false);
    }

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {
        HypnosisData hypnosisData = getAbilityData(localPlayer);

        if(Keybindings.ABILITY_KEY.isDown()) {
            if(!hypnosisData.isActivated()) {
                hypnosisData.setActivated(true);
                PacketDistributor.SERVER.noArg().send(new ServerboundActivateAbilityPacket(false, null));
            }
        } else {
            if(hypnosisData.isActivated()){
                hypnosisData.setActivated(false);
                PacketDistributor.SERVER.noArg().send(new ServerboundDeactivateAbilityPacket());
            }
        }
    }

    private static final float speed = .5f;
    private static final double playerHypnosisRange = 16;
    private static final double playerHypnosisRangeSqr = 16 * 16;

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        if(holder instanceof Player && !getAbilityData(holder).isActivated()) return;// Don't check activatedness for mobs. Only used for overlay
        HypnosisData hypnosisData;
        LivingEntity hypnotisedBy;
        if(holder instanceof Targeting latex){
            LivingEntity target = latex.getTarget();
            if(!DamageSources.checkTarget(target)) return;

            hypnosisData = HypnosisData.dataOf(target);
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

        Vec3 playerPos = holder.getEyePosition();
        Vec3 targetPos;
        Vec2 lookAngles;
        for(LivingEntity target : targets){
            targetPos = target.getEyePosition();

            //check if player can see the target
            if(holder.level().clip(new ClipContext(playerPos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target)).getType() != HitResult.Type.MISS) continue;
            lookAngles = TransfurUtils.targetLookAngles(playerPos, targetPos);
            if(lookAngles.x > 50 || lookAngles.y > 50) continue;//TODO test if ok

            hypnosisData = HypnosisData.dataOf(target);
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

    @Override
    public HypnosisData getAbilityData(@NotNull LivingEntity holder) {
        return HypnosisData.dataOf(holder);
    }
}