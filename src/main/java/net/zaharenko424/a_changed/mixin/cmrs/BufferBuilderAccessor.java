package net.zaharenko424.a_changed.mixin.cmrs;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ByteBufferBuilder.class)
public interface BufferBuilderAccessor {

    @Accessor("capacity")
    int capacity();

    @Accessor("writeOffset")
    int used();

    @Invoker("resize")
    void resizeBuffer(int size);
}
