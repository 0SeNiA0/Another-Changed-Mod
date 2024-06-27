package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public abstract class MixinDebugScreenOverlay {

    @Shadow protected abstract Level getLevel();

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2, shift = At.Shift.AFTER),
            method = "getSystemInformation")
    private void onBlockInformation(CallbackInfoReturnable<List<String>> cir, @Local List<String> list, @Local BlockState state, @Local BlockPos pos){
        if(LatexCoveredData.isLatex(state) || LatexCoveredData.isStateNotCoverable(state)) return;

        CoveredWith coveredWith = LatexCoveredData.of(getLevel().getChunkAt(pos)).getCoveredWith(pos);
        list.add("covered_with: " + switch(coveredWith){
            case DARK_LATEX -> ChatFormatting.BLACK;
            case WHITE_LATEX -> ChatFormatting.WHITE;
            case NOTHING -> ChatFormatting.GRAY;
        } + coveredWith);
    }
}