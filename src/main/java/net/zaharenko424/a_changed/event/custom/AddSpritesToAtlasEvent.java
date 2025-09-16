package net.zaharenko424.a_changed.event.custom;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.resource.EmptyPackResources;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Fired when sprites are collected for the atlas stitching.
 */
@ParametersAreNonnullByDefault
public class AddSpritesToAtlasEvent extends Event implements IModBusEvent {

    public static final ResourceLocation BANNER_ATLAS = ResourceLocation.withDefaultNamespace("banner_patterns");
    public static final ResourceLocation BED_ATLAS = ResourceLocation.withDefaultNamespace("beds");
    public static final ResourceLocation CHEST_ATLAS = ResourceLocation.withDefaultNamespace("chests");
    public static final ResourceLocation SHIELD_PATTERN_ATLAS = ResourceLocation.withDefaultNamespace("shield_patterns");
    public static final ResourceLocation SIGN_ATLAS = ResourceLocation.withDefaultNamespace("signs");
    public static final ResourceLocation SHULKER_BOX_ATLAS = ResourceLocation.withDefaultNamespace("shulker_boxes");
    public static final ResourceLocation ARMOR_TRIM_ATLAS = ResourceLocation.withDefaultNamespace("armor_trims");
    public static final ResourceLocation DECORATED_POT_ATLAS = ResourceLocation.withDefaultNamespace("decorated_pot");
    public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.withDefaultNamespace("blocks");

    private static final EmptyPackResources DUMMY = new EmptyPackResources(new PackLocationInfo("dummy", Component.empty(), PackSource.DEFAULT, Optional.empty()), new PackMetadataSection(Component.empty(), -1, Optional.empty()));

    private final ResourceLocation atlasLocation;
    private final List<SpriteContents> loadedSprites;
    private final SpriteResourceLoader loader;
    private final List<SpriteContents> list;

    public AddSpritesToAtlasEvent(ResourceLocation atlasLocation, List<SpriteContents> loadedSprites, SpriteResourceLoader loader, List<SpriteContents> list){
        this.atlasLocation = atlasLocation;
        this.loadedSprites = loadedSprites;
        this.loader = loader;
        this.list = list;
    }

    /**
     * Use non-static ResourceLocations from ModelManager.VANILLA_ATLASES for determining the atlas.
     */
    public ResourceLocation getAtlasLocation(){
        return atlasLocation;
    }

    public List<SpriteContents> getLoadedSprites() {
        return loadedSprites;
    }

    private Resource createResource(PackResources resources, IoSupplier<InputStream> streamSupplier, @Nullable IoSupplier<ResourceMetadata> metadataSupplier){
        return metadataSupplier != null ? new Resource(resources, streamSupplier, metadataSupplier)
                : new Resource(resources, streamSupplier);
    }

    public IoSupplier<ResourceMetadata> streamToResource(IoSupplier<InputStream> streamSupplier){
        return () -> ResourceMetadata.fromJsonStream(streamSupplier.get());
    }

    public void addSprite(ResourceLocation resourceLocation, IoSupplier<InputStream> streamSupplier, @Nullable IoSupplier<ResourceMetadata> metadataSupplier){
        SpriteContents contents = loader.loadSprite(resourceLocation, createResource(DUMMY, streamSupplier, metadataSupplier));
        if(contents != null) addSprite(contents);
    }

    public void addSprite(ResourceLocation resourceLocation, PackResources resources, IoSupplier<InputStream> streamSupplier, @Nullable IoSupplier<ResourceMetadata> metadataSupplier){
        SpriteContents contents = loader.loadSprite(resourceLocation, createResource(resources, streamSupplier, metadataSupplier));
        if(contents != null) addSprite(contents);
    }

    public void addSprite(ResourceLocation resourceLocation, Resource resource){
        SpriteContents contents = loader.loadSprite(resourceLocation, resource);
        if(contents != null) addSprite(contents);
    }

    public void addSprite(SpriteContents sprite){
        list.add(sprite);
    }
}
