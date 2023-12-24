package net.zaharenko424.a_changed.transfurTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTransfurType {

    public final ResourceLocation id;

    protected final float eyeHeightStanding;
    protected float eyeHeightCrouching;
    protected float eyeHeightSwimming;
    protected boolean male;
    protected boolean organic;

    public AbstractTransfurType(@NotNull Properties properties){
        id = properties.location;
        eyeHeightStanding = properties.eyeHeightStanding;
        eyeHeightCrouching = properties.eyeHeightCrouching;
        eyeHeightSwimming = properties.eyeHeightSwimming;
        male = properties.male;
        organic = properties.organic;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract <E extends LivingEntity> net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel<E> getModel();

    @OnlyIn(Dist.CLIENT)
    public abstract <E extends LivingEntity> net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel<E> getArmorModel();

    public float getEyeHeight(@NotNull Pose pose){
        return switch (pose) {
            case SWIMMING, FALL_FLYING, SPIN_ATTACK -> eyeHeightSwimming;
            case CROUCHING -> eyeHeightCrouching;
            default -> eyeHeightStanding;
        };
    }

    public boolean isOrganic(){
        return organic;
    }

    public Component fancyName(){
        return Component.translatable("transfur."+ id.toString().replace(':','.'));
    }

    public static class Properties{
        ResourceLocation location;
        float eyeHeightStanding=1.62f;
        float eyeHeightCrouching=1.27f;
        float eyeHeightSwimming=.4f;
        boolean male=true;
        boolean organic=false;

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

        public Properties organic(boolean organic){
            this.organic=organic;
            return this;
        }
    }
}