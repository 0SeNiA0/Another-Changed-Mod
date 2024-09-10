package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LatexManipulator extends Item {

    public LatexManipulator() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand pUsedHand) {
        if(level.isClientSide || pUsedHand != InteractionHand.MAIN_HAND) return super.use(level, player, pUsedHand);
        ItemStack manipulator = player.getItemInHand(pUsedHand);
        DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> transfurType = ComponentRegistry.TRANSFUR_TYPE;

        if(player.isCrouching()){
            if(manipulator.has(transfurType)){
                manipulator.remove(transfurType);                                       //success, remove binded tf
            } else if(TransfurManager.isTransfurred(player)) {
                manipulator.set(transfurType, TransfurManager.getTransfurType(player).id);//success, save tf
            } else return InteractionResultHolder.pass(manipulator);
        } else {
            TransfurHandler handler = TransfurHandler.nonNullOf(player);
            if(TransfurManager.isTransfurred(player)){
                handler.unTransfur(TransfurContext.UNTRANSFUR);             //success unTF
            } else if(manipulator.has(transfurType)) {
                TransfurType transfurType1 = TransfurManager.getTransfurType(manipulator.get(transfurType));
                if(transfurType1 == null) return InteractionResultHolder.pass(manipulator);
                handler.transfur(transfurType1, TransfurContext.TRANSFUR_TF);//success, tf
            } else return InteractionResultHolder.pass(manipulator);
        }

        player.getCooldowns().addCooldown(this, 100);
        return InteractionResultHolder.consume(manipulator);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if(!stack.has(ComponentRegistry.TRANSFUR_TYPE)) return;
        tooltipComponents.add(Component.translatable("tooltip.a_changed.latex_manipulator", Objects.requireNonNull(TransfurManager.getTransfurType(stack.get(ComponentRegistry.TRANSFUR_TYPE))).fancyName()).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}