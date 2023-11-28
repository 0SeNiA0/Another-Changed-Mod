package net.zaharenko424.testmod.entity.transfurTypes;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.testmod.client.model.AbstractLatexEntityModel;
import net.zaharenko424.testmod.client.model.LatexWolfMaleModel;
import net.zaharenko424.testmod.client.model.LatexWolfFemaleModel;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends AbstractGenderedTransfurType {

    public LatexWolf(@NotNull ResourceLocation resourceLocation, boolean male){
        super(resourceLocation,male);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public <T extends LivingEntity> AbstractLatexEntityModel<T> getModel(@NotNull EntityRendererProvider.Context context) {
        return male?new LatexWolfMaleModel<>(context,true):new LatexWolfFemaleModel<>(context,true);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public <T extends LivingEntity> AbstractLatexEntityModel<T> getArmorModel(@NotNull EntityRendererProvider.Context context) {
        return male?new LatexWolfMaleModel<>(context,false):new LatexWolfFemaleModel<>(context,false);
    }
}