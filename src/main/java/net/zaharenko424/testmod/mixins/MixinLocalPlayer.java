package net.zaharenko424.testmod.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.entity.TransfurHolder;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player implements TransfurHolder {
    public MixinLocalPlayer(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Unique
    private int mod$transfurProgress =0;
    @Unique
    private String mod$transfurType =null;
    @Unique
    private boolean mod$isTransfurred =false;

    @Override
    public int mod$getTransfurProgress() {
        return mod$transfurProgress;
    }

    @Override
    public void mod$setTransfurProgress(int amount, @NotNull String transfurType) {
        mod$transfurProgress =amount;
        this.mod$transfurType =transfurType;
    }

    @Override
    public @NotNull String mod$getTransfurType() {
        return mod$transfurType!=null?mod$transfurType:"ERR";
    }

    @Override
    public void mod$setTransfurType(@NotNull String transfurType) {
        this.mod$transfurType =transfurType;
    }

    @Override
    public boolean mod$isTransfurred() {
        return mod$isTransfurred;
    }

    @Override
    public void mod$transfur(@NotNull String transfurType) {
        mod$isTransfurred =true;
        //TODO change model, etc.
    }

    @Override
    public void mod$unTransfur() {
        mod$isTransfurred =false;
        mod$transfurProgress =0;
        mod$transfurType =null;
        //TODO reset model, etc.
    }
}
