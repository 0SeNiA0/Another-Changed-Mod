package net.zaharenko424.a_changed.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.*;

public class TransfurCapability {

    public static final ResourceLocation KEY = AChanged.resourceLoc("transfur_capability");
    public static final EntityCapability<ITransfurHandler, Void> CAPABILITY = EntityCapability.createVoid(KEY, ITransfurHandler.class);
    public static final Supplier<RuntimeException> NO_CAPABILITY_EXC = ()-> new RuntimeException("Transfur capability was expected but not found!");

    public static @Nullable ITransfurHandler of(@NotNull LivingEntity entity){
        return entity.getCapability(CAPABILITY);
    }

    public static @NotNull ITransfurHandler nonNullOf(@NotNull LivingEntity entity){
        return Utils.nonNullOrThrow(entity.getCapability(CAPABILITY), NO_CAPABILITY_EXC.get());
    }

    public static class TransfurHandler implements ITransfurHandler {

        private final LivingEntity holder;

        float transfurProgress = 0;
        TransfurType transfurType = null;
        boolean isTransfurred = false;

        static final int ticksUntilTFProgressDecrease = 200;
        static final int ticksBetweenTFProgressDecrease = 20;
        int i0 = 0;

        //Server only data
        boolean isBeingTransfurred = false;
        static final int ticksUntilDeathTF = 400;
        int beingTransfurredTimer;

        public TransfurHandler(IAttachmentHolder holder){
            if(!(holder instanceof LivingEntity living) || !living.getType().is(AChanged.TRANSFURRABLE_TAG))
                throw new IllegalStateException("Tried to create TransfurHandler for unsupported holder: " + holder);
            this.holder = living;
        }

        @Override
        public float getTransfurProgress() {
            return transfurProgress;
        }

        @Override
        public void setTransfurProgress(float amount, @NotNull TransfurType transfurType) {
            i0 = ticksUntilTFProgressDecrease;
            transfurProgress = amount;
            this.transfurType = transfurType;

        }

        @Override
        public @Nullable TransfurType getTransfurType() {
            return transfurType != null ? transfurType : null;
        }

        @Override
        public void setTransfurType(@NotNull TransfurType transfurType) {
            this.transfurType = transfurType;
        }

        @Override
        public boolean isTransfurred() {
            return isTransfurred && transfurType != null;
        }

        @Override
        public void transfur(@NotNull TransfurType transfurType) {
            transfurProgress = TRANSFUR_TOLERANCE;
            this.transfurType = transfurType;
            isTransfurred = true;
            setBeingTransfurred(false);
            AttributeMap map = holder.getAttributes();
            removeModifiers(map);
            addModifiers(map);
        }

        @Override
        public void unTransfur() {
            transfurProgress = 0;
            setBeingTransfurred(false);
            isTransfurred = false;
            if(!holder.level().isClientSide && transfurType != null) removeModifiers(holder.getAttributes());
            transfurType = null;
        }

        private void addModifiers(AttributeMap map){
            AttributeInstance[] instance = new AttributeInstance[1];
            transfurType.modifiers.asMap().forEach((attribute, modifiers) -> {
                if(!map.hasAttribute(attribute)){
                    AChanged.LOGGER.error("Attempted to add transfur modifier to not existing attribute {} {}", attribute, transfurType);
                    return;
                }

                if(attribute == Attributes.MAX_HEALTH){
                    float maxHealthO = holder.getMaxHealth();
                    instance[0] = map.getInstance(attribute);
                    for(AttributeModifier modifier : modifiers){
                        instance[0].addTransientModifier(modifier);
                    }
                    float diff = holder.getMaxHealth() - maxHealthO;
                    if(diff > 0) holder.setHealth(holder.getHealth() + diff);
                    return;
                }

                instance[0] = map.getInstance(attribute);
                for(AttributeModifier modifier : modifiers){
                    instance[0].addTransientModifier(modifier);
                }
            });
        }

        private void removeModifiers(@NotNull AttributeMap map){
            AttributeInstance[] instance = new AttributeInstance[1];
            transfurType.modifiers.asMap().forEach((attribute, modifiers) -> {
                if(!map.hasAttribute(attribute)) return;

                if(attribute == Attributes.MAX_HEALTH){
                    float maxHealthO = holder.getMaxHealth();
                    instance[0] = map.getInstance(attribute);
                    for(AttributeModifier modifier : modifiers){
                        instance[0].removeModifier(modifier.getId());
                    }
                    if(holder.getMaxHealth() - maxHealthO < 0) holder.setHealth(holder.getMaxHealth());
                    return;
                }
                instance[0] = map.getInstance(attribute);
                for(AttributeModifier modifier : modifiers){
                    instance[0].removeModifier(modifier.getId());
                }
            });
        }

        @Override
        public boolean isBeingTransfurred() {
            return isBeingTransfurred;
        }

        @Override
        public void setBeingTransfurred(boolean isBeingTransfurred) {
            if(!this.isBeingTransfurred && isBeingTransfurred) beingTransfurredTimer = ticksUntilDeathTF;
            this.isBeingTransfurred = isBeingTransfurred;
            if(!isBeingTransfurred) beingTransfurredTimer = 0;
        }

        @Override
        public TransfurHandler load(@NotNull CompoundTag tag) {
            transfurProgress = tag.getFloat(TRANSFUR_PROGRESS_KEY);
            if(tag.contains(TRANSFUR_TYPE_KEY)) transfurType = TransfurManager.getTransfurType(new ResourceLocation(tag.getString(TRANSFUR_TYPE_KEY)));
            isTransfurred = tag.getBoolean(TRANSFURRED_KEY);
            if(holder instanceof Player) setBeingTransfurred(tag.getBoolean(BEING_TRANSFURRED_KEY));
            if(isTransfurred()) addModifiers(holder.getAttributes());
            return this;
        }

        @Override
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat(TRANSFUR_PROGRESS_KEY, transfurProgress);
            if(transfurType != null) tag.putString(TRANSFUR_TYPE_KEY, transfurType.id.toString());
            tag.putBoolean(TRANSFURRED_KEY, isTransfurred);
            if(holder instanceof Player) tag.putBoolean(BEING_TRANSFURRED_KEY, isBeingTransfurred);
            return tag;
        }

        @Override
        public void tick() {
            if(isTransfurred() || transfurProgress <= 0) return;

            if(isBeingTransfurred){
                if(beingTransfurredTimer > 0){
                    beingTransfurredTimer--;
                } else TransfurEvent.TRANSFUR_DEATH.accept(holder, transfurType);
                return;
            }

            if(i0 > 0) {
                i0--;
                return;
            }
            if(holder.tickCount % ticksBetweenTFProgressDecrease != 0) return;
            transfurProgress = Math.max(0, transfurProgress - 1);
            if(holder instanceof ServerPlayer player) TransfurEvent.updatePlayer(player);
        }
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, TransfurHandler> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer(){}

        @Override
        public @NotNull TransfurHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag) {
            return new TransfurHandler(holder).load(tag);
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransfurHandler attachment) {
            return attachment.save();
        }
    }
}