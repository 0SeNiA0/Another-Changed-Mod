package net.zaharenko424.a_changed.compat.extractor;

import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.network.packets.PacketRecipeTransfer;
import mezz.jei.common.transfer.RecipeTransferOperationsResult;
import mezz.jei.common.transfer.RecipeTransferUtil;
import mezz.jei.library.transfer.BasicRecipeTransferHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.menu.machines.DNAExtractorMenu;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DNAExtractorTransferHandler extends BasicRecipeTransferHandler<DNAExtractorMenu, DNAExtractorRecipe> {

    private final IConnectionToServer serverConnection;
    private final IStackHelper stackHelper;
    private final IRecipeTransferHandlerHelper handlerHelper;
    private final List<IRecipeTransferInfo<DNAExtractorMenu, DNAExtractorRecipe>> transferInfo0;

    public DNAExtractorTransferHandler(IConnectionToServer serverConnection, IStackHelper stackHelper, IRecipeTransferHandlerHelper handlerHelper, IRecipeTransferInfo<DNAExtractorMenu, DNAExtractorRecipe> transferInfo) {
        super(serverConnection, stackHelper, handlerHelper, transferInfo);
        this.serverConnection = serverConnection;
        this.stackHelper = stackHelper;
        this.handlerHelper = handlerHelper;
        transferInfo0 = List.of(transferInfo, info(1), info(2), info(3));
    }

    private @NotNull IRecipeTransferInfo<DNAExtractorMenu, DNAExtractorRecipe> info(int ingredientSlot){
        return handlerHelper.createBasicRecipeTransferInfo(DNAExtractorMenu.class, MenuRegistry.DNA_EXTRACTOR_MENU.get(),
                DNAExtractorRecipeCategory.TYPE, 36 + ingredientSlot, 1, 0, 36);
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(@NotNull DNAExtractorMenu container, @NotNull DNAExtractorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull Player player, boolean maxTransfer, boolean doTransfer) {
        NonNullList<ItemStack> items = container.getItems();
        for(int i = 36; i < 40; i++){
            if(items.get(i).isEmpty())
                return transfer(container, recipe, transferInfo0.get(i - 36), recipeSlotsView, player, maxTransfer, doTransfer);
        }

        return transfer(container, recipe, transferInfo0.get(0), recipeSlotsView, player, maxTransfer, doTransfer);
    }

    private @Nullable IRecipeTransferError transfer(@NotNull DNAExtractorMenu container, @NotNull DNAExtractorRecipe recipe, IRecipeTransferInfo<DNAExtractorMenu, DNAExtractorRecipe> info, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull Player player, boolean maxTransfer, boolean doTransfer){
        if (!serverConnection.isJeiOnServer()) {
            Component tooltipMessage = Component.translatable("jei.tooltip.error.recipe.transfer.no.server");
            return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
        }

        if (!info.canHandle(container, recipe)) {
            IRecipeTransferError handlingError = info.getHandlingError(container, recipe);
            return Objects.requireNonNullElseGet(handlingError, handlerHelper::createInternalError);
        }

        List<Slot> craftingSlots = Collections.unmodifiableList(info.getRecipeSlots(container, recipe));
        List<Slot> inventorySlots = Collections.unmodifiableList(info.getInventorySlots(container, recipe));
        if (!validateTransferInfo(info, container, craftingSlots, inventorySlots)) {
            return handlerHelper.createInternalError();
        }

        List<IRecipeSlotView> inputItemSlotViews = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);
        if (!validateRecipeView(info, container, craftingSlots, inputItemSlotViews)) {
            return handlerHelper.createInternalError();
        }

        InventoryState inventoryState = getInventoryState(craftingSlots, inventorySlots, player, container, info);
        if (inventoryState == null) {
            return handlerHelper.createInternalError();
        }

        // check if we have enough inventory space to shuffle items around to their final locations
        int inputCount = inputItemSlotViews.size();
        if (!inventoryState.hasRoom(inputCount)) {
            Component message = Component.translatable("jei.tooltip.error.recipe.transfer.inventory.full");
            return handlerHelper.createUserErrorWithTooltip(message);
        }

        RecipeTransferOperationsResult transferOperations = RecipeTransferUtil.getRecipeTransferOperations(
                stackHelper,
                inventoryState.availableItemStacks(),
                inputItemSlotViews,
                craftingSlots
        );

        if (!transferOperations.missingItems.isEmpty()) {
            Component message = Component.translatable("jei.tooltip.error.recipe.transfer.missing");
            return handlerHelper.createUserErrorForMissingSlots(message, transferOperations.missingItems);
        }

        {
            if (!RecipeTransferUtil.validateSlots(player, transferOperations.results, craftingSlots, inventorySlots)) {
                return handlerHelper.createInternalError();
            }
        }

        if (doTransfer) {
            boolean requireCompleteSets = info.requireCompleteSets(container, recipe);
            PacketRecipeTransfer packet = PacketRecipeTransfer.fromSlots(
                    transferOperations.results,
                    craftingSlots,
                    inventorySlots,
                    maxTransfer,
                    requireCompleteSets
            );
            serverConnection.sendPacketToServer(packet);
        }

        return null;
    }
}