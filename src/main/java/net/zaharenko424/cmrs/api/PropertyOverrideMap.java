package net.zaharenko424.cmrs.api;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PropertyOverrideMap {

    <O> void addOverride(@NotNull ModelPropertyType.Overridable<? extends Overridable<O>, O> type, @NotNull O override);

    void removeOverride(@NotNull ModelPropertyType.Overridable<?, ?> type);

    <O> @Nullable O getOverride(@NotNull ModelPropertyType.Overridable<? extends Overridable<O>, O> type);

    boolean isEmpty();

    <P extends Overridable<O>, O> void write(@NotNull FriendlyByteBuf buffer);
}