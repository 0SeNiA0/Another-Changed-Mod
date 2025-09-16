package net.zaharenko424.a_changed.mixin.client.latex;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoader;
import net.zaharenko424.a_changed.event.custom.AddSpritesToAtlasEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(SpriteLoader.class)
public abstract class MixinSpriteLoader {

    @ModifyReceiver(at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;thenApply(Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;"),
            method = "loadAndStitch(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/ResourceLocation;ILjava/util/concurrent/Executor;Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;")
    private <T, U> CompletableFuture<List<SpriteContents>> onLoadAndStitch(CompletableFuture<List<SpriteContents>> instance, Function<? super T, ? extends U> fn, @Local(argsOnly = true) ResourceLocation atlasLocation, @Local SpriteResourceLoader loader){
        return instance.thenApply(sprites -> {
            List<SpriteContents> list = new ArrayList<>();
            ModLoader.postEvent(new AddSpritesToAtlasEvent(atlasLocation, sprites, loader, list));
            if(list.isEmpty()) return sprites;
            list.addAll(sprites);
            return List.copyOf(list);
        });
    }
}