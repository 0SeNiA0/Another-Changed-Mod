package net.zaharenko424.cmrs.api;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ModelPropertyMap {

    <P> void setProperty(@NotNull ModelPropertyType<P> type, @NotNull P property);

    <P> void addLast(@NotNull ModelPropertyType<P> type, @NotNull P property);

    <P> void addAfter(@NotNull ModelPropertyType<?> after, @NotNull ModelPropertyType<P> type, @NotNull P property);

    <P> void addBefore(@NotNull ModelPropertyType<?> before, @NotNull ModelPropertyType<P> type, @NotNull P property);

    default <P> void moveAfter(@NotNull ModelPropertyType<?> after, @NotNull ModelPropertyType<P> type){
        P property = getProperty(type);
        if(property == null) return;
        if(!hasProperty(after)){
            setProperty(type, property);
        } else addAfter(after, type, property);
    }

    default <P> void moveBefore(@NotNull ModelPropertyType<?> before, @NotNull ModelPropertyType<P> type){
        P property = getProperty(type);
        if(property == null) return;
        if(!hasProperty(before)){
            setProperty(type, property);
        } else addBefore(before, type, property);
    }

    void removeProperty(@NotNull ModelPropertyType<?> type);

    <P> @Nullable P getProperty(@NotNull ModelPropertyType<P> type);

    boolean hasProperty(@NotNull ModelPropertyType<?> type);

    void applyOverrides(@NotNull PropertyOverrideMap overrideMap);

    @Nullable PropertyOverrideMap getOverrides();

    void clearOverrides();

    void forEachModelLayer(@NotNull Consumer<ModelLayer> op);

    void forEachRenderLayer(@NotNull Consumer<RenderLayerLike> op);

    boolean isEmpty();

    <P> void write(@NotNull FriendlyByteBuf buffer);
}