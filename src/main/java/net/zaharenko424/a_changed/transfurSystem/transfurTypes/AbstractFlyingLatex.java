package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import net.zaharenko424.a_changed.util.Latex;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFlyingLatex extends TransfurType {

    public AbstractFlyingLatex(@NotNull Properties properties, @Nullable MemorizingSupplier<CustomHumanoidModel<LivingEntity>> modelSupplier) {
        super(properties, modelSupplier);
    }

    public static class FlyingProperties extends Properties {

        protected FlyingProperties(ResourceLocation resourceLocation, Latex latex) {
            super(resourceLocation, latex);
            eyeHeight(1.75f,1.5f);
            airReductionModifier(.5f);
            swimSpeedModifier(-.25f);
        }

        public static @NotNull AbstractLatexCat.CatProperties of(ResourceLocation resourceLocation){
            return new AbstractLatexCat.CatProperties(resourceLocation, Latex.WHITE);
        }

        public static @NotNull AbstractLatexCat.CatProperties of(ResourceLocation resourceLocation, Latex latex){
            return new AbstractLatexCat.CatProperties(resourceLocation, latex);
        }
    }
}