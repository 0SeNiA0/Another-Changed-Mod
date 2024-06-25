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
    private int[] dark_latex;
    @Unique
    private int[] white_latex;

    @Unique
    private float[] mod$uv;

    @Unique
    private final WeakHashMap<Thread, CoveredWith> mod$map = new WeakHashMap<>();

    @Override
    public void mod$darkLatex() {
        mod$map.put(Thread.currentThread(), CoveredWith.DARK_LATEX);
        if(dl_sprite == null) dl_sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(BlockRegistry.DARK_LATEX_BLOCK.getId().withPrefix("block/"));

        if(dark_latex != null) return;

        dark_latex = vertices.clone();

        for(int i = 0; i < 4; i++){
            int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.UV0;

            dark_latex[offset] = Float.floatToRawIntBits(dl_sprite.getU(mod$uv[i * 2]));
            dark_latex[offset + 1] = Float.floatToRawIntBits(dl_sprite.getV(mod$uv[i * 2 + 1]));
        }
    }

    @Override
    public void mod$whiteLatex() {
        mod$map.put(Thread.currentThread(), CoveredWith.WHITE_LATEX);
        if(wl_sprite == null) wl_sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(BlockRegistry.WHITE_LATEX_BLOCK.getId().withPrefix("block/"));

        if(white_latex != null) return;

        white_latex = vertices.clone();

        for(int i = 0; i < 4; i++){
            int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.UV0;

            white_latex[offset] = Float.floatToRawIntBits(wl_sprite.getU(mod$uv[i * 2]));
            white_latex[offset + 1] = Float.floatToRawIntBits(wl_sprite.getV(mod$uv[i * 2 + 1]));
        }
    }

    @Override
    public void mod$clear() {
        mod$map.put(Thread.currentThread(), CoveredWith.NOTHING);
    }

    @Override
    public void mod$initUV(float[] uv) {
        this.mod$uv = uv;
    }

    @ModifyReturnValue(at = @At("TAIL"), method = "getVertices")
    private int[] modifyGetVertices(int[] original){
        return switch (mod$map.getOrDefault(Thread.currentThread(), CoveredWith.NOTHING)){
            case DARK_LATEX -> dark_latex;
            case WHITE_LATEX -> white_latex;
            default -> original;
        };
    }

    @ModifyReturnValue(at = @At("TAIL"), method = "isTinted")
    private boolean modifyIsTinted(boolean original){
        return mod$map.getOrDefault(Thread.currentThread(), CoveredWith.NOTHING) == CoveredWith.NOTHING && original;
    }
}