package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class LogBlock extends RotatedPillarBlock {

    private final Supplier<? extends Block> strippedBlock;

    public LogBlock(Properties pProperties, Supplier<? extends Block> strippedBlock) {
        super(pProperties);
        this.strippedBlock = strippedBlock;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(@NotNull BlockState state, @NotNull UseOnContext context, @NotNull ToolAction toolAction, boolean simulate) {
        if(toolAction == ToolActions.AXE_STRIP){
            return strippedBlock.get().defaultBlockState();
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}