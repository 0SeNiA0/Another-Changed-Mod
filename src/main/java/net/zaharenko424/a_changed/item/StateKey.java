package net.zaharenko424.a_changed.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.block.blocks.ConnectedTextureBlockImpl;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StateKey extends Item {

    public StateKey(Properties pProperties) {
        super(pProperties.durability(256));
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        if(player == null) return InteractionResult.FAIL;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if(!(state.getBlock() instanceof ConnectedTextureBlockImpl)) return InteractionResult.FAIL;

        if(!level.isClientSide){
            level.setBlockAndUpdate(pos, state.setValue(StateProperties.LOCKED_STATE, !state.getValue(StateProperties.LOCKED_STATE)));
            if(!player.isCreative())
                context.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.a_changed.state_key"));
    }
}