package net.zaharenko424.cmrs.client;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
import net.zaharenko424.cmrs.util.StreamCodecUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public class ModelPropertyManager {

    private static ModelPropertyManager INSTANCE;
    private static final File PROPERTIES = new File(FMLPaths.CONFIGDIR.get().toFile(), CMRS.MODID + "_model_properties.bin");
    private static final StreamCodec<FriendlyByteBuf, Map<String, Map<String, ModelProperty>>> PER_SERVER_MAP = ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ModelPropertyRegistry.MAP);

    private Connection server;

    private final Map<String, ModelProperty> mainProperties = new HashMap<>();
    private final Map<String, Map<String, ModelProperty>> perServerProperties = new HashMap<>();
    //Map<ResourceLocation, Map<String, ModelProperty>> perModelProperties;

    private byte[] data;

    private Map<String, ModelProperty> bound;

    private ModelPropertyManager(){
        if(!PROPERTIES.exists()) {
            data = null;
            load();
            return;
        }

        try(FileInputStream fIn = new FileInputStream(PROPERTIES)) {
            data = fIn.readAllBytes();
        } catch (IOException e) {
            CMRS.LOGGER.error("Failed to read model properties file.", e);

            data = null;
            load();
            return;
        }

        load();
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
        save();
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

    public void load(){
        if(data == null) {
            mainProperties.clear();
            perServerProperties.clear();
            return;
        }

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));

        Map<String, ModelProperty> main = StreamCodecUtils.readOptionally(buf, ModelPropertyRegistry.MAP, HashMap::new);
        mainProperties.clear();
        if(main != null) mainProperties.putAll(main);

        Map<String, Map<String, ModelProperty>> perServer = StreamCodecUtils.readOptionally(buf, PER_SERVER_MAP, HashMap::new);
        perServerProperties.clear();
        if(perServer != null) perServerProperties.putAll(perServer);

        bindMain();
    }

    public void save(){
        if(mainProperties.isEmpty() && perServerProperties.isEmpty()){
            PROPERTIES.delete();
            data = null;
            return;
        }

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        StreamCodecUtils.writeOptionally(mainProperties, !mainProperties.isEmpty(), buf, ModelPropertyRegistry.MAP);

        perServerProperties.entrySet().removeIf(map -> map.getValue().isEmpty());
        StreamCodecUtils.writeOptionally(perServerProperties, !perServerProperties.isEmpty(), buf, PER_SERVER_MAP);

        data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        buf.release();

        try(FileOutputStream fOut = new FileOutputStream(PROPERTIES)) {
            fOut.write(data);
        } catch (IOException e) {
            CMRS.LOGGER.error("Failed to save model properties.", e);
        }
    }
}
