package net.zaharenko424.a_changed.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SimpleBlockFeature.class)
public abstract class MixinSimpleBlockFeature extends Feature<SimpleBlockConfiguration> {
    public MixinSimpleBlockFeature(Codec<SimpleBlockConfiguration> p_65786_) {
        super(p_65786_);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",shift = At.Shift.AFTER), method = "place", locals = LocalCapture.CAPTURE_FAILHARD)
    private void onPlace(FeaturePlaceContext<SimpleBlockConfiguration> p_160341_, CallbackInfoReturnable<Boolean> cir, SimpleBlockConfiguration cfg, WorldGenLevel level, BlockPos blockPos, @NotNull BlockState state){
        if(state.hasProperty(StateProperties.PART2)) level.setBlock(blockPos.above(), state.setValue(StateProperties.PART2,1),2);
    }
}