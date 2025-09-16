package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.cmrs.client.CustomModelManager;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;

public class CustomModelWidget extends ModelWidget {

    protected ResourceLocation modelId;

    @Override
    public CustomModelWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    @Override
    public CustomModelWidget setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    @Override
    public CustomModelWidget setSize(float width, float height) {
        super.setSize(width, height);
        return this;
    }

    @Override
    public CustomModelWidget setRotation(float radX, float radY) {
        super.setRotation(radX, radY);
        return this;
    }

    public ModelWidget setTranslation(float x, float y, float z){
        super.setTranslation(x, y, z);
        return this;
    }

    @Override
    public CustomModelWidget setZoom(float zoom) {
        super.setZoom(zoom);
        return this;
    }

    public ModelWidget setModelId(ResourceLocation modelId){
        this.modelId = modelId;
        return this;
    }

    public ResourceLocation getModelId() {
        return modelId;
    }

    protected void renderModel(PoseStack stack){
        Player player = Minecraft.getInstance().player;
        float yBody = player.yBodyRot;
        float yRot = player.getYRot();
        float xRot = player.getXRot();
        float yHead = player.yHeadRot;
        float yHeadO = player.yHeadRotO;

        player.yBodyRot = 0;
        player.setYRot(0);
        player.setXRot(0);
        player.yHeadRot = 0;
        player.yHeadRotO = 0;

        CustomModelManager.getInstance().renderModel(modelId, player, stack, LightTexture.FULL_BRIGHT);
        MultiBufferSource.getInstance().endBatch();

        player.yBodyRot = yBody;
        player.setYRot(yRot);
        player.setXRot(xRot);
        player.yHeadRot = yHead;
        player.yHeadRotO = yHeadO;
    }
}
