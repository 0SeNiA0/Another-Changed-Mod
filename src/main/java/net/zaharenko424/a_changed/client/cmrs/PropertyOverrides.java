package net.zaharenko424.a_changed.client.cmrs;

import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import net.zaharenko424.a_changed.client.cmrs.properties.ModelPropertyType;

public class PropertyOverrides {

    protected final Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties;

    public PropertyOverrides(Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties){
        this.properties = properties;
    }

    public <P> P getOverrideFor(ModelPropertyType<P> type){
        return (P) properties.get(type);
    }
}