package net.zaharenko424.a_changed;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.function.Consumer;

public interface BakedQuadExtension {

    Consumer<BakedQuad> dlMode = quad -> ((BakedQuadExtension)quad).mod$darkLatex();

    Consumer<BakedQuad> wlMode = quad -> ((BakedQuadExtension)quad).mod$whiteLatex();

    Consumer<BakedQuad> clearMode = quad -> ((BakedQuadExtension)quad).mod$clear();

    void mod$darkLatex();

    void mod$whiteLatex();

    void mod$clear();

    void mod$initUV(float[] uv);
}