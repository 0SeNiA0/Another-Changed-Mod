package net.zaharenko424.a_changed.mixin.client.latex;

import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(AnimationMetadataSection.class)
public interface AnimationMetadataAccessor {

    @Accessor
    List<AnimationFrame> getFrames();

    @Accessor
    int getFrameWidth();

    @Accessor
    int getFrameHeight();

    @Accessor
    int getDefaultFrameTime();

    @Accessor
    boolean getInterpolatedFrames();
}
