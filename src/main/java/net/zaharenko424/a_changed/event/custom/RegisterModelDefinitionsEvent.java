package net.zaharenko424.a_changed.event.custom;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This event is called during initialization of ModelDefinitionCache. It is only fired once!
 */
public class RegisterModelDefinitionsEvent extends Event implements IModBusEvent {

    private final Map<ModelLayerLocation, ModelDefinition> definitions;

    public RegisterModelDefinitionsEvent(Map<ModelLayerLocation, ModelDefinition> definitions){
        this.definitions = definitions;
    }

    public void registerModelDefinition(@NotNull ModelLayerLocation location, @NotNull ModelDefinition definition){
        definitions.putIfAbsent(location, definition);
    }
}