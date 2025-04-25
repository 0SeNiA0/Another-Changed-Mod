package net.zaharenko424.a_changed.client.cmrs.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.RoundedButton;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.RoundedRectWidget;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.WidgetContainer;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.WidgetHelper;

public class Cache {

    protected final Minecraft minecraft = Minecraft.getInstance();
    protected ModelOverviewScreen modelOverview;

    protected PlayerOverviewScreen localPlayerS;
    protected ModelManagerScreen modelListS;

    protected final RoundedRectWidget background = new RoundedRectWidget().setSize(600, 400).setRoundingRadius(25);
    protected final RoundedButton localPlayer = new RoundedButton();
    protected final RoundedButton modelList = new RoundedButton();
    protected final RoundedButton settings = new RoundedButton();
    protected final WidgetContainer window = new WidgetContainer().setSize(600, 400);

    public Cache(){
        background.rebuildMesh();
        localPlayer
                .setOrigin(window.getWidth() * -.3f, -window.getHeight() / 2f + 20, 1)
                .setSize(80, 20).setOutlineThickness(2).setRoundingRadius(10)
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f, widget -> minecraft.screen == localPlayerS || widget.isHovering()))
                .setText(Component.literal("Your Models"))
                .setOnClick((button, click) -> {
                    if(minecraft.screen == localPlayerS) return false;
                    minecraft.setScreen(getLocalPlayer().set(null, minecraft.player));
                    return true;
                })
                .rebuildMesh();

        modelList
                .setOrigin(0, -window.getHeight() / 2f + 20, 1)
                .setSize(80, 20).setOutlineThickness(2).setRoundingRadius(10)
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f, widget -> minecraft.screen == modelListS || widget.isHovering()))
                .setText(Component.literal("All Models"))
                .setOnClick((button, click) -> {
                    if(minecraft.screen == modelListS) return false;
                    minecraft.setScreen(getModelManager());
                    return true;
                })
                .rebuildMesh();

        settings
                .setOrigin(window.getWidth() * .3f, -window.getHeight() / 2f + 20, 1)
                .setSize(80, 20).setOutlineThickness(2).setRoundingRadius(10)
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f))
                .setText(Component.literal("Settings(WIP)"))
                .setOnClick((button, click) -> false)
                .rebuildMesh();
    }

    public boolean isDevMode(){
        return false;
    }

    public WidgetContainer getMainWindow(){
        window.clearWidgets();

        window.addWidget(background);
        window.addWidget(localPlayer);
        window.addWidget(modelList);
        window.addWidget(settings);

        return window;
    }

    public ModelOverviewScreen getModelOverview(){
        if(modelOverview == null) modelOverview = new ModelOverviewScreen();
        return modelOverview;
    }

    public PlayerOverviewScreen getLocalPlayer() {
        if(localPlayerS == null) localPlayerS = new PlayerOverviewScreen(this);
        return localPlayerS;
    }

    public ModelManagerScreen getModelManager() {
        if(modelListS == null) modelListS = new ModelManagerScreen(this);
        return modelListS;
    }
}
