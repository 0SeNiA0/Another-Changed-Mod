package net.zaharenko424.cmrs.api;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.cmrs.client.model.Texture;

import java.util.List;

public interface ModelLayer {

    IntSet renderIds();

    void verifyTextures(List<Texture> textures);

    boolean shouldRenderInFirstPerson();

    void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access);
}