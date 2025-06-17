package net.zaharenko424.cmrs.event;

import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.zaharenko424.cmrs.api.CustomModel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This event is called during FMLClientSetup. <p> Registered custom models can be accessed through CustomModelManager.getModel(ResourceLocation)
 */
public class RegisterBuiltInModelsEvent extends Event implements IModBusEvent {

    private final Map<ResourceLocation, Supplier<?>> modelSuppliers;

    @ApiStatus.Internal
    public RegisterBuiltInModelsEvent(@NotNull Map<ResourceLocation, Supplier<?>> modelSuppliers){
        this.modelSuppliers = modelSuppliers;
    }

    public boolean isRegistered(@NotNull ResourceLocation location){
        return modelSuppliers.containsKey(location);
    }

    /**
     * @param function Must return a new model instance on every get() call.
     */
    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> void registerModelSupplier(@NotNull ResourceLocation location, @NotNull Supplier<M> function){
        modelSuppliers.putIfAbsent(location, function);
    }
}