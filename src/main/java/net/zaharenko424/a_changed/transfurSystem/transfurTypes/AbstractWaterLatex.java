package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import net.zaharenko424.a_changed.util.Latex;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractWaterLatex extends TransfurType {

    public AbstractWaterLatex(@NotNull Properties properties, @Nullable MemorizingSupplier<CustomHumanoidModel<LivingEntity>> modelSupplier) {
        super(properties, modelSupplier);
    }

    public static class WaterLatexProperties extends Properties {

        protected WaterLatexProperties(ResourceLocation resourceLocation, Latex latex) {
            super(resourceLocation, latex);
            airReductionModifier(-1);
            swimSpeedModifier(2);
        }

        public static @NotNull WaterLatexProperties of(ResourceLocation resourceLocation){
            return new WaterLatexProperties(resourceLocation, Latex.WHITE);
        }

        public static @NotNull WaterLatexProperties of(ResourceLocation resourceLocation, Latex latex){
            return new WaterLatexProperties(resourceLocation, latex);
        }
    }
}