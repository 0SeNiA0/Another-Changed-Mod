package net.zaharenko424.a_changed.transfurSystem.transfurType;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.transfurSystem.Latex;
import net.zaharenko424.a_changed.transfurSystem.LatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.cmrs.client.CustomModelManager;
import net.zaharenko424.cmrs.client.model.UniversalCustomModel;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class TransfurType <T extends LivingEntity & LatexBeast> {

    public final ResourceLocation id;
    public final Latex latex;

    protected final Supplier<EntityType<T>> entityType;
    protected final int primaryColor;
    protected final int secondaryColor;
    protected final Map<Pose, EntityDimensions> dimensions;
    public final ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers;
    protected final Gender gender;
    protected final boolean organic;
    protected final Consumer<LivingEntity> onTransfur;
    protected final Consumer<LivingEntity> onUnTransfur;
    /**
     * Unmodifiable!
     */
    public final List<? extends Ability> abilities;

    public TransfurType(@NotNull Properties<T> properties){
        id = properties.location;
        entityType = properties.entityType;
        latex = properties.latex;
        primaryColor = properties.primaryColor;
        secondaryColor = properties.secondaryColor;
        dimensions = Map.copyOf(properties.dimensions);
        modifiers = ImmutableMultimap.copyOf(properties.modifiers);
        gender = properties.gender;
        organic = properties.organic;
        onTransfur = properties.onTransfur;
        onUnTransfur = properties.onUnTransfur;

        abilities = properties.abilities.stream().map(DeferredHolder::get).sorted((ability1, ability2) -> {
            boolean active0 = ability1.isSelectable();
            boolean active1 = ability2.isSelectable();
            if(active0 == active1) return 0;
            return active0 ? -1 : 1;
        }).toList();
    }

    /**
     * Called during FMLClientSetup to register builtIn model suppliers for CustomModelManager
     */
    public abstract void registerModels(@NotNull RegisterBuiltInModelsEvent event);

    public UniversalCustomModel<LivingEntity> getDefaultModel(){
        return CustomModelManager.getInstance().getModel(id);
    }

    /**
     * @param entity Transfurred entity.
     * @return ID of the model to render(must be registered to CustomModelManager)
     */
    public @Nullable ResourceLocation getModelIdFor(@NotNull LivingEntity entity){
        return id;
    }

    public @Nullable UniversalCustomModel<LivingEntity> getModelFor(@NotNull LivingEntity entity){
        ResourceLocation modelId = getModelIdFor(entity);
        if(modelId == null) return null;
        return CustomModelManager.getInstance().getModel(modelId);
    }

    public EntityType<T> getEntityType(){
        return entityType.get();
    }

    public int getPrimaryColor(){
        return primaryColor;
    }

    public int getSecondaryColor(){
        return secondaryColor;
    }

    public @Nullable EntityDimensions getPoseDimensions(@NotNull LivingEntity entity, @NotNull Pose pose){
        if(dimensions.isEmpty() || !dimensions.containsKey(pose)) return null;
        return dimensions.get(pose).scale(entity.getAgeScale());
    }

    public Gender getGender(){
        return gender;
    }

    public boolean isOrganic(){
        return organic;
    }

    public void onTransfur(@NotNull LivingEntity entity){
        if(onTransfur != null) onTransfur.accept(entity);
    }

    public void onUnTransfur(@NotNull LivingEntity entity){
        if(onUnTransfur != null) onUnTransfur.accept(entity);
    }

    public Component fancyName(){
        return Component.translatable("transfur."+ id.toLanguageKey());
    }

    @Override
    public String toString() {
        return "TransfurType: " + id.toString();
    }

    public static class Properties <T extends LivingEntity & LatexBeast> {

        protected final ResourceLocation location;
        protected final Latex latex;
        protected final Supplier<EntityType<T>> entityType;
        protected int primaryColor = -1644826;
        protected int secondaryColor = -4934476;
        protected final Map<Pose, EntityDimensions> dimensions = new HashMap<>(Map.of(
                Pose.STANDING, EntityDimensions.scalable(.6f,1.9f)
                        .withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, Player.DEFAULT_VEHICLE_ATTACHMENT)),
                Pose.SLEEPING, EntityDimensions.fixed(0.2f, 0.2f),
                Pose.FALL_FLYING, EntityDimensions.scalable(0.6f, 0.6f).withEyeHeight(.4f),
                Pose.SWIMMING, EntityDimensions.scalable(0.6f, 0.6f).withEyeHeight(.4f),
                Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6f, 0.6f).withEyeHeight(.4f),
                Pose.CROUCHING, EntityDimensions.scalable(0.6f, 1.6f)
                        .withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, Player.DEFAULT_VEHICLE_ATTACHMENT)),
                Pose.DYING, EntityDimensions.scalable(.2f,.2f)
        ));
        protected final Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();
        protected Gender gender = Gender.NONE;
        protected boolean organic = false;
        protected Consumer<LivingEntity> onTransfur;
        protected Consumer<LivingEntity> onUnTransfur;
        protected final List<DeferredHolder<Ability, ? extends Ability>> abilities = new ArrayList<>(TransfurManager.MAX_ABILITIES);

        protected Properties(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType, Latex latex){
            location = resourceLocation;
            this.entityType = entityType;
            this.latex = latex;
        }

        public static <T extends LivingEntity & LatexBeast> @NotNull Properties<T> of(ResourceLocation resourceLocation, Supplier<EntityType<T>> entity, Latex latex){
            return new TransfurType.Properties<>(resourceLocation, entity, latex);
        }

        /**
         * Color in ARGB
         */
        public Properties<T> colors(int primary, int secondary){
            primaryColor = primary;
            secondaryColor = secondary;
            return this;
        }

        public Properties<T> poseSize(Pose pose, EntityDimensions size){
            dimensions.put(pose, size);
            return this;
        }

        public Properties<T> poseSize(Map<Pose, EntityDimensions> map){
            dimensions.putAll(map);
            return this;
        }

        /**
         * @param attribute attribute to add modifier to.
         * @param name name of the modifier. Must be unique!
         * @param amount amount.
         * @param op modifier operation.
         */
        public Properties<T> addModifier(Holder<Attribute> attribute, String name, double amount, AttributeModifier.Operation op){
            return addModifier(attribute, new AttributeModifier(AChanged.resourceLoc(name), amount, op));
        }

        /**
         * @param attribute attribute to add modifier to.
         * @param id ResourceLocation of modifier. Must be unique!
         * @param amount amount.
         * @param op modifier operation.
         */
        public Properties<T> addModifier(Holder<Attribute> attribute, ResourceLocation id, double amount, AttributeModifier.Operation op){
            return addModifier(attribute, new AttributeModifier(id, amount, op));
        }

        /**
         * Make sure that modifier uuid stays the same during single session.
         * @param attribute attribute to add modifier to.
         * @param modifier modifier to add.
         */
        public Properties<T> addModifier(Holder<Attribute> attribute, AttributeModifier modifier){
            modifiers.put(attribute, modifier);
            return this;
        }

        /**
         * Zero (default) -> no changes to minecraft logic. Operation - Addition.<p>modifier > 0 -> faster air depletion. <p>-1 < modifier < 0 -> slower depletion. <p>modifier <= -1 -> no depletion.
         */
        public Properties<T> airReductionModifier(float airReductionModifier){
            return addModifier(AChanged.AIR_DECREASE_SPEED, "latex_air_decrease_speed_mod", airReductionModifier, AttributeModifier.Operation.ADD_VALUE);
        }

        /**
         * Operation - Addition
         */
        public Properties<T> maxHealthModifier(int maxHealthModifier){
            return addModifier(Attributes.MAX_HEALTH, "latex_health_mod", maxHealthModifier, AttributeModifier.Operation.ADD_VALUE);
        }

        /**
         * Higher modifier -> higher swim speed. <p> Operation - Multiply Total.
         */
        public Properties<T> swimSpeedModifier(float swimSpeedModifier){
            return addModifier(NeoForgeMod.SWIM_SPEED, "latex_swim_speed_mod", swimSpeedModifier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }

        public Properties<T> gender(Gender gender){
            this.gender = gender;
            return this;
        }

        public Properties<T> organic(boolean organic){
            this.organic = organic;
            return this;
        }

        /**
         * Executed right after player was transfurred & in latex beast constructor. <p>Called serverside only.</p>
         */
        public Properties<T> onTransfur(Consumer<LivingEntity> onTransfur){
            this.onTransfur = onTransfur;
            return this;
        }

        /**
         * Executed right before player is untransfurred. Transfur, when already transfurred, will trigger this too. <p>Called serverside only.</p>
         */
        public Properties<T> onUnTransfur(Consumer<LivingEntity> onUnTransfur){
            this.onUnTransfur = onUnTransfur;
            return this;
        }

        public int addedAbilities(){
            return abilities.size();
        }

        public Properties<T> addAbility(DeferredHolder<Ability, ? extends Ability> ability){
            if(abilities.size() < TransfurManager.MAX_ABILITIES && !abilities.contains(ability)) abilities.add(ability);
            return this;
        }

        public Properties<T> addAbilityBefore(DeferredHolder<Ability, ? extends Ability> before, DeferredHolder<Ability, ? extends Ability> ability){
            if(abilities.size() == TransfurManager.MAX_ABILITIES || abilities.contains(ability)) return this;
            int index = abilities.indexOf(before);
            if(index < 1) index = 0; else index--;
            abilities.add(index, ability);
            return this;
        }

        public Properties<T> addAbilityAfter(DeferredHolder<Ability, ? extends Ability> after, DeferredHolder<Ability, ? extends Ability> ability){
            if(abilities.size() == TransfurManager.MAX_ABILITIES || abilities.contains(ability)) return this;
            int index = abilities.indexOf(after) + 1;
            abilities.add(index, ability);
            return this;
        }

        public Properties<T> removeAbility(DeferredHolder<Ability, ? extends Ability> ability){
            abilities.remove(ability);
            return this;
        }
    }
}