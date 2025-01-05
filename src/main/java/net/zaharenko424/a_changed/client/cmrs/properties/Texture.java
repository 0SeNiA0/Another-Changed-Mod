package net.zaharenko424.a_changed.client.cmrs.properties;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class Texture {

    public static final StreamCodec<FriendlyByteBuf, Texture> CODEC = StreamCodec.of((buffer, texture) -> {
        buffer.writeBoolean(texture.embedded);
        buffer.writeResourceLocation(texture.location);
        if(texture.embedded) {
            try {
                buffer.writeByteArray(texture.image.asByteArray());
            } catch (IOException e) {
                throw new RuntimeException("Failed to write texture!", e);
            }
        }
        buffer.writeFloat(texture.scale);
    }, buffer -> {
        boolean embedded = buffer.readBoolean();
        ResourceLocation loc = buffer.readResourceLocation();
        if(embedded){
            NativeImage img;
            try {
                img = NativeImage.read(buffer.readByteArray());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read texture!", e);
            }
            return dynamic(loc, img, buffer.readFloat());
        }
        return fromAsset(loc, buffer.readFloat());
    });

    private boolean embedded;
    private ResourceLocation location;
    private NativeImage image;//TODO do not save the bitmap if not embedded/dynamic ?
    private int width;
    private int height;
    private float scale;

    public Texture(boolean embedded, @NotNull ResourceLocation location, NativeImage image, int width, int height, float scale){
        this.embedded = embedded;
        this.location = location;
        if(embedded && image == null) throw new IllegalArgumentException("Either embedded must be false or image not null!");
        this.image = image;
        this.width = width;
        this.height = height;
        this.scale = scale == 0 ? 1 : scale;
    }

    public static Texture fromAsset(@NotNull ResourceLocation location, float scale) {
        Texture texture = new Texture(false, location, null, 0, 0, scale);
        texture.refresh();
        return texture;
    }
//TODO register DynamicTexture here?
    public static Texture dynamic(@NotNull ResourceLocation location, @NotNull NativeImage image, float scale){
        return new Texture(true, location, image, image.getWidth(), image.getHeight(), scale);
    }

    public void refresh(){//TODO test whether minecraft actually loads the texture before every draw call
        if(embedded) return;
        Minecraft minecraft = Minecraft.getInstance();
        ResourceManager manager = minecraft.getResourceManager();
        Resource resource;
        try {
            resource = manager.getResourceOrThrow(location);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No texture found!", e);
        }
        NativeImage img;
        try(InputStream stream = resource.open()){
            img = NativeImage.read(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read texture!", e);
        }
        image = img;
        width = img.getWidth();
        height = img.getHeight();
    }

    /**
     * New location must be unique. Use custom namespace or add "_embedded" at the end.
     */
    public void embedWithLocation(ResourceLocation location){//Don't release old texture as it might be used by other models.
        this.location = location;
        refresh();
        Minecraft.getInstance().getTextureManager().register(location, new DynamicTexture(image));
        embedded = true;
    }

    public ResourceLocation getLocation(){
        return location;
    }

    public NativeImage getImage(){
        if(image == null) refresh();
        return image;
    }

    public int getWidth(){
        return getImage() != null ? image.getWidth() : width;
    }

    public int getHeight(){
        return getImage() != null ? image.getHeight() : height;
    }

    public float getScale(){
        return scale;
    }
}