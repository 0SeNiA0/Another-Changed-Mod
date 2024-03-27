package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class AbstractLatexCat extends AbstractTransfurType {

    private static final UUID speedBuff = UUID.fromString("1c73d915-bf9c-45e4-8afe-6738244e9191");

    public AbstractLatexCat(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public void onTransfur(LivingEntity entity) {
        super.onTransfur(entity);
        entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(speedBuff, "cat_speed_buff", .2, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    @Override
    public void onUnTransfur(LivingEntity entity) {
        super.onUnTransfur(entity);
        entity.getAttribute(Attributes.MOVEMENT_SPEED).removePermanentModifier(speedBuff);
    }

    public static class CatProperties extends Properties {

        protected CatProperties(ResourceLocation resourceLocation) {
            super(resourceLocation);
            eyeHeight(1.75f,1.5f);
            airReductionModifier(1);
            swimSpeedModifier(-.5f);
        }

        public static @NotNull CatProperties of(ResourceLocation resourceLocation){
            return new CatProperties(resourceLocation);
        }
    }
}