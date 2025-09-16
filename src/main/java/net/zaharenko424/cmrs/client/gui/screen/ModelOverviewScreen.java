package net.zaharenko424.cmrs.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.zaharenko424.cmrs.client.gui.widget.CustomModelWidget;
import net.zaharenko424.cmrs.client.gui.widget.RoundedRectWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModelOverviewScreen extends Screen implements MouseMoveListener {

    protected Screen previous;
    protected CustomModelWidget model;
    protected final RoundedRectWidget window = new RoundedRectWidget().setSize(600, 400).setRoundingRadius(25);

    protected ModelOverviewScreen() {
        super(Component.empty());
        window.rebuildMesh();
        model = (CustomModelWidget) new CustomModelWidget().setZoom(100).setInteractable(true);
    }

    public ModelOverviewScreen set(@Nullable Screen previous, @NotNull ResourceLocation modelId){
        this.previous = previous;
        model.setModelId(modelId)
                .setRotation(Mth.DEG_TO_RAD * -10, Mth.DEG_TO_RAD * -10)
                .setZoom(100).setTranslation(0, 0, 0);
        return this;
    }

    @Override
    protected void init() {
        super.init();

        float scale = minecraft.getWindow().getWidth() / 1920f;
        model.setScale(scale, scale, scale);
        model.setOrigin(width / 2f, height / 2f, 0)
                .setSize(width / 3f, height / 3f * 2);
        window.setScale(scale, scale, scale);

        addRenderableWidget(model);
        addRenderableWidget(window.setOrigin(width / 2f, height / 2f, -1000));
    }

    @Override
    public void onClose() {
        if(previous != null) {
            Minecraft.getInstance().setScreen(previous);
            previous = null;
        } else super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
