package net.zaharenko424.a_changed.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    /**
     * Easter egg. Probably will be removed at some point.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 2), method = "render")
    private boolean renderProxy(boolean original, @Local(argsOnly = true) ItemStack stack){
        return original || stack.is(ItemRegistry.ABSOLUTE_SOLVER);
    }
}