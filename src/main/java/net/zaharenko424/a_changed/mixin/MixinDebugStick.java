package net.zaharenko424.a_changed.mixin;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DebugStickState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.util.CoveredWith;
import net.zaharenko424.a_changed.util.StateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

/**
 * Makes debug stick think, that property COVERED_WITH is a thing.
 */
@Mixin(DebugStickItem.class)
public abstract class MixinDebugStick {

    @Unique
    private LatexCoveredData achanged$data;
    @Unique
    private BlockPos achanged$pos;

    /**
     *  Add fake property & init cache if block is coverable!
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/StateDefinition;getProperties()Ljava/util/Collection;"),
            method = "handleInteraction")
    private Collection<Property<?>> addLTCProperty(Collection<Property<?>> original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos){
        if(LatexCoveredData.isLatex(state) || LatexCoveredData.isStateNotCoverable(state)) {
            achanged$data = null;
            achanged$pos = null;
            return original;
        }

        if(achanged$pos != pos) {
            achanged$data = LatexCoveredData.of(player.level().getChunkAt(pos));
            this.achanged$pos = pos;
        }

        ImmutableCollection.Builder<Property<?>> builder = ImmutableList.builderWithExpectedSize(original.size() + 1);
        builder.add(StateProperties.COVERED_WITH);
        builder.addAll(original);
        return builder.build();
    }

    /**
     * Return CoveredWith if name matches.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"),
            method = "handleInteraction")
    private <V> V getSavedProperty(V original, @Local(argsOnly = true) ItemStack debugStick){
        if(!debugStick.has(ComponentRegistry.DEBUG_STICK_LATEX)) return original;
        return (V) StateProperties.COVERED_WITH;
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"),
            method = "handleInteraction")
    private <T> T wrapSetData(ItemStack debugStick, DataComponentType<? super T> component, T value, Operation<T> original, @Local Property<?> property, @Local Holder<Block> holder){
        if(property != StateProperties.COVERED_WITH) {
            debugStick.remove(ComponentRegistry.DEBUG_STICK_LATEX);
            return (T) debugStick.set(DataComponents.DEBUG_STICK_STATE, ((DebugStickState) value).withProperty(holder, property));
        }
        debugStick.set(ComponentRegistry.DEBUG_STICK_LATEX, Unit.INSTANCE);
        return null;
    }

    /**
     *  Don't call operation to not get exceptions.
     */
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DebugStickItem;cycleState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/properties/Property;Z)Lnet/minecraft/world/level/block/state/BlockState;"),
            method = "handleInteraction")
    private <T extends Comparable<T>> BlockState replaceCycleState(BlockState state, Property<T> property, boolean backwards, Operation<BlockState> original){
        if(property != StateProperties.COVERED_WITH) return original.call(state, property, backwards);

        int i = achanged$data.getCoveredWith(achanged$pos).ordinal() + (backwards ? -1 : 1);

        if(i < 0) i += 3;
        if(i > 2) i -= 3;

        achanged$data.coverWith(achanged$pos, CoveredWith.values()[i]);

        return state;
    }

    /**
     * Provides property value name.
     */
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DebugStickItem;getNameHelper(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/String;"),
            method = "handleInteraction")
    private <T extends Comparable<T>> String wrapNameHelper(BlockState pState, Property<T> property, Operation<String> original){
        if(property != StateProperties.COVERED_WITH) return original.call(pState, property);

        return achanged$data.getCoveredWith(achanged$pos).getSerializedName();
    }
}