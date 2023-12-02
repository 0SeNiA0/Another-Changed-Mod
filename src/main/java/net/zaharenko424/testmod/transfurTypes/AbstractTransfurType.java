package net.zaharenko424.testmod.transfurTypes;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.testmod.client.model.AbstractLatexEntityModel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTransfurType {

    protected final AbstractTransfurType.Properties properties;
    public final ResourceLocation location;

    public AbstractTransfurType(@NotNull Properties properties){
        this.properties=properties;
        location=properties.location;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract <T extends LivingEntity> AbstractLatexEntityModel<T> getModel(EntityRendererProvider.@NotNull Context context);
    @OnlyIn(Dist.CLIENT)
    public abstract <T extends LivingEntity> AbstractLatexEntityModel<T> getArmorModel(EntityRendererProvider.@NotNull Context context);

    public float getEyeHeight(@NotNull Pose pose){
        return switch (pose) {
            case SWIMMING, FALL_FLYING, SPIN_ATTACK -> properties.eyeHeightSwimming;
            case CROUCHING -> properties.eyeHeightCrouching;
            default -> properties.eyeHeightStanding;
        };
    }

    public Component fancyName(){
        return Component.translatable("transfur."+ properties.location.toString().replace(':','.'));
    }

    public static class Properties{
        final ResourceLocation location;
        float eyeHeightStanding=1.62f;
        float eyeHeightCrouching=1.27f;
        float eyeHeightSwimming=.4f;
        boolean male=true;

        protected Properties(ResourceLocation resourceLocation){
            location=resourceLocation;
        }

        @Contract(value = "_ -> new", pure = true)
        public static @NotNull Properties of(ResourceLocation resourceLocation){
            return new AbstractTransfurType.Properties(resourceLocation);
        }

        public Properties eyeHeight(float standing){
            eyeHeightStanding=standing;
            return this;
        }

        public Properties eyeHeight(float standing, float crouching){
            eyeHeightStanding=standing;
            eyeHeightCrouching=crouching;
            return this;
        }

        public Properties eyeHeight(float standing,float crouching, float swimming){
            eyeHeightStanding=standing;
            eyeHeightCrouching=crouching;
            eyeHeightSwimming=swimming;
            return this;
        }

        public Properties gender(boolean male){
            this.male=male;
            return this;
        }
    }
}