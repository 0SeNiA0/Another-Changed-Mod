package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import com.google.common.collect.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.util.Latex;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class TransfurType {

    public final ResourceLocation id;
    public final Latex latex;

    protected final int primaryColor;
    protected final int secondaryColor;
    protected final float eyeHeightStanding;
    protected final float eyeHeightCrouching;
    protected final float eyeHeightSwimming;
    protected final ImmutableMap<Pose, EntityDimensions> dimensions;
    public final ImmutableMultimap<Attribute, AttributeModifier> modifiers;
    protected final Gender gender;
    protected final boolean organic;
    protected final Consumer<LivingEntity> onTransfur;
    protected final Consumer<LivingEntity> onUnTransfur;

    public TransfurType(@NotNull Properties properties, @Nullable MemorizingSupplier<CustomHumanoidModel<LivingEntity>> modelSupplier){
        id = properties.location;
        latex = properties.latex;
        primaryColor = properties.primaryColor;
        secondaryColor = properties.secondaryColor;
        eyeHeightStanding = properties.eyeHeightStanding;
        eyeHeightCrouching = properties.eyeHeightCrouching;
        eyeHeightSwimming = properties.eyeHeightSwimming;
        dimensions = ImmutableMap.copyOf(properties.dimensions);
        modifiers = ImmutableMultimap.copyOf(properties.modifiers);
        gender = properties.gender;
        organic = properties.organic;
        onTransfur = properties.onTransfur;
        onUnTransfur = properties.onUnTransfur;
        this.modelSupplier = modelSupplier;
    }

    public final Supplier<CustomHumanoidModel<LivingEntity>> modelSupplier;

    public final CustomHumanoidModel<LivingEntity> getModel(){
        return modelSupplier.get();
    }

    public int getPrimaryColor(){
        return primaryColor;
    }

    public int getSecondaryColor(){
        return secondaryColor;
    }

    public float getEyeHeight(@NotNull Pose pose){
        return switch (pose) {
            case SWIMMING, FALL_FLYING, SPIN_ATTACK -> eyeHeightSwimming;
            case CROUCHING -> eyeHeightCrouching;
            default -> eyeHeightStanding;
        };
    }

    public EntityDimensions getPoseDimensions(Pose pose){
        if(dimensions.isEmpty() || !dimensions.containsKey(pose)) return EntityDimensions.scalable(.6f, 1.9f);
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

    @Override
    public String toString() {
        return id.toString();
    }

    public static class Properties {

        static final UUID airDecreaseSpeed = UUID.fromString("3425eeff-ee2d-44c9-91f5-67044b84baa0");
        static final UUID healthModifier = UUID.fromString("ecc275cc-dc18-4792-bca2-0adf7f331bbc");
        static final UUID swimSpeed = UUID.fromString("577c604f-686a-4224-b9f6-e619c5f2ee06");

        protected ResourceLocation location;
        protected Latex latex;
        protected int primaryColor = -1644826;
        protected int secondaryColor = -4934476;
        protected float eyeHeightStanding = 1.62f;
        protected float eyeHeightCrouching = 1.27f;
        protected float eyeHeightSwimming = .4f;
        protected Map<Pose, EntityDimensions> dimensions = new HashMap<>(Map.of(
                Pose.STANDING, EntityDimensions.scalable(.6f,1.9f),
                Pose.SLEEPING, EntityDimensions.fixed(0.2f, 0.2f),
                Pose.FALL_FLYING, EntityDimensions.scalable(0.6f, 0.6f),
                Pose.SWIMMING, EntityDimensions.scalable(0.6f, 0.6f),
                Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6f, 0.6f),
                Pose.CROUCHING, EntityDimensions.scalable(0.6f, 1.6f),
                Pose.DYING, EntityDimensions.scalable(.2f,.2f)
        ));
        protected Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        protected Gender gender = Gender.NONE;
        protected boolean organic = false;
        protected Consumer<LivingEntity> onTransfur;
        protected Consumer<LivingEntity> onUnTransfur;

        protected Properties(ResourceLocation resourceLocation, Latex latex){
            location = resourceLocation;
            this.latex = latex;
        }

        public static @NotNull Properties of(ResourceLocation resourceLocation, Latex latex){
            return new TransfurType.Properties(resourceLocation, latex);
        }

        /**
         * Color in ARGB
         */
        public Properties colors(int primary, int secondary){
            primaryColor = primary;
            secondaryColor = secondary;
            return this;
        }

        public Properties eyeHeight(float standing){
            eyeHeightStanding = standing;
            return this;
        }

        public Properties eyeHeight(float standing, float crouching){
            eyeHeightStanding = standing;
            eyeHeightCrouching = crouching;
            return this;
        }

        public Properties eyeHeight(float standing, float crouching, float swimming){
            eyeHeightStanding = standing;
            eyeHeightCrouching = crouching;
            eyeHeightSwimming = swimming;
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

        /**
         * @param attribute attribute to add modifier to.
         * @param name name of the modifier. Must be unique!
         * @param amount amount.
         * @param op modifier operation.
         */
        public Properties addModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation op){
            return addModifier(attribute, new AttributeModifier(UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8)), name, amount, op));
        }

        /**
         * @param attribute attribute to add modifier to.
         * @param uuid UUID of modifier. Must be unique!
         * @param name name of the modifier.
         * @param amount amount.
         * @param op modifier operation.
         */
        public Properties addModifier(Attribute attribute, UUID uuid, String name, double amount, AttributeModifier.Operation op){
            return addModifier(attribute, new AttributeModifier(uuid, name, amount, op));
        }

        /**
         * Make sure that modifier uuid stays the same during single session.
         * @param attribute attribute to add modifier to.
         * @param modifier modifier to add.
         */
        public Properties addModifier(Attribute attribute, AttributeModifier modifier){
            modifiers.put(attribute, modifier);
            return this;
        }

        /**
         * Zero (default) -> no changes to minecraft logic. Operation - Addition.<p>modifier > 0 -> faster air depletion. <p>-1 < modifier < 0 -> slower depletion. <p>modifier <= -1 -> no depletion.
         */
        public Properties airReductionModifier(float airReductionModifier){
            return addModifier(AChanged.AIR_DECREASE_SPEED.get(), airDecreaseSpeed, "a", airReductionModifier, AttributeModifier.Operation.ADDITION);
        }

        /**
         * Operation - Addition
         */
        public Properties maxHealthModifier(int maxHealthModifier){
            return addModifier(Attributes.MAX_HEALTH, healthModifier, "a", maxHealthModifier, AttributeModifier.Operation.ADDITION);
        }

        /**
         * Higher modifier -> higher swim speed. <p> Operation - Multiply Total.
         */
        public Properties swimSpeedModifier(float swimSpeedModifier){
            return addModifier(NeoForgeMod.SWIM_SPEED.value(), swimSpeed, "a", swimSpeedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }

        public Properties gender(Gender gender){
            this.gender = gender;
            return this;
        }

        public Properties organic(boolean organic){
            this.organic = organic;
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