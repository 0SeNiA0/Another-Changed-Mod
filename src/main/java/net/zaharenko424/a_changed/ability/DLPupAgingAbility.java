package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.attachments.DLPupAgingData;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.TransfurUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DLPupAgingAbility implements PassiveAbility {

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        DLPupAgingData data = getAbilityData(player);
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
        DLPupAgingData data = getAbilityData(holder);
        data.fromPacket(buf);
    }

    @Override
    public void inputTick(@NotNull Player localPlayer, @NotNull Minecraft minecraft) {}

    @Override
    public void serverTick(@NotNull LivingEntity holder) {
        DLPupAgingData data = getAbilityData(holder);
        if(data.isAgingFrozen()) return;
        boolean wasBaby = data.isBaby();
        data.tickAge();

        if(data.isAboutToTurn()){
            TransfurType type = randomType(holder.getRandom());
            if(holder instanceof Player player){
                TransfurHandler.nonNullOf(player).transfur(type, TransfurContext.TRANSFUR_TF);
            } else {
                TransfurUtils.spawnLatex(type, (ServerLevel) holder.level(), holder.blockPosition());
                holder.discard();
            }
            return;
        }

        if(wasBaby != data.isBaby() || holder.tickCount % 20 == 0) data.syncClients();//only sync every second
    }

    private @NotNull TransfurType randomType(@NotNull RandomSource random){
        return (random.nextBoolean() ? TransfurRegistry.DARK_LATEX_WOLF_F_TF : TransfurRegistry.DARK_LATEX_WOLF_M_TF).get();
    }

    @Override
    public DLPupAgingData getAbilityData(@NotNull LivingEntity holder) {
        return holder.getData(AttachmentRegistry.DL_PUP_AGING_DATA);
    }
}
