package net.zaharenko424.a_changed.mixin.cmrs.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.api.CustomModel;
import net.zaharenko424.cmrs.client.CustomModelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;getShadowRadius(Lnet/minecraft/world/entity/Entity;)F"),
            method = "render")
    private <T extends Entity, E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> float onRender(EntityRenderer instance, T entity, Operation<Float> original){
        if(entity instanceof AbstractClientPlayer player && CustomModelManager.getInstance().hasCustomModel(player)){
            M model = CustomModelManager.getInstance().getModel(player);
            return model != null ? model.getShadowRadius((E) player) : .5f;
        }

        return original.call(instance, entity);
    }
}
