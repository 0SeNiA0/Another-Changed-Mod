package net.zaharenko424.a_changed.client.cmrs.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.model.HierarchicalHumanoidModel;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class StuckInBodyLayerFix<E extends LivingEntity,M extends HierarchicalHumanoidModel<E>> extends RenderLayer<E,M> {

    public StuckInBodyLayerFix(RenderLayerParent<E, M> p_117346_) {
        super(p_117346_);
    }

    protected abstract int numStuck(E p_117565_);

    protected abstract void renderStuckItem(PoseStack p_117566_, MultiBufferSource p_117567_, int p_117568_, E p_117569_, float p_117570_, float p_117571_, float p_117572_, float p_117573_);

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, E entity, float p_117353_, float p_117354_, float ticks, float p_117356_, float p_117357_, float p_117358_) {
        int i = numStuck(entity);
        RandomSource randomsource = RandomSource.create(entity.getId());
        if(i <= 0) return;
        for (int j = 0; j < i; ++j) {
            poseStack.pushPose();
            ModelPart part;
            do {
                part = getParentModel().getRandomModelPart(randomsource);
            } while (!part.hasCubes());

            ModelPart.Cube modelpart$cube = part.getRandomCube(randomsource);
            part.translateAndRotate(poseStack);
            float f = randomsource.nextFloat();
            float f1 = randomsource.nextFloat();
            float f2 = randomsource.nextFloat();
            float f3 = Mth.lerp(f, modelpart$cube.minX, modelpart$cube.maxX) / 16.0F;
            float f4 = Mth.lerp(f1, modelpart$cube.minY, modelpart$cube.maxY) / 16.0F;
            float f5 = Mth.lerp(f2, modelpart$cube.minZ, modelpart$cube.maxZ) / 16.0F;
            poseStack.translate(f3, f4, f5);
            f = -1.0F * (f * 2.0F - 1.0F);
            f1 = -1.0F * (f1 * 2.0F - 1.0F);
            f2 = -1.0F * (f2 * 2.0F - 1.0F);
            renderStuckItem(poseStack, buffer, light, entity, f, f1, f2, ticks);
            poseStack.popPose();
        }
    }
}