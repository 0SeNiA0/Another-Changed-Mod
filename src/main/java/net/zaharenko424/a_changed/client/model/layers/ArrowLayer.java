package net.zaharenko424.a_changed.client.model.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.client.model.HierarchicalHumanoidModel;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ArrowLayer<E extends LivingEntity,M extends HierarchicalHumanoidModel<E>> extends StuckInBodyLayerFix<E,M>{

    private final EntityRenderDispatcher dispatcher;

    public ArrowLayer(EntityRenderDispatcher dispatcher, RenderLayerParent<E, M> parent) {
        super(parent);
        this.dispatcher = dispatcher;
    }

    @Override
    protected int numStuck(E entity) {
        return entity.getArrowCount();
    }

    @Override
    protected void renderStuckItem(PoseStack poseStack, MultiBufferSource buffer, int light, E entity, float x, float y, float z, float ticks) {
        float f = Mth.sqrt(x * x + z * z);
        Arrow arrow = new Arrow(entity.level(), entity.getX(), entity.getY(), entity.getZ(), ItemStack.EMPTY);
        arrow.setYRot((float)(Math.atan2(x, z) * 180.0F / (float)Math.PI));
        arrow.setXRot((float)(Math.atan2(y, f) * 180.0F / (float)Math.PI));
        arrow.yRotO = arrow.getYRot();
        arrow.xRotO = arrow.getXRot();
        dispatcher.render(arrow, 0.0, 0.0, 0.0, 0.0F, ticks, poseStack, buffer, light);
    }
}