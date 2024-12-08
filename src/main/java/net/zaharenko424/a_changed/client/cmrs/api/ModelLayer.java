package net.zaharenko424.a_changed.client.cmrs.api;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.model.RenderStack;

public interface ModelLayer {

    IntSet renderIds();

    void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access);
}