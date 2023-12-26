package net.zaharenko424.a_changed.client.screen;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.ServerboundEditNotePacket;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class NoteScreen extends Screen {
    private boolean isModified;
    private int frameTick;
    private int currentPage;
    private final List<String> pages = Lists.newArrayList();
    private final TextFieldHelper pageEdit;
    private long lastClickTime;
    private int lastIndex = -1;
    private PageButton forwardButton;
    private PageButton backButton;
    @Nullable
    private NoteScreen.DisplayCache displayCache = NoteScreen.DisplayCache.EMPTY;
    private Component pageMsg = CommonComponents.EMPTY;
    private final BlockPos notePos;
    private final boolean finalized;
    private final int textWidth;
    private final int textHeight;
    private final int imageWidth;
    private final int imageHeight;
    private final ResourceLocation background;
    private final int yOffset;
    private int pageNumColor=0;

    private static final int DEF_IMAGE_WIDTH = 192;
    private static final int DEF_IMAGE_HEIGHT = 192;

    public NoteScreen(BlockPos notePos, List<String> text, boolean finalized, int guiId) {
        super(GameNarrator.NO_TITLE);
        pages.addAll(text);
        this.notePos=notePos;
        this.finalized=finalized;
        if (pages.isEmpty()) {
            pages.add("");
        }
        switch(guiId){
            case 0->{
                imageWidth=96;
                imageHeight=128;
                background=new ResourceLocation(AChanged.MODID,"textures/gui/note.png");
                yOffset=100;
            }
            case 1->{
                imageWidth=142;
                imageHeight=196;
                background=new ResourceLocation(AChanged.MODID,"textures/gui/notepad.png");
                yOffset=100;
                pageNumColor=16777215;
            }
            default -> {
                imageWidth=DEF_IMAGE_WIDTH;
                imageHeight=DEF_IMAGE_HEIGHT;
                background=BookViewScreen.BOOK_LOCATION;
                yOffset=0;
            }
        }
        textWidth=imageWidth-18;
        textHeight=imageHeight-36;
        pageEdit=new TextFieldHelper(
                this::getCurrentPageText,
                this::setCurrentPageText,
                this::getClipboard,
                this::setClipboard,
                p_280853_ -> p_280853_.length() < 1024 && font.wordWrapHeight(p_280853_, textWidth) <= textHeight
        );
    }

    private void setClipboard(String p_98148_) {
        if (minecraft != null) {
            TextFieldHelper.setClipboardContents(minecraft, p_98148_);
        }
    }

    private @NotNull String getClipboard() {
        return minecraft != null ? TextFieldHelper.getClipboardContents(minecraft) : "";
    }

    private int getNumPages() {
        return pages.size();
    }

    @Override
    public void tick() {
        super.tick();
        ++frameTick;
    }

    @Override
    protected void init() {
        clearDisplayCache();
        int halfWidth = width/2;
        if(!finalized) {
            addRenderableWidget(Button.builder(Component.translatable("book.signButton"), p_98177_ -> {
                minecraft.setScreen(null);
                saveChanges(true);
            }).bounds(halfWidth - 100, 6+imageHeight+yOffset, 98, 20).build());
            addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, p_280851_ -> {
                minecraft.setScreen(null);
                saveChanges(false);
            }).bounds(halfWidth + 2, 6+imageHeight+yOffset, 98, 20).build());
        }
        int buttonOffset=imageWidth/2;
        if(!finalized||pages.size()>1) forwardButton = addRenderableWidget(new PageButton(halfWidth + buttonOffset, (imageHeight-16)+yOffset, true, p_98144_ -> pageForward(), true));
        backButton = addRenderableWidget(new PageButton(halfWidth - (buttonOffset+23), (imageHeight-16)+yOffset, false, p_98113_ -> pageBack(), true));
        backButton.visible=false;
    }

    private void pageBack() {
        if (currentPage > 0) {
            --currentPage;
        }
        updateButtonVisibility();
        clearDisplayCacheAfterPageChange();
    }

    private void pageForward() {
        if (currentPage < getNumPages() - 1) {
            ++currentPage;
        } else if(!finalized) {
            appendPageToBook();
            if (currentPage < getNumPages() - 1) {
                ++currentPage;
            }
        }
        updateButtonVisibility();
        clearDisplayCacheAfterPageChange();
    }

    private void updateButtonVisibility(){
        forwardButton.visible=pages.size()>currentPage;
        backButton.visible=currentPage>0;
    }

    private void eraseEmptyTrailingPages() {
        ListIterator<String> listiterator = pages.listIterator(pages.size());

        while(listiterator.hasPrevious() && listiterator.previous().isEmpty()) {
            listiterator.remove();
        }
    }

    private void saveChanges(boolean finalize) {
        if (isModified||finalize) {
            eraseEmptyTrailingPages();
            PacketHandler.INSTANCE.sendToServer(new ServerboundEditNotePacket(pages,notePos,finalize));
        }
    }

    private void appendPageToBook() {
        if (getNumPages() < 100) {
            pages.add("");
            isModified = true;
        }
    }

    @Override
    public boolean keyPressed(int p_98100_, int p_98101_, int p_98102_) {
        if (super.keyPressed(p_98100_, p_98101_, p_98102_)) {
            return true;
        }
        boolean flag = bookKeyPressed(p_98100_);
        if (flag) {
            clearDisplayCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char p_98085_, int p_98086_) {
        if (super.charTyped(p_98085_, p_98086_)) {
            return true;
        } else if (finalized){
            return false;
        } else if (SharedConstants.isAllowedChatCharacter(p_98085_)) {
            pageEdit.insertText(Character.toString(p_98085_));
            clearDisplayCache();
            return true;
        } else {
            return false;
        }
    }

    private boolean bookKeyPressed(int p_98153_) {
        if (Screen.isSelectAll(p_98153_)) {
            pageEdit.selectAll();
            return true;
        } else if (Screen.isCopy(p_98153_)) {
            pageEdit.copy();
            return true;
        } else if (Screen.isPaste(p_98153_)&&!finalized) {
            pageEdit.paste();
            return true;
        } else if (Screen.isCut(p_98153_)&&!finalized) {
            pageEdit.cut();
            return true;
        } else if(!finalized||(p_98153_!=257&&p_98153_!=259&&p_98153_!=261&&p_98153_!=335)){
            TextFieldHelper.CursorStep textfieldhelper$cursorstep = Screen.hasControlDown()
                    ? TextFieldHelper.CursorStep.WORD
                    : TextFieldHelper.CursorStep.CHARACTER;
            return switch (p_98153_) {
                case 257, 335 -> {
                    pageEdit.insertText("\n");
                    yield true;
                }
                case 259 -> {
                    pageEdit.removeFromCursor(-1, textfieldhelper$cursorstep);
                    yield true;
                }
                case 261 -> {
                    pageEdit.removeFromCursor(1, textfieldhelper$cursorstep);
                    yield true;
                }
                case 262 -> {
                    pageEdit.moveBy(1, Screen.hasShiftDown(), textfieldhelper$cursorstep);
                    yield true;
                }
                case 263 -> {
                    pageEdit.moveBy(-1, Screen.hasShiftDown(), textfieldhelper$cursorstep);
                    yield true;
                }
                case 264 -> {
                    changeLine(1);
                    yield true;
                }
                case 265 -> {
                    changeLine(-1);
                    yield true;
                }
                case 266 -> {
                    backButton.onPress();
                    yield true;
                }
                case 267 -> {
                    forwardButton.onPress();
                    yield true;
                }
                case 268 -> {
                    keyHome();
                    yield true;
                }
                case 269 -> {
                    keyEnd();
                    yield true;
                }
                default -> false;
            };
        }
        return false;
    }

    private void changeLine(int p_98098_) {
        int i = pageEdit.getCursorPos();
        int j = getDisplayCache().changeLine(i, p_98098_);
        pageEdit.setCursorPos(j, Screen.hasShiftDown());
    }

    private void keyHome() {
        if (Screen.hasControlDown()) {
            pageEdit.setCursorToStart(Screen.hasShiftDown());
        } else {
            int i = pageEdit.getCursorPos();
            int j = getDisplayCache().findLineStart(i);
            pageEdit.setCursorPos(j, Screen.hasShiftDown());
        }
    }

    private void keyEnd() {
        if (Screen.hasControlDown()) {
            pageEdit.setCursorToEnd(Screen.hasShiftDown());
        } else {
            NoteScreen.DisplayCache bookeditscreen$displaycache = getDisplayCache();
            int i = pageEdit.getCursorPos();
            int j = bookeditscreen$displaycache.findLineEnd(i);
            pageEdit.setCursorPos(j, Screen.hasShiftDown());
        }
    }

    private String getCurrentPageText() {
        return currentPage >= 0 && currentPage < pages.size() ? pages.get(currentPage) : "";
    }

    private void setCurrentPageText(String p_98159_) {
        if (currentPage >= 0 && currentPage < pages.size()) {
            pages.set(currentPage, p_98159_);
            isModified = true;
            clearDisplayCache();
        }
    }

    @Override
    public void render(GuiGraphics p_281724_, int p_282965_, int p_283294_, float p_281293_) {
        super.render(p_281724_, p_282965_, p_283294_, p_281293_);
        setFocused(null);
        p_281724_.drawString(font, pageMsg, width / 2 - font.width(pageMsg)/2, 16+yOffset, pageNumColor, false);
        NoteScreen.DisplayCache bookeditscreen$displaycache = getDisplayCache();

        for(NoteScreen.LineInfo bookeditscreen$lineinfo : bookeditscreen$displaycache.lines) {
            p_281724_.drawString(font, bookeditscreen$lineinfo.asComponent, bookeditscreen$lineinfo.x, bookeditscreen$lineinfo.y, -16777216, false);
        }

        renderHighlight(p_281724_, bookeditscreen$displaycache.selection);
        renderCursor(p_281724_, bookeditscreen$displaycache.cursor, bookeditscreen$displaycache.cursorAtEnd);
    }

    @Override
    public void renderBackground(GuiGraphics p_294860_, int p_295019_, int p_294307_, float p_295562_) {
        super.renderBackground(p_294860_, p_295019_, p_294307_, p_295562_);
        p_294860_.blit(background, (width - imageWidth) / 2, 2+yOffset, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    private void renderCursor(GuiGraphics p_281833_, NoteScreen.Pos2i p_282190_, boolean p_282412_) {
        if (frameTick / 6 % 2 == 0) {
            p_282190_ = convertLocalToScreen(p_282190_);
            if (!p_282412_) {
                p_281833_.fill(p_282190_.x, p_282190_.y - 1, p_282190_.x + 1, p_282190_.y + 9, -16777216);
            } else {
                p_281833_.drawString(font, "_", p_282190_.x, p_282190_.y, 0, false);
            }
        }
    }

    private void renderHighlight(GuiGraphics p_282188_, Rect2i[] p_265482_) {
        for(Rect2i rect2i : p_265482_) {
            int i = rect2i.getX();
            int j = rect2i.getY();
            int k = i + rect2i.getWidth();
            int l = j + rect2i.getHeight();
            p_282188_.fill(RenderType.guiTextHighlight(), i, j, k, l, -16776961);
        }
    }

    @Contract(value = "_ -> new", pure = true)
    private NoteScreen.@NotNull Pos2i convertScreenToLocal(NoteScreen.Pos2i p_98115_) {
        return new NoteScreen.Pos2i(p_98115_.x - width / 2 + (imageWidth/2-10), p_98115_.y - 32+yOffset);
    }

    @Contract(value = "_ -> new", pure = true)
    private NoteScreen.@NotNull Pos2i convertLocalToScreen(NoteScreen.Pos2i p_98146_) {
        return new NoteScreen.Pos2i(p_98146_.x + width / 2 - (imageWidth/2-10), p_98146_.y + 32+yOffset);
    }

    @Override
    public boolean mouseClicked(double p_98088_, double p_98089_, int p_98090_) {
        if (super.mouseClicked(p_98088_, p_98089_, p_98090_)) {
            return true;
        } else {
            if (p_98090_ == 0) {
                long i = Util.getMillis();
                NoteScreen.DisplayCache bookeditscreen$displaycache = getDisplayCache();
                int j = bookeditscreen$displaycache.getIndexAtPosition(font, convertScreenToLocal(new NoteScreen.Pos2i((int)p_98088_, (int)p_98089_)));
                if (j >= 0) {
                    if (j != lastIndex || i - lastClickTime >= 250L) {
                        pageEdit.setCursorPos(j, Screen.hasShiftDown());
                    } else if (!pageEdit.isSelecting()) {
                        selectWord(j);
                    } else {
                        pageEdit.selectAll();
                    }

                    clearDisplayCache();
                }

                lastIndex = j;
                lastClickTime = i;
            }

            return true;
        }
    }

    private void selectWord(int p_98142_) {
        String s = getCurrentPageText();
        pageEdit.setSelectionRange(StringSplitter.getWordPosition(s, -1, p_98142_, false), StringSplitter.getWordPosition(s, 1, p_98142_, false));
    }

    @Override
    public boolean mouseDragged(double p_98092_, double p_98093_, int p_98094_, double p_98095_, double p_98096_) {
        if (super.mouseDragged(p_98092_, p_98093_, p_98094_, p_98095_, p_98096_)) {
            return true;
        } else {
            if (p_98094_ == 0) {
                NoteScreen.DisplayCache bookeditscreen$displaycache = getDisplayCache();
                int i = bookeditscreen$displaycache.getIndexAtPosition(font, convertScreenToLocal(new NoteScreen.Pos2i((int)p_98092_, (int)p_98093_)));
                pageEdit.setCursorPos(i, true);
                clearDisplayCache();
            }

            return true;
        }
    }

    private NoteScreen.DisplayCache getDisplayCache() {
        if (displayCache == null) {
            displayCache = rebuildDisplayCache();
            pageMsg = Component.translatable("book.pageIndicator", currentPage + 1, getNumPages());
        }

        return displayCache;
    }

    private void clearDisplayCache() {
        displayCache = null;
    }

    private void clearDisplayCacheAfterPageChange() {
        pageEdit.setCursorToEnd();
        clearDisplayCache();
    }

    private NoteScreen.DisplayCache rebuildDisplayCache() {
        String s = getCurrentPageText();
        if (s.isEmpty()) {
            return NoteScreen.DisplayCache.EMPTY;
        } else {
            int i = pageEdit.getCursorPos();
            int j = pageEdit.getSelectionPos();
            IntList intlist = new IntArrayList();
            List<NoteScreen.LineInfo> list = Lists.newArrayList();
            MutableInt mutableint = new MutableInt();
            MutableBoolean mutableboolean = new MutableBoolean();
            StringSplitter stringsplitter = font.getSplitter();
            stringsplitter.splitLines(s, textWidth, Style.EMPTY, true, (p_98132_, p_98133_, p_98134_) -> {
                int k3 = mutableint.getAndIncrement();
                String s2 = s.substring(p_98133_, p_98134_);
                mutableboolean.setValue(s2.endsWith("\n"));
                String s3 = StringUtils.stripEnd(s2, " \n");
                int l3 = k3 * 9;
                NoteScreen.Pos2i bookeditscreen$pos2i1 = convertLocalToScreen(new NoteScreen.Pos2i(0, l3));
                intlist.add(p_98133_);
                list.add(new NoteScreen.LineInfo(p_98132_, s3, bookeditscreen$pos2i1.x, bookeditscreen$pos2i1.y));
            });
            int[] aint = intlist.toIntArray();
            boolean flag = i == s.length();
            NoteScreen.Pos2i bookeditscreen$pos2i;
            if (flag && mutableboolean.isTrue()) {
                bookeditscreen$pos2i = new NoteScreen.Pos2i(0, list.size() * 9);
            } else {
                int k = findLineFromPos(aint, i);
                int l = font.width(s.substring(aint[k], i));
                bookeditscreen$pos2i = new NoteScreen.Pos2i(l, k * 9);
            }

            List<Rect2i> list1 = Lists.newArrayList();
            if (i != j) {
                int l2 = Math.min(i, j);
                int i1 = Math.max(i, j);
                int j1 = findLineFromPos(aint, l2);
                int k1 = findLineFromPos(aint, i1);
                if (j1 == k1) {
                    int l1 = j1 * 9;
                    int i2 = aint[j1];
                    list1.add(createPartialLineSelection(s, stringsplitter, l2, i1, l1, i2));
                } else {
                    int i3 = j1 + 1 > aint.length ? s.length() : aint[j1 + 1];
                    list1.add(createPartialLineSelection(s, stringsplitter, l2, i3, j1 * 9, aint[j1]));

                    for(int j3 = j1 + 1; j3 < k1; ++j3) {
                        int j2 = j3 * 9;
                        String s1 = s.substring(aint[j3], aint[j3 + 1]);
                        int k2 = (int)stringsplitter.stringWidth(s1);
                        list1.add(createSelection(new NoteScreen.Pos2i(0, j2), new NoteScreen.Pos2i(k2, j2 + 9)));
                    }

                    list1.add(createPartialLineSelection(s, stringsplitter, aint[k1], i1, k1 * 9, aint[k1]));
                }
            }

            return new NoteScreen.DisplayCache(s, bookeditscreen$pos2i, flag, aint, list.toArray(new NoteScreen.LineInfo[0]), list1.toArray(new Rect2i[0]));
        }
    }

    static int findLineFromPos(int[] p_98150_, int p_98151_) {
        int i = Arrays.binarySearch(p_98150_, p_98151_);
        return i < 0 ? -(i + 2) : i;
    }

    private Rect2i createPartialLineSelection(String p_98120_, StringSplitter p_98121_, int p_98122_, int p_98123_, int p_98124_, int p_98125_) {
        String s = p_98120_.substring(p_98125_, p_98122_);
        String s1 = p_98120_.substring(p_98125_, p_98123_);
        NoteScreen.Pos2i bookeditscreen$pos2i = new NoteScreen.Pos2i((int)p_98121_.stringWidth(s), p_98124_);
        NoteScreen.Pos2i bookeditscreen$pos2i1 = new NoteScreen.Pos2i((int)p_98121_.stringWidth(s1), p_98124_ + 9);
        return createSelection(bookeditscreen$pos2i, bookeditscreen$pos2i1);
    }

    private Rect2i createSelection(NoteScreen.Pos2i p_98117_, NoteScreen.Pos2i p_98118_) {
        NoteScreen.Pos2i bookeditscreen$pos2i = convertLocalToScreen(p_98117_);
        NoteScreen.Pos2i bookeditscreen$pos2i1 = convertLocalToScreen(p_98118_);
        int i = Math.min(bookeditscreen$pos2i.x, bookeditscreen$pos2i1.x);
        int j = Math.max(bookeditscreen$pos2i.x, bookeditscreen$pos2i1.x);
        int k = Math.min(bookeditscreen$pos2i.y, bookeditscreen$pos2i1.y);
        int l = Math.max(bookeditscreen$pos2i.y, bookeditscreen$pos2i1.y);
        return new Rect2i(i, k, j - i, l - k);
    }

    @OnlyIn(Dist.CLIENT)
    static class DisplayCache {
        static final NoteScreen.DisplayCache EMPTY = new NoteScreen.DisplayCache(
                "",
                new NoteScreen.Pos2i(0, 0),
                true,
                new int[]{0},
                new NoteScreen.LineInfo[]{new NoteScreen.LineInfo(Style.EMPTY, "", 0, 0)},
                new Rect2i[0]
        );
        private final String fullText;
        final NoteScreen.Pos2i cursor;
        final boolean cursorAtEnd;
        private final int[] lineStarts;
        final NoteScreen.LineInfo[] lines;
        final Rect2i[] selection;

        public DisplayCache(
                String p_98201_, NoteScreen.Pos2i p_98202_, boolean p_98203_, int[] p_98204_, NoteScreen.LineInfo[] p_98205_, Rect2i[] p_98206_
        ) {
            fullText = p_98201_;
            cursor = p_98202_;
            cursorAtEnd = p_98203_;
            lineStarts = p_98204_;
            lines = p_98205_;
            selection = p_98206_;
        }

        public int getIndexAtPosition(Font p_98214_, NoteScreen.Pos2i p_98215_) {
            int i = p_98215_.y / 9;
            if (i < 0) {
                return 0;
            } else if (i >= lines.length) {
                return fullText.length();
            } else {
                NoteScreen.LineInfo bookeditscreen$lineinfo = lines[i];
                return lineStarts[i] + p_98214_.getSplitter().plainIndexAtWidth(bookeditscreen$lineinfo.contents, p_98215_.x, bookeditscreen$lineinfo.style);
            }
        }

        public int changeLine(int p_98211_, int p_98212_) {
            int i = NoteScreen.findLineFromPos(lineStarts, p_98211_);
            int j = i + p_98212_;
            int k;
            if (0 <= j && j < lineStarts.length) {
                int l = p_98211_ - lineStarts[i];
                int i1 = lines[j].contents.length();
                k = lineStarts[j] + Math.min(l, i1);
            } else {
                k = p_98211_;
            }

            return k;
        }

        public int findLineStart(int p_98209_) {
            int i = NoteScreen.findLineFromPos(lineStarts, p_98209_);
            return lineStarts[i];
        }

        public int findLineEnd(int p_98219_) {
            int i = NoteScreen.findLineFromPos(lineStarts, p_98219_);
            return lineStarts[i] + lines[i].contents.length();
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class LineInfo {
        final Style style;
        final String contents;
        final Component asComponent;
        final int x;
        final int y;

        public LineInfo(Style p_98232_, String p_98233_, int p_98234_, int p_98235_) {
            style = p_98232_;
            contents = p_98233_;
            x = p_98234_;
            y = p_98235_;
            asComponent = Component.literal(p_98233_).setStyle(p_98232_);
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class Pos2i {
        public final int x;
        public final int y;

        Pos2i(int p_98249_, int p_98250_) {
            x = p_98249_;
            y = p_98250_;
        }
    }
}