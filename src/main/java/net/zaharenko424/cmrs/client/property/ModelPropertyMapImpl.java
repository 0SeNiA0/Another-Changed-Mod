package net.zaharenko424.cmrs.client.property;

import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.cmrs.api.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public class ModelPropertyMapImpl implements ModelPropertyMap {

    private Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties;
    private Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> tmp;
    private PropertyOverrideMap overrides;

    public ModelPropertyMapImpl(){
        this(new Reference2ObjectLinkedOpenHashMap<>());
    }

    public ModelPropertyMapImpl(Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties){
        this.properties = properties;
    }

    @Override
    public <P> void setProperty(@NotNull ModelPropertyType<P> type, @NotNull P property) {
        properties.replace(type, property);
    }

    @Override
    public <P> void addLast(@NotNull ModelPropertyType<P> type, @NotNull P property) {
        properties.remove(type);
        properties.put(type, property);
    }

    public <P> void addAfter(@NotNull ModelPropertyType<?> after, @NotNull ModelPropertyType<P> type, @NotNull P property){
        if(properties.isEmpty()){
            addLast(type, property);
            return;
        }

        if(tmp == null) {
            tmp = new Reference2ObjectLinkedOpenHashMap<>();
        } else tmp.clear();

        properties.forEach((k, v) -> {
            if(k == type) return;
            tmp.put(k, v);
            if(k == after) {
                tmp.put(type, property);
            }
        });

        Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> map = properties;
        properties = tmp;
        tmp = map;
        tmp.clear();
    }

    public <P> void addBefore(@NotNull ModelPropertyType<?> before, @NotNull ModelPropertyType<P> type, @NotNull P property){
        if(properties.isEmpty()){
            addLast(type, property);
            return;
        }

        if(tmp == null) {
            tmp = new Reference2ObjectLinkedOpenHashMap<>();
        } else tmp.clear();

        properties.forEach((k, v) -> {
            if(k == type) return;
            if(k == before) {
                tmp.put(type, property);
            }
            tmp.put(k, v);

        });

        Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> map = properties;
        properties = tmp;
        tmp = map;
        tmp.clear();
    }

    @Override
    public void removeProperty(@NotNull ModelPropertyType<?> type) {
        properties.remove(type);
    }

    @Override
    public boolean hasProperty(@NotNull ModelPropertyType<?> type) {
        return properties.containsKey(type);
    }

    @Override
    public <P> @Nullable P getProperty(@NotNull ModelPropertyType<P> type) {
        return (P) properties.get(type);
    }

    @Override
    public void applyOverrides(@NotNull PropertyOverrideMap overrideMap) {
        overrides = overrideMap;
    }

    @Override
    public @Nullable PropertyOverrideMap getOverrides() {
        return overrides;
    }

    @Override
    public void clearOverrides() {
        overrides = null;
    }

    @Override
    public void forEachModelLayer(@NotNull Consumer<ModelLayer> op) {
        for(Object obj : properties.values()){
            if(obj instanceof ModelLayer layer) op.accept(layer);
        }
    }

    @Override
    public void forEachRenderLayer(@NotNull Consumer<RenderLayerLike> op) {
        for(Object obj : properties.values()){
            if(obj instanceof RenderLayerLike layerLike) op.accept(layerLike);
        }
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public <P> void write(@NotNull FriendlyByteBuf buffer) {
        Registry<ModelPropertyType<?>> registry = ModelPropertyRegistry.PROPERTY_REGISTRY;
        int size = properties.size();
        buffer.writeVarInt(size);
        if(size == 0) return;
        ModelPropertyType<P> type;
        ResourceLocation key;
        for(Map.Entry<ModelPropertyType<?>, Object> entry : properties.entrySet()){
            type = (ModelPropertyType<P>) entry.getKey();
            key = registry.getKey(type);
            if(key == null) throw new IllegalStateException("Model property type not registered!");
            buffer.writeResourceLocation(key);
            type.codec.encode(buffer, (P) entry.getValue());
        }
    }

    public static <P> ModelPropertyMap read(@NotNull FriendlyByteBuf buf){
        int size = buf.readVarInt();
        if(size == 0) return new ModelPropertyMapImpl();

        Registry<ModelPropertyType<?>> registry = ModelPropertyRegistry.PROPERTY_REGISTRY;
        Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> map = new Reference2ObjectLinkedOpenHashMap<>();
        ResourceLocation loc;
        ModelPropertyType<P> type;
        for(int i = 0; i < size; i++){
            loc = buf.readResourceLocation();
            type = (ModelPropertyType<P>) registry.get(loc);
            if(type == null) throw new IllegalStateException("No model property type found with location " + loc);
            map.put(type, type.codec.decode(buf));
        }

        return new ModelPropertyMapImpl(map);
    }
}