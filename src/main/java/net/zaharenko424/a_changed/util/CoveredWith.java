package net.zaharenko424.a_changed.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum CoveredWith implements StringRepresentable {
    NOTHING("nothing"),
    DARK_LATEX("dark_latex"),
    WHITE_LATEX("white_latex");

    private final String name;

    CoveredWith(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}