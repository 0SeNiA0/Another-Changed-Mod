package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.sounds.SoundEvent;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record TransfurContext(@Nullable SoundEvent onUntransfurSound, @Nullable SoundEvent onTransfurSound, boolean checkResistance, @Nullable TransfurResult result) {

    public static final TransfurContext DEF = new TransfurContext(SoundRegistry.TRANSFUR.get(), SoundRegistry.TRANSFUR.get(), true, null);
    public static final TransfurContext DEF_NO_CHECK = DEF.withCheckResistance(false);
    public static final TransfurContext CRYSTAL = new TransfurContext(SoundRegistry.TRANSFUR_1.get(), SoundRegistry.TRANSFUR_1.get(), true, null);
    public static final TransfurContext TRANSFUR = DEF.withResult(TransfurResult.TRANSFUR);
    public static final TransfurContext TRANSFUR_DEATH = DEF.withResult(TransfurResult.DEATH);

    public static final TransfurContext UNTRANSFUR = of(SoundRegistry.TRANSFUR.get());
    public static final TransfurContext UNTRANSFUR_SILENT = of(null);

    public TransfurContext(@Nullable SoundEvent onUntransfurSound, @Nullable SoundEvent onTransfurSound, TransfurResult result){
        this(onUntransfurSound, onTransfurSound, true, result);
    }

    public static TransfurContext of(SoundEvent onUntransfurSound){
        return new TransfurContext(onUntransfurSound, null, true, null);
    }

    public static TransfurContext of(SoundEvent onUntransfurSound, boolean checkResistance){
        return new TransfurContext(onUntransfurSound, null, checkResistance, null);
    }

    public static TransfurContext of(@Nullable SoundEvent onUntransfurSound, @Nullable TransfurResult result){
        return new TransfurContext(onUntransfurSound, null, true, result);
    }

    public static TransfurContext of(@Nullable SoundEvent onUntransfurSound, @Nullable SoundEvent onTransfurSound, @Nullable TransfurResult result){
        return new TransfurContext(onUntransfurSound, onTransfurSound, true, result);
    }

    @Contract("_ -> new")
    public @NotNull TransfurContext withUnTFSound(SoundEvent onUntransfurSound){
        return new TransfurContext(onUntransfurSound, onTransfurSound, checkResistance, result);
    }

    @Contract("_ -> new")
    public @NotNull TransfurContext withTFSound(SoundEvent onTransfurSound){
        return new TransfurContext(onUntransfurSound, onTransfurSound, checkResistance, result);
    }

    @Contract("_ -> new")
    public @NotNull TransfurContext withCheckResistance(boolean checkResistance){
        return new TransfurContext(onUntransfurSound, onTransfurSound, checkResistance, result);
    }

    @Contract("_ -> new")
    public @NotNull TransfurContext withResult(TransfurResult result){
        return new TransfurContext(onUntransfurSound, onTransfurSound, checkResistance, result);
    }
}