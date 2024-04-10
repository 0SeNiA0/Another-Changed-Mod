package net.zaharenko424.a_changed.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.block.HorizontalDirectionalBlock;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDerelictMachine extends HorizontalDirectionalBlock implements Wrenchable {

    public AbstractDerelictMachine(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull InteractionResult useWrenchOn(BlockState state, BlockPos pos, @NotNull ServerLevel level, @NotNull UseOnContext context) {
        level.removeBlock(pos, false);

        Player player = context.getPlayer();
        //TODO play some wrenching sound
        if(player != null){
            ItemHandlerHelper.giveItemToPlayer(player, player.isCrouching() ? asItem().getDefaultInstance() : getDrop(level.random));
        } else Block.popResource(level, pos, getDrop(level.random));

        return InteractionResult.SUCCESS;
    }

    abstract ItemStack getDrop(RandomSource random);
}