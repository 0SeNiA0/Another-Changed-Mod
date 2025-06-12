package net.zaharenko424.a_changed.client.cmrs.gui.screen;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.*;
import org.jetbrains.annotations.ApiStatus;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@ApiStatus.Experimental
public class PlayerListScreen extends Screen implements MouseMoveListener {

    protected static final Comparator<PMWidget> BY_NAME = Comparator.comparing(pmw -> pmw.player.getName().getString(), String::compareToIgnoreCase);

    protected final Cache cache;
    protected PlayerOverviewScreen playerOverview;
    protected final RoundedTextField searchBar = new RoundedTextField();

    protected final ScrollableContainer modelContainer = (ScrollableContainer) new ScrollableContainer().setSize(600, 300);
    protected final List<PMWidget> models = new ArrayList<>();

    protected PlayerListScreen(Cache cache) {
        super(Component.empty());
        this.cache = cache;

        searchBar
                .setOrigin(0, -150, 1)
                .setDefText(Component.literal("OwO")).setRoundingRadius(10)
                .setSize(230, 20).setOutlineThickness(2)
                .setOnContentsChanged(field -> sortSearch())
                .rebuildMesh();
    }

    protected void sortSearch(){
        if(models.size() < 2) {
            positionWidgets();
            return;
        }

        String searchFor = searchBar.getText();

        if(searchFor.isBlank()) {
            models.sort(BY_NAME);
            positionWidgets();
            return;
        }

        List<PMWidget> tmp = new ArrayList<>();
        List<PMWidget> sort = new ArrayList<>();

        IntSet checked = new IntArraySet();
        PMWidget widget;
        String str;

        for(int i = searchFor.length(); i >= 0 ; i--){
            str = searchFor.substring(0, i);

            for(int ii = 0; ii < models.size(); ii++){
                if(checked.contains(ii)) continue;
                widget = models.get(ii);

                if(!widget.player.getName().getString().contains(str)) continue;
                checked.add(ii);
                sort.add(widget);
            }

            if(sort.isEmpty()) continue;
            sort.sort(BY_NAME);
            tmp.addAll(sort);
            sort.clear();
        }

        models.clear();
        models.addAll(tmp);

        positionWidgets();
    }

    protected PlayerOverviewScreen getPlayerOverview() {
        if(playerOverview == null) playerOverview = new PlayerOverviewScreen(cache);
        return playerOverview;
    }

    protected void refresh(){
        //minecraft.player.connection.getOnlinePlayerIds();//TODO if dev mode show all online players

        List<PMWidget> pool = new ArrayList<>(models);
        models.clear();

        List<AbstractClientPlayer> players = Minecraft.getInstance().level.players();

        PMWidget pmw;
        for(AbstractClientPlayer player : players){
            if(pool.isEmpty()){
                pmw = new PMWidget();
            } else pmw = pool.removeLast();

            models.add(pmw.set(player, () -> {
                if(player == Minecraft.getInstance().player){
                    Minecraft.getInstance().setScreen(cache.getLocalPlayer().set(null, player));
                } else Minecraft.getInstance().setScreen(getPlayerOverview().set(this, player));
            }));
        }

        sortSearch();
    }

    protected void positionWidgets(){
        positionWidgets(modelContainer.getWidth() * -.475f, modelContainer.getHeight() * -.5f, modelContainer.getWidth() * .9f, modelContainer.getWidth() * .015f, modelContainer.getHeight() * .025f);
    }

    protected void positionWidgets(float topX, float leftY, float width, float paddingX, float paddingY){
        float f = 0;
        float height = 0;
        float f1 = 0;
        for (PMWidget cmw : models) {
            if (width - f < cmw.getWidth()) {//do next row
                f = cmw.getWidth() + paddingX;
                height += f1 + paddingY;
                f1 = cmw.getHeight();

                cmw.setOrigin(topX + cmw.getWidth() / 2, leftY + height + cmw.getHeight() / 2, 0);
                continue;
            }

            cmw.setOrigin(topX + f + cmw.getWidth() / 2, leftY + height + cmw.getHeight() / 2, 0);
            f += cmw.getWidth() + paddingX;
            if (cmw.getHeight() > f1) f1 = cmw.getHeight();
        }

        modelContainer.setActualHeight(height + f1);
    }

    @Override
    protected void init() {
        super.init();
        refresh();

        WidgetContainer window = cache.getMainWindow();
        window.setOrigin(width / 2f, height / 2f, -1000);

        window.addWidget(searchBar);
        window.addWidget(modelContainer);

        modelContainer.clearWidgets();
        modelContainer.setOrigin(0, window.getHeight() * .075f, 100);
        models.forEach(modelContainer::addWidget);

        modelContainer.init();
        window.init();
        addRenderableWidget(window);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected static class PMWidget extends WidgetContainer {

        AbstractClientPlayer player;
        final RoundedButton background = new RoundedButton().setSize(100, 150).setOrigin(0, 0, 150).setInsideColorFunc(w -> 0);
        final PlayerModelWidget model = new PlayerModelWidget().setRotation(Mth.DEG_TO_RAD * -10, Mth.DEG_TO_RAD * -10).setOrigin(0, 0, 100);
        final RoundedTextField name = new RoundedTextField().setOrigin(0, 50 ,200).setSize(80, 20);

        PMWidget(){
            setSize(100, 150);

            background.setOutlineColorFunc(b -> isHovering() ? Color.GREEN.getRGB() : RoundedRectWidget.defOutlineColor);
            background.rebuildMesh();
            name.rebuildMesh();

            addWidget(background);
            addWidget(model);
            addWidget(name);
            init();
        }

        PMWidget set(AbstractClientPlayer player, Runnable screenOpener){
            this.player = player;
            model.setPlayer(player);
            name.setDefText(player.getName());
            background.setOnClick((button, click) -> {
                screenOpener.run();
                return true;
            });

            return this;
        }
    }
}
