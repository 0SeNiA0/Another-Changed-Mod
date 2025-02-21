package net.zaharenko424.a_changed.client.cmrs;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModLoader;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.client.cmrs.event.RegisterBuiltInModelsEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomModelManager {

    private static CustomModelManager modelManager;
    private final ConcurrentHashMap<AbstractClientPlayer, CustomModelWrapper<?, ?>> render = new ConcurrentHashMap<>();
    private final Multimap<AbstractClientPlayer, ObjectIntPair<CustomModelWrapper<?, ?>>> modelQueue = Multimaps.synchronizedMultimap(HashMultimap.create());
    private final ConcurrentHashMap<ResourceLocation, DynamicCustomModel<?, ?>> modelCache = new ConcurrentHashMap<>();
    //private final ConcurrentHashMap<ResourceLocation, ModelLoadingState> beingLoaded = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, BuiltInCustomModel<?, ?>> builtInModels;

    private <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> CustomModelManager(Map<ResourceLocation, Supplier<?>> modelSuppliers){
        builtInModels = HashMap.newHashMap(modelSuppliers.size());
        modelSuppliers.forEach((loc, supplier) ->
                builtInModels.put(loc, new BuiltInCustomModel<>(loc, (Supplier<M>)supplier)));
    }

    public static void init(){
        if(modelManager != null) throw new IllegalStateException("CMM already initialized!");

        Map<ResourceLocation, Supplier<?>> map = new HashMap<>();
        ModLoader.postEvent(new RegisterBuiltInModelsEvent(map));
        modelManager = new CustomModelManager(map);
    }

    /**
     * Should not be called before FMLClientSetup!
     */
    public static CustomModelManager getInstance(){
        if(modelManager == null) throw new IllegalStateException("CMM not initialized!");
        return modelManager;
    }

    public boolean isBuiltIn(ResourceLocation modelId){
        return builtInModels.containsKey(modelId);
    }

    public boolean isModelLoaded(ResourceLocation modelId){
        return getModelWrapper(modelId) != null;
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> @Nullable M getModel(@NotNull ResourceLocation location){
        CustomModelWrapper<E, M> wrapper = getModelWrapper(location);
        return wrapper != null ? wrapper.getModel() : null;
    }

    private <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> CustomModelWrapper<E, M> getModelWrapper(@NotNull ResourceLocation location){
        BuiltInCustomModel<E, M> builtIn = (BuiltInCustomModel<E, M>) builtInModels.get(location);

        if(builtIn != null) return builtIn;

        return (CustomModelWrapper<E, M>) modelCache.get(location);
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> @Nullable M getModel(@NotNull AbstractClientPlayer player){
        return (M) render.get(player).getModel();
    }

    public Set<ResourceLocation> getQueuedModels(AbstractClientPlayer player){
        if(!modelQueue.containsKey(player)) return Set.of();

        List<ResourceLocation> ids = new ArrayList<>();
        modelQueue.get(player).forEach(pair -> ids.add(pair.key().getModelId()));

        return Set.copyOf(ids);
    }

    public boolean hasCustomModel(@NotNull AbstractClientPlayer player){
        return render.containsKey(player);
    }

    public Set<ResourceLocation> getRegisteredModels(){//Optimize? extend Set & store an unmodifiable view inside? Will have to update the Set each time models are added/removed...
        return Stream.concat(builtInModels.keySet().stream(), modelCache.keySet().stream()).collect(Collectors.toUnmodifiableSet());
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> void setLocalPlayerModel(@NotNull ResourceLocation modelId, @Nullable Supplier<M> model, int priority){
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if(player != null) setPlayerModel(player, modelId, model, priority);
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> void setPlayerModel(@NotNull AbstractClientPlayer player, @NotNull ResourceLocation modelId, @Nullable Supplier<M> modelSupplier, int priority){
        CustomModelWrapper<E, M> wrapper = getModelWrapper(modelId);
        if(wrapper == null) {
            if(modelSupplier == null) throw new IllegalArgumentException("Model is not cached & supplier is null " + modelId);
            M model = modelSupplier.get();//TODO use Supplier<CustomModelWrapper> instead? or use CompletableFuture<M>  Or add separate method to handle loading the model from bytes
            if(model == null) throw new IllegalStateException("null model returned by supplier");
            wrapper = new DynamicCustomModel<>(modelId, model);
            modelCache.put(modelId, (DynamicCustomModel<E, M>)wrapper);
        }
        modelQueue.put(player, ObjectIntPair.of(wrapper, priority));
        recalculatePlayerModel(player);
    }

    public void removePlayerModel(@NotNull AbstractClientPlayer player, @NotNull ResourceLocation modelId, int priority){
        CustomModelWrapper<?, ?> pair = render.get(player);
        if(pair == null) return;//No models queued for player.

        boolean recalculate = false;
        synchronized (modelQueue){
            recalculate = modelQueue.get(player).removeIf(pair1 ->
                        pair1.key().getModelId() == modelId && pair1.valueInt() == priority)
                    || recalculate;
        }
        if(recalculate) recalculatePlayerModel(player);
    }

    public void removeLocalPlayerModel(@NotNull ResourceLocation modelId){
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if(player != null) removePlayerModel(player, modelId);
    }

    public void removePlayerModel(@NotNull AbstractClientPlayer player, @NotNull ResourceLocation modelId){
        CustomModelWrapper<?, ?> pair = render.get(player);
        if(pair == null) return;//No models queued for player.

        boolean recalculate = false;
        synchronized (modelQueue){
            recalculate = modelQueue.get(player).removeIf(pair1 -> pair1.key().getModelId() == modelId)
                    || recalculate;
        }
        if(recalculate) recalculatePlayerModel(player);
    }

    public void unloadPlayer(@NotNull AbstractClientPlayer player){
        render.remove(player);
        modelQueue.removeAll(player);
    }

    public void recalculatePlayerModel(@NotNull AbstractClientPlayer player){
        if(!modelQueue.containsKey(player)) {
            render.remove(player);//Remove just in case
            return;
        }
        int priority = Integer.MIN_VALUE;
        CustomModelWrapper<?, ?> model = null;
        for(ObjectIntPair<CustomModelWrapper<?, ?>> pair : modelQueue.get(player)){
            if(pair.valueInt() <= priority) continue;
            model = pair.key();
            priority = pair.valueInt();
        }
        assert model != null;
        render.put(player, model);
    }

    @ApiStatus.Internal
    public void rebuildBuiltInModel(ResourceLocation modelId){
        BuiltInCustomModel<?, ?> builtIn = builtInModels.get(modelId);
        if(builtIn != null) builtIn.rebuild();
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> void registerDynamicModel(@NotNull ResourceLocation modelId, @NotNull M model){
        if(modelCache.containsKey(modelId)) return;
        modelCache.put(modelId, new DynamicCustomModel<>(modelId, model));
    }

    public void unloadModel(@NotNull ResourceLocation modelId){
        if(!modelCache.containsKey(modelId)) return;
        modelCache.remove(modelId);

        render.values().removeIf(model1 -> model1.getModelId() == modelId);
        synchronized (modelQueue){
            modelQueue.values().removeIf(pair -> pair.key().getModelId() == modelId);
        }

        modelQueue.keys().forEach(this::recalculatePlayerModel);
    }

    public void unloadAllModels(){
        render.clear();
        modelQueue.clear();
        modelCache.clear();
    }

    interface CustomModelWrapper <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>>{

        @NotNull ResourceLocation getModelId();

        M getModel();
    }

    static class DynamicCustomModel <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> implements CustomModelWrapper<E, M> {
        private final ResourceLocation location;
        private final M model;

        public DynamicCustomModel(@NotNull ResourceLocation location, @NotNull M model){
            this.location = location;
            this.model = model;
        }

        @Override
        public @NotNull ResourceLocation getModelId() {
            return location;
        }

        @Override
        public M getModel() {
            return model;
        }
    }

    static class BuiltInCustomModel <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> implements CustomModelWrapper<E, M>{
        private final ResourceLocation location;
        private final Supplier<M> modelSupplier;
        private M model;

        public BuiltInCustomModel(ResourceLocation location, Supplier<M> modelSupplier){
            this.location = location;
            this.modelSupplier = modelSupplier;
        }

        @Override
        public @NotNull ResourceLocation getModelId() {
            return location;
        }

        public M getModel(){
            if(model == null) model = modelSupplier.get();
            return model;
        }

        public void rebuild(){
            model = null;
        }
    }
}