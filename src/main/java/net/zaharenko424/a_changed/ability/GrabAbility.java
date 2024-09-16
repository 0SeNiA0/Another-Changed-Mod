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
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.screen.ability.GrabAbilityLatexScreen;
import net.zaharenko424.a_changed.client.screen.ability.GrabAbilityPlayerScreen;
import net.zaharenko424.a_changed.client.screen.ability.GrabEscapeScreen;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundAbilityPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundActivateAbilityPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundDeactivateAbilityPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.TransfurUtils;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class GrabAbility implements Ability {

    public static final ResourceLocation icon = AChanged.textureLoc("gui/grab_assimilate");
    public static final float CLOSE_ENOUGH = (float) (2.5 * 2.5);

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        GrabData holderData = getAbilityData(player);
        if(TransfurManager.isTransfurred(player)) {
            if(holderData.isActivated())
                graphics.blit(HypnosisAbility.activated, x - 8, y - 8, 0, 0, 48, 48, 48, 48);
            graphics.blit(holderData.getMode().texture,
                    x, y, 0, 0, 32, 32, 32, 32);
        } else {
            graphics.blit(holderData.wantsToBeGrabbed() ? GrabAbilityPlayerScreen.yes : GrabAbilityPlayerScreen.nope,
                    x, y, 0, 0, 32, 32, 32, 32);
        }
    }

    @Override
    public boolean hasScreen() {
        return true;
    }

    @Override
    public Screen getScreen(@NotNull Player holder) {
        return FMLLoader.getDist().isClient() ?// More anti-crashing magic below
                TransfurManager.isTransfurred(holder) ? Utils.get(GrabAbilityLatexScreen::new) : Utils.get(GrabAbilityPlayerScreen::new)
                : null;//TODO what to do with organic latexes?
    }

    @Override
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull IPayloadContext context) {
        GrabData holderData = getAbilityData(holder);

        if(holder.level().isClientSide){
            holderData.fromPacket(buf);
            return;
        }

        switch(buf.readByte()){
            case 0 -> holderData.setMode(buf.readEnum(GrabMode.class));
            case 1 -> holderData.setWantsToBeGrabbed(buf.readBoolean());
            case 2 -> {
                holderData.escape(buf.readBoolean());
                holderData.syncClient((ServerPlayer) holder);
            }
        }
    }

    @Override
    public void activate(@NotNull LivingEntity holder, boolean oneShot, @NotNull FriendlyByteBuf additionalData) {
        if(!additionalData.isReadable()) {
            getAbilityData(holder).drop();
        } else {
            int target = additionalData.readVarInt();
            if(!(holder.level().getEntity(target) instanceof LivingEntity living)) return;
            activate(holder, living);
        }
    }

    public void activate(@NotNull LivingEntity holder, LivingEntity entity){
        if(TransfurManager.isGrabbed(entity) || holder.distanceTo(entity) > 3 ) return;

        GrabData holderData = GrabData.dataOf(holder);
        if(entity instanceof Player player1
                && (!holderData.getMode().givesDebuffToTarget && !TransfurManager.wantsToBeGrabbed(player1))) return;
        holderData.grab(entity);
    }

    @Override
    public void deactivate(@NotNull LivingEntity holder) {
        getAbilityData(holder).drop();
    }

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {
        if(Keybindings.ABILITY_KEY.consumeClick()) {
            if(TransfurManager.isTransfurred(localPlayer) && !TransfurManager.isOrganic(localPlayer)) {//TMP ignore organic players for now
                clientGrabLogic(localPlayer, minecraft);
                return;
            }

            GrabData data = getAbilityData(localPlayer);
            LivingEntity grabbedBy = data.getGrabbedBy();
            if(grabbedBy != null) {
                if(getAbilityData(grabbedBy).getMode().givesDebuffToTarget) {
                    if(minecraft.screen == null) minecraft.setScreen(Utils.get(()-> new GrabEscapeScreen(localPlayer.getRandom())));
                } else {//TODO make grabbed entities follow where you are looking at
                    PacketDistributor.sendToServer(new ServerboundAbilityPacket(AbilityRegistry.GRAB_ABILITY.getId(),
                            new FriendlyByteBuf(Unpooled.buffer(2)).writeByte(2).writeBoolean(false)));
                }
            }
        }

        if(Keybindings.ABILITY_SELECTION.isDown() && minecraft.screen == null){// will only happen for non tf players
             minecraft.setScreen(Utils.get(GrabAbilityPlayerScreen::new));//Magic
        }
    }

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        GrabData holderData = GrabData.dataOf(holder);

        if(holderData.getGrabbedBy() != null){
            if(holder instanceof Player) ((Player) holder).displayClientMessage(Component.translatable("message.a_changed.try_escape_tip", Component.keybind(Keybindings.ABILITY_KEY.getName())), true);
            return;
        }
        if(!TransfurManager.isTransfurred(holder)) return;

        LivingEntity grabbedEntity = holderData.getGrabbedEntity();
        GrabMode mode = holderData.getMode();

        if(grabbedEntity == null) return;
        if(!grabbedEntity.isAlive()) {
            if(holder instanceof Player player) {
                Entity.RemovalReason reason = grabbedEntity.getRemovalReason();
                if(reason == Entity.RemovalReason.UNLOADED_WITH_PLAYER) player.displayClientMessage(Component.translatable("message.a_changed.grabbed_player_left"), true);
                if(reason != Entity.RemovalReason.DISCARDED) player.displayClientMessage(Component.translatable("message.a_changed.grabbed_entity_died"), true);
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

        if(!mode.givesDebuffToTarget) return;

        if(!grabbedEntity.hasEffect(MobEffectRegistry.GRABBED_DEBUFF)) {
            if(mode == GrabMode.ASSIMILATE) {
                grabbedEntity.hurt(DamageSources.assimilation(holder, null), Integer.MAX_VALUE);
                holder.addEffect(new MobEffectInstance(MobEffectRegistry.ASSIMILATION_BUFF, 6000, 0, false, false));
                if(holder instanceof Player player) player.getFoodData().eat(6, 1);
            } else if(mode == GrabMode.REPLICATE) {
                TransfurHandler handler = TransfurHandler.of(grabbedEntity);
                if(handler != null) handler.transfur(TransfurManager.getTransfurType(holder), TransfurContext.TRANSFUR_TF);
            }
            holderData.drop();
        } else if(mode == GrabMode.REPLICATE){
            TransfurHandler handler = TransfurHandler.of(grabbedEntity);
            handler.addTransfurProgress((TransfurManager.TRANSFUR_TOLERANCE - handler.getTransfurProgress()) / grabbedEntity.getEffect(MobEffectRegistry.GRABBED_DEBUFF).getDuration(), TransfurManager.getTransfurType(holder), TransfurContext.ADD_PROGRESS_DEF);
        }
    }

    @Override
    public GrabData getAbilityData(@NotNull LivingEntity holder) {
        return GrabData.dataOf(holder);
    }

    private void clientGrabLogic(Player player, Minecraft minecraft){
        if(TransfurManager.isHoldingEntity(player)){
            if(player.isCrouching()){
                PacketDistributor.sendToServer(new ServerboundDeactivateAbilityPacket());
                return;
            }
            return;
        }

        Entity crosshairEntity = minecraft.crosshairPickEntity;

        if(crosshairEntity == null || player.distanceToSqr(crosshairEntity) > CLOSE_ENOUGH) return;

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

        if(player.hasEffect(MobEffectRegistry.GRAB_COOLDOWN)){
            player.displayClientMessage(Component.translatable("message.a_changed.grab_cooldown",
                    String.valueOf((float) player.getEffect(MobEffectRegistry.GRAB_COOLDOWN).getDuration() / 20)), true);
            return;
        }

        PacketDistributor.sendToServer(new ServerboundActivateAbilityPacket(true,
                new FriendlyByteBuf(Unpooled.buffer(4)).writeVarInt(entity.getId())));
    }

    private static final float TELEPORT_THRESHOLD = 2.5f * 2.5f;

    private void hold(LivingEntity holder, LivingEntity grabbedEntity, GrabMode mode){
        float distance = 1.2f;
        if(mode == GrabMode.ASSIMILATE){
            distance -= grabbedEntity.hasEffect(MobEffectRegistry.GRABBED_DEBUFF)
                    ? 1 - (float) grabbedEntity.getEffect(MobEffectRegistry.GRABBED_DEBUFF).getDuration() / GrabData.grabDuration
                    : 1;
        }

        float yaw = holder.getYHeadRot();
        Vector3f pos = new Vector3f(-Mth.sin(Mth.DEG_TO_RAD * yaw), -Mth.sin(Mth.DEG_TO_RAD * holder.getXRot()), Mth.cos(Mth.DEG_TO_RAD * yaw))
                .mul(distance).normalize(distance)
                .add((float) holder.getX(), (float) (holder.getY() + .5), (float) holder.getZ())
                .sub((float) grabbedEntity.getX(), (float) grabbedEntity.getY(), (float) grabbedEntity.getZ());

        grabbedEntity.fallDistance = 0;
        if(pos.lengthSquared() >= TELEPORT_THRESHOLD){
            grabbedEntity.teleportRelative(pos.x, pos.y, pos.z);
        } else grabbedEntity.setDeltaMovement(pos.x, pos.y, pos.z);

        if(grabbedEntity instanceof ServerPlayer player) {
            Utils.sendToClient(player, new ClientboundSetEntityMotionPacket(player.getId(), player.getDeltaMovement()));
            TransfurUtils.smoothLookAt(player, EntityAnchorArgument.Anchor.EYES, holder.getEyePosition(), false, .6f);
        } else grabbedEntity.lookAt(EntityAnchorArgument.Anchor.EYES, holder.getEyePosition());
    }
}