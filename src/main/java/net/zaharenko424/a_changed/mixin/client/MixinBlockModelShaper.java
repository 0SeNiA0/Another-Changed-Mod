package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.util.CoveredWith;
import net.zaharenko424.a_changed.util.StateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockModelShaper.class)
public abstract class MixinBlockModelShaper {

    @Shadow
    private static <T extends Comparable<T>> String getValue(Property<T> pProperty, Comparable<?> pValue) {
        return null;
    }

    @Shadow public abstract BakedModel getBlockModel(BlockState pState);

    /**
     *  Returns latex covered particle texture if block is latex covered.
     */
    @ModifyReturnValue(at = @At("TAIL"), method = "getTexture")
    private TextureAtlasSprite onGetParticleTexture(TextureAtlasSprite original, BlockState state, Level level, BlockPos pos){
        CoveredWith coveredWith = LatexCoveredData.of(level.getChunkAt(pos)).getCoveredWith(pos);
        return switch(coveredWith){
            case NOTHING -> original;
            case DARK_LATEX -> getBlockModel(BlockRegistry.DARK_LATEX_BLOCK.get().defaultBlockState()).getParticleIcon(ModelData.EMPTY);
            case WHITE_LATEX -> getBlockModel(BlockRegistry.WHITE_LATEX_BLOCK.get().defaultBlockState()).getParticleIcon(ModelData.EMPTY);
        };
    }

    /**
     * Ignore state lock when creating block models
     */
    @Inject(at = @At("HEAD"), method = "statePropertiesToString", cancellable = true)
    private static void onStatePropertiesToString(Map<Property<?>, Comparable<?>> pPropertyValues, CallbackInfoReturnable<String> cir){
        if(!pPropertyValues.containsKey(StateProperties.LOCKED_STATE)) return;
        HashMap<Property<?>, Comparable<?>> map = new HashMap<>(pPropertyValues);
        map.remove(StateProperties.LOCKED_STATE);
        StringBuilder stringbuilder = new StringBuilder();

        for(Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {
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