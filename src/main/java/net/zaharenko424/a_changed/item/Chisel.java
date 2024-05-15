package net.zaharenko424.a_changed.item;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *  Copy of debug stick cut down to work with only Chiselable blocks
 */
public class Chisel extends TieredItem {

    public Chisel(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer) {
        if (!pLevel.isClientSide) {
            this.handleInteraction(pPlayer, pState, pLevel, pPos, false, pPlayer.getItemInHand(InteractionHand.MAIN_HAND));
        }
        return false;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(@NotNull ItemStack oldStack, @NotNull ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        if (!level.isClientSide && player != null) {
            BlockPos blockpos = pContext.getClickedPos();
            if (!this.handleInteraction(player, level.getBlockState(blockpos), level, blockpos, true, pContext.getItemInHand())) {
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private boolean handleInteraction(Player pPlayer, @NotNull BlockState state, LevelAccessor level, BlockPos pos, boolean pShouldCycleState, ItemStack chisel) {
        if (!state.is(AChanged.CHISELABLE)) {
            return false;
        }
        Block block = state.getBlock();
        StateDefinition<Block, BlockState> statedefinition = block.getStateDefinition();
        Collection<Property<?>> collection = statedefinition.getProperties();
        String s = BuiltInRegistries.BLOCK.getKey(block).toString();
        if (collection.isEmpty()) {
            message(pPlayer, Component.translatable(Items.DEBUG_STICK.getDescriptionId() + ".empty", s));
            return false;
        } else {
            CompoundTag compoundtag = chisel.getOrCreateTagElement("DebugProperty");
            String s1 = compoundtag.getString(s);
            Property<?> property = statedefinition.getProperty(s1);
            if (pShouldCycleState) {
                if (property == null) {
                    property = collection.iterator().next();
                }

                BlockState newState = cycleState(state, property, pPlayer.isSecondaryUseActive());
                level.setBlock(pos, newState, 18);
                message(pPlayer, Component.translatable(Items.DEBUG_STICK.getDescriptionId() + ".update", property.getName(), getNameHelper(newState, property)));
            } else {
                property = getRelative(collection, property, pPlayer.isSecondaryUseActive());
                if(property != BlockStateProperties.WATERLOGGED) {
                    String s2 = property.getName();
                    compoundtag.putString(s, s2);
                    message(pPlayer, Component.translatable(Items.DEBUG_STICK.getDescriptionId() + ".select", s2, getNameHelper(state, property)));
                }
            }

            if(pShouldCycleState && !pPlayer.isCreative())
                chisel.hurtAndBreak(1, pPlayer, p_29910_ -> p_29910_.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            return true;
        }
    }

    public static <T extends Comparable<T>> @NotNull BlockState cycleState(@NotNull BlockState pState, Property<T> pProperty, boolean pBackwards) {
        Collection<T> collection = new ArrayList<>(pProperty.getPossibleValues());
        if(pProperty == BlockStateProperties.SLAB_TYPE) collection.remove(SlabType.DOUBLE);
        return pState.setValue(pProperty, getRelative(collection, pState.getValue(pProperty), pBackwards));
    }

    public static <T> T getRelative(Iterable<T> pAllowedValues, @Nullable T pCurrentValue, boolean pBackwards) {
        return pBackwards ? Util.findPreviousInIterable(pAllowedValues, pCurrentValue) : Util.findNextInIterable(pAllowedValues, pCurrentValue);
    }

    public static void message(Player pPlayer, Component pMessageComponent) {
        ((ServerPlayer)pPlayer).sendSystemMessage(pMessageComponent, true);
    }

    public static <T extends Comparable<T>> @NotNull String getNameHelper(@NotNull BlockState pState, @NotNull Property<T> pProperty) {
        return pProperty.getName(pState.getValue(pProperty));
    }
}