package net.zaharenko424.cmrs.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.ModelProperties;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.network.packets.ServerboundModelPropertySync;
import net.zaharenko424.cmrs.property.ModelPropertyType;
import net.zaharenko424.cmrs.registry.AttachmentRegistry;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public class ModelPropertyManager {

    private static final Codec<ModelProperty> JSON_CODEC = ResourceLocation.CODEC.dispatch(
            "type",
            property -> property.type().getId(),
            loc -> {
                ModelPropertyType<?> type = ModelPropertyRegistry.PROPERTY_REGISTRY.get(loc);

                if(type == null) throw new IllegalStateException("No ModelPropertyType is registered under " + loc);
                return type.jsonCodec().fieldOf("property");
            }
    );
    private static final Codec<Map<String, ModelProperty>> JSON_MAP = Codec.dispatchedMap(
            Codec.STRING, str -> JSON_CODEC
    );
    private static final Codec<Map<String, Map<String, ModelProperty>>> JSON_PER_SERVER = Codec.dispatchedMap(
            Codec.STRING, str -> JSON_MAP
    );

    private static ModelPropertyManager INSTANCE;
    private static final File PROPERTIES = new File(FMLPaths.CONFIGDIR.get().toFile(), CMRS.MODID + "_model_properties.json");

    private Connection server;

    private final Map<String, ModelProperty> mainProperties = new HashMap<>();
    private final Map<String, Map<String, ModelProperty>> perServerProperties = new HashMap<>();
    //Map<ResourceLocation, Map<String, ModelProperty>> perModelProperties;

    private Map<String, ModelProperty> bound;

    private ModelPropertyManager(){
        loadJSONFromFile();
    }

    public static ModelPropertyManager getInstance(){
        if(INSTANCE == null) INSTANCE = new ModelPropertyManager();
        return INSTANCE;
    }

    private static final Map<String, ModelProperty> EMPTY = Map.of();
    public static Map<String, ModelProperty> getPropertiesOrEmpty(LivingEntity entity){
        if(!entity.hasData(AttachmentRegistry.MODEL_PROPERTIES)) return EMPTY;

        return entity.getData(AttachmentRegistry.MODEL_PROPERTIES).properties();
    }

    public Map<String, ModelProperty> getMainProperties(){
        return mainProperties;
    }

    public @Nullable Map<String, ModelProperty> getServerProperties(String server){
        return perServerProperties.get(server);
    }

    public Map<String, ModelProperty> getBound(){
        return bound;
    }

    public void createServerProperties(){
        if(server == null) return;
        createServerProperties(server.getLoggableAddress(true));
    }

    public void createServerProperties(String server){
        perServerProperties.put(server, new HashMap<>());
    }

    public void removeServer(String server){
        perServerProperties.remove(server);
    }



    public void bindMain(){
        bound = mainProperties;
    }

    public boolean bindServerProperties(String server){
        Map<String, ModelProperty> properties = getServerProperties(server);
        if(properties == null) return false;

        bound = properties;
        return true;
    }



    public boolean hasProperty(String key, ModelPropertyType<?> type){
        return getProperty(key, type) != null;
    }

    public boolean hasProperty(String key, DeferredHolder<ModelPropertyType<?>, ?> type){
        ModelProperty property = bound.get(key);

        return property != null && property.type().equals(type);
    }


    public <P extends ModelProperty> P getProperty(String key, ModelPropertyType<P> type){
        ModelProperty property = bound.get(key);

        if(property == null || property.type().get() != type) return null;
        return (P) property;
    }

    public <P extends ModelProperty> P getProperty(String key, DeferredHolder<ModelPropertyType<?>, ModelPropertyType<P>> type){
        ModelProperty property = bound.get(key);

        if(property == null || !property.type().equals(type)) return null;
        return (P) property;
    }


    public <P extends ModelProperty> ModelProperty setProperty(String key, P property){
        return bound.put(key, property);
    }


    public ModelProperty removeProperty(String key){
        return bound.remove(key);
    }

    public <P extends ModelProperty> P removeProperty(String key, ModelPropertyType<P> type){
        ModelProperty property = bound.get(key);
        if(property == null || property.type().get() != type) return null;

        return (P) bound.remove(key);
    }

    //----------------------------------------- Internal ----------------------------------------//

    @ApiStatus.Internal
    public void logIn(ClientPlayerNetworkEvent.LoggingIn event){
        server = event.getConnection();
        syncServer();
    }

    @ApiStatus.Internal
    public void clonePlayer(ClientPlayerNetworkEvent.Clone event){
        ModelProperties properties = event.getNewPlayer().getData(AttachmentRegistry.MODEL_PROPERTIES);
        updateClientProperties(properties);
    }

    @ApiStatus.Internal
    public void logOut(ClientPlayerNetworkEvent.LoggingOut event){
        server = null;
    }

    //------------------------------------------ Sync -------------------------------------------//

    public void saveAndSync(){
        syncServer();
        saveJSON();
    }

    public void syncServer(){
        if(server == null) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        ModelProperties properties = player.getData(AttachmentRegistry.MODEL_PROPERTIES);
        updateClientProperties(properties);

        if(!player.connection.hasChannel(ServerboundModelPropertySync.TYPE)) return;//TODO add toast that server does not have CMRS?
        PacketDistributor.sendToServer(new ServerboundModelPropertySync(properties.properties()));
    }

    private void updateClientProperties(ModelProperties properties){
        Map<String, ModelProperty> serverProperties = getServerProperties(server.getLoggableAddress(true));
        properties.set(serverProperties != null ? serverProperties : mainProperties);
    }

    //------------------------------------------- IO --------------------------------------------//

    public void loadJSONFromFile(){
        if(!PROPERTIES.exists()) {
            jsonData = null;
            loadJSON();
            return;
        }

        try (FileReader reader = new FileReader(PROPERTIES)){
            jsonData = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException | JsonParseException e) {
            CMRS.LOGGER.error("Failed to read model properties file.", e);

            jsonData = null;
            loadJSON();
            return;
        }

        loadJSON();
    }

    public void loadJSON() {
        if(jsonData == null){
            mainProperties.clear();
            perServerProperties.clear();
            bindMain();
            return;
        }

        mainProperties.clear();
        if(jsonData.has("main_properties")) {
            DataResult<Pair<Map<String, ModelProperty>, JsonElement>> main = JSON_MAP.decode(JsonOps.INSTANCE, jsonData.getAsJsonObject("main_properties"));
            if (main.hasResultOrPartial())
                mainProperties.putAll(main.getPartialOrThrow().getFirst());
        }

        perServerProperties.clear();
        if(jsonData.has("per_server_properties")) {
            DataResult<Pair<Map<String, Map<String, ModelProperty>>, JsonElement>> perServer = JSON_PER_SERVER.decode(JsonOps.INSTANCE, jsonData.getAsJsonObject("per_server_properties"));
            perServerProperties.clear();
            if (perServer.hasResultOrPartial())
                perServerProperties.putAll(perServer.getPartialOrThrow().getFirst());
        }
        bindMain();
    }

    JsonObject jsonData = null;

    public void saveJSON() {
        if(mainProperties.isEmpty() && perServerProperties.isEmpty()){
            PROPERTIES.delete();
            jsonData = null;
            return;
        }

        JsonObject json = new JsonObject();

        DataResult<JsonElement> main = JSON_MAP.encode(mainProperties, JsonOps.INSTANCE, new JsonObject());
        if(main.hasResultOrPartial()) json.add("main_properties", main.getPartialOrThrow());

        DataResult<JsonElement> perServer = JSON_PER_SERVER.encode(perServerProperties, JsonOps.INSTANCE, new JsonObject());
        if(perServer.hasResultOrPartial()) json.add("per_server_properties", perServer.getPartialOrThrow());

        jsonData = json;

        try(FileWriter out = new FileWriter(PROPERTIES)){
            JsonWriter writer = new JsonWriter(out);
            writer.setIndent("    ");

            Streams.write(jsonData, writer);

            writer.close();
        } catch (IOException e) {
            CMRS.LOGGER.error("Failed to save model properties.", e);
        }
    }
}
