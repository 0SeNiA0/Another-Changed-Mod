package net.zaharenko424.a_changed.client.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupBuilder;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class CustomBEWLR extends BlockEntityWithoutLevelRenderer {

    private static CustomBEWLR INSTANCE;
    private final ModelPart absoluteSolver;
    private final ResourceLocation solverTexture = AChanged.textureLoc("misc/absolute_solver");


    private CustomBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create(), PartPose.offset(0, 6, -1));
        root.addOrReplaceChild("absolute_solver", GroupBuilder.create()
                .addMesh(new float[]{-0f, 1.2392f, 1, -1.1624f, 0.6634f, 1, -1.0624f, 0.8366f, 1, 1.0624f, 0.8366f, 1, 1.1624f, 0.6634f, 1, 1.1232f, -0.6946f, 1, -1.1232f, -0.6946f, 1, -0.1f, -1.2f, 1, 0.1f, -1.2f, 1, 0f, 0f, 1, 0f, -0.6946f, 1, -0.5616f, 0.3098f, 1, 0.5616f, 0.3098f, 1, 0.05f, -2.55f, 1, -0.05f, -2.55f, 1, 0.4f, -2.725f, 1, -0.4f, -2.725f, 1, -0.075f, -3.7f, 1, 0.175f, -2.65f, 1, -0.175f, -2.65f, 1, 0.1f, -2.6f, 1, -0.1f, -2.6f, 1, 0.3858f, -2.6913f, 1, -0.3858f, -2.6913f, 1, 0.075f, -3.7f, 1, 0f, -3.75f, 1, 0f, -3.75f, 1, -2.3066f, 1.3817f, 1, -2.2566f, 1.4683f, 1, -2.6331f, 1.1661f, 1, -2.2331f, 1.8589f, 1, -3.24f, 2.065f, 1, -2.4557f, 1.3234f, 1, -2.2807f, 1.6266f, 1, -2.3749f, 1.3634f, 1, -2.2749f, 1.5366f, 1, -2.5968f, 1.1616f, 1, -2.211f, 1.8297f, 1, -3.315f, 1.935f, 1, -3.3208f, 2.025f, 1, -3.3208f, 2.025f, 1, 2.2566f, 1.4683f, 1, 2.3066f, 1.3817f, 1, 2.2331f, 1.8589f, 1, 2.6331f, 1.1661f, 1, 3.315f, 1.935f, 1, 2.2807f, 1.6266f, 1, 2.4557f, 1.3234f, 1, 2.2749f, 1.5366f, 1, 2.3749f, 1.3634f, 1, 2.211f, 1.8297f, 1, 2.5968f, 1.1616f, 1, 3.24f, 2.065f, 1, 3.3208f, 2.025f, 1, 3.3208f, 2.025f, 1}, new float[]{6, 1.6047f, 2.3212f, 0, 2.7278f, 0.3874f, 2, 1.6654f, 0.79f, 1, 1.5654f, 0.9632f, 0, 3.2722f, 3.3874f, 5, 4.3953f, 5.3212f, 4, 4.4345f, 3.9632f, 3, 4.3345f, 3.79f, 5, 6.6232f, 0, 6, 4.3768f, 0, 7, 5.4f, 0.5054f, 8, 5.6f, 0.5054f, 6, 0.3768f, 7.9338f, 10, 1.5f, 7.9338f, 9, 1.5f, 7.2392f, 11, 0.9384f, 6.9294f, 0, 1.5f, 6, 11, 0.9384f, 6.9294f, 9, 1.5f, 7.2392f, 12, 2.0616f, 6.9294f, 5, 2.6232f, 7.9338f, 12, 2.0616f, 6.9294f, 9, 1.5f, 7.2392f, 10, 1.5f, 7.9338f, 13, 5.55f, 1.8554f, 8, 5.6f, 0.5054f, 7, 5.4f, 0.5054f, 14, 5.45f, 1.8554f, 15, 7.1f, 2.075f, 16, 7.9f, 2.075f, 17, 7.575f, 3.05f, 24, 7.425f, 3.05f, 14, 5.45f, 1.8554f, 21, 5.4f, 1.9054f, 20, 5.6f, 1.9054f, 13, 5.55f, 1.8554f, 22, 7.1142f, 2.0413f, 23, 7.8858f, 2.0413f, 16, 7.9f, 2.075f, 15, 7.1f, 2.075f, 18, 5.675f, 1.9554f, 20, 5.6f, 1.9054f, 21, 5.4f, 1.9054f, 19, 5.325f, 1.9554f, 23, 7.8858f, 2.0413f, 22, 7.1142f, 2.0413f, 18, 7.325f, 2, 19, 7.675f, 2, 24, 7.425f, 3.05f, 17, 7.575f, 3.05f, 25, 7.5f, 3.1f, 26, 7.5f, 3.1f, 27, 0.4212f, 0.2449f, 1, 1.5654f, 0.9632f, 2, 1.6654f, 0.79f, 28, 0.4712f, 0.1583f, 29, 0.8672f, 4.8989f, 30, 0.4672f, 4.2061f, 31, 1.4741f, 4, 38, 1.5491f, 4.13f, 28, 0.4712f, 0.1583f, 35, 0.453f, 0.09f, 34, 0.353f, 0.2632f, 27, 0.4212f, 0.2449f, 36, 0.8309f, 4.9034f, 37, 0.4451f, 4.2353f, 30, 0.4672f, 4.2061f, 29, 0.8672f, 4.8989f, 32, 0.2722f, 0.3032f, 34, 0.353f, 0.2632f, 35, 0.453f, 0.09f, 33, 0.4471f, 0, 37, 0.4451f, 4.2353f, 36, 0.8309f, 4.9034f, 32, 0.6898f, 4.7416f, 33, 0.5148f, 4.4384f, 38, 1.5491f, 4.13f, 31, 1.4741f, 4, 39, 1.5549f, 4.04f, 40, 1.5549f, 4.04f, 41, 5.5287f, 3.1583f, 3, 4.3345f, 3.79f, 4, 4.4345f, 3.9632f, 42, 5.5788f, 3.2449f, 43, 7.5328f, 5.2061f, 44, 7.1328f, 5.8989f, 45, 6.4509f, 5.13f, 52, 6.5259f, 5, 42, 5.5788f, 3.2449f, 49, 5.647f, 3.2632f, 48, 5.547f, 3.09f, 41, 5.5287f, 3.1583f, 50, 7.5549f, 5.2353f, 51, 7.1691f, 5.9034f, 44, 7.1328f, 5.8989f, 43, 7.5328f, 5.2061f, 46, 5.5528f, 3, 48, 5.547f, 3.09f, 49, 5.647f, 3.2632f, 47, 5.7278f, 3.3032f, 51, 7.1691f, 5.9034f, 50, 7.5549f, 5.2353f, 46, 7.4852f, 5.4384f, 47, 7.3102f, 5.7416f, 52, 6.5259f, 5, 45, 6.4509f, 5.13f, 53, 6.4451f, 5.04f, 54, 6.4451f, 5.04f}));

        absoluteSolver = ModelDefinition.create(modelBuilder, 16, 16).bake().getChild("root");
    }

    public static CustomBEWLR getInstance(){
        if(INSTANCE == null) INSTANCE = new CustomBEWLR();
        return INSTANCE;
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager pResourceManager) {}

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int pPackedLight, int pPackedOverlay) {
        if(stack.is(ItemRegistry.ABSOLUTE_SOLVER)){
            poseStack.pushPose();
            poseStack.translate(0, 1, .5);
            int time = (int) (Minecraft.getInstance().level.getGameTime() % 1800);
            float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
            float deg = time / 5f;
            float nextDeg = (time + 1) / 5f;
            absoluteSolver.x = 7;
            absoluteSolver.y = -3;
            absoluteSolver.zRot = Mth.DEG_TO_RAD * Mth.rotLerp(partialTick, deg, nextDeg);

            /*RenderSystem.setShaderTexture(0, solverTexture);
            //RenderSystem.setShader(CustomShaders.getInstance()::getBLOOM);
            RenderSystem.setShader(GameRenderer::getRendertypeEntitySolidShader);
            BufferBuilder builder = Tesselator.getInstance().getBuilder();
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);
            absoluteSolver.render(poseStack, builder, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            BufferUploader.drawWithShader(builder.end());*/

            absoluteSolver.render(poseStack, buffer.getBuffer(RenderType.entitySolid(solverTexture)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}