package net.zaharenko424.a_changed.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BuildersWand extends Item {

    public BuildersWand(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public boolean onEntitySwing(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        if(entity.level().isClientSide) return true;
        Data data = stack.getOrDefault(ComponentRegistry.BUILDERS_WAND_DATA, Data.DEF);

        int ordinal = data.mode.ordinal();
        if(ordinal >= 3) {
            ordinal = 0;
        } else ordinal++;
        Mode mode = Mode.values()[ordinal];
        stack.set(ComponentRegistry.BUILDERS_WAND_DATA, data.withMode(mode));

        if(entity instanceof Player player) player.sendSystemMessage(Component.literal("Mode set to " + mode));
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if(hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(item);
        if(level.isClientSide) return InteractionResultHolder.consume(item);

        Data data = item.getOrDefault(ComponentRegistry.BUILDERS_WAND_DATA, Data.DEF);

        if(player.isCrouching()){
            item.set(ComponentRegistry.BUILDERS_WAND_DATA, data.withPos(null));
            return InteractionResultHolder.consume(item);
        }

        Mode mode = data.mode;
        if(data.from == null || mode == Mode.GROW) return InteractionResultHolder.pass(item);

        if(mode != Mode.DESTROY && (player.getOffhandItem().isEmpty() || !(player.getOffhandItem().getItem() instanceof BlockItem)))
            return InteractionResultHolder.fail(item);

        BlockPos from = data.from;
        Vec3 vec = player.getLookAngle().multiply(2, 0, 2).add(player.position());
        BlockPos to = new BlockPos.MutableBlockPos(vec.x, vec.y, vec.z).immutable();

        if(mode == Mode.BUILD || mode == Mode.REPLACE) {
            BlockState state = Block.byItem(player.getOffhandItem().getItem()).defaultBlockState();
            if(mode == Mode.BUILD) {
                BlockPos.betweenClosedStream(from, to).forEach(pos -> level.setBlockAndUpdate(pos, state));
            } else {
                BlockPos.betweenClosedStream(from, to).forEach(pos -> {
                    if(!level.getBlockState(pos).isEmpty()) level.setBlockAndUpdate(pos, state);
                });
            }
        }else if(mode == Mode.DESTROY){
            BlockPos.betweenClosedStream(from, to).forEach(pos -> level.removeBlock(pos, false));
        }

        item.set(ComponentRegistry.BUILDERS_WAND_DATA, data.withPos(null));
        return InteractionResultHolder.consume(item);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        if(context.getLevel().isClientSide) return InteractionResult.CONSUME;
        ItemStack item = context.getItemInHand();
        Data data = item.getOrDefault(ComponentRegistry.BUILDERS_WAND_DATA, Data.DEF);
        
        BlockPos clicked = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        Mode mode = data.mode;
        
        if(mode == Mode.GROW){
            Level level = context.getLevel();
            BlockState state = level.getBlockState(clicked);
            Set<BlockPos> list = new HashSet<>();
            findBlocks(level, state.getBlock(), clicked, context.getClickedFace(), list, 0);
            list.forEach(pos -> level.setBlockAndUpdate(pos.relative(clickedFace), state));
            return InteractionResult.CONSUME;
        }

        if(data.from != null && (mode == Mode.DESTROY || mode == Mode.REPLACE)) {
            Player player = context.getPlayer();
            Level level = context.getLevel();
            if(player == null) return InteractionResult.FAIL;
            BlockPos from = data.from;

            if(mode == Mode.DESTROY){
                BlockPos.betweenClosedStream(from, clicked).forEach(pos -> level.removeBlock(pos, false));
            } else {
                if(player.getOffhandItem().isEmpty() || !(player.getOffhandItem().getItem() instanceof BlockItem)) return InteractionResult.FAIL;
                BlockState state = Block.byItem(player.getOffhandItem().getItem()).defaultBlockState();
                BlockPos.betweenClosedStream(from, clicked).forEach(pos -> {
                    if(!level.getBlockState(pos).isEmpty()) level.setBlockAndUpdate(pos, state);
                });
            }

            item.set(ComponentRegistry.BUILDERS_WAND_DATA, data.withPos(null));
            return InteractionResult.CONSUME;
        }

        item.set(ComponentRegistry.BUILDERS_WAND_DATA, data.withPos(clicked.relative(clickedFace)));
        return InteractionResult.CONSUME;
    }

    void findBlocks(Level level, Block block, BlockPos pos, Direction direction, Set<BlockPos> set, int counter){
        if(counter >= 32) return;
        for(Direction dir : Direction.values()){
            if(dir == direction || dir == direction.getOpposite()) continue;
            BlockPos pos1 = pos.relative(dir);
            if(set.contains(pos1)) continue;
            if(!level.getBlockState(pos1).is(block)) continue;
            set.add(pos1);
            findBlocks(level, block, pos1, direction, set, counter + 1);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("mode: " + stack.getOrDefault(ComponentRegistry.BUILDERS_WAND_DATA, Data.DEF).mode).withStyle(ChatFormatting.DARK_GREEN));
    }

    public record Data(Mode mode, BlockPos from){

        public static final Data DEF = new Data(Mode.BUILD, null);
        
        public static Codec<Data> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.BYTE.xmap(b -> Mode.values()[b], mode -> (byte) mode.ordinal()).fieldOf("mode").forGetter(Data::mode),
                BlockPos.CODEC.fieldOf("from").forGetter(Data::from)
        ).apply(builder, Data::new));

        public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        public Data withMode(Mode mode){
            return new Data(mode, from);
        }

        public Data withPos(BlockPos from){
            return new Data(mode, from);
        }
    }

    public enum Mode {
        BUILD,
        DESTROY,
        REPLACE,
        GROW
    }
}