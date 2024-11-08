package net.zaharenko424.a_changed.atest;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.a_changed.client.cmrs.animation.AnimationDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;

public abstract class AnimationComponent {

    //REGISTRY_CODEC -> lookUp the builtIn animations in animation component registry

    public abstract void animate(ModelPart root, AnimationContext context);

    public static final class Dynamic extends AnimationComponent {

        public static final StreamCodec<FriendlyByteBuf, Dynamic> CODEC = StreamCodec.composite(
                AnimationDefinition.CODEC,
                dynamic -> dynamic.definition,
                Dynamic::new
        );

        private final AnimationDefinition definition;

        public Dynamic(AnimationDefinition definition){
            this.definition = definition;
        }

        @Override
        public void animate(ModelPart root, AnimationContext context) {
            //KeyframeAnimator.animate(root, context);
        }
    }

    public static class AnimationContext {

    }
}