package net.zaharenko424.a_changed.mixin.client;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.world.level.block.state.properties.Property;
import net.zaharenko424.a_changed.util.StateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(BlockModelShaper.class)
public class MixinBlockModelShaper {

    @Shadow
    private static <T extends Comparable<T>> String getValue(Property<T> pProperty, Comparable<?> pValue) {
        return null;
    }

    @Inject(at = @At("HEAD"), method = "statePropertiesToString", cancellable = true)
    private static void onStatePropertiesToString(Map<Property<?>, Comparable<?>> pPropertyValues, CallbackInfoReturnable<String> cir){
        if(!pPropertyValues.containsKey(StateProperties.LOCKED_STATE)) return;
        StringBuilder stringbuilder = new StringBuilder();

        for(Map.Entry<Property<?>, Comparable<?>> entry : pPropertyValues.entrySet()) {
            if (!stringbuilder.isEmpty()) {
                stringbuilder.append(',');
            }

            Property<?> property = entry.getKey();
            stringbuilder.append(property.getName());
            stringbuilder.append('=');
            stringbuilder.append(getValue(property, entry.getValue()));
        }

        cir.setReturnValue(stringbuilder.toString());
    }
}