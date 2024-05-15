package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        CompoundTag tag = stack.getOrCreateTag();
        byte mode = tag.getByte("mode");
        if(mode >= 3) mode = 0;
        else mode++;
        tag.putByte("mode", mode);
        tag.remove("data");
        if(entity instanceof Player player) player.sendSystemMessage(Component.literal("Mode set to " + Mode.values()[mode]));
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if(hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(item);
        if(level.isClientSide) return InteractionResultHolder.consume(item);
        if(player.isCrouching()){
            item.getOrCreateTag().remove("data");
            return InteractionResultHolder.consume(item);
        }
        if(item.getTagElement("data") == null) return InteractionResultHolder.fail(item);
        CompoundTag tag = item.getOrCreateTag();
        CompoundTag data = tag.getCompound("data");
        Mode mode = Mode.values()[tag.getByte("mode")];
        if(mode == Mode.GROW) return InteractionResultHolder.pass(item);
        boolean destroy = data.getBoolean("destroy");
        if(!destroy && (player.getOffhandItem().isEmpty() || !(player.getOffhandItem().getItem() instanceof BlockItem)))
            return InteractionResultHolder.fail(item);

        BlockPos from = NBTUtils.getBlockPos(data);
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

        item.getOrCreateTag().remove("data");
        return InteractionResultHolder.consume(item);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        if(context.getLevel().isClientSide) return InteractionResult.CONSUME;
        ItemStack item = context.getItemInHand();
        CompoundTag tag = item.getOrCreateTag();
        BlockPos clicked = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        Mode mode = Mode.values()[tag.getByte("mode")];
        if(mode == Mode.GROW){
            Level level = context.getLevel();
            BlockState state = level.getBlockState(clicked);
            Set<BlockPos> list = new HashSet<>();
            findBlocks(level, state.getBlock(), clicked, context.getClickedFace(), list, 0);
            list.forEach(pos -> level.setBlockAndUpdate(pos.relative(clickedFace), state));
            return InteractionResult.CONSUME;
        }

        if(tag.contains("data") && (mode == Mode.DESTROY || mode == Mode.REPLACE)) {
            Player player = context.getPlayer();
            Level level = context.getLevel();
            if(player == null) return InteractionResult.FAIL;
            BlockPos from = NBTUtils.getBlockPos(tag.getCompound("data"));

            if(mode == Mode.DESTROY){
                BlockPos.betweenClosedStream(from, clicked).forEach(pos -> level.removeBlock(pos, false));
            } else {
                if(player.getOffhandItem().isEmpty() || !(player.getOffhandItem().getItem() instanceof BlockItem)) return InteractionResult.FAIL;
                BlockState state = Block.byItem(player.getOffhandItem().getItem()).defaultBlockState();
                BlockPos.betweenClosedStream(from, clicked).forEach(pos -> {
                    if(!level.getBlockState(pos).isEmpty()) level.setBlockAndUpdate(pos, state);
                });
            }

            tag.remove("data");
            return InteractionResult.CONSUME;
        }

        CompoundTag data = new CompoundTag();
        BlockPos pos = context.getClickedPos().relative(clickedFace);
        NBTUtils.putBlockPos(data, pos);
        tag.put("data", data);
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
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("mode: " + Mode.values()[pStack.getOrCreateTag().getByte("mode")]).withStyle(ChatFormatting.DARK_GREEN));
    }

    public enum Mode {
        BUILD,
        DESTROY,
        REPLACE,
        GROW
    }
}