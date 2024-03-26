package net.zaharenko424.a_changed.block.smalldecor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.SmallDecorBlock;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

public class MetalCan extends SmallDecorBlock {

    public static final MapCodec<MetalCan> CODEC = simpleCodec(MetalCan::new);
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    protected static final VoxelShape SHAPE, SHAPE_OPEN;
    protected static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public MetalCan(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(OPEN, false));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        boolean open = pState.getValue(OPEN);
        return CACHE.getShape(pState.getValue(FACING), open ? 1 : 0, ()-> open ? SHAPE_OPEN : SHAPE);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult pHit) {
        if(level.isClientSide || hand != InteractionHand.MAIN_HAND || player.isCrouching())
            return super.use(state, level, pos, player, hand, pHit);
        openCloseCan(state, pos, level);
        return InteractionResult.SUCCESS;
    }

    protected void openCloseCan(@NotNull BlockState state, BlockPos pos, @NotNull Level level){
        level.setBlockAndUpdate(pos, state.setValue(OPEN, !state.getValue(OPEN)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(OPEN));
    }

    static {
        SHAPE = Shapes.or(
                Shapes.box(0.4062f, 0, 0.4062f, 0.5938f, 0.375f, 0.5938f),
                Shapes.box(0.5938f, 0, 0.4375f, 0.6562f, 0.375f, 0.5625f),
                Shapes.box(0.3438f, 0, 0.4375f, 0.4062f, 0.375f, 0.5625f),
                Shapes.box(0.4375f, 0, 0.5938f, 0.5625f, 0.375f, 0.6562f),
                Shapes.box(0.4062f, 0.375f, 0.4062f, 0.5938f, 0.4062f, 0.5938f),
                Shapes.box(0.3438f, 0.375f, 0.4375f, 0.4062f, 0.4062f, 0.5625f),
                Shapes.box(0.4375f, 0.375f, 0.5938f, 0.5625f, 0.4062f, 0.6562f),
                Shapes.box(0.5938f, 0.375f, 0.4375f, 0.6562f, 0.4062f, 0.5625f),
                Shapes.box(0.4375f, 0, 0.3438f, 0.5625f, 0.375f, 0.4062f),
                Shapes.box(0.4375f, 0.375f, 0.3438f, 0.5625f, 0.4062f, 0.4062f));
        SHAPE_OPEN = Shapes.or(
                Shapes.box(0.4062f, 0, 0.4062f, 0.5938f, 0.375f, 0.5938f),
                Shapes.box(0.5938f, 0, 0.4375f, 0.6562f, 0.375f, 0.5625f),
                Shapes.box(0.3438f, 0, 0.4375f, 0.4062f, 0.375f, 0.5625f),
                Shapes.box(0.4375f, 0, 0.5938f, 0.5625f, 0.375f, 0.6562f),
                Shapes.box(0.1063f, 0.3625f, 0.4062f, 0.2937f, 0.3938f, 0.5938f),
                Shapes.box(0.0437f, 0.3625f, 0.4375f, 0.1063f, 0.3938f, 0.5625f),
                Shapes.box(0.1375f, 0.3625f, 0.5938f, 0.2625f, 0.3938f, 0.6562f),
                Shapes.box(0.2937f, 0.3625f, 0.4375f, 0.3562f, 0.3938f, 0.5625f),
                Shapes.box(0.4375f, 0, 0.3438f, 0.5625f, 0.375f, 0.4062f),
                Shapes.box(0.1375f, 0.3625f, 0.3438f, 0.2625f, 0.3938f, 0.4062f));
    }
}