package net.zaharenko424.a_changed;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.function.Consumer;

public interface BakedQuadExtension {

    Consumer<BakedQuad> dlMode = quad -> ((BakedQuadExtension)quad).achanged$darkLatex();

    Consumer<BakedQuad> wlMode = quad -> ((BakedQuadExtension)quad).achanged$whiteLatex();

    Consumer<BakedQuad> clearMode = quad -> ((BakedQuadExtension)quad).achanged$clear();

    void achanged$darkLatex();

    void achanged$whiteLatex();

    void achanged$clear();

    void achanged$initUV(float[] uv);
}