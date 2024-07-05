package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.sounds.SoundEvent;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record TransfurContext(@Nullable SoundEvent sound, @Nullable SoundEvent onTransfurSound, boolean checkResistance, @Nullable TransfurResult result) {

    public static final TransfurContext ADD_PROGRESS_DEF = new TransfurContext(SoundRegistry.TRANSFUR.get(), SoundRegistry.TRANSFUR.get(), true, null);
    public static final TransfurContext ADD_PROGRESS_CRYSTAL = new TransfurContext(SoundRegistry.TRANSFUR_1.get(), SoundRegistry.TRANSFUR_1.get(), true, null);

    public static final TransfurContext TRANSFUR_DEF = ADD_PROGRESS_DEF;
    public static final TransfurContext TRANSFUR_CRYSTAL_DEF = ADD_PROGRESS_CRYSTAL;
    public static final TransfurContext TRANSFUR_TF = TRANSFUR_DEF.withResult(TransfurResult.TRANSFUR);
    public static final TransfurContext TRANSFUR_DEATH = TRANSFUR_DEF.withResult(TransfurResult.DEATH);

    public static final TransfurContext UNTRANSFUR = of(SoundRegistry.TRANSFUR.get());
    public static final TransfurContext UNTRANSFUR_SILENT = of(null);

    public TransfurContext(@Nullable SoundEvent sound, @Nullable SoundEvent onTransfurSound, TransfurResult result){
        this(sound, onTransfurSound, true, result);
    }

    public static TransfurContext of(SoundEvent sound){
        return new TransfurContext(sound, null, true, null);
    }

    public static TransfurContext of(SoundEvent sound, boolean checkResistance){
        return new TransfurContext(sound, null, checkResistance, null);
    }

    public static TransfurContext of(@Nullable SoundEvent sound, @Nullable TransfurResult result){
        return new TransfurContext(sound, null, true, result);
    }

    public static TransfurContext of(@Nullable SoundEvent sound, @Nullable SoundEvent onTransfurSound, @Nullable TransfurResult result){
        return new TransfurContext(sound, onTransfurSound, true, result);
    }

    @Contract("_ -> new")
    public @NotNull TransfurContext withSound(SoundEvent sound){
        return new TransfurContext(sound, onTransfurSound, checkResistance, result);
    }

    @Contract("_ -> new")
    public @NotNull TransfurContext withTFSound(SoundEvent onTransfurSound){
        return new TransfurContext(sound, onTransfurSound, checkResistance, result);
    }

    @Contract("_ -> new")
    public @NotNull TransfurContext withCheckResistance(boolean checkResistance){
        return new TransfurContext(sound, onTransfurSound, checkResistance, result);
    }

    @Contract("_ -> new")
    public @NotNull TransfurContext withResult(TransfurResult result){
        return new TransfurContext(sound, onTransfurSound, checkResistance, result);
    }
}