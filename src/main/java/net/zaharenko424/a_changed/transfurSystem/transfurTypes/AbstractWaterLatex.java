package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractWaterLatex extends AbstractTransfurType {

    public AbstractWaterLatex(@NotNull Properties properties) {
        super(properties);
    }

    public static class WaterLatexProperties extends Properties {

        protected WaterLatexProperties(ResourceLocation resourceLocation) {
            super(resourceLocation);
            airReductionModifier(-1);
            swimSpeedModifier(2);
        }

        public void a(){}

        public static @NotNull WaterLatexProperties of(ResourceLocation resourceLocation){
            return new WaterLatexProperties(resourceLocation);
        }
    }
}