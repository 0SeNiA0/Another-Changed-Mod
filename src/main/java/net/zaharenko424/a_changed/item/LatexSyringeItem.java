package net.zaharenko424.a_changed.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class LatexSyringeItem extends AbstractSyringe {

    public LatexSyringeItem() {
        this(new Properties().rarity(Rarity.RARE));
    }

    protected LatexSyringeItem(Properties properties){
        super(properties.stacksTo(1));
    }

    @Override
    public int getContentsColor(ItemStack stack) {
        TransfurType transfurType = decodeTransfur(stack);
        return transfurType != null ? transfurType.getPrimaryColor() : Color.WHITE.getRGB();
    }

    @Override
    public int getSecondaryColor(ItemStack stack) {
        TransfurType transfurType = decodeTransfur(stack);
        return transfurType != null ? transfurType.getSecondaryColor() : 0;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if(TransfurManager.isTransfurred(player)
                || !(stack.getItem() instanceof LatexSyringeItem)
                || decodeTransfur(stack) == null) return InteractionResultHolder.pass(stack);
        return super.use(level, player, usedHand);
    }

    @Override
    protected ItemStack applyUseEffects(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if(!level.isClientSide) {
            entity.hurt(DamageSources.syringe(level, entity), .5f);
            transfur(stack, entity);
        }
        return ItemRegistry.SYRINGE_ITEM.toStack();
    }

    protected void transfur(ItemStack stack, LivingEntity entity){
        TransfurType transfurType = decodeTransfur(stack);
        if(transfurType != null) TransfurHandler.nonNullOf(entity).transfur(transfurType, TransfurContext.DEF);
    }

    @Override
    public ItemStack applyEffectsAsProjectile(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, @NotNull SyringeProjectile syringe, @Nullable Entity shooter) {
        if(level.isClientSide) return ItemRegistry.SYRINGE_ITEM.toStack();
        entity.hurt(DamageSources.syringe(level, syringe, shooter), .5f);

        if(DamageSources.checkTFTarget(entity)) {
            TransfurType transfurType = decodeTransfur(stack);
            if(transfurType != null) TransfurHandler.nonNullOf(entity).addTransfurProgress(TransfurManager.TRANSFUR_TOLERANCE / 2, transfurType, TransfurContext.DEF);
        }

        return ItemRegistry.SYRINGE_ITEM.toStack();
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

    public static @Nullable ResourceLocation decodeTransfurId(@NotNull ItemStack latexSyringe){
        if(!(latexSyringe.getItem() instanceof LatexSyringeItem)) throw new IllegalArgumentException("latexSyringe must be an instance of LatexSyringeItem");
        if(!latexSyringe.has(ComponentRegistry.TRANSFUR_TYPE)) return null;
        return latexSyringe.get(ComponentRegistry.TRANSFUR_TYPE);
    }

    public static @Nullable TransfurType decodeTransfur(@NotNull ItemStack latexSyringe){
        ResourceLocation tfId = decodeTransfurId(latexSyringe);
        if(tfId == null) return null;
        return TransfurManager.getTransfurType(tfId);
    }
}