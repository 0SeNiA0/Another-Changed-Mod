package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractWaterLatex extends AbstractTransfurType {

    public AbstractWaterLatex(@NotNull Properties properties) {
        super(properties);
    }

    public static class WaterLatexProperties extends Properties {

        protected WaterLatexProperties(ResourceLocation resourceLocation) {
            super(resourceLocation, Latex.WHITE);
            airReductionModifier(-1);
            swimSpeedModifier(2);
        }

        public static @NotNull WaterLatexProperties of(ResourceLocation resourceLocation){
            return new WaterLatexProperties(resourceLocation);
        }
    }
}