package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTransfurType {

    public final ResourceLocation id;

    protected final float eyeHeightStanding;
    protected final float eyeHeightCrouching;
    protected final float eyeHeightSwimming;
    public final float airReductionModifier;
    public final int maxHealthModifier;
    public final float swimSpeedModifier;
    protected final boolean male;
    protected final boolean organic;

    public AbstractTransfurType(@NotNull Properties properties){
        id = properties.location;
        eyeHeightStanding = properties.eyeHeightStanding;
        eyeHeightCrouching = properties.eyeHeightCrouching;
        eyeHeightSwimming = properties.eyeHeightSwimming;
        airReductionModifier = properties.airReductionModifier;
        maxHealthModifier = properties.maxHealthModifier;
        swimSpeedModifier = properties.swimSpeedModifier;
        male = properties.male;
        organic = properties.organic;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract <E extends LivingEntity> AbstractLatexEntityModel<E> getModel();

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
        float eyeHeightStanding = 1.62f;
        float eyeHeightCrouching = 1.27f;
        float eyeHeightSwimming = .4f;
        float airReductionModifier = 0;
        int maxHealthModifier = 0;
        float swimSpeedModifier = 0;
        boolean male = true;
        boolean organic = false;

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

        public Properties airReductionModifier(int airReductionModifier){
            this.airReductionModifier = airReductionModifier;
            return this;
        }

        public Properties maxHealthModifier(int maxHealthModifier){
            this.maxHealthModifier = maxHealthModifier;
            return this;
        }

        public Properties swimSpeedModifier(float swimSpeedModifier){
            this.swimSpeedModifier = swimSpeedModifier;
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