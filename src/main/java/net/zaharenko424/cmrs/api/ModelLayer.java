package net.zaharenko424.cmrs.api;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.client.model.RenderStack;

public interface ModelLayer {

    IntSet renderIds();

    void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access);
}