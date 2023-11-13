package net.zaharenko424.testmod.mixins;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.entity.Transfurrable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Zombie.class)
public abstract class MixinZombie extends Monster implements Transfurrable {
    public MixinZombie(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Unique
    private int mod$transfurProgress=0;
    @Unique
    private ResourceLocation mod$transfurType=null;

    public int mod$getTransfurProgress() {
        return mod$transfurProgress;
    }

    public void mod$setTransfurProgress(int transfurProgress, @NotNull ResourceLocation transfurType) {
        this.mod$transfurProgress = transfurProgress;
        this.mod$transfurType =transfurType;
    }

    public @Nullable ResourceLocation mod$getTransfurType() {
        return mod$transfurType;
    }

    public void mod$setTransfurType(@NotNull ResourceLocation transfurType) {
        this.mod$transfurType = transfurType;
    }
}