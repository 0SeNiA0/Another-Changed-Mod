package net.zaharenko424.a_changed.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.FluidRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.CoveredWith;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LatexItem extends Item {

    private final Supplier<? extends TransfurType> transfurType;
    private final Latex type;

    public LatexItem(@NotNull Supplier<? extends TransfurType> transfurType, Latex type) {
        super(new Properties().food(new FoodProperties.Builder().fast().nutrition(1).saturationModifier(1).build()));
        this.transfurType = transfurType;
        this.type = type;
    }

    public Latex getLatexType(){
        return type;
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        return context.getPlayer() != null && context.getPlayer().isCrouching() ? InteractionResult.SUCCESS : super.onItemUseFirst(stack, context);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos);

        if(LatexCoveredData.isLatex(state) || LatexCoveredData.isStateNotCoverable(state)) return InteractionResult.PASS;

        LatexCoveredData data = LatexCoveredData.of(level.getChunkAt(pos));
        if(data.getCoveredWith(pos) != CoveredWith.NOTHING) return InteractionResult.PASS;

        if (type == Latex.DARK) {
            data.coverWith(pos, CoveredWith.DARK_LATEX);
        } else {
            data.coverWith(pos, CoveredWith.WHITE_LATEX);
        }

        Player player = context.getPlayer();
        ItemStack item = context.getItemInHand();

        if(player == null || !player.isCreative()) item.shrink(1);
        return InteractionResult.SUCCESS;
    }

    private static TransfurContext ADD_TF_NO_CHECK;

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_41409_, @NotNull Level p_41410_, @NotNull LivingEntity p_41411_) {
        Player player= (Player) p_41411_;
        if(!p_41410_.isClientSide){
            if(TransfurManager.isTransfurred(player)) return super.finishUsingItem(p_41409_,p_41410_,p_41411_);
            if(ADD_TF_NO_CHECK == null) ADD_TF_NO_CHECK = TransfurContext.ADD_PROGRESS_DEF.withCheckResistance(false);
            TransfurHandler.nonNullOf(player).addTransfurProgress(10f, transfurType.get(), ADD_TF_NO_CHECK);
        }
        if(!player.isCreative()) p_41409_.shrink(1);
        return p_41409_;
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        if(!entity.isInFluidType(FluidRegistry.LATEX_SOLVENT_TYPE.get())) return super.onEntityItemUpdate(stack, entity);
        entity.discard();
        return true;
    }
}