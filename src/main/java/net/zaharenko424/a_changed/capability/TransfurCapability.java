package net.zaharenko424.a_changed.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.capabilities.*;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.common.util.NonNullSupplier;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.*;

public class TransfurCapability {

    public static final Capability<ITransfurHandler> CAPABILITY = CapabilityManager.get(new Token());
    public static final ResourceLocation KEY = new ResourceLocation(AChanged.MODID,"transfur_capability");
    public static final NonNullSupplier<RuntimeException> NO_CAPABILITY_EXC = () -> new RuntimeException("Transfur capability was expected but not found!");

    @Contract("_ -> new")
    public static @NotNull ICapabilityProvider createProvider(LivingEntity entity){
        return new Provider(entity);
    }

    static class Token extends CapabilityToken<ITransfurHandler>{}

    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        ITransfurHandler handler;
        LazyOptional<ITransfurHandler> optional;

        public Provider(LivingEntity entity){
            handler = new TransfurHandler(entity);
            optional = LazyOptional.of(() -> handler);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY.orEmpty(cap, optional);
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            handler.load(tag);
        }

        @Override
        public CompoundTag serializeNBT() {
            return handler.save();
        }
    }

    public static class TransfurHandler implements ITransfurHandler {

        final LivingEntity entity;
        static final UUID airDecreaseSpeed = UUID.fromString("3425eeff-ee2d-44c9-91f5-67044b84baa0");
        static final UUID healthModifier = UUID.fromString("ecc275cc-dc18-4792-bca2-0adf7f331bbc");
        static final UUID swimSpeed = UUID.fromString("577c604f-686a-4224-b9f6-e619c5f2ee06");

        float transfurProgress = 0;
        AbstractTransfurType transfurType = null;
        boolean isTransfurred = false;

        static final int ticksUntilTFProgressDecrease=200;
        static final int ticksBetweenTFProgressDecrease=20;
        int i0 = 0;

        boolean isBeingTransfurred = false;

        public TransfurHandler(LivingEntity entity){
            this.entity=entity;
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
            return transfurType!=null?transfurType:null;
        }

        @Override
        public void setTransfurType(@NotNull AbstractTransfurType transfurType) {
            this.transfurType=transfurType;
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
            isBeingTransfurred = false;
            AttributeMap map = entity.getAttributes();
            removeModifiers(map);
            addModifiers(map);
        }

        @Override
        public void unTransfur() {
            transfurProgress = 0;
            transfurType = null;
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
            this.isBeingTransfurred = isBeingTransfurred;
        }

        @Override
        public void load(CompoundTag tag) {
            transfurProgress = tag.getFloat(TRANSFUR_PROGRESS_KEY);
            if(tag.contains(TRANSFUR_TYPE_KEY)) transfurType = TransfurManager.getTransfurType(new ResourceLocation(tag.getString(TRANSFUR_TYPE_KEY)));
            isTransfurred = tag.getBoolean(TRANSFURRED_KEY);
            if(isTransfurred()) addModifiers(entity.getAttributes());
        }

        @Override
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat(TRANSFUR_PROGRESS_KEY, transfurProgress);
            if(transfurType != null) tag.putString(TRANSFUR_TYPE_KEY, transfurType.id.toString());
            tag.putBoolean(TRANSFURRED_KEY, isTransfurred);
            return tag;
        }

        @Override
        public void tick() {
            if(isTransfurred() || transfurProgress <= 0) return;
            if(i0 > 0) {
                i0--;
                return;
            }
            if(entity.tickCount % ticksBetweenTFProgressDecrease != 0) return;
            transfurProgress--;
            if(entity instanceof ServerPlayer player) TransfurEvent.updatePlayer(player);
        }
    }
}