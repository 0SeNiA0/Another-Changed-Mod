package net.zaharenko424.cmrs.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MapView <K, V> implements Map<K, V> {

    private final Map<K, V> map;
    private SetView<K> keys;
    private CollectionView<V> values;
    private SetView<Map.Entry<K, V>> entries;

    public MapView(Map<K, V> map){
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public @Nullable V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Set<K> keySet() {
        if(keys == null) keys = new SetView<>(map.keySet());
        return keys;
    }

    @Override
    public @NotNull Collection<V> values() {
        if(values == null) values = new CollectionView<>(map.values());
        return values;
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        if(entries == null) entries = new SetView<>(map.entrySet());
        return entries;
    }
}
