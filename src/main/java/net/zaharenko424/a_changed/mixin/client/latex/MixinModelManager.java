package net.zaharenko424.a_changed.mixin.client.latex;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.zaharenko424.a_changed.ModelManagerAccess;
import net.zaharenko424.a_changed.attachment.LatexCoveredData;
import net.zaharenko424.a_changed.util.IOUtils;
import net.zaharenko424.a_changed.util.Thing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(ModelManager.class)
public abstract class MixinModelManager implements ModelManagerAccess {

    @Shadow
    private static CompletableFuture<Map<ResourceLocation, BlockModel>> loadBlockModels(ResourceManager resourceManager, Executor executor) {
        return null;
    }

    @Shadow
    private static CompletableFuture<Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>>> loadBlockStates(ResourceManager resourceManager, Executor executor) {
        return null;
    }

    @Shadow @Final private BlockColors blockColors;

    @Shadow protected abstract ModelManager.ReloadState loadModels(ProfilerFiller profilerFiller, Map<ResourceLocation, AtlasSet.StitchResult> atlasPreparations, ModelBakery modelBakery);

    @Shadow protected abstract void apply(ModelManager.ReloadState reloadState, ProfilerFiller profiler);

    @Shadow @Final private AtlasSet atlases;

    @Shadow private int maxMipmapLevels;


    @Unique
    private static boolean achanged$isForceReload = false;

    @Unique
    private static final HashMap<ResourceLocation, TextureAtlasSprite> achanged$sprites = new HashMap<>();
    @Unique
    private static final HashMap<ResourceLocation, Thing<File, File, File, File>> achanged$convertedTextures = new HashMap<>();

    @Unique
    private static void achanged$forceReload(){
        achanged$isForceReload = true;
    }

    @Unique
    private static boolean achanged$isForceReload(){
        return achanged$isForceReload;
    }

    @Override
    public HashMap<ResourceLocation, Thing<File, File, File, File>> achanged$getConvertedTextures() {
        return achanged$convertedTextures;
    }

    @ModifyReturnValue(at = @At(value = "RETURN", ordinal = 0), method = "lambda$loadModels$15")
    private static TextureAtlasSprite onLoadModelsSpriteGetter(TextureAtlasSprite original, @Local(argsOnly = true) ModelResourceLocation location){
        if(achanged$isForceReload()) return original;

        String path = original.contents().name().getPath();
        if(!path.startsWith("block") || path.endsWith("ltx")) return original;//make sure that there are no converted textures in hashSet!

        Block block = BuiltInRegistries.BLOCK.get(location.id());//accept textures only from block dir
        if(LatexCoveredData.isStateNotCoverable(block.defaultBlockState())) return original;//TMP TAGS DON'T WORK BEFORE THE WORLD IS LOADED

        achanged$sprites.put(original.contents().name(), original);
        return original;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Multimap;asMap()Ljava/util/Map;"), method = "loadModels")
    private void onLoadModels(ProfilerFiller profilerFiller, Map<ResourceLocation, AtlasSet.StitchResult> atlasPreparations, ModelBakery modelBakery, CallbackInfoReturnable<?> cir){
        if(achanged$isForceReload() || achanged$sprites.isEmpty()) return;

        File convertedDir = new File(Minecraft.getInstance().gameDirectory, "converted_textures");
        if(!convertedDir.exists()) {
            achanged$generateTextures(convertedDir);
            return;
        }

        File[] subDirectories = convertedDir.listFiles(File::isDirectory);
        if(subDirectories == null) {
            achanged$generateTextures(convertedDir);
            return;
        }

        achanged$convertedTextures.clear();
        for(File subDir : subDirectories){
            IOUtils.visitAllFiles((path, file, name) -> {
                String str = name.endsWith(".png") ? name.replace(".png", "") : name.replace(".png.mcmeta", "");
                ResourceLocation location = ResourceLocation.parse(path + str.replace("_darkltx", "").replace("_whiteltx", ""));

                achanged$convertedTextures.compute(location, (k, v) -> {
                    if(name.endsWith(".png")){
                        if(v == null) return name.contains("_darkltx") ? Thing.ofFirst(file) : Thing.ofThird(file);
                        return name.contains("_darkltx") ? v.first(file) : v.third(file);
                    }

                    if(name.endsWith(".mcmeta")){
                        if(v == null) return name.contains("_darkltx") ? Thing.ofSecond(file) : Thing.ofFourth(file);
                        return name.contains("_darkltx") ? v.second(file) : v.fourth(file);
                    }
                    return v;
                });
            }, subDir, subDir.getName() + ":block/");//Hopefully all the textures are going to be stored in block directory
        }

        achanged$removeUnused();

        if(achanged$convertedTextures.keySet().containsAll(achanged$sprites.keySet())) {
            achanged$sprites.clear();
            return;//All textures present, no need to do anything
        }

        achanged$sprites.keySet().removeAll(achanged$convertedTextures.keySet());
        achanged$generateTextures(convertedDir);
        if(!achanged$isForceReload()) achanged$convertedTextures.clear();
        achanged$sprites.clear();
    }

    @Unique
    private void achanged$removeUnused(){
        if(achanged$convertedTextures.entrySet().removeIf(entry -> {
            if(achanged$sprites.containsKey(entry.getKey())) return false;
            Thing<File, File, File, File> pair = entry.getValue();
            if(pair.first() != null) pair.first().delete();
            if(pair.third() != null) pair.third().delete();
            if(pair.second() != null) pair.second().delete();
            if(pair.fourth() != null) pair.fourth().delete();
            return true;
        })) achanged$forceReload();
    }

    @ModifyReceiver(at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;thenCompose(Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;", ordinal = 0),
            method = "reload")
    private <T, U> CompletableFuture<ModelManager.ReloadState> hiddenReload(CompletableFuture<ModelManager.ReloadState> instance, Function<? super T, ? extends CompletionStage<U>> fn, @Local(argsOnly = true) ResourceManager resourceManager, @Local(ordinal = 0, argsOnly = true) ProfilerFiller preparationsProfiler, @Local(ordinal = 1, argsOnly = true) ProfilerFiller reloadProfiler, @Local(ordinal = 0, argsOnly = true) Executor backgroundExecutor, @Local(ordinal = 1, argsOnly = true) Executor gameExecutor){
        return instance.thenCompose(state -> {
            if(!achanged$isForceReload()) return CompletableFuture.completedFuture(state);

            CompletableFuture<Map<ResourceLocation, BlockModel>> completablefuture = loadBlockModels(resourceManager, backgroundExecutor);
            CompletableFuture<Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>>> completablefuture1 = loadBlockStates(resourceManager, backgroundExecutor);
            CompletableFuture<ModelBakery> completablefuture2 = completablefuture.thenCombineAsync(
                    completablefuture1,
                    (models, blockStateLoaders) -> new ModelBakery(
                            blockColors, preparationsProfiler,
                            models, blockStateLoaders
                    ),
                    backgroundExecutor
            );
            Map<ResourceLocation, CompletableFuture<AtlasSet.StitchResult>> map = atlases.scheduleLoad(resourceManager, maxMipmapLevels, backgroundExecutor);

            return CompletableFuture.allOf(Stream.concat(map.values().stream(), Stream.of(completablefuture2)).toArray(CompletableFuture[]::new))
                    .thenApplyAsync(
                            p_248624_ -> loadModels(
                                    preparationsProfiler,
                                    map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, p_248988_ -> p_248988_.getValue().join())),
                                    completablefuture2.join()
                            ),
                            backgroundExecutor
                    ).thenApply(state1 -> {
                        achanged$isForceReload = false;
                        return state1;
                    });
        });
    }

    @Unique
    private static void achanged$generateTextures(File root){
        if(achanged$sprites.isEmpty()) return;
        achanged$forceReload();

        final float[] hsb = new float[4];
        achanged$generateTextures(root, hsb, FastColor.ARGB32.color(41, 39, 39), "_darkltx");
        achanged$generateTextures(root, hsb, FastColor.ARGB32.color(255, 255, 255), "_whiteltx");

        achanged$sprites.clear();
    }

    @Unique
    private static void achanged$generateTextures(File root, float[] hsb, int latex, String suffix){
        achanged$sprites.forEach((loc, sprite) -> {
            SpriteContents contents = sprite.contents();
            String file = loc.getNamespace() + "\\" + loc.getPath().replace("block/", "").replace('/', File.separatorChar) + suffix;
            File texture = new File(root, file + ".png");
            new File(texture.getParent()).mkdirs();

            NativeImage image;
            try {
                image = contents.getOriginalImage().mappedCopy(originalColor -> {
                    if(FastColor.ARGB32.alpha(originalColor) == 0) return originalColor;

                    Color.RGBtoHSB(FastColor.ARGB32.red(originalColor), FastColor.ARGB32.green(originalColor), FastColor.ARGB32.blue(originalColor), hsb);
                    int desaturated = Color.HSBtoRGB(hsb[0], hsb[1] * .3f, hsb[2] * .9f);

                    return a_changed$combineARGB(desaturated, latex, .75f, hsb);
                });

                image.writeToFile(texture);
                image.close();

                if(contents.metadata() == ResourceMetadata.EMPTY) return;
                JsonObject json = new JsonObject();
                achanged$writeTextureMeta(json, contents.metadata().getSection(TextureMetadataSection.SERIALIZER));
                achanged$writeAnimationMeta(json, contents.metadata().getSection(AnimationMetadataSection.SERIALIZER));

                if(json.isEmpty()) return;
                FileWriter writer = new FileWriter(new File(root, file + ".png.mcmeta"));
                writer.write(json.toString());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Unique
    private static final float[] a_changed$baseRGBA = new float[4];
    @Unique
    private static final float[] a_changed$addedRGBA = new float[4];

    @Unique
    private static int a_changed$combineARGB(int base, int added, float opacity, float[] mix){
        a_changed$baseRGBA[0] = FastColor.ARGB32.red(base);
        a_changed$baseRGBA[1] = FastColor.ARGB32.green(base);
        a_changed$baseRGBA[2] = FastColor.ARGB32.blue(base);
        a_changed$baseRGBA[3] = FastColor.ARGB32.alpha(base) / 255f;

        a_changed$addedRGBA[0] = FastColor.ARGB32.red(added);
        a_changed$addedRGBA[1] = FastColor.ARGB32.green(added);
        a_changed$addedRGBA[2] = FastColor.ARGB32.blue(added);
        a_changed$addedRGBA[3] = FastColor.ARGB32.alpha(added) / 255f;

        a_changed$combineColors(opacity, mix);

        return FastColor.ARGB32.color((int) (mix[3] * 255), (int) mix[0], (int) mix[1], (int) mix[2]);
    }

    //rgb in 0 - 255, a in 0 - 1
    @Unique
    private static void a_changed$combineColors(float opacity, float[] mix){
        if(a_changed$addedRGBA[3] * opacity == 1) {
            System.arraycopy(a_changed$addedRGBA, 0, mix, 0, 4);
            return;
        }

        float originalA = a_changed$addedRGBA[3];
        a_changed$addedRGBA[3] *= opacity;

        mix[3] = Mth.clamp(1 - (1 - a_changed$addedRGBA[3]) * (1 - a_changed$baseRGBA[3]), 0, 1); // alpha
        float aMix = a_changed$addedRGBA[3] / mix[3];
        float baseInvAMix = a_changed$baseRGBA[3] * (1 - a_changed$addedRGBA[3]) / mix[3];
        mix[0] = Math.round(a_changed$addedRGBA[0] * aMix + a_changed$baseRGBA[0] * baseInvAMix); // red
        mix[1] = Math.round(a_changed$addedRGBA[1] * aMix + a_changed$baseRGBA[1] * baseInvAMix); // green
        mix[2] = Math.round(a_changed$addedRGBA[2] * aMix + a_changed$baseRGBA[2] * baseInvAMix); // blue

        a_changed$addedRGBA[3] = originalA;
    }

    @Unique
    private static void achanged$writeTextureMeta(JsonObject json, Optional<TextureMetadataSection> optional){
        if(optional.isEmpty()) return;
        TextureMetadataSection meta = optional.get();

        JsonObject texture = new JsonObject();
        if(meta.isBlur()) texture.addProperty("blur", true);
        if(meta.isClamp()) texture.addProperty("clamp", true);

        if(texture.isEmpty()) return;
        json.add("texture", texture);
    }

    @Unique
    private static void achanged$writeAnimationMeta(JsonObject json, Optional<AnimationMetadataSection> optional){
        if(optional.isEmpty()) return;
        AnimationMetadataAccessor anim = (AnimationMetadataAccessor) optional.get();

        JsonObject animation = new JsonObject();
        if(anim.getDefaultFrameTime() != 1) animation.addProperty("frametime", anim.getDefaultFrameTime());
        if(anim.getFrameWidth() != -1) animation.addProperty("width", anim.getFrameWidth());
        if(anim.getFrameHeight() != -1) animation.addProperty("height", anim.getFrameHeight());
        if(anim.getInterpolatedFrames()) animation.addProperty("interpolate", true);

        List<AnimationFrame> frames = anim.getFrames();
        if(!frames.isEmpty()){
            JsonArray array = new JsonArray();
            JsonObject obj;
            for(AnimationFrame frame : frames){
                if(frame.getTime(-1) == -1) {
                    array.add(new JsonPrimitive(frame.getIndex()));
                    continue;
                }
                obj = new JsonObject();
                obj.addProperty("time", frame.getTime(-1));
                obj.addProperty("index", frame.getIndex());
                array.add(obj);
            }

            if(!array.isEmpty()) animation.add("frames", array);
        }

        json.add("animation", animation);
    }//TODO add setting to update loaded textures on level join
}