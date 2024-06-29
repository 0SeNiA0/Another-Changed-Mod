package net.zaharenko424.a_changed.capability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface IGrabHandler {

    LivingEntity getTarget();

    void grab(LivingEntity target);

    void drop();

    boolean canGrab();

    LivingEntity getGrabbedBy();

    void setGrabbedBy(@Nullable Player player);

    GrabMode grabMode();

    void setGrabMode(GrabMode mode);

    boolean wantsToBeGrabbed();

    void setWantsToBeGrabbed(boolean wantsToBeGrabbed);

    void tick();

    void syncClients();

    void syncClient(ServerPlayer packetReceiver);
}