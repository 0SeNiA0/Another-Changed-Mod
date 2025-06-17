package net.zaharenko424.a_changed.mixin.client.cmrs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.zaharenko424.cmrs.client.CustomModelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;getShadowRadius(Lnet/minecraft/world/entity/Entity;)F"),
            method = "render")
    private <T extends Entity> float onRender(EntityRenderer instance, T entity, Operation<Float> original){
        if(entity instanceof AbstractClientPlayer player && CustomModelManager.getInstance().hasCustomModel(player)){
            return .5f * player.getScale() * player.getAgeScale();//TMP not great but will do. mb put shadow radius into the model? shadow should be determined by the model and not by entity type after all
        }

        return original.call(instance, entity);
    }
}
