package net.zaharenko424.a_changed.client.cmrs;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mojang.blaze3d.platform.NativeImage;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModLoader;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.geom.*;
import net.zaharenko424.a_changed.client.cmrs.model.CustomEntityModel;
import net.zaharenko424.a_changed.event.LoadModelsToCacheEvent;
import net.zaharenko424.a_changed.util.FriendlierByteBuf;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.net.ssl.HttpsURLConnection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CustomModelManager {

    private static CustomModelManager modelManager;
    private final ConcurrentHashMap<AbstractClientPlayer, CustomEntityModel<?>> render = new ConcurrentHashMap<>();
    private final Multimap<AbstractClientPlayer, Pair<CustomEntityModel<?>, Integer>> modelQueue = Multimaps.synchronizedMultimap(HashMultimap.create());
    private final ConcurrentHashMap<ResourceLocation, CustomEntityModel<?>> modelCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ResourceLocation> urlLoaded = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<ResourceLocation>> beingLoaded = new ConcurrentHashMap<>();

    private CustomModelManager(){}

    /**
     * Should not be called before all registries are registered!
     */
    public static CustomModelManager getInstance(){
        if(modelManager == null) {
            modelManager = new CustomModelManager();
            ModLoader.get().postEvent(new LoadModelsToCacheEvent());
        }
        return modelManager;
    }

    public Set<ResourceLocation> getRegisteredModels(){
        return modelCache.keySet().stream().collect(Collectors.toUnmodifiableSet());
    }

    public Set<ResourceLocation> getQueuedModels(AbstractClientPlayer player){
        if(!modelQueue.containsKey(player)) return Set.of();
        List<CustomEntityModel<?>> models = new ArrayList<>();
        modelQueue.get(player).forEach(pair -> models.add(pair.getKey()));
        List<ResourceLocation> ids = new ArrayList<>();
        modelCache.forEach((key, value) -> {
            if (models.contains(value)) ids.add(key);
        });
        return Set.copyOf(ids);
    }

    public boolean hasCustomModel(@NotNull AbstractClientPlayer player){
        return render.containsKey(player);
    }

    public @Nullable <E extends LivingEntity> CustomEntityModel<E> getModel(@NotNull AbstractClientPlayer player){
        return (CustomEntityModel<E>) render.get(player);
    }

    public void setLocalPlayerModel(@NotNull ResourceLocation modelId, @Nullable Supplier<CustomEntityModel<?>> model, int priority){
        if(Minecraft.getInstance().player == null) return;
        setPlayerModel(Minecraft.getInstance().player, modelId, model, priority);
    }

    public void setPlayerModel(@NotNull AbstractClientPlayer player, @NotNull ResourceLocation modelId, @Nullable Supplier<CustomEntityModel<?>> model, int priority){
        if(!modelCache.containsKey(modelId) && model == null) throw new IllegalArgumentException("Model is not cached & supplier is null " + modelId);
        CustomEntityModel<?> model1 = modelCache.computeIfAbsent(modelId, id -> model.get());
        modelQueue.put(player, Pair.of(model1, priority));
        recalculatePlayerModel(player);
    }

    public void setPlayerModel(@NotNull AbstractClientPlayer player, @NotNull String url, @NotNull BiFunction<ModelPart, ResourceLocation, @NotNull CustomEntityModel<?>> func, int priority){
        if(urlLoaded.containsKey(url)){
            setPlayerModel(player, urlLoaded.get(url), null, priority);
        } else loadModel(url, func).whenComplete((modelId, err) ->
                setPlayerModel(player, modelId, null, priority));
    }

    public void removeLocalPlayerModel(@NotNull ResourceLocation modelId){
        if(Minecraft.getInstance().player == null) return;
        removePlayerModel(Minecraft.getInstance().player, modelId);
    }

    public void removeLocalPlayerModel(@NotNull String url){
        if(Minecraft.getInstance().player == null || !urlLoaded.containsKey(url)) return;
        removePlayerModel(Minecraft.getInstance().player, urlLoaded.get(url));
    }

    public void removePlayerModel(@NotNull AbstractClientPlayer player, @NotNull String url){
        if(!urlLoaded.containsKey(url)) return;
        removePlayerModel(player, urlLoaded.get(url));
    }

    public void removePlayerModel(@NotNull AbstractClientPlayer player, @NotNull ResourceLocation modelId){
        if(!modelCache.containsKey(modelId)) return;
        CustomEntityModel<?> model = modelCache.get(modelId);
        if(render.get(player) == model) render.remove(player);
        synchronized (modelQueue){
            modelQueue.get(player).removeIf(pair -> pair.getKey() == model);
        }
        recalculatePlayerModel(player);
    }

    public void recalculatePlayerModel(@NotNull AbstractClientPlayer player){
        if(!modelQueue.containsKey(player)) return;
        int priority = Integer.MIN_VALUE;
        CustomEntityModel<?> model = null;
        for(Pair<CustomEntityModel<?>, Integer> pair : modelQueue.get(player)){
            if(pair.getValue() <= priority) continue;
            model = pair.getKey();
            priority = pair.getValue();
        }
        assert model != null;
        render.put(player, model);
    }

    public void registerModel(@NotNull ResourceLocation modelId, @NotNull CustomEntityModel<?> model){
        if(modelCache.containsKey(modelId)) return;
        modelCache.put(modelId, model);
    }

    public CompletableFuture<ResourceLocation> loadModel(@NotNull String url, @NotNull BiFunction<ModelPart, ResourceLocation, @NotNull CustomEntityModel<?>> func) {
        if(urlLoaded.containsKey(url)) return CompletableFuture.supplyAsync(() -> urlLoaded.get(url));
        if(beingLoaded.containsKey(url)) return beingLoaded.get(url);

        CompletableFuture<ResourceLocation> future = CompletableFuture.supplyAsync(()-> {
            try {
                URL link = new URL("https://raw.githubusercontent.com/0SeNiA0/special_tf_models/main/" + url + ".ccm");
                HttpsURLConnection connection = (HttpsURLConnection) link.openConnection();
                if(connection.getResponseCode() == 404) throw new FileNotFoundException();
                InputStream in = connection.getInputStream();
                byte[] bytes = in.readAllBytes();
                in.close();
                FriendlierByteBuf buf = new FriendlierByteBuf(Unpooled.wrappedBuffer(bytes));

                ResourceLocation modelId = buf.readResourceLocation();
                ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();

                HashMap<String, GroupDefinition> groups = new HashMap<>();
                groups.put("groupDefinition", modelBuilder.getRoot());

                String name, parent;
                GroupBuilder groupBuilder;
                GroupDefinition group;
                float[] ar0, ar1, ar2, ar3;
                char c;
                while(buf.peekChar() != '!') {  // '!' end of model def
                    name = buf.readUtf();
                    parent = buf.readUtf();

                    groupBuilder = GroupBuilder.create();

                    ar2 = FloatArrays.EMPTY_ARRAY;
                    ar3 = FloatArrays.EMPTY_ARRAY;
                    c = buf.readChar();
                    if(c == 'o'){
                        ar2 = buf.readFloatArray();
                        c = buf.readChar();
                    }
                    if(c == 'r'){
                        ar3 = buf.readFloatArray();
                        c = buf.readChar();
                    }

                    Vector3f inflation;
                    while (c != ';') {//  ';' at the end of group definition!
                        switch (c) {
                            case 'a' -> groupBuilder.armor();
                            case 'g' -> groupBuilder.glowing();
                            case 'c' -> {
                                ar0 = buf.readFloatArray();
                                c = buf.readChar();

                                if (c == 'i') {
                                    inflation = new Vector3f(buf.readFloat());
                                    c = buf.readChar();
                                } else inflation = null;

                                CubeUV uv = new CubeUV();
                                while (c == 'n' || c == 's' || c == 'w' || c == 'e' || c == 'u' || c == 'd') {
                                    ar1 = buf.readFloatArray();
                                    if (c == 'n') uv.north(ar1[0], ar1[1], ar1[2], ar1[3]);
                                    if (c == 's') uv.south(ar1[0], ar1[1], ar1[2], ar1[3]);
                                    if (c == 'w') uv.west(ar1[0], ar1[1], ar1[2], ar1[3]);
                                    if (c == 'e') uv.east(ar1[0], ar1[1], ar1[2], ar1[3]);
                                    if (c == 'u') uv.up(ar1[0], ar1[1], ar1[2], ar1[3]);
                                    if (c == 'd') uv.down(ar1[0], ar1[1], ar1[2], ar1[3]);
                                    c = buf.readChar();
                                }
                                buf.unreadChar();

                                if (inflation != null)
                                    groupBuilder.addBox(ar0[0], ar0[1], ar0[2], ar0[3], ar0[4], ar0[5], inflation, uv);
                                else groupBuilder.addBox(ar0[0], ar0[1], ar0[2], ar0[3], ar0[4], ar0[5], uv);
                            }
                            case 'm' -> groupBuilder.addMesh(buf.readFloatArray(), buf.readFloatArray());
                        }
                        c = buf.readChar();
                    }
                    group = groups.get(parent);
                    if(ar2.length == 3 && ar3.length == 3){
                        group = group.addOrReplaceChild(name, groupBuilder, PartPose.offsetAndRotation(ar2[0], ar2[1], ar2[2], ar3[0], ar3[1], ar3[2]));
                    } else if(ar2.length == 3) {
                        group = group.addOrReplaceChild(name, groupBuilder, PartPose.offset(ar2[0], ar2[1], ar2[2]));
                    } else if(ar3.length == 3) {
                        group = group.addOrReplaceChild(name, groupBuilder, PartPose.rotation(ar3[0], ar3[1], ar3[2]));
                    } else group = group.addOrReplaceChild(name, groupBuilder);

                    groups.put(name, group);
                }
                buf.skipChar();

                CustomEntityModel<?> model = func.apply(ModelDefinition.create(modelBuilder, buf.readVarInt(), buf.readVarInt(), buf.readFloat()).bake(), modelId);

                byte[] textureBytes = new byte[buf.readableBytes()];
                buf.readBytes(textureBytes);
                buf.release();

                Minecraft.getInstance().textureManager.register(model.getTexture(),
                        new DynamicTexture(NativeImage.read(textureBytes)));

                modelCache.put(modelId, model);
                urlLoaded.put(url, modelId);
                beingLoaded.remove(url);
                return modelId;
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }).exceptionally(err -> {
            if(!(err.getCause().getCause() instanceof FileNotFoundException)){
                AChanged.LOGGER.error("Error occurred while loading model from {}", url);
                err.printStackTrace();
            }
            beingLoaded.remove(url);
            return null;
        });
        beingLoaded.put(url, future);
        return future;
    }

    public void resetPlayerModels(@NotNull AbstractClientPlayer player){
        render.remove(player);
        modelQueue.removeAll(player);
    }

    public void unloadModel(@NotNull ResourceLocation modelId){
        if(!modelCache.containsKey(modelId)) return;
        CustomEntityModel<?> model = modelCache.remove(modelId);

        render.values().removeIf(model1 -> model1 == model);
        synchronized (modelQueue){
            modelQueue.values().removeIf(pair -> pair.getKey() == model);
        }
        urlLoaded.values().removeIf(id -> id == modelId);
        modelCache.remove(modelId);

        modelQueue.keys().forEach(this::recalculatePlayerModel);
    }

    public void unloadAllModels(){
        render.clear();
        modelQueue.clear();
        modelCache.clear();
        urlLoaded.clear();
    }
}