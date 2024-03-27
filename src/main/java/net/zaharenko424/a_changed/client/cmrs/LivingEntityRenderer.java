package net.zaharenko424.a_changed.client.cmrs;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;
import static net.minecraft.client.renderer.entity.LivingEntityRenderer.isEntityUpsideDown;

@ParametersAreNonnullByDefault
public abstract class LivingEntityRenderer<E extends LivingEntity, M extends EntityModel<E>> extends EntityRenderer<E> implements RenderLayerParent<E,M> {
    
    protected M model;
    protected final List<RenderLayer<E, M>> layers = Lists.newArrayList();
    
    protected LivingEntityRenderer(EntityRendererProvider.Context p_174008_, M model, float shadowRadius) {
        super(p_174008_);
        this.model = model;
        this.shadowRadius = shadowRadius;
    }

    public final void addLayer(RenderLayer<E, M> p_115327_) {
        this.layers.add(p_115327_);
    }

    @Override
    public @NotNull M getModel() {
        return model;
    }

    public void render(E entity, float yaw, float ticks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        model.attackTime = getAttackAnim(entity, ticks);

        boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
        model.riding = shouldSit;
        model.young = entity.isBaby();
        float f = Mth.rotLerp(ticks, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(ticks, entity.yHeadRotO, entity.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
            Entity $$14 = entity.getVehicle();
            if ($$14 instanceof LivingEntity livingentity) {
                f = Mth.rotLerp(ticks, livingentity.yBodyRotO, livingentity.yBodyRot);
                f2 = f1 - f;
                float f6 = Mth.wrapDegrees(f2);
                if (f6 < -85.0F) {
                    f6 = -85.0F;
                }

                if (f6 >= 85.0F) {
                    f6 = 85.0F;
                }

                f = f1 - f6;
                if (f6 * f6 > 2500.0F) {
                    f += f6 * 0.2F;
                }

                f2 = f1 - f;
            }
        }

        float f5 = Mth.lerp(ticks, entity.xRotO, entity.getXRot());
        if (!isEntityUpsideDown(entity)) {
            f5 *= -1.0F;
            f2 *= -1.0F;
        }

        if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            if (direction != null) {
                float f3 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float)(-direction.getStepX()) * f3, 0.0F, (float)(-direction.getStepZ()) * f3);
            }
        }

        float f7 = getBob(entity, ticks);
        setupRotations(entity, poseStack, f7, f, ticks);
        scale(entity, poseStack, ticks);
        float f8 = 0.0F;
        float f4 = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            f8 = entity.walkAnimation.speed(ticks);
            f4 = entity.walkAnimation.position(ticks);
            if (entity.isBaby()) f4 *= 3.0F;

            if (f8 > 1.0F) f8 = 1.0F;
        }

        model.prepareMobModel(entity, f4, f8, ticks);
        model.setupAnim(entity, f4, f8, f7, f2, f5);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = isBodyVisible(entity);
        boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
        RenderType rendertype = getRenderType(entity, flag, flag1, minecraft.shouldEntityAppearGlowing(entity));
        if (rendertype != null) {
            int overlay = getOverlayCoords(entity, getWhiteOverlayProgress(entity, ticks));
            model.renderToBuffer(poseStack, buffer.getBuffer(rendertype), light, overlay, 1, 1, 1, flag1 ? .15f : 1);
        }

        if (!entity.isSpectator()) {
            for(RenderLayer<E, M> renderlayer : layers) {
                renderlayer.render(poseStack, buffer, light, entity, f4, f8, ticks, f7, f2, f5);
            }
        }

        poseStack.popPose();
        super.render(entity, yaw, ticks, poseStack, buffer, light);
    }

    @Nullable
    protected RenderType getRenderType(E entity, boolean bodyVisible, boolean translucent, boolean glowOutline) {
        ResourceLocation resourcelocation = getTextureLocation(entity);
        if(translucent) return RenderType.itemEntityTranslucentCull(resourcelocation);
        if(bodyVisible) return model.renderType(resourcelocation);
        return glowOutline ? RenderType.outline(resourcelocation) : null;
    }

    protected boolean isBodyVisible(E p_115341_) {
        return !p_115341_.isInvisible();
    }

    private static float sleepDirectionToRotation(Direction p_115329_) {
        return switch (p_115329_) {
            case SOUTH -> 90.0F;
            case NORTH -> 270.0F;
            case EAST -> 180.0F;
            default -> 0.0F;
        };
    }

    protected boolean isShaking(E entity) {
        return entity.isFullyFrozen();
    }

    protected void setupRotations(E entity, PoseStack poseStack, float ageInTicks, float yaw, float ticks) {
        if (this.isShaking(entity)) {
            yaw += (float)(Math.cos((double)entity.tickCount * 3.25) * Math.PI * 0.4F);
        }

        if (!entity.hasPose(Pose.SLEEPING)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        }

        if (entity.deathTime > 0) {
            float f = ((float)entity.deathTime + ticks - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            poseStack.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(entity)));
        } else if (entity.isAutoSpinAttack()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - entity.getXRot()));
            poseStack.mulPose(Axis.YP.rotationDegrees(((float)entity.tickCount + ticks) * -75.0F));
        } else if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            float f1 = direction != null ? sleepDirectionToRotation(direction) : yaw;
            poseStack.mulPose(Axis.YP.rotationDegrees(f1));
            poseStack.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(entity)));
            poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (isEntityUpsideDown(entity)) {
            poseStack.translate(0.0F, entity.getBbHeight() + 0.1F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    protected float getAttackAnim(E p_115343_, float p_115344_) {
        return p_115343_.getAttackAnim(p_115344_);
    }

    protected float getBob(E p_115305_, float p_115306_) {
        return (float)p_115305_.tickCount + p_115306_;
    }

    protected float getFlipDegrees(E entity) {
        return 90.0F;
    }

    protected float getWhiteOverlayProgress(E entity, float ticks) {
        return 0.0F;
    }

    protected void scale(E entity, PoseStack poseStack, float ticks) {
    }

    protected boolean shouldShowName(E entity) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
        float f = entity.isDiscrete() ? 32.0F : 64.0F;
        if (d0 >= (double)(f * f)) {
            return false;
        } else {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localplayer = minecraft.player;
            boolean flag = !entity.isInvisibleTo(localplayer);
            if (entity != localplayer) {
                Team team = entity.getTeam();
                Team team1 = localplayer.getTeam();
                if (team != null) {
                    Team.Visibility team$visibility = team.getNameTagVisibility();
                    return switch (team$visibility) {
                        case ALWAYS -> flag;
                        case NEVER -> false;
                        case HIDE_FOR_OTHER_TEAMS ->
                                team1 == null ? flag : team.isAlliedTo(team1) && (team.canSeeFriendlyInvisibles() || flag);
                        case HIDE_FOR_OWN_TEAM -> team1 == null ? flag : !team.isAlliedTo(team1) && flag;
                    };
                }
            }

            return Minecraft.renderNames() && entity != minecraft.getCameraEntity() && flag && !entity.isVehicle();
        }
    }
}