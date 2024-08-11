package net.zaharenko424.a_changed.ability;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.screen.ability.GrabAbilityLatexScreen;
import net.zaharenko424.a_changed.client.screen.ability.GrabAbilityPlayerScreen;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundActivateAbilityPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundDeactivateAbilityPacket;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;

public class GrabAbility implements Ability {

    public static final ResourceLocation icon = AChanged.textureLoc("gui/grab_assimilate");

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void drawIcon(@NotNull LivingEntity holder, @NotNull GuiGraphics graphics, int x, int y) {
        graphics.blit(icon, x, y, 0, 0, 32, 32, 32, 32);
    }

    @Override
    public Screen getScreen(@NotNull Player holder) {
        return FMLLoader.getDist().isClient() ?// More anti-crashing magic below
                TransfurManager.isTransfurred(holder) ? Client.get(GrabAbilityLatexScreen::new) : Client.get(GrabAbilityPlayerScreen::new)
                : null;//TODO what to do with organic latexes?
    }

    @Override
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull PlayPayloadContext context) {
        GrabData holderData = getAbilityData(holder);

        if(holder.level().isClientSide){
            holderData.fromPacket(buf);
            return;
        }

        switch(buf.readByte()){
            case 0 -> holderData.setMode(buf.readEnum(GrabMode.class));
            case 1 -> holderData.setWantsToBeGrabbed(buf.readBoolean());
        }
    }

    @Override
    public void activate(@NotNull LivingEntity holder, boolean oneShot, @NotNull FriendlyByteBuf additionalData) {
        if(!additionalData.isReadable()) {
            getAbilityData(holder).drop();
        } else {
            int targetId = additionalData.readVarInt();
            Entity entity0 = holder.level().getEntity(targetId);

            if(!(entity0 instanceof LivingEntity entity) || TransfurManager.isGrabbed(entity) || holder.distanceTo(entity) > 3 ) return;

            GrabData holderData = GrabData.dataOf(holder);
            if(entity0 instanceof Player player1
                    && (!holderData.getMode().givesDebuffToTarget && !TransfurManager.wantsToBeGrabbed(player1))) return;
            holderData.grab(entity);
        }
    }

    @Override
    public void deactivate(@NotNull LivingEntity holder) {
        getAbilityData(holder).drop();
    }

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {
        if(Keybindings.GRAB_KEY.consumeClick() && TransfurManager.isTransfurred(localPlayer)) {
            if(TransfurManager.isOrganic(localPlayer)) return;//TMP ignore organic players for now
            clientGrabLogic(localPlayer, minecraft);
            return;
        }

        if(Keybindings.ABILITY_SELECTION.isDown() && minecraft.screen == null){
            if(TransfurManager.isTransfurred(localPlayer)) {
                AChanged.LOGGER.warn("That's not supposed to happen ...");
            } else minecraft.setScreen(Client.get(GrabAbilityPlayerScreen::new));//Magic
        }
    }

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        GrabData holderData = GrabData.dataOf(holder);

        LivingEntity grabbedEntity = holderData.getGrabbedEntity();
        GrabMode mode = holderData.getMode();

        if(holderData.getGrabbedBy() != null || grabbedEntity == null) return;
        if(!grabbedEntity.isAlive()) {
            if(holder instanceof Player player) {
                if (grabbedEntity.getRemovalReason() == Entity.RemovalReason.UNLOADED_WITH_PLAYER)
                    player.displayClientMessage(Component.translatable("message.a_changed.grabbed_player_left"), true);
                else player.displayClientMessage(Component.translatable("message.a_changed.grabbed_entity_died"), true);
            }
            holderData.drop();
            return;
        }
        if(mode == GrabMode.FRIENDLY){
            ((ServerPlayer)grabbedEntity).setCamera(holder);
            return;
        }
        hold(holder, grabbedEntity, mode);

        if(TransfurManager.isTransfurred(grabbedEntity)) return;//TMP make possible to hold latexes ?

        if(mode.givesDebuffToTarget && !grabbedEntity.hasEffect(MobEffectRegistry.GRABBED_DEBUFF.get())) {
            if(mode == GrabMode.ASSIMILATE) {
                grabbedEntity.hurt(DamageSources.assimilation(holder, null), Integer.MAX_VALUE);
                holder.addEffect(new MobEffectInstance(MobEffectRegistry.ASSIMILATION_BUFF.get(), 6000, 0, false, false));
                if(holder instanceof Player player) player.getFoodData().eat(6, 1);
            } else if(mode == GrabMode.REPLICATE) {
                TransfurHandler handler = TransfurHandler.of(grabbedEntity);
                if(handler != null) handler.transfur(TransfurManager.getTransfurType(holder), TransfurContext.TRANSFUR_TF);
            }
            holderData.drop();
        }
    }

    @Override
    public GrabData getAbilityData(@NotNull LivingEntity holder) {
        return GrabData.dataOf(holder);
    }

    private void clientGrabLogic(Player player, Minecraft minecraft){
        if(TransfurManager.isHoldingEntity(player)){
            if(player.isCrouching()){
                PacketDistributor.SERVER.noArg().send(new ServerboundDeactivateAbilityPacket());
                return;
            }
            return;
        }

        Entity crosshairEntity = minecraft.crosshairPickEntity;

        if(crosshairEntity == null || player.distanceTo(crosshairEntity) > 2.5) return;

        if(TransfurManager.isGrabbed(player)) {
            player.displayClientMessage(Component.translatable("message.a_changed.grabbed"),true);
            return;
        }

        if(!(crosshairEntity instanceof LivingEntity entity)
                || !entity.getType().is(AChanged.TRANSFURRABLE_TAG)) {
            player.displayClientMessage(Component.translatable("message.a_changed.grabbed_wrong_entity"), true);
            return;
        }

        GrabMode mode = TransfurManager.getGrabMode(player);
        if(!mode.checkTarget(entity)) {
            player.displayClientMessage(Component.translatable("message.a_changed.only_friendly_grab_players"), true);
            return;
        }
        if(entity instanceof Player player1){
            if(TransfurManager.isBeingTransfurred(player1) || TransfurManager.isTransfurred(player1)) return;
            if(TransfurManager.isGrabbed(player1)){
                player.displayClientMessage(Component.translatable("message.a_changed.player_already_grabbed"), true);
                return;
            }
            if(!mode.givesDebuffToTarget && !TransfurManager.wantsToBeGrabbed(player1)){
                player.displayClientMessage(Component.translatable("message.a_changed.player_doesnt_want_to_be_grabbed"), true);
                return;
            }
        }

        if(player.hasEffect(MobEffectRegistry.GRAB_COOLDOWN.get())){
            player.displayClientMessage(Component.translatable("message.a_changed.grab_cooldown",
                    String.valueOf((float) player.getEffect(MobEffectRegistry.GRAB_COOLDOWN.get()).getDuration() / 20)), true);
            return;
        }

        PacketDistributor.SERVER.noArg().send(new ServerboundActivateAbilityPacket(true,
                new FriendlyByteBuf(Unpooled.buffer(4)).writeVarInt(entity.getId())));
    }

    private void hold(LivingEntity holder, LivingEntity grabbedEntity, GrabMode mode){
        float distance = 1.2f;
        if(mode == GrabMode.ASSIMILATE){
            distance -= grabbedEntity.hasEffect(MobEffectRegistry.GRABBED_DEBUFF.get())
                    ? 1 - (float) grabbedEntity.getEffect(MobEffectRegistry.GRABBED_DEBUFF.get()).getDuration() / GrabData.grabDuration
                    : 1;
        }

        float yaw = holder.getYHeadRot();
        float x = (float) ((-Mth.sin(Mth.DEG_TO_RAD * yaw) * distance) + holder.getX());
        float z = (float) ((Mth.cos(Mth.DEG_TO_RAD * yaw) * distance) + holder.getZ());

        grabbedEntity.fallDistance = 0;
        grabbedEntity.setDeltaMovement(x - grabbedEntity.getX(), holder.getY() - grabbedEntity.getY(), z - grabbedEntity.getZ());

        if(grabbedEntity instanceof ServerPlayer player) {
            PacketDistributor.PLAYER.with(player).send(new ClientboundSetEntityMotionPacket(player.getId(), player.getDeltaMovement()));
            TransfurUtils.smoothLookAt(player, EntityAnchorArgument.Anchor.EYES, holder.getEyePosition(), false, .6f);
        } else grabbedEntity.lookAt(EntityAnchorArgument.Anchor.EYES, holder.getEyePosition());
    }
}