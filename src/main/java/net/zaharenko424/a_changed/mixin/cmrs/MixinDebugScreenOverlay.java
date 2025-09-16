package net.zaharenko424.a_changed.mixin.cmrs;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;
import net.zaharenko424.cmrs.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class MixinDebugScreenOverlay {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isDemo()Z"), method = "getSystemInformation")
    private void onMemUsage(CallbackInfoReturnable<List<String>> cir, @Local List<String> list){
        for(int i = 0; i < list.size(); i++){
            if(!list.get(i).startsWith("Allocated")) continue;

            list.add(i + 1, "CMRS Buffers: " + Utils.memFormat(MultiBufferSource.getInstance().allocated()));
            return;
        }
    }
}
