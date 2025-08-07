package net.zaharenko424.cmrs.api;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.client.material.MaterialType;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;

import java.util.List;

public interface Material {

    DeferredHolder<MaterialType<?>, ?> type();

    void verifyTextures(List<Texture> textures);

    boolean shouldRenderInFirstPerson();

    void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack.ParameterList parameters, MultiBufferSource source);
}