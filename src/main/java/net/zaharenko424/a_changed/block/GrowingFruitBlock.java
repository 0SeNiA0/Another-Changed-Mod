package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class GrowingFruitBlock extends Block implements BonemealableBlock {

    protected final Supplier<Item> fruitItem;

    public GrowingFruitBlock(Properties p_49795_, Supplier<Item> fruitItem) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(ageProperty(), 0));
        this.fruitItem = fruitItem;
    }

    public Item getFruitItem(){
        return fruitItem.get();
    }

    public abstract IntegerProperty ageProperty();

    public abstract int maxAge();

    public int getAge(@NotNull BlockState state) {
        return state.getValue(ageProperty());
    }

    public boolean isMaxAge(@NotNull BlockState state){
        return state.getValue(ageProperty()) == maxAge();
    }

    public BlockState getAgeState(int age){
        return defaultBlockState().setValue(ageProperty(), Mth.clamp(age, 0, maxAge()));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(level.isClientSide || !isMaxAge(state)) return super.use(state, level, pos, player, pHand, pHit);
        level.removeBlock(pos, false);
        ItemHandlerHelper.giveItemToPlayer(player, fruitItem.get().getDefaultInstance());
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (net.neoforged.neoforge.common.CommonHooks.onCropsGrowPre(level, pos, state, random.nextInt(15) == 0)) {
            level.setBlock(pos, getAgeState(getAge(state) + 1), Block.UPDATE_CLIENTS);
            net.neoforged.neoforge.common.CommonHooks.onCropsGrowPost(level, pos, state);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ageProperty()));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        return new ItemStack(fruitItem.get());
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        level.setBlock(pos, getAgeState(getAge(state) +  boneMealGrowAmount(level, random, pos, state)), Block.UPDATE_CLIENTS);
    }

    protected int boneMealGrowAmount(ServerLevel level, RandomSource random, BlockPos pos, BlockState state){
        return Mth.nextInt(random, 1, 3);
    }
}