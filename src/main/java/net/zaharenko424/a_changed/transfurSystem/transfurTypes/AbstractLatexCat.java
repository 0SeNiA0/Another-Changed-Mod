package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import net.zaharenko424.a_changed.util.Latex;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractLatexCat extends TransfurType {

    private static final UUID speedBuff = UUID.fromString("1c73d915-bf9c-45e4-8afe-6738244e9191");

    public AbstractLatexCat(@NotNull Properties properties, @Nullable MemorizingSupplier<CustomHumanoidModel<LivingEntity>> modelSupplier) {
        super(properties, modelSupplier);
    }

    @Override
    public void onTransfur(LivingEntity entity) {
        super.onTransfur(entity);
        entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(speedBuff, "cat_speed_buff", .2, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    @Override
    public void onUnTransfur(LivingEntity entity) {
        super.onUnTransfur(entity);
        entity.getAttribute(Attributes.MOVEMENT_SPEED).removePermanentModifier(speedBuff);//TODO make non permanent?
    }

    public static class CatProperties extends Properties {

        protected CatProperties(ResourceLocation resourceLocation, Latex latex) {
            super(resourceLocation, latex);
            eyeHeight(1.75f,1.5f);
            airReductionModifier(1);
            swimSpeedModifier(-.5f);
        }

        public static @NotNull CatProperties of(ResourceLocation resourceLocation){
            return new CatProperties(resourceLocation, Latex.WHITE);
        }

        public static @NotNull CatProperties of(ResourceLocation resourceLocation, Latex latex){
            return new CatProperties(resourceLocation, latex);
        }
    }
}