package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum CoveredWith implements StringRepresentable {
    NOTHING("nothing", false),
    DARK_LATEX("dark_latex", false),
    WHITE_LATEX("white_latex", false),
    LIGHT_DARK_LATEX("light_dark_latex", true),
    LIGHT_WHITE_LATEX("light_white_latex", true);

    private final String name;
    public final boolean isLightlyCovered;

    CoveredWith(String name, boolean lightlyCovered) {
        this.name = name;
        isLightlyCovered = lightlyCovered;
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