package net.zaharenko424.a_changed.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.capabilities.*;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.common.util.NonNullSupplier;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.TransfurManager;
import net.zaharenko424.a_changed.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.zaharenko424.a_changed.TransfurManager.*;

public class TransfurCapability {

    public static final Capability<ITransfurHandler> CAPABILITY = CapabilityManager.get(new Token());
    public static final ResourceLocation KEY = new ResourceLocation(AChanged.MODID,"transfur_capability");
    public static final NonNullSupplier<RuntimeException> NO_CAPABILITY_EXC = ()->new RuntimeException("Transfur capability was expected but not found!");

    @Contract("_ -> new")
    public static @NotNull ICapabilityProvider createProvider(LivingEntity entity){
        return new Provider(entity);
    }

    static class Token extends CapabilityToken<ITransfurHandler>{}

    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        ITransfurHandler handler;
        LazyOptional<ITransfurHandler> optional;

        public Provider(LivingEntity entity){
            handler=new TransfurHandler(entity);
            optional=LazyOptional.of(()->handler);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY.orEmpty(cap,optional);
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

        float transfurProgress=0;
        AbstractTransfurType transfurType=null;
        boolean isTransfurred=false;

        static final int ticksUntilTFProgressDecrease=200;
        static final int ticksBetweenTFProgressDecrease=20;
        int i0,i1 =0;

        public TransfurHandler(LivingEntity entity){
            this.entity=entity;
        }

        @Override
        public float getTransfurProgress() {
            return transfurProgress;
        }

        @Override
        public void setTransfurProgress(float amount, @NotNull AbstractTransfurType transfurType) {
            i0=ticksUntilTFProgressDecrease;
            transfurProgress=amount;
            this.transfurType=transfurType;
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
            return isTransfurred;
        }

        @Override
        public void transfur(@NotNull AbstractTransfurType transfurType) {
            transfurProgress=20;
            this.transfurType=transfurType;
            isTransfurred=true;
        }

        @Override
        public void unTransfur() {
            transfurProgress=0;
            transfurType=null;
            isTransfurred=false;
        }

        @Override
        public void load(@NotNull CompoundTag tag) {
            transfurProgress=tag.getFloat(TRANSFUR_PROGRESS_KEY);
            if(tag.contains(TRANSFUR_TYPE_KEY)) transfurType=TransfurManager.getTransfurType(new ResourceLocation(tag.getString(TRANSFUR_TYPE_KEY)));
            isTransfurred=tag.getBoolean(TRANSFURRED_KEY);
        }

        @Override
        public CompoundTag save() {
            CompoundTag tag=new CompoundTag();
            tag.putFloat(TRANSFUR_PROGRESS_KEY,transfurProgress);
            if(transfurType!=null) tag.putString(TRANSFUR_TYPE_KEY,transfurType.id.toString());
            tag.putBoolean(TRANSFURRED_KEY,isTransfurred);
            return tag;
        }

        @Override
        public void tick() {
            if(isTransfurred) return;
            if(i0>0) {
                i0--;
                return;
            }
            if(i1>0){
                i1--;
                return;
            }
            i1=ticksBetweenTFProgressDecrease;
            transfurProgress--;
            if(entity instanceof ServerPlayer player) TransfurManager.updatePlayer(player);
        }
    }
}
