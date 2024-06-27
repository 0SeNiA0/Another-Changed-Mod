package net.zaharenko424.a_changed.mixin;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.util.CoveredWith;
import net.zaharenko424.a_changed.util.StateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Collection;

/**
 * Makes debug stick think, that property COVERED_WITH is a thing.
 */
@Mixin(DebugStickItem.class)
public abstract class MixinDebugStick {

    @Unique
    private LatexCoveredData mod$data;
    @Unique
    private BlockPos mod$pos;

    /**
     *  Add fake property & init cache if block is coverable!
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/StateDefinition;getProperties()Ljava/util/Collection;"),
            method = "handleInteraction")
    private Collection<Property<?>> addLTCProperty(Collection<Property<?>> original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos){
        if(LatexCoveredData.isLatex(state) || LatexCoveredData.isStateNotCoverable(state)) {
            mod$data = null;
            mod$pos = null;
            return original;
        }

        if(mod$pos != pos) {
            mod$data = LatexCoveredData.of(player.level().getChunkAt(pos));
            this.mod$pos = pos;
        }

        ImmutableCollection.Builder<Property<?>> builder = ImmutableList.builderWithExpectedSize(original.size() + 1);
        builder.add(StateProperties.COVERED_WITH);
        builder.addAll(original);
        return builder.build();
    }

    /**
     *   Return CoveredWith if name matches.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/StateDefinition;getProperty(Ljava/lang/String;)Lnet/minecraft/world/level/block/state/properties/Property;"),
            method = "handleInteraction")
    private Property<?> getSavedProperty(Property<?> original, @Local(ordinal = 1) String s1){
        return s1.equals(StateProperties.COVERED_WITH.getName()) ? StateProperties.COVERED_WITH : original;
    }

    /**
     *  Don't call operation to not get exceptions.
     */
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DebugStickItem;cycleState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/properties/Property;Z)Lnet/minecraft/world/level/block/state/BlockState;"),
            method = "handleInteraction")
    private <T extends Comparable<T>> BlockState replaceCycleState(BlockState state, Property<T> property, boolean backwards, Operation<BlockState> original){
        if(property != StateProperties.COVERED_WITH) return original.call(state, property, backwards);

        int i = mod$data.getCoveredWith(mod$pos).ordinal() + (backwards ? -1 : 1);

        if(i < 0) i += 3;
        if(i > 2) i -= 3;

        mod$data.coverWith(mod$pos, CoveredWith.values()[i]);

        return state;
    }

    /**
     * Provides property value name.
     */
    @WrapOperation(slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getString(Ljava/lang/String;)Ljava/lang/String;")), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DebugStickItem;getNameHelper(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/String;"),
            method = "handleInteraction")
    private <T extends Comparable<T>> String wrapNameHelper(BlockState pState, Property<T> property, Operation<String> original){
        if(property != StateProperties.COVERED_WITH) return original.call(pState, property);

        return mod$data.getCoveredWith(mod$pos).getSerializedName();
    }
}