package net.zaharenko424.cmrs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModLoader;
import net.zaharenko424.cmrs.api.CustomModel;
import net.zaharenko424.cmrs.api.ModelSetReason;
import net.zaharenko424.cmrs.client.renderer.AnyModelRenderer;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.cmrs.util.SetView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class CustomModelManager {

    private static CustomModelManager modelManager;
    private final ConcurrentHashMap<UUID, PlayerProfile> players = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ResourceLocation, DynamicModelWrapper<?, ?>> modelCache = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, BuiltInModelWrapper<?, ?>> builtInModels;

    private <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> CustomModelManager(Map<ResourceLocation, Supplier<?>> modelSuppliers){
        builtInModels = HashMap.newHashMap(modelSuppliers.size());
        modelSuppliers.forEach((loc, supplier) ->
                builtInModels.put(loc, new BuiltInModelWrapper<>(loc, (Supplier<M>)supplier)));
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


// ============================== Model methods ===============================
    public boolean isBuiltIn(ResourceLocation modelId){
        return builtInModels.containsKey(modelId);
    }

    public boolean modelExists(ResourceLocation modelId){
        return getModelWrapper(modelId) != null;
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> @Nullable M getModel(ResourceLocation location){
        ModelWrapper<E, M> wrapper = getModelWrapper(location);
        return wrapper != null ? wrapper.getModel() : null;
    }

    private <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> @Nullable ModelWrapper<E, M> getModelWrapper(ResourceLocation location){
        BuiltInModelWrapper<E, M> builtIn = (BuiltInModelWrapper<E, M>) builtInModels.get(location);
        if(builtIn != null) return builtIn;

        return (ModelWrapper<E, M>) modelCache.get(location);
    }

    public Set<ResourceLocation> getLoadedModels(){//Optimize? extend Set & store an unmodifiable view inside? Will have to update the Set each time models are added/removed...
        return Stream.concat(builtInModels.keySet().stream(), modelCache.keySet().stream()).collect(Collectors.toUnmodifiableSet());
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> void loadDynamic(ResourceLocation modelId, Supplier<M> modelSupplier){
        ModelWrapper<?, ?> wrapper = getModelWrapper(modelId);
        if(wrapper != null) return;

        M model = modelSupplier.get();
        if(model == null) throw new IllegalStateException("Null model returned by model supplier.");
        modelCache.put(modelId, new DynamicModelWrapper<>(modelId, model));
    }

    public void unloadDynamicModel(ResourceLocation modelId){
        if(!modelCache.containsKey(modelId)) return;
        modelCache.remove(modelId);

        players.values().forEach(profile ->
                profile.removeModel(entry -> entry.getModelId().equals(modelId)));
    }

    public void unloadAllDynamicModels(){
        players.values().forEach(profile ->
                profile.removeModel(entry -> entry.model() instanceof DynamicModelWrapper<?,?>));
        modelCache.clear();
    }

    AnyModelRenderer<?, ?> renderer;

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> void renderModel(ResourceLocation modelId, E entity, PoseStack stack, int light){
        M model = getModel(modelId);
        if(model == null) return;
        if(renderer == null) renderer = new AnyModelRenderer<>(new EntityRendererProvider.Context(Minecraft.getInstance().getEntityRenderDispatcher(), null, null, null, null, null, null));
        ((AnyModelRenderer<E, M>)renderer).render(model, entity, 0, 1, stack, Minecraft.getInstance().renderBuffers().bufferSource(), light);
    }


// =========================== Player model methods ===========================
    public boolean hasCustomModel(AbstractClientPlayer player){
        PlayerProfile profile = players.get(player.getUUID());
        return profile != null && profile.getModelWrapper() != null;
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> @Nullable M getModel(AbstractClientPlayer player){
        PlayerProfile profile = players.get(player.getUUID());
        return profile != null ? profile.getModel() : null;
    }

    public Set<ModelEntry> getQueuedModels(AbstractClientPlayer player){
        PlayerProfile profile = players.get(player.getUUID());
        return profile != null ? profile.getQueue() : Set.of();
    }

    public void setPlayerModel(AbstractClientPlayer player, ResourceLocation modelId, int priority){
        setPlayerModel(player, modelId, priority, false, ModelSetReason.LOCAL);
    }

    public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> void setPlayerModel(AbstractClientPlayer player, ResourceLocation modelId, int priority, boolean removeOnDeath, ModelSetReason reason){
        ModelWrapper<E, M> wrapper = getModelWrapper(modelId);
        if(wrapper == null) throw new IllegalArgumentException("Model is not cached: " + modelId);

        players.computeIfAbsent(player.getUUID(), uid -> new PlayerProfile()).setModel(wrapper, priority, removeOnDeath, reason);
    }


    @ApiStatus.Internal
    public void playerDied(AbstractClientPlayer player){
        PlayerProfile profile = players.get(player.getUUID());
        if(profile == null) return;

        profile.removeModel(ModelEntry::removeOnDeath);
    }


    public void removePlayerModel(AbstractClientPlayer player, ModelSetReason reason){
        removePlayerModelInternal(player.getUUID(), entry -> entry.reason() == reason);
    }

    public void removePlayerModel(AbstractClientPlayer player, ResourceLocation modelId){
        removePlayerModelInternal(player.getUUID(), entry -> entry.getModelId().equals(modelId));
    }

    public void removePlayerModel(AbstractClientPlayer player, ResourceLocation modelId, int priority){
        removePlayerModelInternal(player.getUUID(), entry -> entry.getModelId().equals(modelId) && entry.priority() == priority);
    }

    public void removePlayerModel(AbstractClientPlayer player, ModelEntry entry){
        removePlayerModelInternal(player.getUUID(), e -> e == entry);
    }

    private void removePlayerModelInternal(UUID uid, Predicate<ModelEntry> predicate){
        PlayerProfile profile = players.get(uid);
        if(profile != null) profile.removeModel(predicate);
    }

    public void unloadPlayer(UUID player){
        if(Minecraft.getInstance().player != null && player.equals(Minecraft.getInstance().player.getUUID())) return;
        players.remove(player);
    }

    interface ModelWrapper<E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>>{

        @NotNull ResourceLocation getModelId();

        M getModel();
    }

    static class DynamicModelWrapper<E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> implements ModelWrapper<E, M> {
        //private final Supplier<CompletableFuture<M>> futureSupplier;
        //private CompletableFuture<M> modelLoader;
        private final ResourceLocation location;
        private final M model;

        public DynamicModelWrapper(@NotNull ResourceLocation location, @NotNull M model){
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

    static class BuiltInModelWrapper<E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> implements ModelWrapper<E, M> {
        private final ResourceLocation location;
        private Supplier<M> modelSupplier;
        private M model;

        public BuiltInModelWrapper(ResourceLocation location, Supplier<M> modelSupplier){
            this.location = location;
            this.modelSupplier = modelSupplier;
        }

        @Override
        public @NotNull ResourceLocation getModelId() {
            return location;
        }

        public M getModel(){
            if(model == null) {
                model = modelSupplier.get();
                modelSupplier = null;
            }
            return model;
        }
    }

    static class PlayerProfile {

        private static final Comparator<ModelEntry> comp = Comparator.comparingInt(ModelEntry::priority).reversed();

        private Set<ModelEntry> view;
        private final List<ModelEntry> models = Collections.synchronizedList(new ArrayList<>());

        public <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> @Nullable M getModel(){
            ModelWrapper<?, ?> wrapper = getModelWrapper();
            return wrapper != null ? (M) wrapper.getModel() : null;
        }

        public ModelWrapper<?, ?> getModelWrapper(){
            if(models.isEmpty()) return null;
            return models.getFirst().model();
        }

        public void setModel(ModelWrapper<?, ?> model, int priority, boolean removeOnDeath, ModelSetReason reason){
            ModelEntry e = new ModelEntry(model, priority, removeOnDeath, reason);
            if(models.contains(e)) return;
            models.add(e);
            models.sort(comp);
        }

        public void removeModel(@NotNull Predicate<ModelEntry> predicate){
            if(models.isEmpty()) return;
            if(models.removeIf(predicate) && !models.isEmpty()) models.sort(comp);
        }

        public Set<ModelEntry> getQueue(){
            if(view != null) return view;

            synchronized (models){
                if(view != null) return view;
                view = new SetView<>(models);
            }

            return view;
        }
    }

    public record ModelEntry(ModelWrapper<?, ?> model, int priority, boolean removeOnDeath, ModelSetReason reason){

        public ResourceLocation getModelId(){
            return model.getModelId();
        }
    }
}