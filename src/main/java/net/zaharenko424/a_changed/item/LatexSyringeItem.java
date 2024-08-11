package net.zaharenko424.a_changed.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TYPE_KEY;

public class LatexSyringeItem extends AbstractSyringe{
    public LatexSyringeItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(TransfurManager.isTransfurred(pPlayer) || !NBTUtils.hasModTag(pPlayer.getItemInHand(pUsedHand).getTag())) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        Player player = (Player) pLivingEntity;
        if(!pLevel.isClientSide)
            TransfurHandler.nonNullOf(player).transfur(TransfurManager.getTransfurType(Objects.requireNonNull(decodeTransfur(pStack))), TransfurContext.TRANSFUR_DEF);
        return onUse(pStack, new ItemStack(ItemRegistry.SYRINGE_ITEM.get()), player);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_41421_, @Nullable Level p_41422_, @NotNull List<Component> p_41423_, @NotNull TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        ResourceLocation transfurType = decodeTransfur(p_41421_);
        if(transfurType == null) return;
        try {
            p_41423_.add(TransfurManager.getTransfurType(transfurType).fancyName());
        } catch (Exception ex) {
            p_41423_.add(Component.literal("Invalid transfur type " + transfurType));
        }
    }

    public static @NotNull ItemStack encodeTransfur(@NotNull TransfurType transfurType){
        ItemStack syringe = new ItemStack(ItemRegistry.LATEX_SYRINGE_ITEM.asItem());
        CompoundTag tag = syringe.hasTag() ? syringe.getTag() : new CompoundTag();
        NBTUtils.modTag(tag).putString(TRANSFUR_TYPE_KEY, transfurType.id.toString());
        syringe.setTag(tag);
        return syringe;
    }

    public static @Nullable ResourceLocation decodeTransfur(@NotNull ItemStack latexSyringe){
        if(!(latexSyringe.getItem() instanceof LatexSyringeItem)) throw new IllegalArgumentException("latexSyringe must be an instance of LatexSyringeItem");
        if(!latexSyringe.hasTag()) return null;
        CompoundTag modTag = NBTUtils.modTag(latexSyringe.getTag());
        return modTag.contains(TRANSFUR_TYPE_KEY) ? new ResourceLocation(modTag.getString(TRANSFUR_TYPE_KEY)) : null;
    }
}