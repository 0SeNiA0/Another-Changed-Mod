package net.zaharenko424.a_changed.atest;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * Prepare buffer -> ensure capacity for all the quads that need to be rendered -> ForkJoinPool.invoke(ConcurrentRenderingAction) -> in each subtask qalloc(amount of quads for this task) -> use the index to put vertices in buffer -> all done upload()
 */
public class ConcurrentVertexBuffer {

    private static ConcurrentVertexBuffer INSTANCE;

    private ByteBuffer buffer;
    private VertexFormat format;
    private int nextAlloc;
    private boolean uploaded;

    private ConcurrentVertexBuffer(){}

    public static ConcurrentVertexBuffer getInstance(){
        if(INSTANCE == null) INSTANCE = new ConcurrentVertexBuffer();
        return INSTANCE;
    }

    public ConcurrentVertexBuffer prepare(RenderType renderType){
        format = renderType.format();
        if(buffer != null) buffer.rewind();
        nextAlloc = 0;
        uploaded = false;
        return this;
    }

    public void ensureQuadCapacity(int capacity){
        capacity *= 4 * format.getVertexSize();

        if(buffer == null) {
            buffer = MemoryUtil.memAlloc(capacity);
            return;
        }

        if(buffer.capacity() >= capacity) return;

        MemoryUtil.memFree(buffer);
        buffer = MemoryUtil.memAlloc(capacity);
    }

    /**
     * @return index to be used with vertex()
     */
    public synchronized int qalloc(int quads){
        if(uploaded) throw new IllegalStateException("Buffer is already uploaded!");
        int tmp = nextAlloc;
        nextAlloc = tmp + format.getVertexSize() * 4 * quads;
        return tmp;
    }

    public void upload(VertexConsumer consumer){
        if(uploaded) throw new IllegalStateException("Buffer is already uploaded!");
        if(!(consumer instanceof BufferBuilder builder))
            throw new IllegalArgumentException("Provided consumer is not an instance of BufferBuilder!");
        //builder.putBulkData(buffer);
        uploaded = true;
    }

    /**
     * @return new index to be used for the next vertex() call.
     */
    public int vertex(int index, float x, float y, float z,
                       float red, float green, float blue, float alpha,
                       float texU, float texV,
                       int overlayUV, int lightmapUV,
                       float normalX, float normalY, float normalZ) {
        if(uploaded) throw new IllegalStateException("Buffer is already uploaded!");

        putFloat(index, x);
        putFloat(index + 4, y);
        putFloat(index + 8, z);

        putByte(index + 12, (byte) (int) (red * 256));
        putByte(index + 13, (byte) (int) (green * 256));
        putByte(index + 14, (byte) (int) (blue * 256));
        putByte(index + 15, (byte) (int) (alpha * 256));

        putFloat(index + 16, texU);
        putFloat(index + 20, texV);

        putShort(index + 24, (short) (overlayUV & 65535));
        putShort(index + 26, (short) (overlayUV >> 16 & 65535));

        putShort(index + 28, (short) (lightmapUV & 65535));
        putShort(index + 30, (short) (lightmapUV >> 16 & 65535));

        putByte(index + 32, normalIntValue(normalX));
        putByte(index + 33, normalIntValue(normalY));
        putByte(index + 34, normalIntValue(normalZ));

        return index + format.getVertexSize();
    }

    void putByte(int index, byte value){
        buffer.put(index, value);
    }

    void putShort(int index, short value){
        buffer.putShort(index, value);
    }

    void putFloat(int index, float value){
        buffer.putFloat(index, value);
    }

    static byte normalIntValue(float pNum) {
        return (byte)((int)(Mth.clamp(pNum, -1.0F, 1.0F) * 127.0F) & 0xFF);
    }
}