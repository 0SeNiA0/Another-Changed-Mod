package net.zaharenko424.a_changed.mixin.client.latex;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.zaharenko424.a_changed.BakedQuadExtension;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.WeakHashMap;

@Mixin(BakedQuad.class)
public abstract class MixinBakedQuad implements BakedQuadExtension {

    @Shadow @Final protected int[] vertices;

    @Unique
    private static TextureAtlasSprite dl_sprite;
    @Unique
    private static TextureAtlasSprite wl_sprite;

    @Unique
    private int[] achanged$dark_latex;
    @Unique
    private int[] achanged$white_latex;

    @Unique
    private float[] achanged$uv;

    @Unique
    private final WeakHashMap<Thread, CoveredWith> achanged$map = new WeakHashMap<>();

    @Override
    public void achanged$darkLatex() {
        achanged$map.put(Thread.currentThread(), CoveredWith.DARK_LATEX);
        if(dl_sprite == null) dl_sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(BlockRegistry.DARK_LATEX_BLOCK.getId().withPrefix("block/"));

        if(achanged$dark_latex != null) return;

        achanged$dark_latex = vertices.clone();

        for(int i = 0; i < 4; i++){
            int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.UV0;

            achanged$dark_latex[offset] = Float.floatToRawIntBits(dl_sprite.getU(achanged$uv[i * 2]));
            achanged$dark_latex[offset + 1] = Float.floatToRawIntBits(dl_sprite.getV(achanged$uv[i * 2 + 1]));
        }
    }

    @Override
    public void achanged$whiteLatex() {
        achanged$map.put(Thread.currentThread(), CoveredWith.WHITE_LATEX);
        if(wl_sprite == null) wl_sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(BlockRegistry.WHITE_LATEX_BLOCK.getId().withPrefix("block/"));

        if(achanged$white_latex != null) return;

        achanged$white_latex = vertices.clone();

        for(int i = 0; i < 4; i++){
            int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.UV0;

            achanged$white_latex[offset] = Float.floatToRawIntBits(wl_sprite.getU(achanged$uv[i * 2]));
            achanged$white_latex[offset + 1] = Float.floatToRawIntBits(wl_sprite.getV(achanged$uv[i * 2 + 1]));
        }
    }

    @Override
    public void achanged$clear() {
        achanged$map.put(Thread.currentThread(), CoveredWith.NOTHING);
    }

    @Override
    public void achanged$initUV(float[] uv) {
        this.achanged$uv = uv;
    }

    @ModifyReturnValue(at = @At("TAIL"), method = "getVertices")
    private int[] modifyGetVertices(int[] original){
        return switch (achanged$map.getOrDefault(Thread.currentThread(), CoveredWith.NOTHING)){
            case DARK_LATEX -> achanged$dark_latex;
            case WHITE_LATEX -> achanged$white_latex;
            default -> original;
        };
    }

    @ModifyReturnValue(at = @At("TAIL"), method = "isTinted")
    private boolean modifyIsTinted(boolean original){
        return achanged$map.getOrDefault(Thread.currentThread(), CoveredWith.NOTHING) == CoveredWith.NOTHING && original;
    }
}