package net.zaharenko424.a_changed.client.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.Scoreboard;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.api.BufferSourceAccess;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.properties.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CustomModelRenderer<E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> extends LivingEntityRenderer<E, M> {

    public static final ModelPropertyType<Textures> TEXTURES = new ModelPropertyType<>(Textures.CODEC);
    /**
     * Remaps absolute uv to uv relative to the size of the texture. //TODO create UV container for each vertex to easier differentiate absolute & relative?
     */
    public static final ModelPropertyType<Unit> REMAP_UV = new ModelPropertyType<>(StreamCodec.unit(Unit.INSTANCE));//TMP mb relative UV(%) is actually better? Blockbench uses absolute...
    public static final ModelPropertyType<Armor> ARMOR = new ModelPropertyType<>(Armor.CODEC);
    public static final ModelPropertyType<Glow> GLOW = new ModelPropertyType<>(Glow.CODEC);
    public static final ModelPropertyType<Head> HEAD = new ModelPropertyType<>(Head.CODEC);
    public static final ModelPropertyType<Armed> ARMED = new ModelPropertyType<>(Armed.CODEC);

    protected final EntityRendererProvider.Context context;

    public CustomModelRenderer(EntityRendererProvider.Context context, @Nullable M model, float shadowRadius) {
        super(context, model, shadowRadius);
        this.context = context;
    }

    static final ModelPart.Quad quad = new ModelPart.Quad(new ModelPart.Vertex[]{
            new ModelPart.Vertex(new ModelPart.VertexData(new Vector3f(8, 8, 0), new ModelPart.Quad[0]), 1, 1),
            new ModelPart.Vertex(new ModelPart.VertexData(new Vector3f(-8, 8, 0), new ModelPart.Quad[0]), 0, 1),
            new ModelPart.Vertex(new ModelPart.VertexData(new Vector3f(-8, -8, 0), new ModelPart.Quad[0]), 0, 0),
            new ModelPart.Vertex(new ModelPart.VertexData(new Vector3f(8, -8, 0), new ModelPart.Quad[0]), 1, 0)});
    static final Quaternionf rot = new Quaternionf();
    static final ResourceLocation tex = AChanged.textureLoc("misc/loading");

//TODO elytra, spin attack, stuck arrows
    @Override
    public void render(@NotNull E entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        updateCustomPlayerModel(entity);

        if(model == null){
            renderLoadingAnim(entity, poseStack, partialTicks, packedLight);

            var event = new net.neoforged.neoforge.client.event.RenderNameTagEvent(entity, entity.getDisplayName(), this, poseStack, buffer, packedLight, partialTicks);
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event);
            if (event.canRender().isTrue() || event.canRender().isDefault() && this.shouldShowName(entity)) {
                this.renderNameTag(entity, event.getContent(), poseStack, buffer, packedLight, partialTicks);
            }
            return;
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void renderHand(E entity, PoseStack stack, int packedLight, HumanoidArm arm){
        updateCustomPlayerModel(entity);

        if(model == null) return;
        model.renderHand(entity, stack, packedLight, arm);
    }

    protected void updateCustomPlayerModel(LivingEntity entity){
        if(entity instanceof AbstractClientPlayer player){
            CustomModelManager<E, M> manager = CustomModelManager.getInstance();
            if(manager.hasCustomModel(player)){
                model = manager.getModel(player);
            } else model = null;
        }
    }

    private static void renderLoadingAnim(LivingEntity entity, PoseStack poseStack, float partialTicks, int packedLight){
        poseStack.pushPose();
        poseStack.translate(0, 1, 0);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        int ticks = entity.tickCount;
        int degCurrent = ticks % 90 * 4;
        int degPrev = (degCurrent > 0 ? ticks -1 : 89) % 90 * 4;


        float prev = degPrev * -Mth.DEG_TO_RAD;
        float current = degCurrent * -Mth.DEG_TO_RAD;
        if(degPrev == 356){
            poseStack.mulPose(rot.identity().rotateZYX(Mth.lerp(partialTicks, prev, 360 * -Mth.DEG_TO_RAD), 0, 0));
        } else poseStack.mulPose(rot.identity().rotateZYX(Mth.lerp(partialTicks, prev, current), 0, 0));

        poseStack.scale(entity.getScale(), -entity.getScale(), entity.getScale());
        PoseStack.Pose pose = poseStack.last();
        for (ModelPart.Vertex vertex : quad.vertices) {
            vertex.data().resetTransform();
        }
        quad.resetTransform();
        quad.compile(pose.pose(), pose.normal(), BufferSourceAccess.get().getBuffer(RenderType.entityTranslucent(tex)), packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }

    @Override
    protected void setupRotations(E entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        float f = entity.getSwimAmount(partialTick);
        float f1 = entity.getViewXRot(partialTick);
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        if (entity.isFallFlying() && entity instanceof AbstractClientPlayer player) {
            float f2 = (float) player.getFallFlyingTicks() + partialTick;
            float f3 = Mth.clamp(f2 * f2 / 100.0F, 0.0F, 1.0F);
            if (!player.isAutoSpinAttack()) {
                poseStack.mulPose(Axis.XP.rotationDegrees(f3 * (-90.0F - f1)));
            }

            Vec3 vec3 = player.getViewVector(partialTick);
            Vec3 vec31 = player.getDeltaMovementLerped(partialTick);
            double d0 = vec31.horizontalDistanceSqr();
            double d1 = vec3.horizontalDistanceSqr();
            if (d0 > 0.0 && d1 > 0.0) {
                double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
                double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
                poseStack.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            float f3 = entity.isInWater() || entity.isInFluidType((fluidType, height) -> entity.canSwimInFluidType(fluidType)) ? -90.0F - entity.getXRot() : -90.0F;
            float f5 = Mth.lerp(f, 0.0F, f3);
            poseStack.mulPose(Axis.XP.rotationDegrees(f5));
            if (entity.isVisuallySwimming()) {
                poseStack.translate(0.0F, -1.0F, 0.3F);
            }
        }
    }

    protected boolean shouldShowName(@NotNull E entity) {
        return super.shouldShowName(entity)
                && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    protected void renderNameTag(@NotNull E entity, @NotNull Component displayName, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, float partialTick) {
        if(!(entity instanceof AbstractClientPlayer client)){
            super.renderNameTag(entity, displayName, poseStack, bufferSource, packedLight, partialTick);
            return;
        }
        double d0 = this.entityRenderDispatcher.distanceToSqr(client);
        poseStack.pushPose();
        if (d0 < 100.0) {
            Scoreboard scoreboard = client.getScoreboard();
            Objective objective = scoreboard.getDisplayObjective(DisplaySlot.BELOW_NAME);
            if (objective != null) {
                ReadOnlyScoreInfo readonlyscoreinfo = scoreboard.getPlayerScoreInfo(client, objective);
                Component component = ReadOnlyScoreInfo.safeFormatValue(readonlyscoreinfo, objective.numberFormatOrDefault(StyledFormat.NO_STYLE));
                super.renderNameTag(
                        entity,
                        Component.empty().append(component).append(CommonComponents.SPACE).append(objective.getDisplayName()),
                        poseStack,
                        bufferSource,
                        packedLight,
                        partialTick
                );
                poseStack.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
            }
        }

        super.renderNameTag(entity, displayName, poseStack, bufferSource, packedLight, partialTick);
        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull E entity) {
        return model.getTexture();
    }
}