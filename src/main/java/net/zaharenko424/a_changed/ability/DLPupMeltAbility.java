package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.DLPupMeltData;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundActivateAbilityPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundDeactivateAbilityPacket;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;

public class DLPupMeltAbility implements Ability {

    private static final ResourceLocation tex = AChanged.textureLoc("mob_effect/latex_solvent");

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        graphics.blit(tex, x, y, 32, 32, 0, 0, 64, 64, 64, 64);
        DLPupMeltData data = getAbilityData(player);
        if(data.isMolten()){
            graphics.blit(HypnosisAbility.activated, x - 16, y - 16, 0, 0, 0, 64, 64, 64, 64);
        }
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
    public void handleData(@NotNull LivingEntity holder, @NotNull FriendlyByteBuf buf, @NotNull IPayloadContext context) {
        if(!holder.level().isClientSide) return;

        getAbilityData(holder).fromPacket(buf);
        holder.refreshDimensions();

        if(!(holder instanceof AbstractClientPlayer player)) return;
        TransfurHandler handler = TransfurHandler.nonNullOf(player);
        handler.setLastTFModelId(TransfurUtils.updateTFModel(player, handler.getLastTFModelId(), handler.getTransfurType()));
    }

    @Override
    public void activate(@NotNull LivingEntity holder, boolean oneShot, @NotNull FriendlyByteBuf additionalData) {
        getAbilityData(holder).setMolten(true);
        holder.refreshDimensions();
    }

    @Override
    public void deactivate(@NotNull LivingEntity holder) {
        getAbilityData(holder).setMolten(false);
        holder.refreshDimensions();
    }

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {
        DLPupMeltData meltData = getAbilityData(localPlayer);

        if(Keybindings.ABILITY_KEY.isDown()) {
            if(meltData.isMolten()) return;
            meltData.setMolten(true);
            PacketDistributor.sendToServer(new ServerboundActivateAbilityPacket(false, null));
        } else {
            if(!meltData.isMolten()) return;
            meltData.setMolten(false);
            PacketDistributor.sendToServer(new ServerboundDeactivateAbilityPacket());
        }
    }

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        if(holder.level().getGameTime() % 10 != 0) return;//run every 10th tick
        DLPupMeltData data = getAbilityData(holder);
        if(!data.isMolten()) return;
        holder.forceAddEffect(new MobEffectInstance(MobEffectRegistry.INVISIBLE_SLOWDOWN, 15, 3, false, false, false), null);

        holder.level().getEntitiesOfClass(LivingEntity.class, holder.getBoundingBox(), entity ->
                entity != holder && (!TransfurManager.hasAbility(this, entity) || !getAbilityData(entity).isMolten())
        ).forEach(entity ->
                entity.forceAddEffect(new MobEffectInstance(MobEffectRegistry.INVISIBLE_SLOWDOWN, 15, 3, false, false, false), holder));
    }

    @Override
    public DLPupMeltData getAbilityData(@NotNull LivingEntity holder) {
        return DLPupMeltData.dataOf(holder);
    }
}
