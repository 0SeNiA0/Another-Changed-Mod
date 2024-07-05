package net.zaharenko424.a_changed.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.BiConsumer;

@Mixin(BlockBehaviour.class)
public abstract class MixinBlockBehaviour {

    /**
     *  Can be potentially overriden by other mods, but it will do.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDrops(Lnet/minecraft/world/level/storage/loot/LootParams$Builder;)Ljava/util/List;"),
            method = "onExplosionHit")
    private List<ItemStack> onExplodeLatexCovered(List<ItemStack> original, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) BiConsumer<ItemStack, BlockPos> consumer){
        if(original.isEmpty()) return original;

        CoveredWith coveredWith = LatexCoveredData.of(level.getChunkAt(pos)).getCoveredWith(pos);
        if(coveredWith == CoveredWith.NOTHING) return original;

        consumer.accept(coveredWith == CoveredWith.DARK_LATEX ? ItemRegistry.DARK_LATEX_ITEM.toStack()
                : ItemRegistry.WHITE_LATEX_ITEM.toStack(), pos);

        return original;
    }
}