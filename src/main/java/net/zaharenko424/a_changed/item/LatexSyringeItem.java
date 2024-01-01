package net.zaharenko424.a_changed.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TYPE_KEY;

public class LatexSyringeItem extends AbstractSyringe{
    public LatexSyringeItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel , @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(TransfurManager.isTransfurred(pPlayer)||!TransfurManager.hasModTag(pPlayer.getItemInHand(pUsedHand).getTag())) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        Player player= (Player) pLivingEntity;
        if(!pLevel.isClientSide) TransfurManager.transfur(pLivingEntity,TransfurManager.getTransfurType(new ResourceLocation(TransfurManager.modTag(Objects.requireNonNull(pStack.getTag())).getString(TRANSFUR_TYPE_KEY))));
        return onUse(pStack,new ItemStack(ItemRegistry.SYRINGE_ITEM.get()),player);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_41421_, @Nullable Level p_41422_, @NotNull List<Component> p_41423_, @NotNull TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        CompoundTag tag=p_41421_.getTag();
        if(TransfurManager.hasModTag(tag)){
            ResourceLocation transfurType=new ResourceLocation(TransfurManager.modTag(tag).getString(TRANSFUR_TYPE_KEY));
            try {
                p_41423_.add(TransfurManager.getTransfurType(transfurType).fancyName());
            } catch (Exception ex) {
                p_41423_.add(Component.literal("Invalid transfur type "+transfurType));
            }
        }
    }

    public static @NotNull ItemStack encodeTransfur(@NotNull ResourceLocation transfurType){
        ItemStack syringe=new ItemStack(ItemRegistry.LATEX_SYRINGE_ITEM.asItem());
        CompoundTag tag=syringe.hasTag()?syringe.getTag():new CompoundTag();
        TransfurManager.modTag(tag).putString(TRANSFUR_TYPE_KEY,transfurType.toString());
        syringe.setTag(tag);
        return syringe;
    }
}