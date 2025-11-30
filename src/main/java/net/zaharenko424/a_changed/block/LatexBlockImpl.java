package net.zaharenko424.a_changed.block;

import net.minecraft.world.level.block.Block;
import net.zaharenko424.a_changed.transfurSystem.Latex;

public class LatexBlockImpl extends Block implements LatexBlock {

    protected final Latex latex;

    public LatexBlockImpl(Properties properties, Latex latex) {
        super(properties);
        this.latex = latex;
    }

    @Override
    public Latex getLatex() {
        return latex;
    }
}
