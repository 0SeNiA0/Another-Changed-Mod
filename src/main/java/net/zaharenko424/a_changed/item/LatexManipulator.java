package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
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
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LatexManipulator extends Item {

    public LatexManipulator() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand pUsedHand) {
        if(level.isClientSide || pUsedHand != InteractionHand.MAIN_HAND) return super.use(level, player, pUsedHand);
        ItemStack manipulator = player.getItemInHand(pUsedHand);
        CompoundTag tag = manipulator.getOrCreateTag();
        if(player.isCrouching()){
            if(NBTUtils.hasModTag(tag)){
                tag.remove(NBTUtils.KEY);
                manipulator.setTag(tag);
                //success, remove binded tf
            } else if(TransfurManager.isTransfurred(player)) {
                NBTUtils.modTag(tag).putString(TransfurManager.TRANSFUR_TYPE_KEY, TransfurManager.getTransfurType(player).id.toString());
                //success, save tf
            } else return InteractionResultHolder.pass(manipulator);
        } else {
            ITransfurHandler handler = TransfurCapability.nonNullOf(player);
            if(TransfurManager.isTransfurred(player)){
                handler.unTransfur(TransfurContext.UNTRANSFUR);
                //success unTF
            } else if(NBTUtils.hasModTag(tag)) {
                TransfurType transfurType = decodeTransfurType(NBTUtils.modTag(tag));
                if(transfurType == null) return InteractionResultHolder.pass(manipulator);
                handler.transfur(transfurType, TransfurContext.TRANSFUR_TF);
                //success, tf
            } else return InteractionResultHolder.pass(manipulator);
        }

        player.getCooldowns().addCooldown(this, 100);
        return InteractionResultHolder.consume(manipulator);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        CompoundTag tag = pStack.getOrCreateTag();
        if(!NBTUtils.hasModTag(tag)) return;
        pTooltipComponents.add(Component.translatable("tooltip.a_changed.latex_manipulator", decodeTransfurType(NBTUtils.modTag(tag)).fancyName()).withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    private TransfurType decodeTransfurType(@NotNull CompoundTag modTag){
        return TransfurManager.getTransfurType(new ResourceLocation(modTag.getString(TransfurManager.TRANSFUR_TYPE_KEY)));
    }
}