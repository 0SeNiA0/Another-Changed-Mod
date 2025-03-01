package net.zaharenko424.a_changed.util;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.client.cmrs.CustomModelManager;
import net.zaharenko424.a_changed.client.cmrs.network.ModelSetReason;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransfurUtilsClient {
    /**
     * Applies the new transfur model to the provided player and returns its id.
     *
     * @param modelIdO Current modelId
     * @return New modelId
     */
    @Contract("_, _, null -> null")
    public static ResourceLocation updateTFModel(@NotNull AbstractClientPlayer player, @Nullable ResourceLocation modelIdO, @Nullable TransfurType transfurType){
        ResourceLocation modelId = null;
        if(transfurType != null){
            modelId = transfurType.getModelIdFor(player);//Leave it like this for now. Might be a problem if getModelId() will check for ability data that isn't synced yet
        }
        if(modelIdO != modelId){
            CustomModelManager manager = CustomModelManager.getInstance();
            if(modelIdO != null) manager.removePlayerModel(player, modelIdO);
            if(modelId != null) manager.setPlayerModel(player, modelId, 1, true, ModelSetReason.MOD);
        }//Will throw if the model isn't registered ^
        return modelId;
    }
}
