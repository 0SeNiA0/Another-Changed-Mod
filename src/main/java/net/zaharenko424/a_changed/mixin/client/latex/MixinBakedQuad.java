package net.zaharenko424.a_changed.mixin.client.latex;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.zaharenko424.a_changed.BakedQuadExtension;
import net.zaharenko424.a_changed.ClientConfig;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.transfurSystem.CoveredWith;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.WeakHashMap;

@Mixin(BakedQuad.class)
public abstract class MixinBakedQuad implements BakedQuadExtension {

    @Shadow @Final protected TextureAtlasSprite sprite;

    @Unique
    private final TextureAtlasSprite[] achanged$sprites = new TextureAtlasSprite[4];

    @Unique
    private float[] achanged$uv;

    @Unique
    private final WeakHashMap<Thread, CoveredWith> achanged$map = new WeakHashMap<>();

    @Inject(at = @At("TAIL"), method = "<init>([IILnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZZ)V")
    private void onInit(int[] vertices, int tintIndex, Direction direction, TextureAtlasSprite sprite, boolean shade, boolean hasAmbientOcclusion, CallbackInfo ci){
        achanged$uv = new float[8];
        for(int i = 0; i < 4; i++){
            achanged$uv[i * 2] = sprite.getUOffset(Float.intBitsToFloat(vertices[IQuadTransformer.STRIDE * i + IQuadTransformer.UV0]));
            achanged$uv[i * 2 + 1] = sprite.getVOffset(Float.intBitsToFloat(vertices[IQuadTransformer.STRIDE * i + IQuadTransformer.UV0 + 1]));
        }
    }

    @Override
    public void achanged$prepareLatex(CoveredWith latex){
        achanged$map.put(Thread.currentThread(), latex);
        if(latex == CoveredWith.NOTHING) return;

        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
        achanged$sprites[latex.ordinal() - 1] = switch (latex) {
            case DARK_LATEX -> atlas.getSprite(BlockRegistry.DARK_LATEX_BLOCK.getId().withPrefix("block/"));
            case WHITE_LATEX -> atlas.getSprite(BlockRegistry.WHITE_LATEX_BLOCK.getId().withPrefix("block/"));
            case LIGHT_DARK_LATEX -> atlas.getSprite(ClientConfig.LIGHTLY_COVERED_BLOCKS.getAsBoolean() ? sprite.contents().name().withSuffix("_darkltx") : BlockRegistry.DARK_LATEX_BLOCK.getId().withPrefix("block/"));
            case LIGHT_WHITE_LATEX -> atlas.getSprite(ClientConfig.LIGHTLY_COVERED_BLOCKS.getAsBoolean() ? sprite.contents().name().withSuffix("_whiteltx") : BlockRegistry.WHITE_LATEX_BLOCK.getId().withPrefix("block/"));
            default -> throw new IllegalStateException("Unexpected value: " + latex);
        };
    }

    @Override
    public boolean achanged$isCovered() {
        return achanged$map.getOrDefault(Thread.currentThread(), CoveredWith.NOTHING) != CoveredWith.NOTHING;
    }

    @Unique
    private TextureAtlasSprite achanged$getSprite(){
        CoveredWith coveredWith = achanged$map.getOrDefault(Thread.currentThread(), CoveredWith.NOTHING);
        if(coveredWith == CoveredWith.NOTHING) return null;

        return achanged$sprites[coveredWith.ordinal() - 1];
    }

    @Override
    public float achanged$getU(int vertId) {
        TextureAtlasSprite sprite = achanged$getSprite();
        if(sprite == null) return 0;

        return sprite.getU(achanged$uv[vertId * 2]);
    }

    @Override
    public float achanged$getV(int vertId) {
        TextureAtlasSprite sprite = achanged$getSprite();
        if(sprite == null) return 0;

        return sprite.getV(achanged$uv[vertId * 2 + 1]);
    }

    @Override
    public void achanged$clear() {
        achanged$map.put(Thread.currentThread(), CoveredWith.NOTHING);
    }

    @ModifyReturnValue(at = @At("TAIL"), method = "isTinted")
    private boolean modifyIsTinted(boolean original){
        return achanged$map.getOrDefault(Thread.currentThread(), CoveredWith.NOTHING) == CoveredWith.NOTHING && original;
    }
}