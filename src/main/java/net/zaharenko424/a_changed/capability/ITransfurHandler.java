package net.zaharenko424.a_changed.capability;

import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface ITransfurHandler {

    float getTransfurProgress();

    void addTransfurProgress(float amount, TransfurType transfurType, TransfurContext context);

    @Nullable
    TransfurType getTransfurType();

    void setTransfurType(TransfurType transfurType);

    boolean isTransfurred();

    void transfur(TransfurType transfurType, TransfurContext context);

    void unTransfur(TransfurContext context);

    boolean isBeingTransfurred();

    void setBeingTransfurred(boolean isBeingTransfurred);

    void tick();

    void syncClient(ServerPlayer packetReceiver);

    void syncClients();

    @ApiStatus.Internal
    void loadSyncedData(float transfurProgress, boolean isTransfurred, TransfurType transfurType);
}