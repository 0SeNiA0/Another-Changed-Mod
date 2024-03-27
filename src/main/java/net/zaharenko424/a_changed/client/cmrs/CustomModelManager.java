package net.zaharenko424.a_changed.client.cmrs;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModLoader;
import net.zaharenko424.a_changed.client.cmrs.geom.*;
import net.zaharenko424.a_changed.client.cmrs.model.CustomEntityModel;
import net.zaharenko424.a_changed.event.LoadModelsToCacheEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

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

    public void loadModel(@NotNull ResourceLocation modelId, @NotNull CustomEntityModel<?> model){
        if(modelCache.containsKey(modelId)) return;
        modelCache.put(modelId, model);
    }

    public CompletableFuture<ResourceLocation> loadModel(@NotNull String url, @NotNull BiFunction<ModelPart, ResourceLocation, @NotNull CustomEntityModel<?>> func){
        if(urlLoaded.containsKey(url)) return CompletableFuture.supplyAsync(() -> urlLoaded.get(url));
        if(beingLoaded.containsKey(url)) return beingLoaded.get(url);
        //TODO test HeapByteBuffer !!!
        CompletableFuture<ResourceLocation> future = CompletableFuture.supplyAsync(() -> {
            try {
                URL link = new URL("https://raw.githubusercontent.com/0SeNiA0/mod/" + url);
                InputStream in = link.openStream();
                byte[] bytes = in.readAllBytes();
                in.close();

                StringReader reader = new StringReader(new String(bytes));
                ResourceLocation modelId = new ResourceLocation(reader.readStringUntil('!'));

                ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
                HashMap<String, GroupDefinition> groups = new HashMap<>();
                groups.put("groupDefinition", modelBuilder.getRoot());

                char c;
                String str, str1;
                GroupBuilder groupBuilder;
                float[] ar, ar1;
                Vector3f inflate;
                CubeUV uv;
                while (reader.canRead()){
                    c = reader.read();
                    if(c == 'g'){// group
                        str = reader.readQuotedString();
                        if(reader.peek() != 'p') {
                            str1 = "groupDefinition";
                        } else {
                            reader.read();
                            str1 = reader.readQuotedString();
                        }
                        groupBuilder = GroupBuilder.create();
                        c = reader.read();
                        while(c == 'a' || c == 'l' ||  c == 'c' || c == 'm'){
                            switch(c){
                                case 'a' -> groupBuilder.armor();
                                case 'l' -> groupBuilder.glowing();//   not 'g' to not have conflict with group
                                case 'c' -> {// cube
                                    ar = readAr(reader, 6);
                                    c = reader.read();
                                    if(c == 'i'){
                                        ar1 = readAr(reader, 3);
                                        inflate = new Vector3f(ar1[0], ar1[1], ar1[2]);
                                    } else inflate = new Vector3f();
                                    uv = new CubeUV();
                                    while(c == 'n' || c == 's' || c == 'w' || c == 'e' || c == 'u' || c == 'd'){
                                        ar1 = readAr(reader, 4);
                                        if(c == 'n') uv.north(ar1[0], ar1[1], ar1[2], ar[3]);
                                        if(c == 's') uv.south(ar1[0], ar1[1], ar1[2], ar[3]);
                                        if(c == 'w') uv.west(ar1[0], ar1[1], ar1[2], ar[3]);
                                        if(c == 'e') uv.east(ar1[0], ar1[1], ar1[2], ar[3]);
                                        if(c == 'u') uv.up(ar1[0], ar1[1], ar1[2], ar[3]);
                                        if(c == 'd') uv.down(ar1[0], ar1[1], ar1[2], ar[3]);
                                        c = reader.read();
                                    }
                                    reader.setCursor(reader.getCursor() - 1);
                                    groupBuilder.addBox(ar[0], ar[1], ar[2], ar[3], ar[4], ar[5], inflate, uv);
                                }
                                case 'm' -> {// mesh
                                    ar = readAr(reader, -1);
                                    reader.skip();//    skip 'q'
                                    ar1 = readAr(reader, -1);
                                    groupBuilder.addMesh(ar, ar1);
                                }
                            }
                            c = reader.read();
                        }
                        reader.setCursor(reader.getCursor() - 1);
                        if(c == 'o') {
                            ar = readAr(reader, 6);
                            groups.put(str, groups.get(str1).addOrReplaceChild(str, groupBuilder, PartPose.offsetAndRotation(ar[0], ar[1], ar[2], ar[3], ar[4], ar[5])));
                        } else groups.put(str, groups.get(str1).addOrReplaceChild(str, groupBuilder));
                    }
                    if(c == 't') break;
                    //  end / texture
                }
                if(!reader.canRead()) throw new IllegalStateException("Invalid model file! " + link);

                int[] textureSize = readIntAr(reader, 3);
                CustomEntityModel<?> model = func.apply(ModelDefinition.create(modelBuilder, textureSize[0], textureSize[1], textureSize[2]).bake(), modelId);

                int read = reader.getRead().getBytes().length;
                int toRead = bytes.length - read;
                byte[] texture = new byte[toRead];
                System.arraycopy(bytes, read, texture, 0, toRead);

                Minecraft.getInstance().textureManager.register(model.getTexture(),
                        new DynamicTexture(NativeImage.read(texture)));//TODO test

                modelCache.put(modelId, model);
                urlLoaded.put(url, modelId);
                beingLoaded.remove(url);
                return modelId;
            } catch (IOException | CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        });
        beingLoaded.put(url, future);
        return future;
    }

    private float[] readAr(@NotNull StringReader reader, int limit) throws CommandSyntaxException {
        FloatArrayList list = new FloatArrayList();
        while(StringReader.isAllowedNumber(reader.peek()) || reader.peek() == ','){
            if(reader.peek() == ',') continue;
            if(list.size() == limit) break;
            list.add(reader.readFloat());
        }
        return list.elements();
    }

    private int[] readIntAr(@NotNull StringReader reader, int limit) throws CommandSyntaxException {
        IntArrayList list = new IntArrayList();
        while(StringReader.isAllowedNumber(reader.peek()) || reader.peek() == ','){
            if(reader.peek() == ',') continue;
            if(list.size() == limit) break;
            list.add(reader.readInt());
        }
        return list.elements();
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