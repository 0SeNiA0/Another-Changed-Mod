package net.zaharenko424.a_changed.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;
import net.zaharenko424.cmrs.api.BufferSourceAccess;
import net.zaharenko424.cmrs.api.MatrixStack;
import net.zaharenko424.cmrs.client.geom.*;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.item.AbstractSyringe;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

public class SyringeProjectileRenderer extends EntityRenderer<SyringeProjectile> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(EntityRegistry.SYRINGE_PROJECTILE.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/syringe_projectile");
    private final ModelPart root;
    private final ModelPart piston;
    private final RenderStack stack = new RenderStack();

    public SyringeProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        root = ModelDefinitionCache.getInstance().bake(LAYER).getDirectChild("root");
        piston = root.getDirectChild("piston");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addBox(-0.9f, 7f, -0.9f, 1.8f, 5.5f, 1.8f, new CubeUV().west(2, 12, 0, 6).down(8, 0, 6, 2).north(2, 6, 0, 0).east(4, 6, 2, 0).south(6, 6, 4, 0), 1)
                .addBox(-1.1f, 12.5f, -1.1f, 2.2f, 0.5f, 2.2f, new CubeUV().west(12, 10, 10, 9).down(10, 6, 8, 8).up(10, 6, 8, 4).north(12, 7, 10, 6).east(12, 8, 10, 7).south(12, 9, 10, 8), 1)
                .addBox(-0.3f, 3f, -0.3f, 0.6f, 4.3f, 0.6f, new CubeUV().west(5, 10, 4, 6).down(11, 10, 10, 11).up(8, 11, 7, 10).north(3, 10, 2, 6).east(7, 6, 6, 2).south(4, 10, 3, 6))
                .addBox(-0.6f, 7.5f, -0.6f, 1.2f, 4, 1.2f, new CubeUV().west(3, 14, 2, 10).down(11, 11, 10, 12).up(8, 12, 7, 11).north(9, 12, 8, 8).east(10, 12, 9, 8).south(11, 4, 10, 0), 2)
                .addBox(-0.5f, 7.6f, -0.5f, 1, 3.8f, 1, new CubeUV().west(16, 8, 14, 0).down(12, 12, 10, 14).up(10, 14, 8, 12).north(14, 8, 12, 0).east(16, 16, 14, 8).south(14, 16, 12, 8), 3), PartPose.offsetAndRotation(9, 0, 0, 0, 0, 1.5708f));
        root.addOrReplaceChild("piston", GroupBuilder.create()
                .addBox(-0.6f, 12f, -0.6f, 1.2f, 0.5f, 1.2f, new CubeUV().west(5, 12, 4, 11).down(7, 11, 6, 12).up(6, 12, 5, 11).north(12, 3, 11, 2).east(4, 12, 3, 11).south(12, 4, 11, 3))
                .addBox(-0.9f, 16f, -0.9f, 1.8f, 0.5f, 1.8f, new CubeUV().west(12, 6, 10, 5).down(10, 2, 8, 4).up(10, 2, 8, 0).north(5, 11, 3, 10).east(12, 5, 10, 4).south(7, 11, 5, 10))
                .addBox(-0.2f, 12.5f, -0.2f, 0.4f, 3.5f, 0.4f, new CubeUV().west(8, 10, 7, 6).down(12, 1, 11, 2).up(12, 1, 11, 0).north(6, 10, 5, 6).east(7, 10, 6, 6).south(8, 6, 7, 2)));

        return ModelDefinition.create(modelBuilder, 16, 16);
    }

    @Override
    public void render(@NotNull SyringeProjectile entity, float entityYaw, float partialTick, @NotNull PoseStack stack, @NotNull MultiBufferSource buffer, int packedLight) {
        MatrixStack.push(stack);
        stack.translate(0, 2/16f, 0);
        stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));

        float f9 = (float)entity.shakeTime - partialTick;
        if (f9 > 0.0F) {
            stack.mulPose(Axis.ZP.rotationDegrees(-Mth.sin(f9 * 3.0F) * f9));
        }

        this.stack.reset();
        BufferSourceAccess access = BufferSourceAccess.get();
        access.cmrs$finishBatched();

        VertexConsumer solid = access.cmrs$getBuffer(RenderType.entitySolid(TEXTURE), 0);
        this.stack.getOrCreate(0).add().set(solid);
        this.stack.getOrCreate(1).add().set(access.cmrs$getBuffer(RenderType.entityTranslucent(TEXTURE), 2));

        ItemStack syringe = entity.getPickupItemStackOrigin();
        AbstractSyringe item = (AbstractSyringe) syringe.getItem();

        piston.resetPose();
        boolean empty = true;
        int color = item.getContentsColor(syringe);
        if(FastColor.ARGB32.alpha(color) != 0){
            this.stack.getOrCreate(2).add().set(access.cmrs$getBuffer(RenderType.entityTranslucent(TEXTURE), 1)).setColor(color);
            empty = false;
        }

        color = item.getSecondaryColor(syringe);
        if(FastColor.ARGB32.alpha(color) != 0){
            this.stack.getOrCreate(3).add().set(access.cmrs$getBuffer(RenderType.entityTranslucent(TEXTURE), 0)).setColor(color);
            empty = false;
        }

        if(empty){
            piston.y -= 3;
        }

        root.render(stack, this.stack, packedLight, OverlayTexture.NO_OVERLAY, -1);
        access.cmrs$finishBatched();
        MatrixStack.pop(stack);
        super.render(entity, entityYaw, partialTick, stack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SyringeProjectile entity) {
        return TEXTURE;
    }
}