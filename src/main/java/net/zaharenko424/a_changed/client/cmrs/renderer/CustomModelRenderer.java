package net.zaharenko424.a_changed.client.cmrs.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.Scoreboard;
import net.zaharenko424.a_changed.client.cmrs.RenderUtil;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.client.cmrs.api.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomModelRenderer<E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> extends LivingEntityRenderer<E, M> {

    protected final EntityRendererProvider.Context context;//Kinda useless. Mostly everything can be statically obtained from Minecraft

    public CustomModelRenderer(@NotNull EntityRendererProvider.Context context, @Nullable M model, float shadowRadius) {
        super(context, model, shadowRadius);
        this.context = context;
    }

//TODO stuck arrows -> getRandomVertex instead of getRandomCube
    @Override
    public void render(@NotNull E entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if(model == null){
            RenderUtil.renderLoadingAnim(entity, poseStack, partialTicks, packedLight);

            var event = new net.neoforged.neoforge.client.event.RenderNameTagEvent(entity, entity.getDisplayName(), this, poseStack, buffer, packedLight, partialTicks);
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event);
            if (event.canRender().isTrue() || event.canRender().isDefault() && this.shouldShowName(entity)) {
                this.renderNameTag(entity, event.getContent(), poseStack, buffer, packedLight, partialTicks);
            }
            return;
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void renderHand(@NotNull E entity, @NotNull PoseStack stack, int packedLight, @NotNull HumanoidArm arm){
        if(model != null) model.renderHand(entity, stack, packedLight, arm);
    }

    @Override
    protected void setupRotations(@NotNull E entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);//TODO replace with AnimationComponents
    }

    protected boolean shouldShowName(@NotNull E entity) {
        return super.shouldShowName(entity)
                && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    protected void renderNameTag(@NotNull E entity, @NotNull Component displayName, @NotNull PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLight, float partialTick) {
        if(!(entity instanceof AbstractClientPlayer client)){
            super.renderNameTag(entity, displayName, stack, bufferSource, packedLight, partialTick);
            return;
        }
        double d0 = this.entityRenderDispatcher.distanceToSqr(client);
        MatrixStack.push(stack);
        if (d0 < 100.0) {
            Scoreboard scoreboard = client.getScoreboard();
            Objective objective = scoreboard.getDisplayObjective(DisplaySlot.BELOW_NAME);
            if (objective != null) {
                ReadOnlyScoreInfo readonlyscoreinfo = scoreboard.getPlayerScoreInfo(client, objective);
                Component component = ReadOnlyScoreInfo.safeFormatValue(readonlyscoreinfo, objective.numberFormatOrDefault(StyledFormat.NO_STYLE));
                super.renderNameTag(
                        entity,
                        Component.empty().append(component).append(CommonComponents.SPACE).append(objective.getDisplayName()),
                        stack,
                        bufferSource,
                        packedLight,
                        partialTick
                );
                stack.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
            }
        }

        super.renderNameTag(entity, displayName, stack, bufferSource, packedLight, partialTick);
        MatrixStack.pop(stack);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull E entity) {
        return model.getTexture();
    }
}