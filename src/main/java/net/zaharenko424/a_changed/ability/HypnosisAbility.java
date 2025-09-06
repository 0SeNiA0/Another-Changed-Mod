package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.commands.arguments.EntityAnchorArgument;
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
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.ability.api.ActivationType;
import net.zaharenko424.a_changed.attachment.HypnosisData;
import net.zaharenko424.a_changed.attachment.HypnotisedData;
import net.zaharenko424.a_changed.network.packets.ClientboundSmoothLookPacket;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class HypnosisAbility implements Ability {

    public static final ResourceLocation activated = AChanged.textureLoc("gui/ability_activated");

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        graphics.drawCenteredString(Minecraft.getInstance().font, "Hypnosis", x + 16, y + 6, Color.MAGENTA.getRGB());
        if(overlay && getAbilityData(player).isActivated()) graphics.blit(activated,x - 16, y - 16, 0, 0, 0, 64, 64, 64, 64);
        //TODO add icon
    }

    @Override
    public boolean canUse(@NotNull LivingEntity holder) {
        return TransfurManager.getTransfurType(holder) != null;
    }

    @Override
    public ActivationType activationType() {
        return ActivationType.HOLD;
    }

    @Override
    public boolean isActivated(LivingEntity holder) {
        return getAbilityData(holder).isActivated();
    }

    @Override
    public void activate(@NotNull LivingEntity holder) {
        getAbilityData(holder).setActivated(true);
    }

    @Override
    public void deactivate(@NotNull LivingEntity holder) {
        getAbilityData(holder).setActivated(false);
    }

    private static final float speed = .5f;
    private static final double playerHypnosisRange = 16;
    private static final double playerHypnosisRangeSqr = 16 * 16;

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        if(holder instanceof Player && !getAbilityData(holder).isActivated()) return;// Don't check activatedness for mobs. Only used for overlay

        if(holder instanceof Targeting latex){
            LivingEntity target = latex.getTarget();
            if(!DamageSources.checkTFTarget(target)) return;

            doHypnotise(holder, target);
            return;
        }
        if(!(holder instanceof Player)) return; //player

        List<LivingEntity> targets = holder.level().getEntitiesOfClass(LivingEntity.class, holder.getBoundingBox().inflate(playerHypnosisRange), entity ->
                entity != holder && DamageSources.checkTFTarget(entity) && entity.distanceToSqr(holder) <= playerHypnosisRangeSqr);

        Vec3 playerPos = holder.getEyePosition();
        Vec3 targetPos;
        Vec2 lookAngles;
        for(LivingEntity target : targets){
            targetPos = target.getEyePosition();

            //check if player can see the target
            if(holder.level().clip(new ClipContext(playerPos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target)).getType() != HitResult.Type.MISS) continue;
            lookAngles = TransfurUtils.targetLookAngles(playerPos, targetPos);
            if(lookAngles.x > 50 || lookAngles.y > 50) continue;

            doHypnotise(holder, target);
        }
    }

    private void doHypnotise(LivingEntity holder, LivingEntity target){
        LivingEntity hypnotisedBy = HypnotisedData.getHypnotisedBy(target);
        if(hypnotisedBy != null && hypnotisedBy != holder && hypnotisedBy.isAlive()) return;

        if(TransfurUtils.smoothLookAt(target, EntityAnchorArgument.Anchor.EYES, holder.getEyePosition(), true, speed)){
            HypnotisedData.hypnotise(target, holder);
            return;
        }

        if(!(target instanceof ServerPlayer sPlayer)) return;

        if(hypnotisedBy != null)
            PacketDistributor.sendToPlayer(sPlayer,
                    new ClientboundSmoothLookPacket(sPlayer.getXRot(), sPlayer.getYRot(), speed, target.getType().updateInterval() + 1));
        HypnotisedData.hypnotise(target, null);
    }

    @Override
    public HypnosisData getAbilityData(@NotNull LivingEntity holder) {
        return HypnosisData.dataOf(holder);
    }
}