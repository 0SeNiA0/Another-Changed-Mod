package net.zaharenko424.cmrs.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.FastColor;
import net.zaharenko424.cmrs.CMRS;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class GIF implements Closeable {

    public static final byte GLOBAL_COLOR_TABLE_SIZE_MASK = 0x07;
    public static final byte COLOR_TABLE_SORT_MASK = 0x08;
    public static final byte COLOR_RESOLUTION_MASK = 0x70;
    public static final byte GLOBAL_COLOR_TABLE_MASK = (byte) 0x80;

    final short width;        //Width of Display Screen in Pixels
    final short height;       //Height of Display Screen in Pixels
    final byte packedFlags;   //Screen and Color Map Information
    final byte bgColorIndex;  //Background Color Index

    final IntList globalColors;
    final List<Image> images = new ArrayList<>();

    List<BakedGIFFrame> frames = List.of();
    int totalMS;

    public GIF(File file){
        FriendlyByteBuf buf;

        try(FileInputStream in = new FileInputStream(file)){
            buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(in.readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //HEADER
        buf.skipBytes(3);
        logBytes(buf, 3);              //version

        //Logical Screen Descriptor
        width = buf.readShortLE();
        height = buf.readShortLE();
        packedFlags = buf.readByte();
        bgColorIndex = buf.readByte();
        logByte(buf);                      //Pixel Aspect Ratio

        //Global Color Table
        if((packedFlags & GLOBAL_COLOR_TABLE_MASK) != 0){//Global Color Table present
            int size = 1 << ((packedFlags & GLOBAL_COLOR_TABLE_SIZE_MASK) + 1);

            globalColors = new IntArrayList(size);

            for(int i = 0; i < size; i++){
                int color = FastColor.ARGB32.color(buf.readByte(), buf.readByte(), buf.readByte());//swap red and blue
                globalColors.add(FastColor.ARGB32.color(FastColor.ARGB32.blue(color), FastColor.ARGB32.green(color), FastColor.ARGB32.red(color)));
            }
        } else globalColors = null;

        byte separator;
        GraphicControlExtension ext = null;
        while(true){
            separator = buf.readByte();
            if(separator == 0x3B) break;

            if(separator == 0x21){//Extension
                separator = buf.readByte();

                if(separator == (byte) 0xF9){
                    ext = new GraphicControlExtension(buf);
                } else {
                    do separator = buf.readByte();//comments, application extensions, text and everything else is ignored
                    while (separator != 0);
                }
            } else if(separator == 0x2C) {//Image
                images.add(new Image(buf, ext));
                ext = null;
            }
        }

        try {
            bake();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bake() throws IOException {
        if(images.isEmpty()){
            if(!frames.isEmpty()){
                frames.forEach(f -> RenderSystem.deleteTexture(f.texId()));
                frames = List.of();
            }
            return;
        }

        List<BakedGIFFrame> newFrames = new ArrayList<>();

        NativeImage img = new NativeImage(NativeImage.Format.RGBA, width, height, true);
        Image current, im;
        for(int i = 0; i < images.size(); i++){
            current = images.get(i);
            if(i != 0) img.fillRect(0, 0, width, height, 0);

            for(int ii = 0; ii < i; ii++){
                im = images.get(ii);
                if(im.ext == null) continue;
                if((im.ext.packedFlags & GraphicControlExtension.DISPOSAL_METHOD_MASK) >> 2 == 1){
                    copyRect(im.img, img, 0, 0, im.left, im.top, im.width, im.height);
                }
            }

            copyRect(current.img, img, 0, 0, current.left, current.top, current.width, current.height);

            int texId = GL30.glGenTextures();
            GlStateManager._bindTexture(texId);
            GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width, height, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            img.upload(0, 0, 0, false);
            newFrames.add(new BakedGIFFrame(texId, current.ext != null ? current.ext.delayTime * 10 : 0));

            totalMS += newFrames.getLast().delayMS;
        }
        img.close();

        List<BakedGIFFrame> oldFrames = frames;
        frames = newFrames;
        if(!oldFrames.isEmpty()){
            oldFrames.forEach(f -> GL30.glDeleteTextures(f.texId()));
        }
    }

    @Override
    public void close() {
        if(!frames.isEmpty()) {
            frames.forEach(BakedGIFFrame::close);
            frames = List.of();
        }

        if(!images.isEmpty()){
            images.forEach(Image::close);
        }
    }

    public int getTexture(int index){

        try{
            File out = new File("C:\\Users\\zahar\\Desktop\\gif");
            out.mkdir();
            for (int i = 0; i < images.size(); i++) {
                images.get(i).img.writeToFile(new File(out, "img_" + i + ".png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if(index >= frames.size()) return -1;
        return frames.get(index).texId();
    }

    public int getCurrentTexture(long startTimeMS){
        long currMS = System.currentTimeMillis();
        long diff = (currMS - startTimeMS) % totalMS;

        int accumulator = 0;
        for (BakedGIFFrame frame : frames) {
            accumulator += frame.delayMS;
            if (accumulator > diff) return frame.texId();
        }

        return -1;
    }

    protected void fillRectSafe(NativeImage to, int toX, int toY, int width, int height, int color){
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                safeSetRGBAIgnoreTransparent(to, toX + x, toY + y, color);
            }
        }
    }

    protected void copyRect(NativeImage from, NativeImage to, int fromX, int fromY, int toX, int toY, int width, int height){
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                safeSetRGBAIgnoreTransparent(to, toX + x, toY + y, safeGetRGBA(from, fromX + x, fromY + y));
            }
        }
    }

    protected boolean isOutOfBounds(NativeImage img, int x, int y){
        return x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight();
    }

    protected int safeGetRGBA(NativeImage img, int x, int y){
        //if(isOutOfBounds(img, x, y)) return 0;
        return img.getPixelRGBA(x, y);
    }

    protected void safeSetRGBA(NativeImage img, int x, int y, int color){
        if(isOutOfBounds(img, x, y)) return;
        img.setPixelRGBA(x, y, color);
    }

    protected void safeSetRGBAIgnoreTransparent(NativeImage img, int x, int y, int color){
        if(FastColor.ARGB32.alpha(color) == 0/* || isOutOfBounds(img, x, y)*/) return;
        img.setPixelRGBA(x, y, color);
    }

    void logBytes(FriendlyByteBuf buf, int count){
        CMRS.LOGGER.info(String.valueOf(buf.readBytes(count)));
    }

    void logByte(FriendlyByteBuf buf){
        byte b = buf.readByte();
        CMRS.LOGGER.info("{} Byte", b);
    }

    static class BakedGIFFrame implements Closeable {

        private int texId;
        public final int delayMS;

        public BakedGIFFrame(int texId, int delayMS){
            this.texId = texId;
            this.delayMS = delayMS;
        }

        public int texId(){
            return texId;
        }

        @Override
        public void close() {
            RenderSystem.deleteTexture(texId);
            texId = -1;
        }
    }

    static class GraphicControlExtension {

        public static final byte TRANSPARENT_COLOR_MASK = 0x01;
        public static final byte USER_INPUT_MASK = 0x02;        // /-> 0 == No disposal specified. The decoder is not required to take any action.
        public static final byte DISPOSAL_METHOD_MASK = 0x1C;// -----> 1 == Do not dispose. The graphic is to be left in place.
        public static final byte RESERVED_MASK = (byte) 0xA0;   // \-> 2 == The area used by the graphic must be restored to the background color.
                                                                //  -> 3 == Restore to previous. The decoder is required to restore the area
                                                                //          overwritten by the graphic with what was there prior to rendering the graphic.
        final byte packedFlags;
        final short delayTime;// 1 == 0.01 of a second
        final byte transparentColorIndex;

        public GraphicControlExtension(FriendlyByteBuf buf){
            buf.skipBytes(1);//skip size as it's always the same
            packedFlags = buf.readByte();
            delayTime = buf.readShortLE();
            transparentColorIndex = buf.readByte();
            buf.skipBytes(1);//skip terminator
        }
    }

    class Image implements Closeable {

        public static final byte LOCAL_COLOR_TABLE_SIZE_MASK = 0x07;
        public static final byte RESERVED_MASK = 0x18;
        public static final byte SORT_MASK = 0x20;
        public static final byte INTERLACE_MASK = 0x40;
        public static final byte LOCAL_COLOR_TABLE_MASK = (byte) 0x80;

        final short left;
        final short top;
        final short width;
        final short height;
        final byte packedFlags;
        final byte LZWMinCodeSize;

        final GraphicControlExtension ext;

        final IntList localColors;

        final NativeImage img;

        public Image(FriendlyByteBuf buf, @Nullable GraphicControlExtension ext){
            left = buf.readShortLE();
            top = buf.readShortLE();
            width = buf.readShortLE();
            height = buf.readShortLE();
            packedFlags = buf.readByte();
            LZWMinCodeSize = buf.readByte();
            this.ext = ext;

            if((packedFlags & LOCAL_COLOR_TABLE_MASK) != 0){
                int size = 1 << (packedFlags & LOCAL_COLOR_TABLE_SIZE_MASK + 1);
                localColors = new IntArrayList(size);

                for (int i = 0; i < size; i++) {
                    localColors.add(FastColor.ARGB32.color(buf.readByte(), buf.readByte(), buf.readByte()));
                }
            } else localColors = null;

            FriendlyByteBuf lzwEncodedData = new FriendlyByteBuf(Unpooled.buffer());
            byte size;
            while(true){
                size = buf.readByte();
                if(size == 0) break;

                lzwEncodedData.writeBytes(buf, size & 0xFF);
            }

            byte[] indices = decodeImageData(lzwEncodedData, LZWMinCodeSize, width, height, 4096);

            if((packedFlags & INTERLACE_MASK) != 0){//Un-interlace
                IntList rows = new IntArrayList(height);
                byte[] tmp = new byte[indices.length];
                System.arraycopy(indices, 0, tmp, 0, indices.length);

                int j = 0;

                for(int i = 0; i < height; i += 8){
                    rows.add(j++);
                }

                for(int i = 4; i < height; i += 8){
                    rows.add(j++);
                }

                for(int i = 2; i < height; i += 4){
                    rows.add(j++);
                }

                for(int i = 1; i < height; i += 2){
                    rows.add(j++);
                }

                j = 0;
                for(int i : rows){
                    System.arraycopy(indices, i * width, tmp, j++ * width, width);
                }
                indices = tmp;
            }

            img = new NativeImage(NativeImage.Format.RGBA, width, height, true);
            byte index;
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    index = indices[y * width + x];
                    img.setPixelRGBA(x, y, ext != null && (ext.packedFlags & GraphicControlExtension.TRANSPARENT_COLOR_MASK) != 0 && ext.transparentColorIndex == index ? 0 : getColor(index));
                }
            }
        }

        @Override
        public void close() {
            img.close();
        }

        int getColor(byte index){
            return localColors != null ? localColors.getInt(index & 0xFF) : globalColors.getInt(index & 0xFF);
        }

        private static final int NULL_CODE = -1;

        protected byte[] decodeImageData(FriendlyByteBuf buf, int minCodeSize, int width, int height, int maxDictSize) {
            final int npix = width * height;

            byte[] pixels = new byte[npix];
            short[] prefix = new short[maxDictSize];
            byte[] suffix = new byte[maxDictSize];
            byte[] pixelStack = new byte[maxDictSize + 1];

            int available;
            final int clear;
            int code_mask;
            int code_size;
            final int end_of_information;
            int in_code;
            int old_code;
            int bits;
            int code;
            int i;
            int datum;
            int first;
            int top;
            int pi;

            //  Initialize GIF data stream decoder.
            clear = 1 << minCodeSize;
            end_of_information = clear + 1;
            available = clear + 2;
            old_code = NULL_CODE;
            code_size = minCodeSize + 1;
            code_mask = ( 1 << code_size ) - 1;
            for (code = 0; code < clear; code++) {
                prefix[code] = 0;
                suffix[code] = ( byte ) code;
            }

            //  Decode GIF pixel stream.

            datum = bits = first = top = pi = 0;

            for (i = 0; i < npix;) {
                if (top == 0) {
                    if (bits < code_size) {
                        //  Load bytes until there are enough bits for a code.
                        if(!buf.isReadable()) break;

                        datum += ((int) buf.readByte() & 0xff) << bits;
                        bits += 8;
                        continue;
                    }

                    //  Get the next code.

                    code = datum & code_mask;
                    datum >>= code_size;
                    bits -= code_size;

                    //  Interpret the code

                    if ((code > available) || (code == end_of_information)) {
                        break;
                    }
                    if (code == clear) {
                        //  Reset decoder.
                        code_size = minCodeSize + 1;
                        code_mask = (1 << code_size) - 1;
                        available = clear + 2;
                        old_code = NULL_CODE;
                        continue;
                    }

                    if (old_code == NULL_CODE) {
                        pixelStack[top++] = suffix[code];
                        old_code = code;
                        first = code;
                        continue;
                    }

                    in_code = code;
                    if (code == available) {
                        pixelStack[top++] = (byte) first;
                        code = old_code;
                    }

                    while (code > clear) {
                        pixelStack[top++] = suffix[code];
                        code = prefix[code];
                    }
                    first = (int) suffix[code] & 0xff;

                    //  Add a new string to the string table,

                    if (available >= maxDictSize) {
                        break;
                    }

                    pixelStack[top++] = (byte) first;
                    prefix[available] = (short) old_code;
                    suffix[available] = (byte) first;
                    available++;
                    if (((available & code_mask) == 0) && (available < maxDictSize)) {
                        code_size++;
                        code_mask += available;
                    }
                    old_code = in_code;
                }

                //  Pop a pixel off the pixel stack.

                top--;
                pixels[pi++] = pixelStack[top];
                i++;
            }

            for (i = pi; i < npix; i++) {
                pixels[i] = 0;  // clear missing pixels
            }

            return pixels;
        }
    }
}
