package net.zaharenko424.testmod.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.entity.TransfurHolder;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import static net.zaharenko424.testmod.TransfurManager.*;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements TransfurHolder {
    public MixinServerPlayer(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Override
    public int mod$getTransfurProgress() {
        return getPersistentData().getInt(TRANSFUR_PROGRESS_KEY);
    }

    @Override
    public void mod$setTransfurProgress(int amount, @NotNull String transfurType) {
        CompoundTag tag=getPersistentData();
        tag.putInt(TRANSFUR_PROGRESS_KEY,amount);
        tag.putString(TRANSFUR_TYPE_KEY,transfurType);
    }

    @Override
    public @NotNull String mod$getTransfurType() {
        CompoundTag tag=getPersistentData();
        if(!tag.contains(TRANSFUR_TYPE_KEY)) return "ERR";
        return getPersistentData().getString(TRANSFUR_TYPE_KEY);
    }

    @Override
    public void mod$setTransfurType(@NotNull String transfurType) {
        getPersistentData().putString(TRANSFUR_TYPE_KEY,transfurType);
    }

    @Override
    public boolean mod$isTransfurred() {
        return getPersistentData().getBoolean(TRANSFURRED_KEY);
    }

    @Override
    public void mod$transfur(@NotNull String transfurType) {
        CompoundTag tag=getPersistentData();
        tag.putBoolean(TRANSFURRED_KEY,true);
        tag.putString(TRANSFUR_TYPE_KEY,transfurType);
    }

    @Override
    public void mod$unTransfur() {
        CompoundTag tag=getPersistentData();
        tag.remove(TRANSFUR_TYPE_KEY);
        tag.putInt(TRANSFUR_PROGRESS_KEY,0);
        tag.putBoolean(TRANSFURRED_KEY,false);
    }
}