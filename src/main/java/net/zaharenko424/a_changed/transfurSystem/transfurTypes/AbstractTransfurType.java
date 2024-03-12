package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.cmrs.model.CustomEntityModel;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractTransfurType {

    public final ResourceLocation id;

    protected final float eyeHeightStanding;
    protected final float eyeHeightCrouching;
    protected final float eyeHeightSwimming;
    protected final ImmutableMap<Pose, EntityDimensions> dimensions;
    public final float airReductionModifier;
    public final int maxHealthModifier;
    public final float swimSpeedModifier;
    protected final Gender gender;
    protected final boolean organic;
    protected final Consumer<LivingEntity> onTransfur;
    protected final Consumer<LivingEntity> onUnTransfur;

    public AbstractTransfurType(@NotNull Properties properties){
        id = properties.location;
        eyeHeightStanding = properties.eyeHeightStanding;
        eyeHeightCrouching = properties.eyeHeightCrouching;
        eyeHeightSwimming = properties.eyeHeightSwimming;
        dimensions = ImmutableMap.copyOf(properties.dimensions);
        airReductionModifier = properties.airReductionModifier;
        maxHealthModifier = properties.maxHealthModifier;
        swimSpeedModifier = properties.swimSpeedModifier;
        gender = properties.gender;
        organic = properties.organic;
        onTransfur = properties.onTransfur;
        onUnTransfur = properties.onUnTransfur;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract <E extends LivingEntity> CustomEntityModel<E> getModel(int modelVariant);

    public float getEyeHeight(@NotNull Pose pose){
        return switch (pose) {
            case SWIMMING, FALL_FLYING, SPIN_ATTACK -> eyeHeightSwimming;
            case CROUCHING -> eyeHeightCrouching;
            default -> eyeHeightStanding;
        };
    }

    public EntityDimensions getPoseDimensions(Pose pose){
        if(dimensions == null || dimensions.isEmpty() || !dimensions.containsKey(pose)) return EntityDimensions.scalable(.6f, 2);
        return dimensions.get(pose);
    }

    public Gender getGender(){
        return gender;
    }

    public boolean isOrganic(){
        return organic;
    }

    public void onTransfur(LivingEntity entity){
        if(onTransfur != null) onTransfur.accept(entity);
    }

    public void onUnTransfur(LivingEntity entity){
        if(onUnTransfur != null) onUnTransfur.accept(entity);
    }

    public Component fancyName(){
        return Component.translatable("transfur."+ id.toLanguageKey());
    }

    public static class Properties{
        ResourceLocation location;
        float eyeHeightStanding = 1.62f;
        float eyeHeightCrouching = 1.27f;
        float eyeHeightSwimming = .4f;
        Map<Pose, EntityDimensions> dimensions = new HashMap<>(Map.of(
                Pose.STANDING, EntityDimensions.scalable(.6f,1.9f),
                Pose.SLEEPING, EntityDimensions.fixed(0.2f, 0.2f),
                Pose.FALL_FLYING, EntityDimensions.scalable(0.6f, 0.6f),
                Pose.SWIMMING, EntityDimensions.scalable(0.6f, 0.6f),
                Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6f, 0.6f),
                Pose.CROUCHING, EntityDimensions.scalable(0.6f, 1.6f),
                Pose.DYING, EntityDimensions.scalable(.2f,.2f)
        ));
        float airReductionModifier = 0;
        int maxHealthModifier = 0;
        float swimSpeedModifier = 0;
        boolean male = true;
        Gender gender = Gender.NONE;
        boolean organic = false;
        Consumer<LivingEntity> onTransfur;
        Consumer<LivingEntity> onUnTransfur;

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

        public Properties poseSize(Pose pose, EntityDimensions size){
            dimensions.put(pose, size);
            return this;
        }

        public Properties poseSize(Map<Pose, EntityDimensions> map){
            dimensions.putAll(map);
            return this;
        }

        public Properties airReductionModifier(float airReductionModifier){
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

        public Properties gender(Gender gender){
            this.gender = gender;
            return this;
        }

        public Properties organic(boolean organic){
            this.organic=organic;
            return this;
        }

        /**
         * Executed right after player was transfurred & in latex beast constructor
         */
        public Properties onTransfur(Consumer<LivingEntity> onTransfur){
            this.onTransfur = onTransfur;
            return this;
        }

        /**
         * Executed right before player is untransfurred
         */
        public Properties onUnTransfur(Consumer<LivingEntity> onUnTransfur){
            this.onUnTransfur = onUnTransfur;
            return this;
        }
    }
}