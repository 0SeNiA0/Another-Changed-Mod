package net.zaharenko424.a_changed.compat.encoder;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.library.transfer.BasicRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.menu.machines.LatexEncoderMenu;
import net.zaharenko424.a_changed.network.packets.ServerboundLatexEncoderScreenPacket;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexEncoderTransferHandler extends BasicRecipeTransferHandler<LatexEncoderMenu, LatexEncoderRecipe> {

    public LatexEncoderTransferHandler(IConnectionToServer serverConnection, IStackHelper stackHelper, IRecipeTransferHandlerHelper handlerHelper, IRecipeTransferInfo<LatexEncoderMenu, LatexEncoderRecipe> transferInfo) {
        super(serverConnection, stackHelper, handlerHelper, transferInfo);
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(@NotNull LatexEncoderMenu container, @NotNull LatexEncoderRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull Player player, boolean maxTransfer, boolean doTransfer) {
        IRecipeTransferError error = super.transferRecipe(container, recipe, recipeSlotsView, player, maxTransfer, doTransfer);
        if(error != null || !doTransfer) return error;

        PacketDistributor.sendToServer(new ServerboundLatexEncoderScreenPacket(container.getEntity().getBlockPos(),
                0, recipe.getGender().ordinal()));

        return null;
    }
}