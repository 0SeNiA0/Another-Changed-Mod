package net.zaharenko424.a_changed.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.block.machines.Wrenchable;
import org.jetbrains.annotations.NotNull;

public class Wrench extends TieredItem implements Vanishable {

    public Wrench(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(level.isClientSide || !(level.getBlockState(pos).getBlock() instanceof Wrenchable wrenchable)) return super.useOn(context);
        InteractionResult result = wrenchable.useWrenchOn(level.getBlockState(pos), pos, (ServerLevel) level, context);
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        if(result.consumesAction() && (player == null || !player.isCreative())) context.getItemInHand().hurt(2, level.random, player);
        return result;
    }
}