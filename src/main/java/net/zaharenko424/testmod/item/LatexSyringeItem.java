package net.zaharenko424.testmod.item;

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
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.TransfurManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.zaharenko424.testmod.TransfurManager.TRANSFUR_TYPE_KEY;

public class LatexSyringeItem extends AbstractSyringe{
    public LatexSyringeItem(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel , @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(TransfurManager.isTransfurred(pPlayer)||!TransfurManager.hasModTag(pPlayer.getItemInHand(pUsedHand).getTag())) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        Player player= (Player) pLivingEntity;
        if(pLevel.isClientSide) return ItemStack.EMPTY;
        TransfurManager.transfur(pLivingEntity,new ResourceLocation(TransfurManager.modTag(Objects.requireNonNull(pStack.getTag())).getString(TRANSFUR_TYPE_KEY)));
        ItemStack syringe=new ItemStack(TestMod.SYRINGE_ITEM.get());
        return onUse(pStack,syringe,player);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_41421_, @Nullable Level p_41422_, @NotNull List<Component> p_41423_, @NotNull TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        CompoundTag tag=p_41421_.getTag();
        if(TransfurManager.hasModTag(tag)){
            p_41423_.add(TransfurManager.getTransfurType(new ResourceLocation(TransfurManager.modTag(tag).getString(TRANSFUR_TYPE_KEY))).fancyName());
        }
    }
}