package net.zaharenko424.testmod.client.screen;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.network.PacketHandler;
import net.zaharenko424.testmod.network.packets.ServerboundTryPasswordPacket;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeypadScreen extends Screen {
    private static final int MAX_SIZE=8;
    private Button button;
    private final List<CharBox> charBoxes=new ArrayList<>();
    private int selectedChar=-1;
    private final boolean passwordSet;
    private final BlockPos pos;

    public KeypadScreen(boolean isPasswordSet, int length, BlockPos pos) {
        super(Component.empty());
        passwordSet=isPasswordSet;
        this.pos=pos;
        int a=isPasswordSet?length:MAX_SIZE;
        for(int i=0;i<a;i++){
            charBoxes.add(new CharBox());
            if(isPasswordSet) charBoxes.get(i).num=0;
        }
    }

    @Override
    protected void init() {
        if(!passwordSet){
            button=addRenderableWidget(Button.builder(Component.translatable("misc.testmod.keypad_save_password"), button ->{
                int[] code=getCode();
                if(code.length<4) return;
                minecraft.setScreen(null);
                PacketHandler.INSTANCE.sendToServer(new ServerboundTryPasswordPacket(code,pos));
            }).bounds(width/2-40,height/3+30,80,20).build());
        } else {
            button=addRenderableWidget(Button.builder(Component.translatable("misc.testmod.keypad_attempt"), button ->{
                minecraft.setScreen(null);
                PacketHandler.INSTANCE.sendToServer(new ServerboundTryPasswordPacket(getCode(),pos));
            }).bounds(width/2-40,height/3+30,80,20).build());
        }
        checkLength();
        recalculateCoordinates();
    }

    private void recalculateCoordinates(){
        int halfWidth=width/2;
        int a=halfWidth-(25*charBoxes.size()/2);
        for(int i=0;i<charBoxes.size();i++){
            charBoxes.get(i).recalculate(a+i*25,height/3);
        }
    }

    public int[] getCode(){
        IntList list=new IntArrayList();
        charBoxes.forEach(box->{
            if(box.num!=null) list.add((int)box.num);
        });
        return list.toIntArray();
    }

    @Override
    public boolean charTyped(char p_94683_, int p_94684_) {
        if(super.charTyped(p_94683_, p_94684_)){
            return true;
        }
        if(Character.isDigit(p_94683_)){
            int i=Character.getNumericValue(p_94683_);
            if(i>9||i<0) return false;
            CharBox box=charBoxes.get(selectedChar);
            boolean b=box.num==null;
            box.num=i;
            if(b) checkLength();
            return true;
        }
        return false;
    }

    private static final List<Integer> keys= Arrays.asList(32,257,259,261,335);
    @Override
    public boolean keyPressed(int p_96552_, int p_96553_, int p_96554_) {
        if(super.keyPressed(p_96552_, p_96553_, p_96554_)){
            return true;
        }
        if(selectedChar==-1&&!keys.contains(p_96552_)) return false;
        return switch (p_96552_){
            case 32,257,335 -> {
                button.onPress();
                yield true;
            }
            case 259,261 -> {
                CharBox box=charBoxes.get(selectedChar);
                if(!passwordSet) {
                    box.num=null;
                    checkLength();
                    List<CharBox> newList=new ArrayList<>();
                    charBoxes.forEach(charBox->{
                        if(charBox.num!=null) newList.add(charBox);
                    });
                    selectedChar=newList.size();
                    charBoxes.clear();
                    charBoxes.addAll(newList);
                    for(int i=charBoxes.size();i<MAX_SIZE;i++){
                        charBoxes.add(new CharBox());
                    }
                    recalculateCoordinates();
                } else box.num = 0;
                yield true;
            }
            case 262 -> {
                if(charBoxes.size()>selectedChar+1){
                    selectedChar++;
                } else selectedChar=0;
                yield true;
            }
            case 263 ->{
                if(selectedChar>-1) selectedChar--;
                yield true;
            }
            case 264 ->{
                CharBox box=charBoxes.get(selectedChar);
                if(box.num==null){
                    box.num=9;
                    checkLength();
                } else if(box.num==0) {
                    box.num=9;
                } else box.num--;
                yield true;
            }
            case 265 ->{
                CharBox box=charBoxes.get(selectedChar);
                if(box.num==null){
                    box.num=0;
                    checkLength();
                } else if(box.num==9) {
                    box.num = 0;
                } else box.num++;
                yield true;
            }
            default -> false;
        };
    }

    private void checkLength(){
        if(passwordSet) return;
        if(getCode().length<4) button.active=false; else if(!button.isActive()) button.active=true;
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        if(super.mouseClicked(p_94695_, p_94696_, p_94697_)){
            return true;
        }
        if(p_94697_==0) {
            CharBox box;
            for (int i = 0; i < charBoxes.size(); i++) {
                box = charBoxes.get(i);
                if (p_94695_ >= box.x && p_94695_ <= box.x1 && p_94696_ >= box.y && p_94696_ <= box.y1) {
                    selectedChar = i;
                    TestMod.LOGGER.warn("charBox selected");
                    return true;
                }
            }
            selectedChar = -1;
        }
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        super.render(p_281549_, p_281550_, p_282878_, p_282465_);
        setFocused(null);

        for(CharBox charBox:charBoxes){
            p_281549_.fill(charBox.x,charBox.y,charBox.x1,charBox.y1,-16777216);
            if(charBox.num!=null) p_281549_.drawString(font,String.valueOf(charBox.num),charBox.x+7,charBox.y+9, 16777215);
        }

        if(selectedChar!=-1) {
            CharBox selected=charBoxes.get(selectedChar);
            p_281549_.drawString(font, "__", selected.x+4, selected.y+12,16777215,false);
        }
    }

    static class CharBox{
        public int x;
        public int y;
        public int x1;
        public int y1;
        public Integer num;

        public void recalculate(int x,int y){
            this.x=x;
            this.y=y;
            x1=x+20;
            y1=y+25;
        }
    }
}