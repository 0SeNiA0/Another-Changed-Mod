package net.zaharenko424.cmrs.client.gui.screen;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.zaharenko424.cmrs.client.CustomModelManager;
import net.zaharenko424.cmrs.client.gui.LayoutHelper;
import net.zaharenko424.cmrs.client.gui.widget.*;
import net.zaharenko424.cmrs.api.ModelSetReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class PlayerOverviewScreen extends Screen implements MouseMoveListener {

    protected static final Comparator<CMWidget> BY_ID = Comparator.comparing(pair -> pair.entry.getModelId(), ResourceLocation::compareNamespaced);
    protected static final Comparator<CMWidget> BY_PRIORITY = Comparator.<CMWidget, Integer>comparing(pair -> pair.entry.priority()).reversed();

    protected final Cache cache;
    protected final RoundedTextField searchBar = new RoundedTextField();

    protected final ScrollableContainer modelContainer = (ScrollableContainer) new ScrollableContainer().setSize(600, 300);
    protected final List<CMWidget> models = new ArrayList<>();

    protected Screen previous;
    protected AbstractClientPlayer player;

    protected PlayerOverviewScreen(Cache cache) {
        super(Component.empty());
        this.cache = cache;

        searchBar
                .setOrigin(0, -150, 1)
                .setDefText(Component.literal("OwO")).setRoundingRadius(10)
                .setSize(230, 20).setOutlineThickness(2)
                .setOnContentsChanged(field -> sortSearch())
                .rebuildMesh();
    }

    public PlayerOverviewScreen set(@Nullable Screen previous, @NotNull AbstractClientPlayer player){
        this.previous = previous;
        this.player = player;
        return this;
    }

    protected void removeModel(CMWidget widget){
        modelContainer.removeWidget(widget);
        models.remove(widget);

        positionWidgets();
    }

    protected void sortSearch(){
        if(models.size() < 2) {
            positionWidgets();
            return;
        }

        String searchFor = searchBar.getText();

        if(searchFor.isBlank()) {
            models.sort(BY_PRIORITY);
            positionWidgets();
            return;
        }

        boolean modId = searchFor.indexOf(':') != -1;
        String modId0 = modId ? searchFor.substring(0, searchFor.indexOf(':')) : null;
        if(modId) searchFor = searchFor.split(":", 2)[1];


        List<CMWidget> tmp = new ArrayList<>();
        List<CMWidget> sort = new ArrayList<>();

        IntSet checked = new IntArraySet();
        CMWidget widget;
        String str;

        if(modId){//Filter out non-matching modIds
            for(int i = 0; i < models.size(); i++){
                widget = models.get(i);
                str = widget.entry.getModelId().getNamespace();
                if(str.equals(modId0)) continue;

                checked.add(i);
                sort.add(widget);
            }

            if(!sort.isEmpty()) {
                sort.sort(BY_ID);
                tmp.addAll(sort);
                sort.clear();
            }
        }

        for(int i = searchFor.length(); i >= 0 ; i--){
            str = searchFor.substring(0, i);

            for(int ii = 0; ii < models.size(); ii++){
                if(checked.contains(ii)) continue;
                widget = models.get(ii);

                if(!widget.entry.getModelId().getPath().contains(str)) continue;
                checked.add(ii);
                sort.add(widget);
            }

            if(sort.isEmpty()) continue;
            sort.sort(BY_ID);
            tmp.addAll(sort);
            sort.clear();
        }

        models.clear();
        models.addAll(tmp);

        positionWidgets();
    }

    protected void refresh(){
        List<CMWidget> pool = new ArrayList<>(models);
        models.clear();

        Set<CustomModelManager.ModelEntry> entries = CustomModelManager.getInstance().getQueuedModels(player);

        CMWidget cmw;
        for(CustomModelManager.ModelEntry entry : entries){
            if(player != Minecraft.getInstance().player && entry.reason() == ModelSetReason.MOD) continue;//Hide mod models if viewing others

            if(pool.isEmpty()){
                cmw = new CMWidget();
            } else {
                cmw = pool.removeLast();
            }

            models.add(cmw.set(entry, player, () -> Minecraft.getInstance().setScreen(cache.getModelOverview().set(this, entry.getModelId())), this::removeModel));
        }

        sortSearch();
    }

    protected void positionWidgets(){
        LayoutHelper.tileLayout(modelContainer, models, modelContainer.getWidth() * -.475f, modelContainer.getHeight() * -.5f, modelContainer.getWidth() * .9f, modelContainer.getWidth() * .015f, modelContainer.getHeight() * .025f);
    }

    @Override
    protected void init() {
        super.init();
        refresh();

        float scale = minecraft.getWindow().getWidth() / 1920f;
        WidgetContainer window = cache.getMainWindow();
        window.setScale(scale, scale, scale);
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
    public void onClose() {
        if(previous != null && player != Minecraft.getInstance().player) {
            Minecraft.getInstance().setScreen(previous);
            previous = null;
            return;
        }
        previous = null;
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected class CMWidget extends WidgetContainer {

        CustomModelManager.ModelEntry entry;
        final RoundedButton background = new RoundedButton().setSize(100, 150).setOrigin(0, 0, 150).setInsideColorFunc(w -> 0);
        final CustomModelWidget model = new CustomModelWidget()
                .setRotation(Mth.DEG_TO_RAD * -10, Mth.DEG_TO_RAD * -10).setOrigin(0, 0, 100);
        final RoundedTextField id = new RoundedTextField().setOrigin(0, 50 ,200).setSize(80, 20);
        final RoundedTextField priority = new RoundedTextField().setOrigin(30, 10 ,200).setSize(30, 20);
        final RoundedTextField reason = new RoundedTextField().setOrigin(25, 30 ,200).setSize(40, 20);
        final RoundedButton removeModel = new RoundedButton().setOrigin(35, -60, 200).setRoundingRadius(8).setSize(0, 0);

        CMWidget(){
            setSize(100, 150);

            background.setOutlineColorFunc(b ->{
                removeModel.setVisible((cache.isDevMode()
                        || (player == Minecraft.getInstance().player && entry.reason() == ModelSetReason.LOCAL)) && isHovering());
                return isHovering() ? Color.GREEN.getRGB() : RoundedRectWidget.defOutlineColor;
            });
            background.rebuildMesh();
            id.rebuildMesh();
            priority.rebuildMesh();
            reason.rebuildMesh();

            removeModel.setText(Component.literal("-").withStyle(ChatFormatting.RED)).setVisible(false);
            removeModel.rebuildMesh();

            addWidget(background);
            addWidget(model);
            addWidget(id);
            addWidget(priority);
            addWidget(reason);
            addWidget(removeModel);
            init();
        }

        CMWidget set(CustomModelManager.ModelEntry entry, AbstractClientPlayer player, Runnable screenOpener, Consumer<CMWidget> remove){
            this.entry = entry;
            model.setModelId(entry.getModelId());
            background.setOnClick((button, click) -> {
                screenOpener.run();
                return true;
            });

            id.setDefText(Component.literal(entry.getModelId().toString()));
            priority.setDefText(Component.empty().append(Component.literal("⮝").withStyle(ChatFormatting.GREEN)).append(Component.literal(String.valueOf(entry.priority()))));
            reason.setDefText(Component.literal(entry.reason().toString()));

            removeModel.setOnClick((button, key) -> {
                CustomModelManager.getInstance().removePlayerModel(player, entry);
                remove.accept(this);
                return true;
            });

            return this;
        }
    }
}
