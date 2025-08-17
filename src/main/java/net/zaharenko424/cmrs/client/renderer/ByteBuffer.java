package net.zaharenko424.cmrs.client.renderer;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import net.zaharenko424.a_changed.mixin.cmrs.BufferBuilderAccessor;
import net.zaharenko424.cmrs.util.Pool;

public class ByteBuffer implements Pool.Poolable {

    ByteBufferBuilder buffer;
    private long lastUse;
    private long lastLowUsage;

    public ByteBuffer(int capacity){
        buffer = new ByteBufferBuilder(capacity);
    }

    public ByteBufferBuilder getBuffer() {
        return buffer;
    }

    public int capacity(){
        return ((BufferBuilderAccessor)buffer).capacity();
    }

    public boolean isUnused(int clearTimeMS){
        return System.currentTimeMillis() - lastUse > clearTimeMS;
    }

    public void maybeShrink(int shrinkTimeMS){
        int capacity = capacity();
        if(capacity <= MultiBufferSource.MAX_POOL_BUF_SIZE) {
            lastLowUsage = 0;
            return;
        }

        if(((BufferBuilderAccessor)buffer).used() > capacity / 2) {
            lastLowUsage = 0;
        } else if(lastLowUsage == 0) lastLowUsage = System.currentTimeMillis();

        if(lastLowUsage == 0 || System.currentTimeMillis() - lastLowUsage <= shrinkTimeMS) return;


        ((BufferBuilderAccessor)buffer).resizeBuffer(capacity / 2);
        lastLowUsage = 0;
    }

    public boolean deallocateUnused(int clearTime){
        if(!isUnused(clearTime)) return false;

        deallocate();
        return true;
    }

    public void deallocate(){
        buffer.close();
    }

    public void use(){
        lastUse = System.currentTimeMillis();
    }

    @Override
    public void reset() {
        buffer.discard();
    }
}
