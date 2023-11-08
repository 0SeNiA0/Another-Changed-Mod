package net.zaharenko424.testmod.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.entity.TransfurHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static net.zaharenko424.testmod.TransfurManager.*;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements TransfurHolder {
    public MixinServerPlayer(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Override
    public int mod$getTransfurProgress() {
        return TransfurManager.modTag(getPersistentData()).getInt(TRANSFUR_PROGRESS_KEY);
    }

    @Override
    public void mod$setTransfurProgress(int amount, @NotNull String transfurType) {
        mod$updateCompound(amount,transfurType,null);
    }

    @Override
    public @NotNull String mod$getTransfurType() {
        CompoundTag tag=TransfurManager.modTag(getPersistentData());
        return tag.contains(TRANSFUR_TYPE_KEY)?tag.getString(TRANSFUR_TYPE_KEY):"ERR";
    }

    @Override
    public void mod$setTransfurType(@NotNull String transfurType) {
        mod$updateCompound(null,transfurType,null);
    }

    @Override
    public boolean mod$isTransfurred() {
        return TransfurManager.modTag(getPersistentData()).getBoolean(TRANSFURRED_KEY);
    }

    @Override
    public void mod$transfur(@NotNull String transfurType) {
        mod$updateCompound(20,transfurType,true);
    }

    @Override
    public void mod$unTransfur() {
        mod$updateCompound(0,null,false);
    }

    @Unique
    private void mod$updateCompound(@Nullable Integer transfurProgress, @Nullable String transfurType,@Nullable Boolean isTransfurred){
        CompoundTag tag=TransfurManager.modTag(getPersistentData());
        if(transfurProgress!=null) tag.putInt(TRANSFUR_PROGRESS_KEY,transfurProgress);
        if(transfurType!=null) tag.putString(TRANSFUR_TYPE_KEY,transfurType); else tag.remove(TRANSFUR_TYPE_KEY);
        if(isTransfurred!=null) tag.putBoolean(TRANSFURRED_KEY,isTransfurred);
    }
}