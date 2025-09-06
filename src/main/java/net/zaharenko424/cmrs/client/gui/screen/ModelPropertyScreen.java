package net.zaharenko424.cmrs.client.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.client.ModelPropertyManager;
import net.zaharenko424.cmrs.client.gui.LayoutHelper;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.client.gui.widget.*;
import net.zaharenko424.cmrs.property.IntProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ModelPropertyScreen extends Screen implements MouseMoveListener {

    protected static boolean changed = false;

    protected final Cache cache;

    protected final RoundedRectWidget profilesBg = new RoundedRectWidget().setOrigin(-200, 0, 0).setSize(150, 300);
    protected final ScrollableContainer profiles = (ScrollableContainer) new ScrollableContainer().setOrigin(-200, 0, 1).setSize(150, 300);
    protected List<Profile> profilesL = new ArrayList<>();
    protected Profile selectedProfile;

    protected final List<Key> propertyKeys = new ArrayList<>();
    protected Key selectedProperty;

    protected final Property propertyScreen = (Property) new Property().setOrigin(75, 0, 1);

    protected final Input input = (Input) new Input().setOrigin(0, 0, 1000);
    protected final DoubleInput doubleInput = (DoubleInput) new DoubleInput().setOrigin(0, 0, 1000);

    protected final RoundedButton loadFromFile = new RoundedButton().setOrigin(-200, 160, 1).setSize(100, 20)
            .setText(Component.literal("Load from file")).setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f));
    protected final RoundedButton revert = new RoundedButton().setOrigin(-25, 160, 1).setSize(50, 20)
            .setText(Component.literal("Revert")).setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f));
    protected final RoundedButton save = new RoundedButton().setOrigin(75, 160, 1).setSize(50, 20)
            .setText(Component.literal("Save")).setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f));

    protected ModelPropertyScreen(Cache cache) {
        super(Component.empty());
        this.cache = cache;

        profilesBg.rebuildMesh();

        Profile main = new Profile().set(Component.literal("Main properties"), ModelPropertyManager.getInstance()::bindMain);
        profilesL.add(main);
        selectedProfile = main;

        propertyScreen.setVisible(false);

        loadFromFile.setOnClick((button, key) -> {
            if(key == null) return false;

            String selectedO = selectedProperty != null ? selectedProperty.key : null;
            ModelPropertyManager.getInstance().loadJSONFromFile();
            changed = false;

            updateProfiles();
            if(selectedO != null) trySelectProperty(selectedO);
            return true;
        });
        loadFromFile.rebuildMesh();

        revert.setOnClick((button, key) -> {
            if(key == null) return false;

            String selectedO = selectedProperty != null ? selectedProperty.key : null;
            ModelPropertyManager.getInstance().loadJSON();
            changed = false;

            updateProfiles();
            if(selectedO != null) trySelectProperty(selectedO);
            return true;
        });
        revert.rebuildMesh();

        save.setOnClick((button, key) -> {
            if(key == null) return false;

            ModelPropertyManager.getInstance().saveAndSync();
            changed = false;
            return true;
        });
        save.rebuildMesh();
    }

    @Override
    protected void init() {
        float scale = minecraft.getWindow().getWidth() / 1920f;
        WidgetContainer window = cache.getMainWindow();
        window.setScale(scale, scale, scale);
        window.setOrigin(width / 2f, height / 2f, -1000);

        window.addWidget(profilesBg);
        window.addWidget(propertyScreen);

        updateProfiles();
        window.addWidget(profiles);

        updateProfiles();

        window.addWidget(input);
        window.addWidget(doubleInput);
        window.addWidget(loadFromFile);
        window.addWidget(revert);
        window.addWidget(save);

        window.init();
        addRenderableWidget(window);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        revert.setVisible(changed);
        save.setVisible(changed);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    void selectProfile(Profile profile){
        if(selectedProfile == profile) return;

        selectedProfile = profile;
        profile.bind.run();
        updateProfiles();
    }

    void trySelectProperty(String name){
        Key key = null;
        for(Key k : propertyKeys){
            if(!k.key.equals(name)) continue;

            key = k;
            break;
        }

        selectProperty(key);
    }

    void selectProperty(Key property){
        if(selectedProperty == property) return;
        selectedProperty = property;

        if(property == null){
            propertyScreen.setVisible(false);
            propertyScreen.clear();
            return;
        }

        propertyScreen.setVisible(true);
        propertyScreen.set(property.key, property.value);
    }

    <W extends Widget & SizedWidget> void updateProfiles(){
        List<Key> keyCache = new ArrayList<>(propertyKeys);
        propertyKeys.clear();

        Map<String, ModelProperty> propertyMap = ModelPropertyManager.getInstance().getBound();
        for(Map.Entry<String, ModelProperty> entry : propertyMap.entrySet()){
            propertyKeys.add(!keyCache.isEmpty() ? keyCache.removeLast().set(entry) : new Key().set(entry));
        }

        List<W> l = new ArrayList<>();
        for (Profile profile : profilesL){
            l.add((W) profile);
            if(selectedProfile != profile) continue;

            for(Key propKey : propertyKeys){
                l.add((W) propKey);
            }
        }

        profiles.clearWidgets();
        LayoutHelper.listLayout(profiles, l, 10, 0);
        profiles.init();
    }

    protected void getInput(Component hint, Predicate<String> callback){
        input.set(hint, callback);
        input.setVisible(true);
    }

    protected void getDoubleInput(Component hint, Component secondHint, BiPredicate<String, String> callback){
        doubleInput.set(hint, secondHint, callback);
        doubleInput.setVisible(true);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == InputConstants.KEY_ESCAPE){
            if(input.isVisible()){
                input.set(null, null);
                input.setVisible(false);
                return true;
            }

            if(doubleInput.isVisible()){
                doubleInput.set(null, null, null);
                doubleInput.setVisible(false);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected class Profile extends WidgetContainer {

        final RoundedButton bindB = new RoundedButton();
        final RoundedButton addProperty = new RoundedButton().setOrigin(60, 0, 1).setSize(0, 0).setRoundingRadius(6).setOutlineThickness(1.5f).setText(Component.literal("+").withStyle(ChatFormatting.GREEN));

        Runnable bind;

        public Profile(){
            setSize(140, 20);
            setOrigin(0, 0, 1);

            bindB.setSize(getWidth(), getHeight()).setRoundingRadius(0);
            bindB.setOnClick((button, key) -> {
                selectProfile(this);
                return true;
            }).setRenderTransform((button, stack) ->
                addProperty.setVisible(selectedProfile == this)
            ).rebuildMesh();
            addWidget(bindB);

            addProperty.setOnClick((button, click) -> {
                if(click == null) return false;

                getDoubleInput(Component.literal("Property name"), Component.literal("Value"), (str1, str2) -> {
                    if(str1.isBlank() || str2.isBlank()) return false;

                    int val;
                    try{
                        val = Integer.parseInt(str2);
                    } catch (NumberFormatException e) {
                        return false;
                    }

                    ModelPropertyManager.getInstance().setProperty(str1, new IntProperty(val));
                    changed = true;

                    updateProfiles();
                    return true;
                });
                return true;
            }).rebuildMesh();
            addWidget(addProperty);

            init();
        }

        public Profile set(Component text, Runnable bind){
            bindB.setText(text);
            this.bind = bind;
            return this;
        }
    }

    protected class Key extends WidgetContainer {

        private static final RoundedTextField minus = (RoundedTextField) new RoundedTextField().insertAtCursor("-")
                .setSize(8, 8).setRoundingRadius(0).setClickThrough(true).setOrigin(-60, 0, 0);

        String key;
        ModelProperty value;

        final RoundedButton bg = new RoundedButton().setRoundingRadius(0);
        final RoundedTextField keyF = new RoundedTextField();

        final RoundedButton rename = new RoundedButton();
        final RoundedButton remove = new RoundedButton();

        protected Key(){
            setSize(140, 16);

            bg.setSize(getWidth(), getHeight()).setOnClick((button, key) -> {
                if(key == null) return false;

                selectProperty(this);
                return true;
            });
            addWidget(bg);

            addWidget(minus);

            keyF.setOrigin(-15, 0, 0).setSize(80, getHeight()).setClickThrough(true);
            addWidget(keyF);

            rename.setOrigin(40, 0, 1).setSize(20, 12);
            rename.setText(Component.literal("aI").withColor(Color.GREEN.getRGB()));
            rename.setOnClick((button, key) -> {
                if(key == null) return false;
                getInput(Component.literal("Rename property: " + this.key), str -> {
                    if(str.isBlank()) return false;
                    ModelPropertyManager.getInstance().removeProperty(this.key, value.type().get());
                    ModelPropertyManager.getInstance().setProperty(str, value);
                    changed = true;

                    this.key = str;
                    keyF.clearText().insertAtCursor(str);
                    if(selectedProperty == this) propertyScreen.set(this.key, value);
                    return true;
                });
                return true;
            });
            rename.setOutlineThickness(1.5f);
            rename.rebuildMesh();
            addWidget(rename);

            remove.setOutlineThickness(1.5f);
            remove.setOrigin(60, 0, 1).setRoundingRadius(6).setSize(0, 0);
            remove.setText(Component.literal("X").withColor(Color.RED.getRGB()));
            remove.setOnClick((button, key) -> {
                if(key == null) return false;
                ModelPropertyManager.getInstance().removeProperty(this.key, value.type().get());
                changed = true;

                if(selectedProperty == this) selectProperty(null);
                updateProfiles();
                return true;
            });
            remove.rebuildMesh();
            addWidget(remove);

            init();
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            boolean visible = bg.isHovering() || rename.isHovering() || remove.isHovering();
            rename.setVisible(visible);
            remove.setVisible(visible);
            super.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        public Key set(Map.Entry<String, ModelProperty> entry){
            rename.setVisible(false);
            remove.setVisible(false);

            key = entry.getKey();
            keyF.clearText();
            keyF.insertAtCursor(key);

            value = entry.getValue();
            return this;
        }
    }

    protected class Property extends WidgetContainer {

        String key;
        ModelProperty property;

        final RoundedTextField fProperty = new RoundedTextField().setOrigin(-50, -140, 0).insertAtCursor("Property");
        final RoundedTextField fName = new RoundedTextField().setOrigin(-150, -120, 0).setSize(50, 16).insertAtCursor("Name: ");
        final RoundedTextField fType = new RoundedTextField().setOrigin(-150, -104, 0).setSize(50, 16).insertAtCursor("Type: ");
        final RoundedTextField fValue = new RoundedTextField().setOrigin(-150, -88, 0).setSize(50, 16).insertAtCursor("Value: ");

        final RoundedTextField name = new RoundedTextField().setOrigin(-25, -120, 0).setSize(200, 16).setEditable(false);
        final RoundedTextField type = new RoundedTextField().setOrigin(-25, -104, 0).setSize(200, 16).setEditable(false);
        final RoundedTextField value = new RoundedTextField().setOrigin(-25, -88, 0).setSize(200, 16).setEditable(false);
        final RoundedButton modify = new RoundedButton().setOrigin(190, -88, 1).setSize(0,0).setRoundingRadius(8).setOutlineThickness(1.5f).setText(Component.literal("✎"));

        public Property(){
            setSize(400, 300);

            addWidget(fProperty.setInteractable(false));
            addWidget(fName.setInteractable(false));
            addWidget(fType.setInteractable(false));
            addWidget(fValue.setInteractable(false));

            addWidget(name);
            addWidget(type);
            addWidget(value);

            modify.rebuildMesh();
            addWidget(modify.setOnClick((button, click) -> {
                if(click == null) return false;
                getInput(Component.literal("New value"), str -> {
                    if(str.isBlank()) return false;

                    int newVal;
                    try{
                        newVal = Integer.parseInt(str);
                    } catch (NumberFormatException e) {
                        return false;
                    }

                    ModelPropertyManager.getInstance().removeProperty(key, property.type().get());
                    ModelProperty newProperty = new IntProperty(newVal);
                    ModelPropertyManager.getInstance().setProperty(key, newProperty);
                    changed = true;

                    if(selectedProperty.value == property) propertyScreen.set(key, newProperty);
                    property = newProperty;
                    return true;
                });
                return true;
            }));

            init();
        }

        void clear(){
            key = null;
            property = null;

            name.clearText();
            type.clearText();
            value.clearText();
        }

        void set(String key, ModelProperty property){
            this.key = key;
            this.property = property;

            this.name.clearText().insertAtCursor(key);
            type.clearText().insertAtCursor(property.type().getId().toString());
            value.clearText().insertAtCursor(property.toString());
            modify.setVisible(property.type() == ModelPropertyRegistry.INT);
        }
    }

    protected static class Input extends WidgetContainer {

        final RoundedRectWidget bg = new RoundedRectWidget();
        final RoundedTextField input = new RoundedTextField().setOrigin(0, -15, 1).setSize(140, 20).setOutlineThickness(1.5f);
        final RoundedButton done = new RoundedButton().setOrigin(-30, 15, 1).setSize(50, 20)
                .setText(Component.literal("Done").withColor(Color.GREEN.getRGB())).setOutlineThickness(1.5f).setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f));
        final RoundedButton cancel = new RoundedButton().setOrigin(30, 15, 1).setSize(50, 20)
                .setText(Component.literal("Cancel").withColor(Color.RED.getRGB())).setOutlineThickness(1.5f).setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f));
        Predicate<String> callback;

        public Input(){
            setSize(160, 80);
            setVisible(false);

            bg.setSize(getWidth(), getHeight());
            bg.rebuildMesh();
            addWidget(bg);

            input.rebuildMesh();
            addWidget(input);

            done.setOnClick((mouse, key) -> {
               if(callback.test(input.getText())) setVisible(false);
               return true;
            });
            done.rebuildMesh();
            addWidget(done);

            cancel.setOnClick((button, key) -> {
                callback = null;
                setVisible(false);
                return true;
            });
            cancel.rebuildMesh();
            addWidget(cancel);

            init();
        }

        public Input set(Component hint, Predicate<String> callback){
            input.setDefText(hint);
            input.clearText();
            this.callback = callback;
            return this;
        }
    }

    protected static class DoubleInput extends WidgetContainer {

        final RoundedRectWidget bg = new RoundedRectWidget();

        final RoundedTextField input = new RoundedTextField().setOrigin(0, -25, 1).setSize(140, 20).setOutlineThickness(1.5f);
        final RoundedTextField secondInput = new RoundedTextField().setOrigin(0, 0, 1).setSize(140, 20).setOutlineThickness(1.5f);

        final RoundedButton done = new RoundedButton().setOrigin(-30, 25, 1).setSize(50, 20)
                .setText(Component.literal("Done").withColor(Color.GREEN.getRGB())).setOutlineThickness(1.5f).setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f));
        final RoundedButton cancel = new RoundedButton().setOrigin(30, 25, 1).setSize(50, 20)
                .setText(Component.literal("Cancel").withColor(Color.RED.getRGB())).setOutlineThickness(1.5f).setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f));
        BiPredicate<String, String> callback;

        public DoubleInput(){
            setSize(160, 90);
            setVisible(false);

            bg.setSize(getWidth(), getHeight());
            bg.rebuildMesh();
            addWidget(bg);

            input.rebuildMesh();
            addWidget(input);

            secondInput.rebuildMesh();
            addWidget(secondInput);

            done.setOnClick((mouse, key) -> {
                if(callback.test(input.getText(), secondInput.getText())) setVisible(false);
                return true;
            });
            done.rebuildMesh();
            addWidget(done);

            cancel.setOnClick((button, key) -> {
                callback = null;
                setVisible(false);
                return true;
            });
            cancel.rebuildMesh();
            addWidget(cancel);

            init();
        }

        public DoubleInput set(Component hint, Component secondHint, BiPredicate<String, String> callback){
            input.setDefText(hint);
            input.clearText();

            secondInput.setDefText(secondHint);
            secondInput.clearText();

            this.callback = callback;
            return this;
        }
    }
}
