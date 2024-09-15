package net.zaharenko424.a_changed.item;

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
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class LatexSyringeItem extends AbstractSyringe {

    public LatexSyringeItem() {
        this(new Properties().rarity(Rarity.RARE));
    }

    protected LatexSyringeItem(Properties properties){
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(TransfurManager.isTransfurred(pPlayer) || !pPlayer.getItemInHand(pUsedHand).has(ComponentRegistry.TRANSFUR_TYPE)) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
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
    public void appendHoverText(@NotNull ItemStack syringe, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(syringe, context, tooltipComponents, tooltipFlag);
        if(syringe.has(ComponentRegistry.TRANSFUR_TYPE)){
            tooltipComponents.add(TransfurManager.getTransfurType(syringe.get(ComponentRegistry.TRANSFUR_TYPE)).fancyName());
        } else tooltipComponents.add(Component.literal("Invalid transfur type!"));
    }

    public static @NotNull ItemStack encodeTransfur(@NotNull TransfurType transfurType){
        ItemStack syringe = ItemRegistry.LATEX_SYRINGE.toStack();
        syringe.set(ComponentRegistry.TRANSFUR_TYPE, transfurType.id);
        return syringe;
    }

    public static @Nullable ResourceLocation decodeTransfur(@NotNull ItemStack latexSyringe){
        if(!(latexSyringe.getItem() instanceof LatexSyringeItem)) throw new IllegalArgumentException("latexSyringe must be an instance of LatexSyringeItem");
        if(!latexSyringe.has(ComponentRegistry.TRANSFUR_TYPE)) return null;
        return latexSyringe.get(ComponentRegistry.TRANSFUR_TYPE);
    }
}