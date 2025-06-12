package net.zaharenko424.a_changed.client.cmrs.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

public class Int2ObjArrayMap<V> extends Int2ObjectArrayMap<V> {

    public Int2ObjArrayMap() {
        super();
    }

    public Int2ObjArrayMap(int capacity) {
        super(capacity);
    }

    public V firstValue(){
        if(size == 0) return null;
        return (V) value[0];
    }

    public V getValue(int index){
        if(index > size - 1) return null;
        return (V) value[index];
    }
}