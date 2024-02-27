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
import net.neoforged.neoforge.common.NeoForgeMod;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
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

        private final LivingEntity entity;
        static final UUID airDecreaseSpeed = UUID.fromString("3425eeff-ee2d-44c9-91f5-67044b84baa0");
        static final UUID healthModifier = UUID.fromString("ecc275cc-dc18-4792-bca2-0adf7f331bbc");
        static final UUID swimSpeed = UUID.fromString("577c604f-686a-4224-b9f6-e619c5f2ee06");

        float transfurProgress = 0;
        AbstractTransfurType transfurType = null;
        boolean isTransfurred = false;

        static final int ticksUntilTFProgressDecrease = 200;
        static final int ticksBetweenTFProgressDecrease = 20;
        int i0 = 0;

        boolean isBeingTransfurred = false;
        static final int ticksUntilDeathTF = 400;
        int beingTransfurredTimer;

        public TransfurHandler(IAttachmentHolder holder){
            if(!(holder instanceof LivingEntity living) || !living.getType().is(AChanged.TRANSFURRABLE_TAG))
                throw new IllegalStateException("Tried to create TransfurHandler for unsupported holder: " + holder);
            this.entity = living;
        }

        @Override
        public float getTransfurProgress() {
            return transfurProgress;
        }

        @Override
        public void setTransfurProgress(float amount, @NotNull AbstractTransfurType transfurType) {
            i0 = ticksUntilTFProgressDecrease;
            transfurProgress = amount;
            this.transfurType = transfurType;

        }

        @Override
        public @Nullable AbstractTransfurType getTransfurType() {
            return transfurType != null ? transfurType : null;
        }

        @Override
        public void setTransfurType(@NotNull AbstractTransfurType transfurType) {
            this.transfurType = transfurType;
        }

        @Override
        public boolean isTransfurred() {
            return isTransfurred && transfurType != null;
        }

        @Override
        public void transfur(@NotNull AbstractTransfurType transfurType) {
            transfurProgress = TRANSFUR_TOLERANCE;
            this.transfurType = transfurType;
            isTransfurred = true;
            setBeingTransfurred(false);
            AttributeMap map = entity.getAttributes();
            removeModifiers(map);
            addModifiers(map);
        }

        @Override
        public void unTransfur() {
            transfurProgress = 0;
            transfurType = null;
            setBeingTransfurred(false);
            isTransfurred = false;
            if(!entity.level().isClientSide) removeModifiers(entity.getAttributes());
        }

        private void addModifiers(AttributeMap map){
            float f = transfurType.airReductionModifier;
            int i = transfurType.maxHealthModifier;
            if(f != 0) map.getInstance(AChanged.AIR_DECREASE_SPEED).addTransientModifier(new AttributeModifier(airDecreaseSpeed,"a", f, AttributeModifier.Operation.ADDITION));
            if(i != 0) {
                map.getInstance(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(healthModifier, "a", i, AttributeModifier.Operation.ADDITION));
                if(i > 0) entity.setHealth(entity.getHealth() + i);
            }
            f = transfurType.swimSpeedModifier;
            if(f != 0) map.getInstance(NeoForgeMod.SWIM_SPEED).addTransientModifier(new AttributeModifier(swimSpeed,"a", f, AttributeModifier.Operation.ADDITION));
        }

        private void removeModifiers(@NotNull AttributeMap map){
            map.getInstance(AChanged.AIR_DECREASE_SPEED).removeModifier(airDecreaseSpeed);

            AttributeInstance instance = map.getInstance(Attributes.MAX_HEALTH);
            if(instance.getModifier(healthModifier) != null) {
                float modifier = (float) instance.getModifier(healthModifier).getAmount();
                float max = entity.getMaxHealth();
                if (modifier > 0 && entity.getHealth() > max - modifier) entity.setHealth(max - modifier);
                instance.removeModifier(healthModifier);
            }

            map.getInstance(NeoForgeMod.SWIM_SPEED).removeModifier(swimSpeed);
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
            if(entity instanceof Player) setBeingTransfurred(tag.getBoolean(BEING_TRANSFURRED_KEY));
            if(isTransfurred()) addModifiers(entity.getAttributes());
            return this;
        }

        @Override
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat(TRANSFUR_PROGRESS_KEY, transfurProgress);
            if(transfurType != null) tag.putString(TRANSFUR_TYPE_KEY, transfurType.id.toString());
            tag.putBoolean(TRANSFURRED_KEY, isTransfurred);
            if(entity instanceof Player) tag.putBoolean(BEING_TRANSFURRED_KEY, isBeingTransfurred);
            return tag;
        }

        @Override
        public void tick() {
            if(isTransfurred() || transfurProgress <= 0) return;

            if(isBeingTransfurred){
                if(beingTransfurredTimer > 0){
                    beingTransfurredTimer--;
                } else TransfurEvent.TRANSFUR_DEATH.accept(entity, transfurType);
                return;
            }

            if(i0 > 0) {
                i0--;
                return;
            }
            if(entity.tickCount % ticksBetweenTFProgressDecrease != 0) return;
            transfurProgress--;
            if(entity instanceof ServerPlayer player) TransfurEvent.updatePlayer(player);
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