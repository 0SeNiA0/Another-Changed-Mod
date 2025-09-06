package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.ability.api.ActivationType;
import net.zaharenko424.a_changed.ability.network.packets.BidirectionalAbilityPacket;
import net.zaharenko424.a_changed.attachment.GrabData;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.client.screen.ability.GrabAbilityLatexScreen;
import net.zaharenko424.a_changed.client.screen.ability.GrabAbilityPlayerScreen;
import net.zaharenko424.a_changed.client.screen.ability.GrabEscapeScreen;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.util.ClipUtil;
import net.zaharenko424.a_changed.util.TransfurUtils;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class GrabAbility implements Ability {

    public static final float CLOSE_ENOUGH = 2.5f;
    public static final float CLOSE_ENOUGH_SQR = CLOSE_ENOUGH * CLOSE_ENOUGH;

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        GrabData holderData = getAbilityData(player);
        if(TransfurManager.isTransfurred(player)) {
            if(holderData.isActivated())
                graphics.blit(HypnosisAbility.activated, x - 16, y - 16, 0, 0, 0, 64, 64, 64, 64);
            graphics.blit(holderData.getMode().texture,
                    x, y, 32, 32, 0, 0, 64, 64, 64, 64);
        } else {
            graphics.blit(holderData.wantsToBeGrabbed() ? GrabAbilityPlayerScreen.yes : GrabAbilityPlayerScreen.nope,
                    x, y, 32, 32, 0, 0, 64, 64, 64, 64);
        }
    }

    @Override
    public boolean hasScreen() {
        return true;
    }

    @Override
    public Screen getScreen(@NotNull Player holder) {
        if(!FMLLoader.getDist().isClient()) return null;

        TransfurHandler handler = TransfurHandler.nonNullOf(holder);
        if(handler.isTransfurred() && !handler.getTransfurType().isOrganic()) return Utils.get(GrabAbilityLatexScreen::new);

        return Utils.get(GrabAbilityPlayerScreen::new);//organic latexes use same thing as humans
    }

    @Override
    public boolean canUse(@NotNull LivingEntity holder) {
        TransfurType<?> tf = TransfurManager.getTransfurType(holder);
        return tf != null ? tf.abilities.contains(this) : GrabData.dataOf(holder).getGrabbedBy() != null;
    }

    //make special activationType to account for grab escape (if grabbed act as INSTANT, otherwise SWITCH_CROUCH)
    private static final ActivationType TYPE = ((holder, controller, ability) -> {
        GrabData data = ((GrabAbility)ability).getAbilityData(holder.asEntity());
        if(data.getGrabbedBy() != null) {
            ActivationType.INSTANT.onInput(holder, controller, ability);
        } else ActivationType.SWITCH_CROUCH.onInput(holder, controller, ability);
    });

    @Override
    public ActivationType activationType() {
        return TYPE;
    }

    @Override
    public boolean isActivated(LivingEntity holder) {
        return getAbilityData(holder).isActivated();
    }

    @Override
    public void activate(LivingEntity holder) {
        GrabData data = getAbilityData(holder);

        LivingEntity grabbedBy = data.getGrabbedBy();
        if(grabbedBy != null){
            if(getAbilityData(grabbedBy).getMode().isOffensive()) {
                if(holder instanceof ServerPlayer player) PacketDistributor.sendToPlayer(player, new BidirectionalAbilityPacket(AbilityRegistry.GRAB_ABILITY, buf -> {}, 0));
                return;
            }

            data.escape(false);
            return;
        }

        EntityHitResult entityHit = ClipUtil.getLookingAt(holder, CLOSE_ENOUGH, true, e -> e instanceof LivingEntity && !e.isSpectator());
        if(entityHit == null) return;

        tryGrab(holder, data, (LivingEntity) entityHit.getEntity());
    }

    public void activate(@NotNull LivingEntity holder, @NotNull LivingEntity target){
        GrabData data = getAbilityData(holder);
        if(data.getGrabbedBy() != null) return;

        tryGrab(holder, data, target);
    }

    @Override
    public void deactivate(@NotNull LivingEntity holder) {
        getAbilityData(holder).drop();
    }

    @Override
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull IPayloadContext context) {
        if(holder.level().isClientSide) {
            Minecraft.getInstance().setScreen(Utils.get(GrabEscapeScreen::new));
            return;
        }
        GrabData holderData = getAbilityData(holder);

        switch(buf.readByte()){
            case 0 -> holderData.setMode(buf.readEnum(GrabMode.class));
            case 1 -> holderData.setWantsToBeGrabbed(buf.readBoolean());
            case 2 -> {
                holderData.escape(buf.readBoolean());
                holderData.sync();
            }
        }
    }

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        GrabData holderData = GrabData.dataOf(holder);

        if(holderData.getGrabbedBy() != null){
            if(holder instanceof Player player) player.displayClientMessage(Component.translatable("message.a_changed.try_escape_tip", Component.keybind("key." + AChanged.MODID + ".ability_key")), true);
            return;
        }
        if(!TransfurManager.isTransfurred(holder)) return;

        LivingEntity grabbedEntity = holderData.getGrabbedEntity();
        GrabMode mode = holderData.getMode();

        if(grabbedEntity == null) {
            holderData.tickCooldown();
            return;
        }

        if(!grabbedEntity.isAlive()) {
            if(holder instanceof Player player) {
                player.displayClientMessage(Component.translatable(switch (grabbedEntity.getRemovalReason()){
                    case UNLOADED_WITH_PLAYER -> "message.a_changed.grabbed_player_left";
                    case CHANGED_DIMENSION -> "message.a_changed.grabbed_entity_changed_dim";
                    case null, default -> "message.a_changed.grabbed_entity_died";
                }, nameOrFallback(grabbedEntity, "Grabbed entity")), true);
            }
            holderData.drop();
            return;
        }

        if(mode == GrabMode.FRIENDLY){
            ((ServerPlayer)grabbedEntity).setCamera(holder);
            return;
        }

        if(TransfurManager.isBeingTransfurred(grabbedEntity)){
            holderData.drop();
            return;
        }

        hold(holder, grabbedEntity, mode);

        if(!mode.isOffensive() || TransfurManager.isTransfurred(grabbedEntity)) return;

        if(!grabbedEntity.hasEffect(MobEffectRegistry.GRABBED_DEBUFF)) {
            if(mode == GrabMode.ASSIMILATE) {
                grabbedEntity.hurt(DamageSources.assimilation(holder), Float.MAX_VALUE);
                holder.addEffect(new MobEffectInstance(MobEffectRegistry.ASSIMILATION_BUFF, 6000, 0, false, false));
                if(holder instanceof Player player) player.getFoodData().eat(6, 1);
            } else if(mode == GrabMode.REPLICATE) {
                TransfurHandler handler = TransfurHandler.of(grabbedEntity);
                if(handler != null) handler.transfur(TransfurManager.getTransfurType(holder), TransfurContext.TRANSFUR);
            }
            holderData.drop();
        } else if(mode == GrabMode.REPLICATE){
            TransfurHandler handler = TransfurHandler.of(grabbedEntity);
            handler.addTransfurProgress((TransfurManager.TRANSFUR_TOLERANCE - handler.getTransfurProgress()) / grabbedEntity.getEffect(MobEffectRegistry.GRABBED_DEBUFF).getDuration(), TransfurManager.getTransfurType(holder), TransfurContext.DEF);
            if(handler.isTransfurred() || handler.isBeingTransfurred() || grabbedEntity.isDeadOrDying()) holderData.drop();
        }
    }

    @Override
    public GrabData getAbilityData(@NotNull LivingEntity holder) {
        return GrabData.dataOf(holder);
    }

    private void tryGrab(LivingEntity holder, GrabData holderData, LivingEntity target){
        if(holder.distanceToSqr(target) > CLOSE_ENOUGH_SQR || holderData.getGrabbedEntity() != null
                || TransfurManager.isBeingTransfurred(target)) return;

        if(holderData.getGrabbedEntity() != null) return;

        GrabData targetData = getAbilityData(target);
        if(targetData.getGrabbedBy() != null){
            maybeDisplayMessage(holder, Component.translatable("message.a_changed.target_held_already",
                    nameOrFallback(targetData.getGrabbedBy(), "Something"), nameOrFallback(target, "the target entity")));
            return;
        }

        int cooldown = holderData.getGrabCooldown();
        if(cooldown > 0){
            maybeDisplayMessage(holder, Component.translatable("message.a_changed.grab_cooldown",
                    String.valueOf(cooldown / 20f)));
            return;
        }

        GrabMode mode = holderData.getMode();
        if(!mode.checkTarget(target)) {
            maybeDisplayMessage(holder, Component.translatable("message.a_changed.cannot_grab_with_selected_mode",
                    nameOrFallback(target, "this entity")));
            return;
        }

        if(!mode.isOffensive() && target instanceof Player pl && !targetData.wantsToBeGrabbed()){
            maybeDisplayMessage(holder, Component.translatable("message.a_changed.player_doesnt_want_to_be_grabbed",
                    pl.getDisplayName()));
            return;
        }

        holderData.grab(target);
    }

    Object nameOrFallback(LivingEntity entity, String fallback){
        if(entity instanceof Player || entity.getCustomName() != null) return entity.getDisplayName();
        return fallback;
    }

    void maybeDisplayMessage(LivingEntity holder, Component component){
        if(holder instanceof Player player) player.displayClientMessage(component, true);
    }

    private void hold(LivingEntity holder, LivingEntity grabbedEntity, GrabMode mode){//TODO improve with something similar to vanilla riding
        float distance = 1.2f;
        if(mode == GrabMode.ASSIMILATE){
            distance -= grabbedEntity.hasEffect(MobEffectRegistry.GRABBED_DEBUFF)
                    ? 1 - (float) grabbedEntity.getEffect(MobEffectRegistry.GRABBED_DEBUFF).getDuration() / GrabData.grabDuration
                    : 1;
        }

        float yaw = holder.getYHeadRot();
        Vector3f pos = new Vector3f(-Mth.sin(Mth.DEG_TO_RAD * yaw), -Mth.sin(Mth.DEG_TO_RAD * holder.getXRot()), Mth.cos(Mth.DEG_TO_RAD * yaw))
                .mul(distance).normalize(distance)
                .add((float) holder.getX(), (float) (holder.getY() + holder.getEyeHeight()), (float) holder.getZ())
                .sub((float) grabbedEntity.getX(), (float) grabbedEntity.getY() + grabbedEntity.getBbHeight() / 1.6f, (float) grabbedEntity.getZ());

        grabbedEntity.fallDistance = 0;
        if(pos.lengthSquared() >= CLOSE_ENOUGH_SQR){
            grabbedEntity.teleportRelative(pos.x, pos.y, pos.z);
        } else grabbedEntity.setDeltaMovement(pos.x, pos.y, pos.z);

        if(grabbedEntity instanceof ServerPlayer player) {
            Utils.sendVanillaToClient(player, new ClientboundSetEntityMotionPacket(player.getId(), player.getDeltaMovement()));
            TransfurUtils.smoothLookAt(player, EntityAnchorArgument.Anchor.EYES, holder.getEyePosition(), false, .6f);
        } else grabbedEntity.lookAt(EntityAnchorArgument.Anchor.EYES, holder.getEyePosition());
    }
}