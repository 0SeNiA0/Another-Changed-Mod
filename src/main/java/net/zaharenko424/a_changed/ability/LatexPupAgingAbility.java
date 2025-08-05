package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.attachment.LatexPupAgingData;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.network.packets.ability.ClientboundRemoveAttachmentPacket;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.function.Function;

public class LatexPupAgingAbility implements PassiveAbility {

    protected final Function<LivingEntity, TransfurType<?>> turnInto;

    public LatexPupAgingAbility(Function<LivingEntity, TransfurType<?>> turnInto){
        this.turnInto = turnInto;
    }

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        LatexPupAgingData data = getAbilityData(player);
        graphics.drawCenteredString(Minecraft.getInstance().font, "Age: " + data.getAge(), x + 16, y + 6, Color.WHITE.getRGB());
        graphics.drawCenteredString(Minecraft.getInstance().font, "Frozen: " + data.isAgingFrozen(), x + 16, y + 16, Color.WHITE.getRGB());
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
        LatexPupAgingData data = getAbilityData(holder);
        data.fromPacket(buf);
    }

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {}

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        LatexPupAgingData data = getAbilityData(holder);
        if(data.isAgingFrozen()) return;
        boolean wasBaby = data.isBaby();
        data.tickAge();

        if(data.isAboutToTurn()){
            TransfurType<?> type = turnInto.apply(holder);
            if(holder instanceof Player player){
                TransfurHandler.nonNullOf(player).transfur(type, TransfurContext.TRANSFUR);
            } else {
                TransfurUtils.spawnLatex(type, (ServerLevel) holder.level(), holder.blockPosition());
                holder.discard();
            }
            return;
        }

        if(wasBaby != data.isBaby() || holder.tickCount % 20 == 0) data.syncClients();//only sync every second
    }

    @Override
    public void remove(@NotNull LivingEntity holder) {
        holder.removeData(AttachmentRegistry.LATEX_PUP_AGING_DATA);
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, new ClientboundRemoveAttachmentPacket(holder.getId(), AttachmentRegistry.LATEX_PUP_AGING_DATA.getId()));
    }

    @Override
    public LatexPupAgingData getAbilityData(@NotNull LivingEntity holder) {
        return holder.getData(AttachmentRegistry.LATEX_PUP_AGING_DATA);
    }
}
