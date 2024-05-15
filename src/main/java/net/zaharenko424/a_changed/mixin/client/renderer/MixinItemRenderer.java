package net.zaharenko424.a_changed.mixin.client.renderer;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 2), method = "render")
    private boolean renderProxy(@NotNull ItemStack instance, Item pItem){
        return instance.is(pItem) || instance.is(ItemRegistry.ABSOLUTE_SOLVER);
    }
}