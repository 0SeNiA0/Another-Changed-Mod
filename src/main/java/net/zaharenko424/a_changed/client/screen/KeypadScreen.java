package net.zaharenko424.a_changed.client.screen;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.network.packets.ServerboundTryPasswordPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
public class KeypadScreen extends Screen {
    private static final int MAX_SIZE = 8;
    private Button button;
    private final List<CharBox> charBoxes = new ArrayList<>();
    private int selectedChar = -1;
    private final boolean passwordSet;
    private final BlockPos pos;

    public KeypadScreen(boolean isPasswordSet, int length, BlockPos pos) {
        super(Component.empty());
        passwordSet = isPasswordSet;
        this.pos = pos;
        int a = isPasswordSet?length:MAX_SIZE;
        for(int i = 0; i < a; i++){
            charBoxes.add(new CharBox());
            if(isPasswordSet) charBoxes.get(i).num = 0;
        }
    }

    @Override
    protected void init() {
        if(!passwordSet){
            button = addRenderableWidget(Button.builder(Component.translatable("misc.a_changed.keypad_save_password"), button ->{
                int[] code = getCode();
                if(code.length < 4) return;
                minecraft.setScreen(null);
                sendPacket(code);
            }).bounds(width / 2 - 40,height / 3 + 30,80,20).build());
        } else {
            button = addRenderableWidget(Button.builder(Component.translatable("misc.a_changed.keypad_attempt"), button ->{
                minecraft.setScreen(null);
                sendPacket(getCode());
            }).bounds(width / 2 - 40,height / 3 + 30,80,20).build());
        }
        checkLength();
        recalculateCoordinates();
    }

    private void sendPacket(int[] code){
        PacketDistributor.SERVER.noArg().send(new ServerboundTryPasswordPacket(code, pos));
    }

    private void recalculateCoordinates(){
        int halfWidth = width / 2;
        int a = halfWidth - (25 * charBoxes.size() / 2);
        for(int i = 0; i < charBoxes.size(); i++){
            charBoxes.get(i).recalculate(a + i * 25,height / 3);
        }
    }

    public int[] getCode(){
        IntList list = new IntArrayList();
        charBoxes.forEach(box -> {
            if(box.num != null) list.add((int)box.num);
        });
        return list.toIntArray();
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        if(super.charTyped(character, modifiers)){
            return true;
        }
        if(Character.isDigit(character) && selectedChar > -1 && selectedChar < 8){
            int i = Character.getNumericValue(character);
            if(i > 9 || i < 0) return false;
            CharBox box = charBoxes.get(selectedChar);
            boolean b = box.num == null;
            box.num = i;
            if(b) checkLength();
            return true;
        }
        return false;
    }

    private static final List<Integer> keys = Arrays.asList(32, 257, 262, 263, 335);
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(super.keyPressed(keyCode, scanCode, modifiers)){
            return true;
        }
        if(selectedChar == -1 && !keys.contains(keyCode)) return false;
        return switch (keyCode){
            case 32, 257, 335 -> {
                button.onPress();
                yield true;
            }
            case 259, 261 -> {
                CharBox box = charBoxes.get(selectedChar);
                if(!passwordSet) {
                    box.num = null;
                    checkLength();
                    List<CharBox> newList = new ArrayList<>();
                    charBoxes.forEach(charBox -> {
                        if(charBox.num != null) newList.add(charBox);
                    });
                    selectedChar = newList.size();
                    charBoxes.clear();
                    charBoxes.addAll(newList);
                    for(int i = charBoxes.size(); i < MAX_SIZE;i++){
                        charBoxes.add(new CharBox());
                    }
                    recalculateCoordinates();
                } else box.num = 0;
                yield true;
            }
            case 262 -> {
                if(charBoxes.size() > selectedChar + 1){
                    selectedChar++;
                } else selectedChar = 0;
                yield true;
            }
            case 263 -> {
                if(selectedChar>0) selectedChar--; else selectedChar = charBoxes.size()-1;
                yield true;
            }
            case 264 -> {
                CharBox box = charBoxes.get(selectedChar);
                if(box.num == null){
                    box.num = 9;
                    checkLength();
                } else if(box.num == 0) {
                    box.num = 9;
                } else box.num--;
                yield true;
            }
            case 265 ->  {
                CharBox box = charBoxes.get(selectedChar);
                if(box.num == null){
                    box.num = 0;
                    checkLength();
                } else if(box.num == 9) {
                    box.num = 0;
                } else box.num++;
                yield true;
            }
            default -> false;
        };
    }

    private void checkLength(){
        if(passwordSet) return;
        if(getCode().length < 4) button.active = false;
        else if(!button.isActive()) button.active = true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(super.mouseClicked(mouseX, mouseY, button)){
            return true;
        }
        if(button == 0) {
            CharBox box;
            for (int i = 0; i < charBoxes.size(); i++) {
                box = charBoxes.get(i);
                if (mouseX >= box.x && mouseX <= box.x1 && mouseY >= box.y && mouseY <= box.y1) {
                    selectedChar = i;
                    return true;
                }
            }
            selectedChar = -1;
        }
        return false;
    }

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/keypad");

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(guiGraphics, pMouseX, pMouseY, pPartialTick);
        guiGraphics.blit(TEXTURE, width / 2 - 105, height / 3 - 21, 0, 0, 211, 84, 256, 128);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        setFocused(null);

        for(CharBox charBox : charBoxes){
            guiGraphics.blit(TEXTURE, charBox.x, charBox.y, 211, 0, 20, 25, 256, 128);
            if(charBox.num != null) guiGraphics.drawString(font,
                    String.valueOf(charBox.num),charBox.x + 7,charBox.y + 9, 16777215);
        }

        if(selectedChar != -1) {
            CharBox selected = charBoxes.get(selectedChar);
            guiGraphics.drawString(font, "__", selected.x + 4, selected.y + 12,16777215,false);
        }
    }

    static class CharBox{
        public int x;
        public int y;
        public int x1;
        public int y1;
        public Integer num;

        public void recalculate(int x,int y){
            this.x = x;
            this.y = y;
            x1 = x + 20;
            y1 = y + 25;
        }
    }
}