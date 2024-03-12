package net.zaharenko424.a_changed.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.Set;

public class RegisterModelsEvent extends Event implements IModBusEvent {

    private final Set<ResourceLocation> models;

    public RegisterModelsEvent(Set<ResourceLocation> set){
        this.models = set;
    }

    public boolean registerModel(ResourceLocation modelId){
        return models.add(modelId);
    }
}