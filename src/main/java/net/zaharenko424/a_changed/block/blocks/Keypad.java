package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.block.HorizontalDirectionalBlock;
import net.zaharenko424.a_changed.entity.block.KeypadEntity;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenKeypadPacket;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Keypad extends HorizontalDirectionalBlock implements EntityBlock {

    private static final VoxelShape SHAPE_NORTH = Shapes.box(0.0625, 0.1875, 0.625, 0.9375, 0.8125, 1);
    private static final VoxelShape SHAPE_EAST = Utils.rotateShape(Direction.EAST,SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = Utils.rotateShape(Direction.SOUTH,SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST = Utils.rotateShape(Direction.WEST,SHAPE_NORTH);
    public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");

    public Keypad(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(UNLOCKED,false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new KeypadEntity(p_153215_,p_153216_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch (p_60555_.getValue(FACING)){
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60504_.isClientSide||p_60503_.getValue(UNLOCKED)) return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
        BlockEntity entity=p_60504_.getBlockEntity(p_60505_);
        if(entity instanceof KeypadEntity keypad){
            if(keypad.isCodeSet()){
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) p_60506_), new ClientboundOpenKeypadPacket(true,keypad.codeLength(),p_60505_));
                return InteractionResult.SUCCESS;
            }
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) p_60506_), new ClientboundOpenKeypadPacket(false,4,p_60505_));
            return InteractionResult.SUCCESS;
        }
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return p_153212_.isClientSide? null:(a,b,c,d)->{
            if(d instanceof KeypadEntity keypad) keypad.tick();
        };
    }

    @Override
    public int getSignal(BlockState p_60483_, BlockGetter p_60484_, BlockPos p_60485_, Direction p_60486_) {
        return p_60483_.getValue(UNLOCKED)?15:0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
        p_49915_.add(UNLOCKED);
    }
}