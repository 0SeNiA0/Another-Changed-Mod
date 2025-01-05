package net.zaharenko424.a_changed.client.animations;

import net.minecraft.client.animation.AnimationChannel.Interpolations;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.zaharenko424.a_changed.client.cmrs.animation.AnimationChannel;
import net.zaharenko424.a_changed.client.cmrs.animation.AnimationDefinition;

import static net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR;

public interface Animations {//rotation in blockbench seems to be pre flipped so need to *-1 so it looks right with NoYFlip stuff

    AnimationDefinition EAR_ANIM = AnimationDefinition.Builder.withLength(4.0F).looping()
            .addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), Interpolations.LINEAR),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
            ))
            .addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), Interpolations.LINEAR),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.LINEAR)
            ))
            .build();

    AnimationDefinition TAIL_DEF = AnimationDefinition.Builder.withLength(4.0F).looping()
            .addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM)
            ))
            .build();

    AnimationDefinition TAIL_DRAGON = AnimationDefinition.Builder.withLength(4.0F).looping()
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(5f, 0f, 0f), Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.CATMULLROM)))
            .addAnimation("tail2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f), Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.CATMULLROM)))
            .addAnimation("tail3",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f), Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.CATMULLROM))).build();

    AnimationDefinition TAIL_CAT = AnimationDefinition.Builder.withLength(4f).looping()
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail0",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail3",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail4",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM))).build();

    AnimationDefinition STATIC_TAIL_DRAGON = AnimationDefinition.Builder.withLength(0f)
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(42.5f, 0f, 0f),
                                    Interpolations.LINEAR)))
            .addAnimation("tail0",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                    Interpolations.LINEAR)))
            .addAnimation("tail1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f),
                                    Interpolations.LINEAR)))
            .addAnimation("tail2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                    Interpolations.LINEAR)))
            .addAnimation("tail3",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                    Interpolations.LINEAR))).build();

    AnimationDefinition TAIL_STATIC_SHARK = AnimationDefinition.Builder.withLength(0)
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0, KeyframeAnimations.degreeVec(42.5f, 0, 0),
                                    Interpolations.LINEAR)))
            .addAnimation("tail0",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0, KeyframeAnimations.degreeVec(-10, 0, 0),
                                    Interpolations.LINEAR)))
            .addAnimation("tail1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0, KeyframeAnimations.degreeVec(-7.5f, 0, 0),
                                    Interpolations.LINEAR)))
            .addAnimation("tail2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0, KeyframeAnimations.degreeVec(-7.5f, 0, 0),
                                    Interpolations.LINEAR))).build();

    AnimationDefinition STATIC_TAIL = AnimationDefinition.Builder.withLength(0f)
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(27.5f, 0f, 0f),
                                    LINEAR)))
            .addAnimation("tail_0",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                    LINEAR))).build();

    AnimationDefinition STATIC_TAIL_LEO = AnimationDefinition.Builder.withLength(0f)
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(37.5f, 0f, 0f),
                                    LINEAR)))
            .addAnimation("tail0",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    LINEAR)))
            .addAnimation("tail1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    LINEAR)))
            .addAnimation("tail2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                    LINEAR)))
            .addAnimation("tail3",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                    LINEAR)))
            .addAnimation("tail4",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f),
                                    LINEAR))).build();

    AnimationDefinition TAIL_SWIM_SHARK = AnimationDefinition.Builder.withLength(3.5f).looping()
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, -1f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(45f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(0.75f, KeyframeAnimations.degreeVec(97.5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(1.75f, KeyframeAnimations.degreeVec(135f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2.5f, KeyframeAnimations.degreeVec(95f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(3.5f, KeyframeAnimations.degreeVec(45f, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail0",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(12.5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(0.75f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(1.75f, KeyframeAnimations.degreeVec(-12.5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2.5f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(3.5f, KeyframeAnimations.degreeVec(12.5f, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(0.75f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(1.75f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2.5f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(3.5f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(0.75f, KeyframeAnimations.degreeVec(-10f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(1.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2.5f, KeyframeAnimations.degreeVec(10f, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(3.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    Interpolations.CATMULLROM))).build();
    AnimationDefinition TAIL_SWING_SHARK = AnimationDefinition.Builder.withLength(3f).looping()
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(0.75f, KeyframeAnimations.degreeVec(0, 5f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(1.5f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2.25f, KeyframeAnimations.degreeVec(0, -5f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail0",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(0.75f, KeyframeAnimations.degreeVec(0, 7.5f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(1.5f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2.25f, KeyframeAnimations.degreeVec(0, -7.5f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(0.75f, KeyframeAnimations.degreeVec(0, 7.5f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(1.5f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2.25f, KeyframeAnimations.degreeVec(0, -7.5f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM)))
            .addAnimation("tail2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(0.75f, KeyframeAnimations.degreeVec(0, 5f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(1.5f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(2.25f, KeyframeAnimations.degreeVec(0, -5f, 0f),
                                    Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0, 0f, 0f),
                                    Interpolations.CATMULLROM))).build();
}