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
import net.zaharenko424.cmrs.client.gui.widget.*;
import net.zaharenko424.cmrs.api.ModelSetReason;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelManagerScreen extends Screen implements MouseMoveListener {

    protected static final Comparator<CMWidget> BY_ID = Comparator.comparing(w -> w.modelId, ResourceLocation::compareNamespaced);

    protected final Cache cache;
    protected final RoundedTextField searchBar = new RoundedTextField();

    protected final ScrollableContainer modelContainer = (ScrollableContainer) new ScrollableContainer().setSize(600, 300);
    protected final List<CMWidget> models = new ArrayList<>();
    protected AMWidget widget;

    public ModelManagerScreen() {
        this(new Cache());
    }

    protected ModelManagerScreen(Cache cache){
        super(Component.empty());
        this.cache = cache;
        cache.modelListS = this;

        searchBar
                .setOrigin(0, -150, 1)
                .setDefText(Component.literal("OwO")).setRoundingRadius(10)
                .setSize(230, 20).setOutlineThickness(2)
                .setOnContentsChanged(field -> sortSearch())
                .rebuildMesh();
    }

    protected void sortSearch(){
        String searchFor = searchBar.getText();

        if(searchFor.isBlank()) {
            models.sort(BY_ID);
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
                str = widget.modelId.getNamespace();
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

                if(!widget.modelId.getPath().contains(str)) continue;
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

        Set<ResourceLocation> set = CustomModelManager.getInstance().getLoadedModels();

        CMWidget cmw;
        for(ResourceLocation loc : set){
            if(pool.isEmpty()){
                cmw = new CMWidget();
            } else cmw = pool.removeLast();

            models.add(cmw.setModelId(loc, () -> Minecraft.getInstance().setScreen(cache.getModelOverview().set(this, loc)),
                    modelId -> showApplyModelWidget(Minecraft.getInstance().player, modelId)));
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
        for (CMWidget cmw : models) {
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

    protected void showApplyModelWidget(AbstractClientPlayer player, ResourceLocation modelId){
        if(widget == null) {
            widget = new AMWidget();
            widget.setOrigin(0, 0, 1000);
            cache.window.addWidget(widget);
            cache.window.sortWidgets();
        }
        widget.setVisible(true);
        //if(!cache.isDevMode() && player != Minecraft.getInstance().player) player = Minecraft.getInstance().player;
        widget.set((priority, remOnDeath) -> CustomModelManager.getInstance().setPlayerModel(player, modelId, priority, remOnDeath, ModelSetReason.LOCAL));
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
        if(widget != null) window.addWidget(widget);
        window.init();
        addRenderableWidget(window);
    }

    @Override
    public void onClose() {
        if(widget != null && widget.isVisible()){
            widget.setVisible(false);
            return;
        }
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected static class CMWidget extends WidgetContainer {

        ResourceLocation modelId;
        final RoundedButton background = new RoundedButton().setSize(100, 150).setOrigin(0, 0, 150).setInsideColorFunc(w -> 0);
        final CustomModelWidget model = new CustomModelWidget()
                .setRotation(Mth.DEG_TO_RAD * -10, Mth.DEG_TO_RAD * -10).setOrigin(0, 0, 100);
        final RoundedTextField textField = new RoundedTextField().setOrigin(0, 50 ,200).setSize(80, 20);
        final RoundedButton applyModel = new RoundedButton().setOrigin(35, -60, 200).setRoundingRadius(8).setSize(0, 0);

        CMWidget(){
            setSize(100, 150);
            background.setOutlineColorFunc(b ->{
                applyModel.setVisible(isHovering());
                return isHovering() ? Color.GREEN.getRGB() : RoundedRectWidget.defOutlineColor;
            });
            background.rebuildMesh();
            textField.setClickThrough(true);
            textField.rebuildMesh();
            applyModel.setText(Component.literal("+").withStyle(ChatFormatting.GREEN)).setVisible(false);
            applyModel.rebuildMesh();

            addWidget(background);
            addWidget(model);
            addWidget(textField);
            addWidget(applyModel);
            init();
        }

        CMWidget setModelId(ResourceLocation modelId, Runnable screenOpener, Consumer<ResourceLocation> func){
            this.modelId = modelId;
            background.setOnClick((button, click) -> {
                screenOpener.run();
                return true;
            });
            model.setModelId(modelId);
            textField.setDefText(Component.literal(modelId.toString()));
            applyModel.setOnClick((button, click) -> {
                func.accept(modelId);
                return true;
            });

            return this;
        }
    }

    protected static class AMWidget extends WidgetContainer {

        final RoundedRectWidget background = new RoundedRectWidget().setSize(200, 100);
        final RoundedTextField hint = new RoundedTextField().setDefText(Component.literal("Priority")).setOrigin(-30, 0, 1).setSize(60, 20);
        final RoundedTextField priority = new RoundedTextField().insertAtCursor("0").setOrigin(30, 0, 1).setSize(40, 20);
        //TODO checkbox remove on death?
        final RoundedButton apply = new RoundedButton().setOrigin(-50, 30, 1).setSize(60, 20);
        final RoundedButton cancel = new RoundedButton().setOrigin(50, 30, 1).setSize(60, 20)
                .setOnClick((button, click) -> {
                    setVisible(false);
                    return true;
                });

        AMWidget(){
            setSize(200, 100);
            background.rebuildMesh();
            priority.rebuildMesh();
            hint.setClickThrough(true);
            apply.setText(Component.literal("Apply model").withStyle(ChatFormatting.GREEN));
            apply.rebuildMesh();
            cancel.setText(Component.literal("Cancel").withStyle(ChatFormatting.RED));
            cancel.rebuildMesh();

            addWidget(background);
            addWidget(hint);
            addWidget(priority);
            addWidget(apply);
            addWidget(cancel);
            init();
        }

        void set(BiConsumer<Integer, Boolean> modelApplier){
            apply.setOnClick((button, click) -> {
                int i;
                try {
                    i = Integer.parseInt(priority.getText());
                } catch (NumberFormatException e) {
                    return false;
                }
                modelApplier.accept(i, false);
                setVisible(false);
                return true;
            });
        }
    }
}
